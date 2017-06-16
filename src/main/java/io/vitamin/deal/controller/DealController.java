package io.vitamin.deal.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vitamin.deal.domain.Deal;
import io.vitamin.deal.domain.FxRate;
import io.vitamin.deal.service.DealReturnService;
import io.vitamin.deal.service.DealService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.utils.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DealController
{
    private static final Logger LOG = LoggerFactory.getLogger(DealController.class);

    private final DealReturnService dealService;
    private final ObjectMapper mapper = new ObjectMapper();

    public DealController(DealReturnService dealService) {
        this.dealService = dealService;
    }

    public Answer addDeal(String requestBody, Map<String, String> queryParams){
        try {
            Deal deal = mapper.readValue(requestBody, Deal.class);
            if (deal.isValid()) {
                String dealId = dealService.add(deal);
                return new Answer(HttpStatus.OK, dealId);
            }
        } catch (Exception ex) {
            LOG.error("Issue while adding deal.", ex);
        }
        return new Answer(HttpStatus.BAD_REQUEST, "Unexpected Deal entry.");
    }

    public Answer updateFx(String requestBody, Map<String, String> queryParams){
        try {
            FxRate fxRate = mapper.readValue(requestBody, FxRate.class);
            if (fxRate.isValid()) {
                Boolean done = dealService.updateFxRate(fxRate);

                return done ? new Answer(HttpStatus.OK): new Answer(HttpStatus.INTERNAL_ERROR);
            }
        } catch (Exception ex) {
            LOG.error("Issue while adding deal.", ex);
        }
        return new Answer(HttpStatus.BAD_REQUEST, "Unexpected Deal entry.");
    }

    public Answer deleteDeal(String requestBody, Map<String, String> queryParams){
        String dealId = queryParams.get(Params.DEAL_ID);
        HttpStatus status = HttpStatus.NOT_FOUND;
        if (!StringUtils.isBlank(dealId)) {
            Optional<Deal> deal = dealService.remove(dealId);
            status = deal.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        }
        return new Answer(status);
    }

    public Answer getDeal(String requestBody, Map<String, String> queryParams){
        String dealId = queryParams.get(Params.DEAL_ID);
        HttpStatus status = HttpStatus.NOT_FOUND;
        try{
            if (!StringUtils.isBlank(dealId)) {
                Optional<Deal> deal = dealService.get(dealId);
                if(deal.isPresent()){
                    return Answer.ok(mapper.writeValueAsString(deal.get()));
                } else
                    return new Answer(HttpStatus.NOT_FOUND);
            }
        }catch (JsonProcessingException jse){
            LOG.error("Error while parsing deal.", jse);
            status = HttpStatus.INTERNAL_ERROR;
        }
        return new Answer(status);
    }

    public Answer getReturns(String requestBody, Map<String, String> queryParams){

        HttpStatus status = HttpStatus.NOT_FOUND;
        try{
           List<Deal> deals = this.dealService.getDeals();
           return Answer.ok(mapper.writeValueAsString(deals));

        }catch (JsonProcessingException jse){
            LOG.error("Error while parsing deals.", jse);
            status = HttpStatus.INTERNAL_ERROR;
        }
        return new Answer(status);
    }


}
