package com.heinsoft.heo.view;


import com.heinsoft.heo.bean.HeoCodeResponse;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/4/7 9:49
 */

public interface IMerchantView extends IView{

    /**
     * @param info
     */
    void ResolveSettleInfo(HeoCodeResponse info);
    /**
     * @param info
     */
    void ResolveSettle(HeoCodeResponse response );
    /**
     * @param info
     */
    void ResolveMerchantInfo(HeoCodeResponse info);
}
