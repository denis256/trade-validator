package com.tradevalidator.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

/**
 * Trade details model instance
 {"customer":"PLUTO1","ccyPair":"EURUSD","type":"VanillaOption","style":"EUROPEAN","direction":"BUY","strategy":"CALL","tradeDate"
 :"2016-08-11","amount1":1000000.00,"amount2":1120000.00,"rate":1.12,"deliveryDate":"2016-08-22","expiryDate":"2016-08-
 19","payCcy":"USD","premium":0.20,"premiumCcy":"USD","premiumType":"%USD","premiumDate":"2016-08-12","legalEntity":"CS
 Zurich","trader":"Johann Baumfiddler"}

 {"customer":"PLUTO1","ccyPair":"EURUSD","type":"Spot","direction":"BUY","tradeDate":"2016-08-
 11","amount1":1000000.00,"amount2":1120000.00,"rate":1.12,"valueDate":"2016-08-15","legalEntity":"CS Zurich","trader":"Johann
 Baumfiddler"}

 {"customer":"PLUTO1","ccyPair":"EURUSD","type":"VanillaOption","style":"AMERICAN","direction":"BUY","strategy":"CALL","tradeDate"
 :"2016-08-11","amount1":1000000.00,"amount2":1120000.00,"rate":1.12,"deliveryDate":"2016-08-22","expiryDate":"2016-08-
 19","excerciseStartDate":"2016-08-12","payCcy":"USD","premium":0.20,"premiumCcy":"USD","premiumType":"%USD","premiumDate":"2016-
 08-12","legalEntity":"CS Zurich","trader":"Johann Baumfiddler"}


 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Trade {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date tradeDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date valueDate;

    private String customer;

    private String ccyPair;

    private String type;

    private String style;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date excerciseStartDate;

    public Date getValueDate() {
        return valueDate;
    }

    public void setValueDate(Date valueDate) {
        this.valueDate = valueDate;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCcyPair() {
        return ccyPair;
    }

    public void setCcyPair(String ccyPair) {
        this.ccyPair = ccyPair;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(Date tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public Date getExcerciseStartDate() {
        return excerciseStartDate;
    }

    public void setExcerciseStartDate(Date excerciseStartDate) {
        this.excerciseStartDate = excerciseStartDate;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "customer='" + customer + '\'' +
                ", ccyPair='" + ccyPair + '\'' +
                ", type='" + type + '\'' +
                ", tradeDate=" + tradeDate +
                ", style='" + style + '\'' +
                ", excerciseStartDate=" + excerciseStartDate +
                ", valueDate=" + valueDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trade trade = (Trade) o;

        if (customer != null ? !customer.equals(trade.customer) : trade.customer != null) return false;
        if (ccyPair != null ? !ccyPair.equals(trade.ccyPair) : trade.ccyPair != null) return false;
        if (type != null ? !type.equals(trade.type) : trade.type != null) return false;
        if (tradeDate != null ? !tradeDate.equals(trade.tradeDate) : trade.tradeDate != null) return false;
        if (style != null ? !style.equals(trade.style) : trade.style != null) return false;
        if (excerciseStartDate != null ? !excerciseStartDate.equals(trade.excerciseStartDate) : trade.excerciseStartDate != null)
            return false;
        return valueDate != null ? valueDate.equals(trade.valueDate) : trade.valueDate == null;

    }

    @Override
    public int hashCode() {
        int result = customer != null ? customer.hashCode() : 0;
        result = 31 * result + (ccyPair != null ? ccyPair.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (tradeDate != null ? tradeDate.hashCode() : 0);
        result = 31 * result + (style != null ? style.hashCode() : 0);
        result = 31 * result + (excerciseStartDate != null ? excerciseStartDate.hashCode() : 0);
        result = 31 * result + (valueDate != null ? valueDate.hashCode() : 0);
        return result;
    }
}
