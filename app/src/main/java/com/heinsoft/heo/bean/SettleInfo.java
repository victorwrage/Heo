package com.heinsoft.heo.bean;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/5/13 14:47
 */

public class SettleInfo {
    String merchant_id;
    String status;
    String fixed_qrcode;

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFixed_qrcode() {
        return fixed_qrcode;
    }

    public void setFixed_qrcode(String fixed_qrcode) {
        this.fixed_qrcode = fixed_qrcode;
    }

    @Override
    public String toString() {

        return "merchant_id:" + merchant_id + "status:" + status + "fixed_qrcode:" + fixed_qrcode;
    }
}
