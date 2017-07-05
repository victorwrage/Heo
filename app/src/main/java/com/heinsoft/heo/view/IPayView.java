package com.heinsoft.heo.view;


import com.heinsoft.heo.bean.HeoCodeResponse;
import com.heinsoft.heo.bean.HeoProfitResponse;

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
    void ResolveProfitInfo(HeoProfitResponse info);

}
