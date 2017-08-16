package com.heinsoft.heo.view;


import com.heinsoft.heo.bean.HeoCodeObjResponse;
import com.heinsoft.heo.bean.HeoCodeResponse;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/4/7 9:49
 */

public interface IInviteView extends IView{
    /**
     * @param info
     */
    void ResolveInviteInfo(HeoCodeObjResponse info);
    /**
     * @param info
     */
    void ResolveUsefullInviteInfo(HeoCodeResponse info);
    /**
     * @param info
     */
    void ResolveObtainInviteInfo(HeoCodeResponse info);
    /**
     * @param info
     */
    void ResolveAssignInviteInfo(HeoCodeResponse info);

}
