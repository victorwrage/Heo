package com.heinsoft.heo.view;


import com.heinsoft.heo.bean.HeoCodeResponse;

import okhttp3.ResponseBody;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/4/7 9:49
 */

public interface IUserView extends IView{
    /**
     * @param info
     */
    void ResolveLoginInfo(HeoCodeResponse info);

    /**
     * @param info
     */
    void ResolveRegisterInfo(HeoCodeResponse info);


    /**
     * @param info
     */
    void ResolveCodeInfo(ResponseBody info);
    /**
     * @param info
     */
    void ResolveForgetInfo(HeoCodeResponse info);
    /**
     * @param info
     */
    void ResolveAlterInfo(HeoCodeResponse info);
    /**
     * @param info
     */
    void ResolveEmailInfo(HeoCodeResponse info);
    /**
     * @param info
     */
    void ResolveVerifyInfo(HeoCodeResponse info);
    /**
     * @param info
     */
    void ResolveCodeVerifyInfo(HeoCodeResponse info);
    /**
     * @param info
     */
    void ResolvePhoneChangeInfo(HeoCodeResponse info);

    /**
     * @param info
     */
    void ResolveResetPasswordInfo(HeoCodeResponse info);

}
