package com.heinsoft.heo;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.heinsoft.heo.activity.BaseActivity;
import com.heinsoft.heo.fragment.DialogFragmentPhotoTip;
import com.heinsoft.heo.fragment.DialogFragmentVerifyTip;
import com.heinsoft.heo.fragment.DialogFragmentWithdraw;
import com.heinsoft.heo.fragment.FragmentLogin;
import com.heinsoft.heo.fragment.FragmentMain;
import com.heinsoft.heo.fragment.FragmentPay;
import com.heinsoft.heo.fragment.FragmentRecord;
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
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateStatus;

public class MainActivity extends BaseActivity implements FragmentLogin.ILoginListener, FragmentRegisterLogin.IRegisterListener, FragmentMain.IMainListener, FragmentPay.IPayListener
        , FragmentVerify.IVerifyListener, DialogFragmentVerifyTip.IVerifyTipListtener, DialogFragmentPhotoTip.IPhotoTipListtener, FragmentRecord.IRecordListener {
    private final String COOKIE_KEY = "cookie";
    private final int OCR_CODE_OCR_CAED = 1011;
    private static int REQUEST_THUMBNAIL = 1;// 请求缩略图信号标识
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

    @Bind(R.id.main_user_level)
    TextView main_user_level;
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

    private boolean isInit = false;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OCR.getInstance().release();
    }

    private void initDate() {
        util = Utils.getInstance();
        sp = getSharedPreferences(COOKIE_KEY, 0);
        Constant.MESSAGE_PASSWORD = util.MD5(Constant.MESSAGE_PASSWORD);
        Bmob.initialize(this, Constant.PUBLIC_BMOB_KEY);
       // BmobUpdateAgent.initAppVersion(context);
        BmobUpdateAgent.setUpdateListener((updateStatus, updateInfo) -> {
            if (updateStatus == UpdateStatus.Yes) {//版本有更新

            }else if(updateStatus == UpdateStatus.No){
                KLog.v("版本无更新");
                if(Constant.MESSAGE_UPDATE_TIP.equals("")) {
                    Constant.MESSAGE_UPDATE_TIP = "<<锄头信用>>已经是最新版本";
                }else{
                    VToast.toast(context, Constant.MESSAGE_UPDATE_TIP);
                }
            }else if(updateStatus==UpdateStatus.EmptyField){//此提示只是提醒开发者关注那些必填项，测试成功后，无需对用户提示
                KLog.v("请检查你AppVersion表的必填项，1、target_size（文件大小）是否填写；2、path或者android_url两者必填其中一项。");
            }else if(updateStatus==UpdateStatus.IGNORED){
                KLog.v("该版本已被忽略更新");
            }else if(updateStatus==UpdateStatus.ErrorSizeFormat){
                KLog.v("请检查target_size填写的格式，请使用file.length()方法获取apk大小。");
            }else if(updateStatus==UpdateStatus.TimeOut){
                KLog.v("查询出错或查询超时");
            }
        });

        OCR.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                // 调用成功，返回AccessToken对象
                String token = result.getAccessToken();
                OCR.getInstance().initWithToken(getApplicationContext(), token);//初始化OCR
                KLog.v("baidu " + token);
            }

            @Override
            public void onError(OCRError error) {
                KLog.v("baidu " + error.toString());
                // 调用失败，返回OCRError子类SDKError对象
            }
        }, context, Constant.PUBLIC_OCR_KEY, Constant.PUBLIC_OCR_SECRET);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        gotoPage(0);
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
        RxView.clicks(main_mine_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> openSliding());
        RxView.clicks(main_bank_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> gotoPage(R.id.main_bank_lay));
        RxView.clicks(main_title_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> gotoMain());
    }

    private void openSliding() {
        toggle();
    }

    private void gotoManage() {

    }

    private void gotoPage(int pageId) {
        if(pageId ==R.id.main_bank_lay){
            VToast.toast(context,"敬请期待");
            return;
        }
        main_mine_cv.setImageDrawable(getResources().getDrawable(R.drawable.mine));
        main_loan_cv.setImageDrawable(getResources().getDrawable(R.drawable.loan));
        main_bank_cv.setImageDrawable(getResources().getDrawable(R.drawable.checkin));
        main_withdraw_cv.setImageDrawable(getResources().getDrawable(R.drawable.up_rank));
        main_card_cv.setImageDrawable(getResources().getDrawable(R.drawable.card_affaire));

        main_bank_tv.setTextColor(getResources().getColor(R.color.chutou_btn));
        main_withdraw_tv.setTextColor(getResources().getColor(R.color.chutou_btn));
        main_card_tv.setTextColor(getResources().getColor(R.color.chutou_btn));
        main_loan_tv.setTextColor(getResources().getColor(R.color.chutou_btn));
        main_mine_tv.setTextColor(getResources().getColor(R.color.chutou_btn));

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
        if (fragment8 != null) {
            ft.hide(fragment8);
        }
        switch (pageId) {
            case 0:
                cur_page = 0;
                if (fragment0 == null) {
                    fragment0 = new FragmentRegisterLogin();
                    ft.add(R.id.fragment_container, fragment0, PAGE_0);
                }
                if (fragment1 != null) {
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
                if (!isVerfy()) return;
                cur_page = 8;
                if (fragment8 == null) {
                    fragment8 = new FragmentRecord();
                    ft.add(R.id.fragment_container, fragment8, PAGE_8);
                }
                ft.show(fragment8);
                fragment8.initState();
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);

                break;
            case R.id.main_withdraw_lay:
                if (!isVerfy()) return;
                main_withdraw_cv.setImageDrawable(getResources().getDrawable(R.drawable.up_rank_select));
                main_withdraw_tv.setTextColor(getResources().getColor(R.color.chutou_select));

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
                main_loan_tv.setTextColor(getResources().getColor(R.color.chutou_select));
                cur_page = 2;
                if (fragment2 == null) {
                    fragment2 = new FragmentWebview();
                    ft.add(R.id.fragment_container, fragment2, PAGE_2);
                }
                ft.show(fragment2);
                fragment2.loadUrl("https://m.rong360.com/shenzhen");

                break;
            case R.id.main_card_lay:
                if (!isVerfy()) return;
                main_card_cv.setImageDrawable(getResources().getDrawable(R.drawable.card_affaire_select));
                main_card_tv.setTextColor(getResources().getColor(R.color.chutou_select));
                cur_page = 3;
                if (fragment3 == null) {
                    fragment3 = new FragmentWebview();
                    ft.add(R.id.fragment_container, fragment3, PAGE_3);
                }
                ft.show(fragment3);
                fragment3.loadUrl("http://creditcard.ecitic.com/h5/shenqing/shanghu/index.html?sid=SJUSZJMKJ3");

                break;
            case R.id.main_bank_lay:
                if (!isVerfy()) return;

                main_bank_cv.setImageDrawable(getResources().getDrawable(R.drawable.checkin_select));
                main_bank_tv.setTextColor(getResources().getColor(R.color.chutou_select));
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
                main_mine_tv.setTextColor(getResources().getColor(R.color.chutou_select));
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
        }else{
            fragment1.initState();
        }

        ft.hide(fragment0);
        ft.show(fragment1);
        ft.commit();
        showTab();
        initSliding();
        BmobUpdateAgent.update(this);
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
        KLog.v("onResume"+isForeground);

        super.onResume();
        isForeground = true;
        if (!isInit && isLogin) {//登录界面从后台回到前台时调用
            loadData();
        }
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
        main_user_level.setVisibility(View.GONE);
        loadData();
        // VToast.toast(context, "登陆成功");
        refreshSliding();
        isLogin = true;
        String sta = "未实名";
        int color = R.color.chutou_txt;
        switch (Integer.parseInt(Constant.user_info == null ? "0" : Constant.user_info.get(Constant.USER_INFO_ISAUTH))) {
            case 0:
                sta = "未实名";
                color = R.color.white;
                main_home_iv.setImageResource(R.drawable.verify_ico);
                break;
            case 1:
                color = R.color.white;
                sta = "已实名";
                main_home_iv.setImageResource(R.drawable.verify_ico);
                KLog.v("ResolveMerchantInfo" + Constant.user_info.get(Constant.LEVEL_NAME));
                main_user_level.setVisibility(View.VISIBLE);
                main_user_level.setText(Constant.user_info.get(Constant.LEVEL_NAME));
                break;
            case 2:
                color = R.color.white;
                sta = "实名已失效";
                main_home_iv.setImageResource(R.drawable.verify_ico);
                break;
            case 3:
                color = R.color.white;
                sta = "实名未通过";
                main_home_iv.setImageResource(R.drawable.verify_ico);
                break;
        }
        main_user_login.setText(Constant.user_info == null ? "未实名用户" : sta);
        main_user_login.setTextColor(getResources().getColor(color));

    }

    @Override
    public void OnRegisterSuccess() {
        loadData();
        VToast.toast(context, "注册成功,密码默认为手机后六位");
        refreshSliding();
        isLogin = true;
        main_user_login.setText("未实名");
        main_user_login.setTextColor(getResources().getColor(R.color.gold));
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

        if (fragment6 != null) {
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
    public void orderRecord() {
        super.orderRecord();
        gotoPage(8);
    }

    @Override
    public void showVerify() {
        super.showVerify();
        isVerfy();
    }

    @Override
    public void update() {
        super.update();
        BmobUpdateAgent.forceUpdate(this);
    }

    @Override
    public void finishVerify() {
        gotoMain();
    }

    @Override
    public void updateSuccess() {
        finishVerify();
        if (fragment1 != null) {
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
                startActivityForResult(intent, OCR_CODE_OCR_CAED);
                break;
            case 1:
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
                startActivityForResult(intent, OCR_CODE_OCR_CAED);
                break;
            case 2:
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, OCR_CODE_OCR_CAED);
               /* Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 启动相机
                startActivityForResult(intent1, REQUEST_THUMBNAIL);*/
                break;
            case 3:
            case 4:
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_BANK_CARD);
                startActivityForResult(intent, OCR_CODE_OCR_CAED);
                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        KLog.v("resultCode" + resultCode);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == OCR_CODE_OCR_CAED) {
                KLog.v("onActivityResult" + path);
                if (fragment6 != null) {
                    fragment6.uploadImg(type, path);
                }
            }
            if (requestCode == REQUEST_THUMBNAIL) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                String path_thumb = util.saveImageToGallery(context, bitmap);
                if (fragment6 != null) {
                    fragment6.uploadImg(type, path_thumb);
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
    public void withdraw() {
        super.withdraw();
        if(isVerfy()) {
            if(Constant.user_info.get(Constant.MY_AGENT_ID)==null || Constant.user_info.get(Constant.MY_AGENT_ID).trim().equals("")){
                VToast.toast(context,"你不是代理商");
                return;
            }
            DialogFragmentWithdraw fragmentWithdraw = new DialogFragmentWithdraw();
            fragmentWithdraw.setCancelable(false);
            fragmentWithdraw.show(getSupportFragmentManager(), "");
        }
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

    @Override
    public void finishRecord() {
        gotoMain();
    }
}
