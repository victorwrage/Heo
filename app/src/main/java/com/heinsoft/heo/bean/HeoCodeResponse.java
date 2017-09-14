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
    String allot_time;
    String allot_num;

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

    String agent_id;
    String agent_name;

    public String getUsefulcode() {
        return usefulcode;
    }

    public void setUsefulcode(String usefulcode) {
        this.usefulcode = usefulcode;
    }

    String batch_no;
    String usefulcode;
    SettleInfo info;

    public SettleInfo getInfo() {
        return info;
    }

    public void setInfo(SettleInfo info) {
        this.info = info;
    }

    public String getAllot_time() {
        return allot_time;
    }

    public void setAllot_time(String allot_time) {
        this.allot_time = allot_time;
    }

    public String getAllot_num() {
        return allot_num;
    }

    public void setAllot_num(String allot_num) {
        this.allot_num = allot_num;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    String num;
    String range;



    public String getBankcode() {
        return bankcode;
    }

    public void setBankcode(String bankcode) {
        this.bankcode = bankcode;
    }

    public String getBanktype() {
        return banktype;
    }

    public void setBanktype(String banktype) {
        this.banktype = banktype;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    String bankcode;
    String banktype;
    String bankname;
    String citycode;


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
        if(info !=null){
            return "errcode:" + errcode + "errmsg:" + errmsg + "getStatus:" + info.getStatus() + "getMerchant_id:" + info.getMerchant_id() + "getFixed_qrcode:" + info.getFixed_qrcode();
        }
        return "errcode:" + errcode + "errmsg:" + errmsg + "merchant_id:" + merchant_id + "name:" + name + "username" + username;
    }
}
