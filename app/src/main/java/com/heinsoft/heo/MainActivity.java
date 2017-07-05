package com.heinsoft.heo;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.ocr.ui.camera.CameraActivity;
import com.heinsoft.heo.activity.BaseActivity;
import com.heinsoft.heo.fragment.DialogFragmentPhotoTip;
import com.heinsoft.heo.fragment.DialogFragmentVerifyTip;
import com.heinsoft.heo.fragment.FragmentLogin;
import com.heinsoft.heo.fragment.FragmentMain;
import com.heinsoft.heo.fragment.FragmentPay;
import com.heinsoft.heo.fragment.FragmentRegisterLogin;
import com.heinsoft.heo.fragment.FragmentVerify;
import com.heinsoft.heo.fragment.FragmentWebview;
import com.heinsoft.heo.present.QueryPresent;
import com.heinsoft.heo.util.Constant;
import com.heinsoft.heo.util.Utils;
import com.heinsoft.heo.util.VToast;
import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;

public class MainActivity extends BaseActivity implements FragmentLogin.ILoginListener, FragmentRegisterLogin.ILoginListener, FragmentMain.IMainListener, FragmentPay.IPayListener
        , FragmentVerify.IVerifyListtener, DialogFragmentVerifyTip.IVerifyTipListtener ,DialogFragmentPhotoTip.IPhotoTipListtener{
    private final String COOKIE_KEY = "cookie";
    private final int OCR_CODE_OCR_CAED = 1011;

    @Bind(R.id.main_message_lay)
    RelativeLayout main_message_lay;
    @Bind(R.id.main_home_iv)
    ImageView main_home_iv;

    @Bind(R.id.main_mine_lay)
    LinearLayout main_mine_lay;
    @Bind(R.id.main_loan_lay)
    LinearLayout main_loan_lay;
    @Bind(R.id.main_card_lay)
    LinearLayout main_card_lay;
    @Bind(R.id.main_withdraw_lay)
    LinearLayout main_withdraw_lay;
    @Bind(R.id.main_bank_lay)
    LinearLayout main_bank_lay;
    @Bind(R.id.main_title_lay)
    LinearLayout main_title_lay;

    @Bind(R.id.main_user_login)
    TextView main_user_login;

    @Bind(R.id.main_bottom_lay)
    LinearLayout main_bottom_lay;

    @Bind(R.id.main_header_lay)
    RelativeLayout main_header_lay;

    @Bind(R.id.main_withdraw_tv)
    TextView main_withdraw_tv;
    @Bind(R.id.main_loan_tv)
    TextView main_loan_tv;
    @Bind(R.id.main_card_tv)
    TextView main_card_tv;
    @Bind(R.id.main_bank_tv)
    TextView main_bank_tv;
    @Bind(R.id.main_mine_tv)
    TextView main_mine_tv;

    @Bind(R.id.main_withdraw_cv)
    ImageView main_withdraw_cv;
    @Bind(R.id.main_loan_cv)
    ImageView main_loan_cv;
    @Bind(R.id.main_card_cv)
    ImageView main_card_cv;
    @Bind(R.id.main_bank_cv)
    ImageView main_bank_cv;
    @Bind(R.id.main_mine_cv)
    ImageView main_mine_cv;

    private boolean isInit;
    private boolean isForeground = false;
    boolean isLogin = false;
    SharedPreferences sp;
    QueryPresent present;
    Utils util;
    boolean onSaveInstanceState = false;
    String path;
    int type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fore_lay);
        ButterKnife.bind(MainActivity.this);
        initDate();
        initView();
    }

    private void initDate() {
        util = Utils.getInstance();
        sp = getSharedPreferences(COOKIE_KEY, 0);
        Constant.MESSAGE_PASSWORD = util.MD5(Constant.MESSAGE_PASSWORD);
        Bmob.initialize(this, Constant.PUBLIC_BMOB_KEY);
    }

    private void initView() {
        main_header_lay.setVisibility(View.GONE);
        main_bottom_lay.setVisibility(View.GONE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragment0 = new FragmentRegisterLogin();
        ft.add(R.id.fragment_container, fragment0, PAGE_0);
        ft.show(fragment0);
        ft.commit();

        RxView.clicks(main_home_iv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> gotoManage());
        RxView.clicks(main_withdraw_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> gotoPage(R.id.main_withdraw_lay));
        RxView.clicks(main_card_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> gotoPage(R.id.main_card_lay));
        RxView.clicks(main_loan_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> gotoPage(R.id.main_loan_lay));
        RxView.clicks(main_mine_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> toggle());
        RxView.clicks(main_bank_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> gotoPage(R.id.main_bank_lay));
        RxView.clicks(main_title_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> gotoMain());
    }

    private void gotoManage() {

    }

    private void gotoPage(int pageId) {

        main_mine_cv.setImageDrawable(getResources().getDrawable(R.drawable.mine));
        main_loan_cv.setImageDrawable(getResources().getDrawable(R.drawable.loan));
        main_bank_cv.setImageDrawable(getResources().getDrawable(R.drawable.checkin));
        main_withdraw_cv.setImageDrawable(getResources().getDrawable(R.drawable.up_rank));
        main_card_cv.setImageDrawable(getResources().getDrawable(R.drawable.card_affaire));

        main_bank_tv.setTextColor(getResources().getColor(R.color.gold));
        main_withdraw_tv.setTextColor(getResources().getColor(R.color.gold));
        main_card_tv.setTextColor(getResources().getColor(R.color.gold));
        main_loan_tv.setTextColor(getResources().getColor(R.color.gold));
        main_mine_tv.setTextColor(getResources().getColor(R.color.gold));

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragment0 != null) {
            ft.hide(fragment0);
        }
        if (fragment1 != null && pageId != R.id.main_mine_lay) {
            ft.hide(fragment1);
        }
        if (fragment2 != null) {
            ft.hide(fragment2);
        }
        if (fragment3 != null) {
            ft.hide(fragment3);
        }
        if (fragment4 != null) {
            ft.hide(fragment4);
        }
        if (fragment5 != null) {
            ft.hide(fragment5);
        }
        if (fragment6 != null) {
            ft.hide(fragment6);
        }
        if (fragment7 != null) {
            ft.hide(fragment7);
        }
        //  if (fragment8 != null) {
        //    ft.hide(fragment8);
        //  }
        switch (pageId) {
            case 0:
                cur_page = 0;
                if (fragment0 == null) {
                    fragment0 = new FragmentRegisterLogin();
                    ft.add(R.id.fragment_container, fragment0, PAGE_0);
                }
                if(fragment1!=null){
                    fragment1.initState();
                }
                ft.show(fragment0);
                break;
            case 1:
                cur_page = 1;
                if (fragment1 == null) {
                    fragment1 = new FragmentMain();
                    ft.add(R.id.fragment_container, fragment1, PAGE_1);
                }
                ft.show(fragment1);
                break;
            case 5:
                if (!isVerfy()) return;
                cur_page = 5;
                if (fragment5 == null) {
                    fragment5 = new FragmentPay();
                    ft.add(R.id.fragment_container, fragment5, PAGE_5);
                }
                ft.show(fragment5);
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                break;

            case 6:
                cur_page = 6;
                if (fragment6 == null) {
                    fragment6 = new FragmentVerify();
                    ft.add(R.id.fragment_container, fragment6, PAGE_6);
                }
                ft.show(fragment6);

                fragment6.initState();

                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                break;

            case 8:
//                cur_page = 8;
//                if (fragment8 == null) {
//                    fragment8 = new FragmentCamera();
//                    ft.add(R.id.fragment_container, fragment8, PAGE_8);
//                }
//                ft.show(fragment8);
                break;
            case R.id.main_withdraw_lay:
                if (!isVerfy()) return;
                main_withdraw_cv.setImageDrawable(getResources().getDrawable(R.drawable.up_rank_select));
                main_withdraw_tv.setTextColor(getResources().getColor(R.color.chutou_txt));

                cur_page = 4;
                if (fragment4 == null) {
                    fragment4 = new FragmentWebview();
                    ft.add(R.id.fragment_container, fragment4, PAGE_4);
                }
                ft.show(fragment4);
                fragment4.loadUrl("https://m.rong360.com/credit/article");
                break;
            case R.id.main_loan_lay:
                if (!isVerfy()) return;
                main_loan_cv.setImageDrawable(getResources().getDrawable(R.drawable.loan_select));
                main_loan_tv.setTextColor(getResources().getColor(R.color.chutou_txt));
                cur_page = 2;
                if (fragment2 == null) {
                    fragment2 = new FragmentWebview();
                    ft.add(R.id.fragment_container, fragment2, PAGE_2);
                }
                ft.show(fragment2);
                fragment2.loadUrl("https://m.rong360.com/credit/article");
                break;
            case R.id.main_card_lay:
                if (!isVerfy()) return;
                main_card_cv.setImageDrawable(getResources().getDrawable(R.drawable.card_affaire_select));
                main_card_tv.setTextColor(getResources().getColor(R.color.chutou_txt));
                cur_page = 3;
                if (fragment3 == null) {
                    fragment3 = new FragmentWebview();
                    ft.add(R.id.fragment_container, fragment3, PAGE_3);
                }
                ft.show(fragment3);
                fragment3.loadUrl("https://m.rong360.com/credit/article");
                break;
            case R.id.main_bank_lay:
                if (!isVerfy()) return;
                main_bank_cv.setImageDrawable(getResources().getDrawable(R.drawable.checkin_select));
                main_bank_tv.setTextColor(getResources().getColor(R.color.chutou_txt));
                cur_page = 7;
                if (fragment7 == null) {
                    fragment7 = new FragmentWebview();
                    ft.add(R.id.fragment_container, fragment7, PAGE_7);
                }
                ft.show(fragment7);
                fragment7.loadUrl("https://m.rong360.com/shenzhen");
                break;
            case R.id.main_mine_lay:
                main_mine_cv.setImageDrawable(getResources().getDrawable(R.drawable.mine_select));
                main_mine_tv.setTextColor(getResources().getColor(R.color.chutou_txt));
                toggleSliding();
                break;
        }
        ft.commit();
    }

    private Boolean isVerfy() {
        if (Constant.user_info == null || !Constant.user_info.get(Constant.USER_INFO_ISAUTH).equals("1")) {
            DialogFragmentVerifyTip dialogFragmentVerifyTip = new DialogFragmentVerifyTip();
            dialogFragmentVerifyTip.setCancelable(true);
            dialogFragmentVerifyTip.show(getSupportFragmentManager(), "");
            return false;
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isForeground = false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        onSaveInstanceState = true;
    }

    private void loadData() {
        if (!isForeground) {
            KLog.v("app is not foreground");
            return;
        }
        KLog.v("app is foreground");
        isInit = true;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragment1 == null) {
            fragment1 = new FragmentMain();
            ft.add(R.id.fragment_container, fragment1, PAGE_1);
        }
        ft.hide(fragment0);
        ft.show(fragment1);
        ft.commit();
        showTab();
        initSliding();

    }

    /**
     * 当fragment1创建完成后显示tab
     */
    private void showTab() {
        main_header_lay.setVisibility(View.VISIBLE);
        main_bottom_lay.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        KLog.v("onResume" + Constant.user_info);
        isForeground = true;
        super.onResume();
      /*  if (Constant.user_info == null && fragment1 != null && !isLogin) {//登录界面从后台回到前台时调用
            refreshSliding();
            showLogin();
        }*/
      /*  if (!isInit && Constant.user_info != null && !onSaveInstanceState) {
            KLog.v("isInit" + isInit);
            loadData();
            refreshSliding();
        }*/
    }


    @Override
    public void AutoLoginFinished() {

    }

    @Override
    public void OnLoginSuccess() {
        loadData();
       // VToast.toast(context, "登陆成功");
        refreshSliding();
        isLogin = true;
        String sta = "未实名";
        switch (Integer.parseInt(Constant.user_info==null?"0":Constant.user_info.get(Constant.USER_INFO_ISAUTH))) {
            case 0:
                sta = "未实名";
                break;
            case 1:
                sta = "已实名";
                break;
            case 2:
                sta = "实名已失效";
                break;
            case 3:
                sta = "实名未通过";
                break;
        }
        main_user_login.setText(Constant.user_info == null ? "未实名用户" : sta);
        main_home_iv.setImageResource(Constant.user_info == null ? R.drawable.authority_gray : R.drawable.authority);

    }

    @Override
    public void OnRegisterSuccess() {
        loadData();
        VToast.toast(context, "注册成功,密码默认为手机后六位");
        refreshSliding();
        isLogin = true;
        main_user_login.setText("未实名");
        main_home_iv.setImageResource(Constant.user_info == null ? R.drawable.authority_gray : R.drawable.authority);
    }

    @Override
    public void OnLoginCancel() {

    }

    @Override
    public void OnFinish() {

    }

    @Override
    public void gotoPay() {
        gotoPage(5);
    }

    @Override
    public void updateMerchant() {
        if(fragment6!=null) {
            fragment6.initState();
        }
    }

    @Override
    public void finishPay() {
        gotoMain();
    }

    @Override
    protected void gotoVerify() {
        super.gotoVerify();
        gotoPage(6);
    }

    @Override
    public void showVerify() {
        super.showVerify();
        isVerfy();
    }

    @Override
    public void finishVerify() {
        gotoMain();
    }

    @Override
    public void updateSuccess() {
        finishVerify();
        if(fragment1 !=null){
            fragment1.fetchMerchantInfo();
        }
    }

    @Override
    public void openCamera(int type_) {
        type = type_;
        Intent intent = new Intent(MainActivity.this, CameraActivity.class);
        path = util.getSaveFile(context).getAbsolutePath();
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, path);
        switch (type) {
            case 0:
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                break;
            case 1:
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
                break;
            case 2:
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_GENERAL);
                break;
            case 3:
            case 4:
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_BANK_CARD);
                break;
        }
        startActivityForResult(intent, OCR_CODE_OCR_CAED);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        KLog.v("resultCode" + resultCode);
        if (resultCode == Activity.RESULT_OK) {
            String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
            if (requestCode == OCR_CODE_OCR_CAED) {
                KLog.v("onActivityResult" + path);
                if(fragment6 !=null) {
                    fragment6.uploadImg(type, path);
                }
            }
        }
    }

    @Override
    protected void gotoLogin() {
        super.gotoLogin();
        gotoPage(0);
        fragment0.initState();
        main_header_lay.setVisibility(View.GONE);
        main_bottom_lay.setVisibility(View.GONE);
    }

    @Override
    protected void gotoMain() {
        super.gotoMain();
        gotoPage(1);
        main_header_lay.setVisibility(View.VISIBLE);
        main_bottom_lay.setVisibility(View.VISIBLE);
    }

    @Override
    protected void webviewBack() {
        super.webviewBack();
        switch (cur_page) {
            case 2:
                if (fragment2 == null) return;
                fragment2.back();
                break;
            case 3:
                if (fragment3 == null) return;
                fragment3.back();
                break;
            case 4:
                if (fragment4 == null) return;
                fragment4.back();
                break;
            case 7:
                if (fragment7 == null) return;
                fragment7.back();
                break;
        }
    }

    @Override
    protected boolean webviewCanBack() {
        switch (cur_page) {
            case 2:
                if (fragment2 == null) return false;
                return fragment2.canBack();
            case 3:
                if (fragment3 == null) return false;
                return fragment3.canBack();
            case 4:
                if (fragment4 == null) return false;
                return fragment4.canBack();
            case 7:
                if (fragment7 == null) return false;
                return fragment7.canBack();
        }
        return false;
    }

    @Override
    public void doVerify() {
        gotoVerify();
    }

    @Override
    public void doPhoto(int type) {
        openCamera(type);
    }
}
