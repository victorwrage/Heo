package com.heinsoft.heo.view;


import com.heinsoft.heo.bean.HeoCodeResponse;

import org.json.JSONException;

import okhttp3.ResponseBody;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/4/7 9:49
 */

public interface IPayView extends IView{
    /**
     * @param info
     */
    void ResolvePayInfo(HeoCodeResponse info);
    /**
     * @param info
     */
    void ResolveScoreQuickPayInfo(ResponseBody info);
    /**
     * @param info
     */
    void ResolveScoreQuickPayConfirmInfo(ResponseBody info);
    /**
     * @param info
     */
    void ResolveQuickPayInfo(ResponseBody info);
    /**
     * @param info
     */
    void ResolveOpenCreditInfo(ResponseBody info);
    /**
     * @param info
     */
    void ResolveQuickPayConfirmInfo(ResponseBody info);
    /**
     * @param info
     */
    void ResolveProfitInfo(HeoCodeResponse info);
    /**
     * @param info
     */
    void ResolveAgentOrderInfo(HeoCodeResponse info);
    /**
     * @param info
     */
    void ResolveAgentProfitInfo(HeoCodeResponse info);
    /**
     * @param info
     */
    void ResolveAgentWithdrawInfo(HeoCodeResponse info);
    /**
     * @param info
     */
    void ResolveMerchantOrderInfo(HeoCodeResponse info);
}
