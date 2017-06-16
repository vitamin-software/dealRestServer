package io.vitamin.deal;

import io.vitamin.deal.controller.DealController;
import io.vitamin.deal.controller.Params;
import io.vitamin.deal.service.DealReturnServiceImpl;
import io.vitamin.deal.service.DealServiceImpl;
import io.vitamin.deal.controller.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;

import static spark.Spark.*;

public class DealControllerApp
{
     private final static Logger LOG = LoggerFactory.getLogger(DealControllerApp.class);
     public static void main(String args[])
     {
         AtomicLong idGenerator = new AtomicLong(0);
         DealController controller = new DealController(new DealReturnServiceImpl(
                 new DealServiceImpl(idGenerator::incrementAndGet)));

         port(9999);
         path("/rest/api", () -> {
             before("/*", (req, res) -> LOG.debug("Received:{}", req));
             path("/deal", () -> {
                 get("/" + Params.DEAL_ID,    new SparkAdapter(controller::getDeal));
                 post("",                     new SparkAdapter(controller::addDeal));
                 delete("/" + Params.DEAL_ID, new SparkAdapter(controller::deleteDeal));
             });
             path("/fx", () -> {
                 post("",                    new SparkAdapter(controller::updateFx));
             });
             path("/returns", () -> {
                 get("",  new SparkAdapter(controller::getReturns));
             });
         });
     }

     public static class SparkAdapter implements Route{
         private final BiFunction<String, Map<String, String>, Answer> underlying;

         public SparkAdapter(BiFunction<String, Map<String, String>, Answer> underlying) {
             this.underlying = underlying;
         }

         public String handle(Request request, Response response){
             Answer answer = this.underlying.apply(request.body(), request.params());
             response.status(answer.getStatusCode());
             response.type(answer.getContentType());
             return answer.getBody();
         }
     }
}
