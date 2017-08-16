package com.heinsoft.heo.bean;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/5/13 14:47
 */

public class HeoCodeObjResponse {
    String errcode;


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

    public HeoMerchantInfoResponse getContent() {
        return content;
    }

    public void setContent(HeoMerchantInfoResponse content) {
        this.content = content;
    }

    String errmsg;
    HeoMerchantInfoResponse content;
    @Override
    public String toString() {
        if(content!=null ) {
            return "errcode:" + errcode + "errmsg:" + errmsg + "content:" + content.toString() ;
        }
        return "errcode:" + errcode + "errmsg:"+errmsg;
    }
}
