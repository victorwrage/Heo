package com.heinsoft.heo;


import android.app.Activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.google.gson.Gson;
import com.heinsoft.heo.activity.BaseActivity;
import com.heinsoft.heo.bean.MessageBean;
import com.heinsoft.heo.bean.MessageBeanDao;
import com.heinsoft.heo.fragment.DialogFragmentMessage;
import com.heinsoft.heo.fragment.DialogFragmentVerifyTip;
import com.heinsoft.heo.fragment.DialogFragmentWithdraw;
import com.heinsoft.heo.fragment.FragmentForget;
import com.heinsoft.heo.fragment.FragmentLogin;
import com.heinsoft.heo.fragment.FragmentMain;
import com.heinsoft.heo.fragment.FragmentMessage;
import com.heinsoft.heo.fragment.FragmentPay;
import com.heinsoft.heo.fragment.FragmentRecord;
import com.heinsoft.heo.fragment.FragmentRegister;
import com.heinsoft.heo.fragment.FragmentRepayment;
import com.heinsoft.heo.fragment.FragmentSettle;
import com.heinsoft.heo.fragment.FragmentVerify;
import com.heinsoft.heo.fragment.FragmentWebview;
import com.heinsoft.heo.view.IFragmentActivity;
import com.heinsoft.heo.present.QueryPresent;
import com.heinsoft.heo.util.Constant;
import com.heinsoft.heo.util.Utils;
import com.heinsoft.heo.util.VToast;
import com.heinsoft.heo.view.IMessageView;
import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;

import org.greenrobot.greendao.query.WhereCondition;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateStatus;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.ResponseBody;

public class MainActivity extends BaseActivity implements IFragmentActivity, IMessageView {
    private final String COOKIE_KEY = "cookie";
    private final int OCR_CODE_OCR_CAED = 1011;
    private static int REQUEST_THUMBNAIL = 1;// 请求缩略图信号标识
    @Bind(R.id.main_message_lay)
    FrameLayout main_message_lay;
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

    @Bind(R.id.main_message_tv)
    TextView main_message_tv;
    MessageBeanDao messageBeanDao;

