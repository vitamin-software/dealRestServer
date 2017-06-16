package io.vitamin.deal.domain;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import io.vitamin.deal.controller.DealTypeIdResolver;

import java.math.BigDecimal;
import java.util.Date;

@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeIdResolver(DealTypeIdResolver.class)
public abstract class Deal implements Validable{
    public enum Status {
        NEW,
        CANCEL;
    }

    private Status status;
    private String clientId;
    private BigDecimal principal;
    private String ccy;
    private Date dealDate;

    public Deal() {
    }

    public Deal(Status status, String clientId, BigDecimal principal, String ccy) {
        this.status = status;
        this.clientId = clientId;
        this.principal = principal;
        this.ccy = ccy;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public String getCcy() {
        return ccy;
    }

    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    public abstract BigDecimal calculate(BigDecimal fxRate);
}
