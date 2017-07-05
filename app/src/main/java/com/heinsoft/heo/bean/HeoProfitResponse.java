package com.heinsoft.heo.bean;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/5/13 14:47
 */

public class HeoProfitResponse {
    String errcode;
    String errmsg;
    String statistics_date;
    String agent_id;
    String agent_name;
    String order_amount;//
    String total_money;//
    String total_fee;//
    String payoff_money;//
    String t0_fee;//
    String state;//

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getStatistics_date() {
        return statistics_date;
    }

    public void setStatistics_date(String statistics_date) {
        this.statistics_date = statistics_date;
    }

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public String getAgent_name() {
        return agent_name;
    }

    public void setAgent_name(String agent_name) {
        this.agent_name = agent_name;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getTotal_money() {
        return total_money;
    }

    public void setTotal_money(String total_money) {
        this.total_money = total_money;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getPayoff_money() {
        return payoff_money;
    }

    public void setPayoff_money(String payoff_money) {
        this.payoff_money = payoff_money;
    }

    public String getT0_fee() {
        return t0_fee;
    }

    public void setT0_fee(String t0_fee) {
        this.t0_fee = t0_fee;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "errcode:"+errcode+"errmsg:"+ errmsg +"statistics_date:"+statistics_date+"agent_id:"+agent_id+"agent_name"+agent_name+
        "order_amount:"+order_amount+
                "total_money:"+total_money+"total_fee:"+total_fee+"payoff_money:"+payoff_money+
                "t0_fee:"+t0_fee+"state:"+state;
    }
}