    SharedPreferences sp;
    QueryPresent present;
    Utils util;
    String path;
    int type;
    private boolean canSwitch = false;
    private String SetAlias = "setAlias";
    private static final int MSG_SET_ALIAS = 1001;
    private String SUCCESS = "0";
    public static String RECEIVE_REDIRECT_MESSAGE = "receive_redirect_message";
    BroadcastReceiver receiver_redirect = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            KLog.d("onReceive - " + intent.getAction());
            if (RECEIVE_REDIRECT_MESSAGE.equals(intent.getAction())) {
                DialogFragmentMessage dialogFragmentMessage = new DialogFragmentMessage();
                dialogFragmentMessage.setCancelable(true);
                Gson gson = new Gson();
                MessageBean msg_obj = gson.fromJson((String) bundle.get("message"), MessageBean.class);
                dialogFragmentMessage.setMessage((msg_obj));
                dialogFragmentMessage.show(getSupportFragmentManager(), "");
                fetchMessage();
            }
        }
    };

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
        unregisterReceiver(receiver_redirect);
        OCR.getInstance().release();
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    KLog.v("Set alias in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    KLog.i("Unhandled msg - " + msg.what);
            }
        }
    };

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    KLog.i(logs);
                    sp.edit().putBoolean(SetAlias, true).commit();
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    KLog.i(logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    KLog.e(logs);
            }

        }
    };


    private void initDate() {
        util = Utils.getInstance();
        sp = getSharedPreferences(COOKIE_KEY, 0);
        present = QueryPresent.getInstance(context);
        present.setView(MainActivity.this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(RECEIVE_REDIRECT_MESSAGE);
        registerReceiver(receiver_redirect, filter);

        Constant.MESSAGE_PASSWORD = util.MD5(Constant.MESSAGE_PASSWORD);
        messageBeanDao = HeoApplication.getDaoSession(context).getMessageBeanDao();
        Bmob.initialize(this, Constant.PUBLIC_BMOB_KEY);
        // BmobUpdateAgent.initAppVersion(context);
        BmobUpdateAgent.setUpdateListener((updateStatus, updateInfo) -> {
            if (updateStatus == UpdateStatus.Yes) {//版本有更新

            } else if (updateStatus == UpdateStatus.No) {
                KLog.v("版本无更新");
                if (Constant.MESSAGE_UPDATE_TIP.equals("")) {
                    Constant.MESSAGE_UPDATE_TIP = "<<锄头信用>>已经是最新版本";
                } else {
                    VToast.toast(context, Constant.MESSAGE_UPDATE_TIP);
                }
            } else if (updateStatus == UpdateStatus.EmptyField) {//此提示只是提醒开发者关注那些必填项，测试成功后，无需对用户提示
                KLog.v("请检查你AppVersion表的必填项，1、target_size（文件大小）是否填写；2、path或者android_url两者必填其中一项。");
            } else if (updateStatus == UpdateStatus.IGNORED) {
                KLog.v("该版本已被忽略更新");
            } else if (updateStatus == UpdateStatus.ErrorSizeFormat) {
                KLog.v("请检查target_size填写的格式，请使用file.length()方法获取apk大小。");
            } else if (updateStatus == UpdateStatus.TimeOut) {
                KLog.v("查询出错或查询超时");
            }
        });

        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(MainActivity.this);
        // builder.statusBarDrawable = R.drawable.ic_launcher;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
                | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
        //    builder.notificationDefaults = Notification.;  // 设置为铃声、震动、呼吸灯闪烁都要
        JPushInterface.setPushNotificationBuilder(6, builder);

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

    private void initView() {
        main_header_lay.setVisibility(View.GONE);
        main_bottom_lay.setVisibility(View.GONE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragment0 = new FragmentLogin();
        ft.add(R.id.fragment_container, fragment0, PAGE_0);
        curFragment = fragment0;
        ft.show(fragment0);
        ft.commit();

        RxView.clicks(main_withdraw_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> gotoPage(13));
        RxView.clicks(main_card_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> gotoPage(3));
        RxView.clicks(main_loan_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> gotoPage(2));
        RxView.clicks(main_mine_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> gotoPage(9));
        RxView.clicks(main_bank_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> gotoPage(11));
        RxView.clicks(main_title_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> gotoMain());
        RxView.clicks(main_message_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> gotoMessage());
    }

    private void gotoMessage() {
        gotoPage(12);
    }

    private void fetchMessage() {
        if (Constant.message == null) {
            Constant.message = new ArrayList<>();
        }
        Constant.message.clear();
        Constant.message.addAll(messageBeanDao.loadAll());
        present.initRetrofit(Constant.URL_BAIBAO, false);
        present.QueryMessage();
    }


    private void gotoPage(int pageId) {

        if (cur_page == pageId) {
            return;
        }
        if (curFragment instanceof FragmentSettle && !canSwitch) {
            if (!((FragmentSettle) curFragment).isVerifyed()) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title("提示")
                        .content("(商户入驻)信息还未提交，确定退出？")
                        .positiveText(R.string.btn_confirm)
                        .negativeText(R.string.btn_cancel)
                        .autoDismiss(true)
                        .cancelable(false)
                        .onNegative((dialog, which) -> {
                            canSwitch = false;
                        })
                        .onPositive((materialDialog, dialogAction) -> {
                            canSwitch = true;
                            gotoPage(pageId);
                        })
                        .show();
                return;
            }
        }
        if (curFragment instanceof FragmentVerify && !canSwitch) {
            if (!((FragmentVerify) curFragment).isVerifyed() && pageId != 1) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title("提示")
                        .content("(实名认证)信息还未提交，确定退出？")
                        .positiveText(R.string.btn_confirm)
                        .negativeText(R.string.btn_cancel)
                        .autoDismiss(true)
                        .cancelable(false)
                        .onNegative((dialog, which) -> {
                            canSwitch = false;
                        })
                        .onPositive((materialDialog, dialogAction) -> {
                            canSwitch = true;
                            gotoPage(pageId);
                        })
                        .show();
                return;
            }
        }

        canSwitch = false;
        SelectTab();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (pageId != 9) {
            ft.hide(curFragment);
            if (fragment1 != null && pageId != R.id.main_mine_lay) {
                ft.hide(fragment1);
            }
        }
        if (curFragment instanceof FragmentWebview && pageId != 9) {
            ft.remove(curFragment);
            fragment2 = null;
        }
        switch (pageId) {
            case 0:
                if (fragment0 == null) {
                    fragment0 = new FragmentLogin();
                    ft.add(R.id.fragment_container, fragment0, PAGE_0);
                } else {
                    fragment0.RefreshState();
                }
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                curFragment = fragment0;
                ft.show(fragment0);
                break;
            case 1:
                if (fragment1 == null) {
                    fragment1 = new FragmentMain();
                    initSliding();
                    ft.add(R.id.fragment_container, fragment1, PAGE_1);
                } else {
                    fragment1.RefreshState();
                }
                curFragment = fragment1;
                main_header_lay.setVisibility(View.VISIBLE);
                main_bottom_lay.setVisibility(View.VISIBLE);
                ft.show(fragment1);
                break;
            case 2:
                if (!isVerify()) return;
                main_loan_cv.setImageDrawable(getResources().getDrawable(R.drawable.loan_select));
                main_loan_tv.setTextColor(getResources().getColor(R.color.chutou_select));
                if (fragment2 == null) {
                    fragment2 = new FragmentWebview();
                    fragment2.setTitle("贷款");
                    ft.add(R.id.fragment_container, fragment2, PAGE_2);
                } else {
                    fragment2.setTitle("贷款");
                    fragment2.RefreshState();
                }
                curFragment = fragment2;
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.VISIBLE);
                ft.show(fragment2);
                fragment2.loadUrl("https://m.rong360.com/shenzhen");
                break;
            case 3:
                if (!isVerify()) return;
                main_card_cv.setImageDrawable(getResources().getDrawable(R.drawable.card_affaire_select));
                main_card_tv.setTextColor(getResources().getColor(R.color.chutou_select));
                if (fragment2 == null) {
                    fragment2 = new FragmentWebview();
                    fragment2.setTitle("办卡");
                    ft.add(R.id.fragment_container, fragment2, PAGE_2);
                } else {
                    fragment2.setTitle("办卡");
                    fragment2.RefreshState();
                }
                curFragment = fragment2;
                ft.show(fragment2);
                fragment2.loadUrl("http://creditcard.ecitic.com/h5/shenqing/shanghu/index.html?sid=SJUSZJMKJ3");
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.VISIBLE);
                break;
            case 4:
                if (!isVerify()) return;
                main_withdraw_cv.setImageDrawable(getResources().getDrawable(R.drawable.up_rank_select));
                main_withdraw_tv.setTextColor(getResources().getColor(R.color.chutou_select));
                if (fragment2 == null) {
                    fragment2 = new FragmentWebview();
                    fragment2.setTitle("提额");
                    ft.add(R.id.fragment_container, fragment2, PAGE_2);
                } else {
                    fragment2.setTitle("提额");
                    fragment2.RefreshState();
                }
                curFragment = fragment2;
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.VISIBLE);
                ft.show(fragment2);
                fragment2.loadUrl("https://m.rong360.com/credit/article");

                break;
            case 5:
                if (!isVerify()) return;

                if (fragment3 == null) {
                    fragment3 = new FragmentPay();
                    ft.add(R.id.fragment_container, fragment3, PAGE_3);
                } else {
                    fragment3.RefreshState();
                }
                curFragment = fragment3;
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                ft.show(fragment3);
                break;

            case 6:
                if (fragment4 == null) {
                    fragment4 = new FragmentVerify();
                    ft.add(R.id.fragment_container, fragment4, PAGE_4);
                } else {
                    fragment4.RefreshState();
                }
                curFragment = fragment4;
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                ft.show(fragment4);
                break;
            case 7:
                if (!isVerify()) return;
                main_bank_cv.setImageDrawable(getResources().getDrawable(R.drawable.checkin_select));
                main_bank_tv.setTextColor(getResources().getColor(R.color.chutou_select));
                if (fragment2 == null) {
                    fragment2 = new FragmentWebview();
                    fragment2.setTitle("入驻");
                    ft.add(R.id.fragment_container, fragment2, PAGE_2);
                } else {
                    fragment2.RefreshState();
                }
                curFragment = fragment2;
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.VISIBLE);
                ft.show(fragment2);
                fragment2.loadUrl("https://m.rong360.com/shenzhen");

                break;
            case 8:
                if (!isVerify()) return;
                if (fragment5 == null) {
                    fragment5 = new FragmentRecord();
                    ft.add(R.id.fragment_container, fragment5, PAGE_5);
                } else {
                    fragment5.RefreshState();
                }
                curFragment = fragment5;
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                ft.show(fragment5);
                break;
            case 9:
                main_mine_cv.setImageDrawable(getResources().getDrawable(R.drawable.mine_select));
                main_mine_tv.setTextColor(getResources().getColor(R.color.chutou_select));
                openSliding();
                break;
            case 10:
                if (fragment6 == null) {
                    fragment6 = new FragmentRegister();
                    ft.add(R.id.fragment_container, fragment6, PAGE_6);
                } else {
                    fragment6.RefreshState();
                }
                curFragment = fragment6;
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                ft.show(fragment6);
                break;
            case 11:
                if (!isVerify()) return;
                main_bank_cv.setImageDrawable(getResources().getDrawable(R.drawable.checkin_select));
                main_bank_tv.setTextColor(getResources().getColor(R.color.chutou_select));
                if (fragment7 == null) {
                    fragment7 = new FragmentSettle();
                    ft.add(R.id.fragment_container, fragment7, PAGE_7);
                } else {
                    fragment7.RefreshState();
                }
                curFragment = fragment7;
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.VISIBLE);
                ft.show(fragment7);
                break;
            case 12:
                if (fragment8 == null) {
                    fragment8 = new FragmentMessage();
                    ft.add(R.id.fragment_container, fragment8, PAGE_8);
                } else {
                    fragment8.RefreshState();
                }
                curFragment = fragment8;
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                ft.show(fragment8);
                break;
            case 13:
                if (fragment9 == null) {
                    fragment9 = new FragmentRepayment();
                    ft.add(R.id.fragment_container, fragment9, PAGE_9);
                } else {
                    fragment9.RefreshState();
                }
                curFragment = fragment9;
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                ft.show(fragment9);
                break;
            case 14:
                if (fragment10 == null) {
                    fragment10 = new FragmentForget();
                    ft.add(R.id.fragment_container, fragment10, PAGE_10);
                } else {
                    fragment10.RefreshState();
                }
                curFragment = fragment10;
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                ft.show(fragment10);
                break;
        }
        cur_page = pageId;
        ft.commitNowAllowingStateLoss();
    }

    private void SelectTab() {
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
    }

    private Boolean isVerify() {
        if (Constant.user_info == null || !Constant.user_info.get(Constant.USER_INFO_ISAUTH).equals("1")) {
            DialogFragmentVerifyTip dialogFragmentVerifyTip = new DialogFragmentVerifyTip();
            dialogFragmentVerifyTip.setCancelable(true);
            dialogFragmentVerifyTip.show(getSupportFragmentManager(), "");
            return false;
        }
        return true;
    }


    @Override
    public void gotoPay() {
        super.gotoPay();
        gotoPage(5);
    }


    @Override
    public void gotoRegister() {
        super.gotoRegister();
        gotoPage(10);
    }

    @Override
    public void NotifyMessage(MessageBean messageBean_) {

        messageBeanDao.insertOrReplace(messageBean_);
        int c = 0;
        for (MessageBean messageBean : Constant.message) {
            if (!messageBean.getIs_read()) {
                c++;
            }
        }
        if (c == 0) {
            main_message_tv.setVisibility(View.GONE);
        } else {
            main_message_tv.setVisibility(View.VISIBLE);
            main_message_tv.setText(c + "");
        }
    }

    @Override
    public void gotoForgot() {
        super.gotoForgot();
        gotoPage(14);
    }

    @Override
    public void gotoVerify() {
        super.gotoVerify();
        gotoPage(6);
    }

    @Override
    public void showVerify() {
        super.showVerify();
        isVerify();
    }

    @Override
    protected void closeSliding() {
        super.closeSliding();
    }

    @Override
    protected void bottomToMain() {
        super.bottomToMain();
        if (cur_page == 9) {
            cur_page = 1;
            main_mine_cv.setImageDrawable(getResources().getDrawable(R.drawable.mine));
            main_mine_tv.setTextColor(getResources().getColor(R.color.chutou_btn));
        }
    }

    @Override
    public void openCamera(int type_) {
        super.openCamera(type_);
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
                if (fragment4 != null) {
                    fragment4.uploadImg(type, path);
                }
            }
            if (requestCode == REQUEST_THUMBNAIL) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                String path_thumb = util.saveImageToGallery(context, bitmap);
                if (fragment4 != null) {
                    fragment4.uploadImg(type, path_thumb);
                }
            }
        }
    }

    @Override
    public void gotoLogin() {
        super.gotoLogin();
        gotoPage(0);
    }

    @Override
    public void gotoRecord() {
        super.gotoRecord();
        gotoPage(8);
    }

    @Override
    public void showWithdraw() {
        super.showWithdraw();
        if (isVerify()) {
            if (Constant.user_info.get(Constant.MY_AGENT_ID) == null || Constant.user_info.get(Constant.MY_AGENT_ID).trim().equals("")) {
                VToast.toast(context, "身份不符合");
                return;
            }
            DialogFragmentWithdraw fragmentWithdraw = new DialogFragmentWithdraw();
            fragmentWithdraw.setCancelable(false);
            fragmentWithdraw.show(getSupportFragmentManager(), "");
        }
    }

    @Override
    public void gotoMain() {
        if (curFragment instanceof FragmentLogin && Constant.user_info != null) {
            KLog.v(sp.getBoolean(SetAlias, false) + "");
            if (!sp.getBoolean(SetAlias, false)) {
                KLog.v("MSG_SET_ALIAS");
                mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, Constant.user_info.get(Constant.MERCHANT_ID)));//设置推送别名
            }
            fetchMessage();
        }
        if (curFragment instanceof FragmentVerify) {
            fragment1.fetchMerchantInfo();
        }
        super.gotoMain();
        gotoPage(1);

    }

    @Override
    public void doPhoto(int type) {
        super.doPhoto(type);
        openCamera(type);
    }

    @Override
    public void ResolveMessageInfo(ResponseBody info) {
        if (info.source() == null) {
            return;
        }
        JSONObject jsonObject = null;
        try {
            String res = info.string();
            KLog.v(res);
            jsonObject = new JSONObject(res);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        if (jsonObject.optString(Constant.ERRCODE).equals(SUCCESS)) {

            String str = jsonObject.optString("list");
            ArrayList<MessageBean> temp = new ArrayList<>();
            temp.addAll(JSON.parseArray(str, MessageBean.class));
            /*temp.addAll(JSON.parseArray(str, MessageBean.class));
            temp.addAll(JSON.parseArray(str, MessageBean.class));
            temp.get(1).setId("10");
            temp.get(2).setId("11");*/
            for (MessageBean messageBean : temp) {
                //      KLog.v("ResolveMessage-" + messageBean.getId() + "------" + messageBean.getIs_read());
                WhereCondition wc1 = MessageBeanDao.Properties.Id.eq(messageBean.getId() == null ? "-1" : messageBean.getId());
                MessageBean add_temp = messageBeanDao.queryBuilder().where(wc1).
                        orderAsc(MessageBeanDao.Properties.Send_time).limit(1).unique();

                if (add_temp == null) {
                    messageBeanDao.insert(messageBean);
                    Constant.message.add(messageBean);
                } else {
                    //              KLog.v("ResolveMessage---add_temp:" + add_temp.getId() + "--" + add_temp.getIs_read() + "--" + add_temp.getIdx());
                }
            }
            KLog.v("ResolveMessage---Constant.message:" + Constant.message.size());
            ArrayList<MessageBean> d_temp = new ArrayList<>();
            ArrayList<MessageBean> d_temp2 = new ArrayList<>();
            d_temp2.addAll(Constant.message);
            for (MessageBean messageBean : Constant.message) {
                for (MessageBean messageBean2 : temp) {
                    if (messageBean.getId().equals(messageBean2.getId())) {
                        d_temp.add(messageBean);
                        break;
                    }
                }
            }
            //         KLog.v("ResolveMessage---d_temp2:" + d_temp2.size());
            //         KLog.v("ResolveMessage---d_temp:" + d_temp.size());
            d_temp2.removeAll(d_temp);

            for (MessageBean messageBean : d_temp2) {
                messageBeanDao.delete(messageBean);
                Constant.message.remove(messageBean);
            }
            KLog.v("ResolveMessage:" + Constant.message.size());
            int c = 0;
            for (MessageBean messageBean : Constant.message) {
                if (!messageBean.getIs_read()) {
                    c++;
                }
            }
            if (c == 0) {
                main_message_tv.setVisibility(View.GONE);
            } else {
                main_message_tv.setVisibility(View.VISIBLE);
                main_message_tv.setText(c + "");
            }
        }

    }
}
