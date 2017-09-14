package com.heinsoft.heo.bean;

/**
 * Created by Administrator on 2017/6/24/024.
 */
public class CardBean  {
    private String cardnum;
    private String cardname;
    private int cardicon;
    private Boolean is_plan;

    public Boolean getIs_plan() {
        return is_plan;
    }

    public void setIs_plan(Boolean is_plan) {
        this.is_plan = is_plan;
    }

    public String getCardnum() {
        return cardnum;
    }

    public void setCardnum(String cardnum) {
        this.cardnum = cardnum;
    }

    public String getCardname() {
        return cardname;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    public int getCardicon() {
        return cardicon;
    }

    public void setCardicon(int cardicon) {
        this.cardicon = cardicon;
    }
}