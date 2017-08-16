package com.heinsoft.heo.view;


import com.heinsoft.heo.bean.HeoCodeResponse;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/4/7 9:49
 */

public interface IAgentView extends IView{
    /**
     * @param info
     */
    void ResolveAddAgentInfo(HeoCodeResponse info);
    /**
     * @param info
     */
    void ResolveAgentInfo(HeoCodeResponse info);

}
