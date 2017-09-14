package com.heinsoft.heo.view;

import com.heinsoft.heo.bean.MessageBean;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/8/26 10:36
 */

public interface IFragmentActivity {


     void gotoPay();

     void gotoMain() ;

    void openCamera(int type);
    void doPhoto(int type);
    void gotoVerify();
    void showVerify();

    void gotoLogin();

    void gotoRecord();
    void showWithdraw();
    void gotoRegister();

    void NotifyMessage(MessageBean messageBean);

    void gotoForgot();
}
