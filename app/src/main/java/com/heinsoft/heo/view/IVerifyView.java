package com.heinsoft.heo.view;


import okhttp3.ResponseBody;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/4/7 9:49
 */

public interface IVerifyView extends IView{
    /**
     * @param info
     */
    void ResolveOCRInfo(ResponseBody info);



}