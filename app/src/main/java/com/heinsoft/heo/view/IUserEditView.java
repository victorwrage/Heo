package com.heinsoft.heo.view;


import com.heinsoft.heo.bean.HeoCodeResponse;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/4/7 9:49
 */

public interface IUserEditView extends IView{
    /**
     * @param info
     */
    void ResolveEditMerchantInfo(HeoCodeResponse info);
    void ResolveEditMerchantBankInfo(HeoCodeResponse info);
    void ResolveEditMerchantPicInfo(HeoCodeResponse info);

}
