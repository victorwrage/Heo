package com.heinsoft.heo.bean;

import java.util.ArrayList;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/5/13 14:47
 */

public class HeoCodeResponse {
    String errcode;
    String errmsg;
    String merchant_id;
    String name;
    String create_time;
    String update_time;
    String aid;//支付接入ID
    String order_id;//百宝订单号
    String qrcode;//二维码URL
    String pay_money;//订单支付金额
    ArrayList<HeoMerchantInfoResponse> content;

    public ArrayList<HeoMerchantInfoResponse> getContent() {
        return content;
    }

    public void setContent(ArrayList<HeoMerchantInfoResponse> content) {
        this.content = content;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getPay_money() {
        return pay_money;
    }

    public void setPay_money(String pay_money) {
        this.pay_money = pay_money;
    }

    public String getMach_order_id() {
        return mach_order_id;
    }

    public void setMach_order_id(String mach_order_id) {
        this.mach_order_id = mach_order_id;
    }

    String mach_order_id;//商户订单号

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    String username;
    String sign;

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

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        if(content!=null && content.get(0)!=null) {
            return "errcode:" + errcode + "errmsg:" + errmsg + "merchant_id:" + merchant_id + "name:" + name + "username" + username + "content" + content.get(0).toString();
        }
        return "errcode:" + errcode + "errmsg:" + errmsg + "merchant_id:" + merchant_id + "name:" + name + "username" + username;
    }
}
