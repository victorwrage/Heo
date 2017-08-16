
package com.heinsoft.heo.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heinsoft.heo.R;
import com.heinsoft.heo.bean.HeoCodeResponse;
import com.heinsoft.heo.bean.HeoMerchantInfoResponse;
import com.heinsoft.heo.present.QueryPresent;
import com.heinsoft.heo.util.Constant;
import com.heinsoft.heo.util.Utils;
import com.heinsoft.heo.util.VToast;
import com.heinsoft.heo.view.IUserView;
import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.update.BmobUpdateAgent;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


/**
 * @ClassName: DialogFragmentAddTag
 * @Description:TODO(登录界面)
 * @author: xiaoyl
 * @date: 2013-07-20 下午6:38:07
 */
public class FragmentRegisterLogin extends BaseFragment implements IUserView {
    private IRegisterListener callBack;
    ProgressDialog progressDialog;
    private final int STEP_MERCHAT_ID = 0;
    private final int STEP_PHONE_NO = STEP_MERCHAT_ID + 1;
    private final int STEP_CODE = STEP_PHONE_NO + 1;
    private final int STEP_PASSWORD = STEP_CODE + 1;
    private final int STEP_COMPLETE = STEP_PASSWORD + 1;

    private final String STR_PHONE = "phone_num";
    private final String STR_USER_NAME = "username";
    private final String STR_PW = "password";
    private final String COOKIE_KEY = "cookie";

    private final String STATUS_SUCCESS = "0";
    private final String STATUS_FAIL = "1002";

    private final int TIMER_CODE = 0;
    private final int TIMER_QCODE = TIMER_CODE + 1;

    private final int TYPE_REGISTER = 0;
    private final int TYPE_LOGIN = TYPE_REGISTER + 1;


    boolean isRegisterOrForgot = false;
    private Disposable disposable_q;
    private Disposable disposable;
    @Bind(R.id.login_phone_et)
    EditText login_phone_et;
    @Bind(R.id.login_code_et)
    EditText login_code_et;
    @Bind(R.id.login_pw1_et)
    EditText login_pw1_et;
    @Bind(R.id.login_pw2_et)
    EditText login_pw2_et;
    @Bind(R.id.login_invite_et)
    EditText login_invite_et;
    @Bind(R.id.login_password_et)
    EditText login_password_et;

    @Bind(R.id.login_login_btn)
    Button login_login_btn;
    @Bind(R.id.login_register_tv)
    TextView login_register_tv;
    @Bind(R.id.login_login_tv)
    TextView login_login_tv;

    @Bind(R.id.login_time_tv)
    TextView login_time_tv;

    @Bind(R.id.login_prefix_tv)
    TextView login_prefix_tv;
    @Bind(R.id.login_forget_tv)
    TextView login_forget_tv;

    @Bind(R.id.update_iv)
    ImageView update_iv;
    @Bind(R.id.update_tv)
    TextView update_tv;

    @Bind(R.id.login_qcode_et)
    EditText login_qcode_et;
    @Bind(R.id.login_qcode_iv)
    ImageView login_qcode_iv;
    @Bind(R.id.login_qcode_lay)
    LinearLayout login_qcode_lay;
    @Bind(R.id.login_pw_lay)
    LinearLayout login_pw_lay;
    @Bind(R.id.login_header)
    LinearLayout login_header;

    @Bind(R.id.login_auto_activity)
    RelativeLayout login_auto_activity;
    @Bind(R.id.login_login_activity)
    RelativeLayout login_login_activity;

    @Bind(R.id.header_btn)
    ImageView header_btn;
    @Bind(R.id.header_title_icon)
    ImageView header_title_icon;
    @Bind(R.id.login_remember_btn)
    CheckBox login_remember_btn;
    int cur_step = STEP_PASSWORD;
    private int CUR_TYPE = TYPE_LOGIN;
    SharedPreferences sp;
    QueryPresent present;
    Utils util;
    String merchant_id;
    private Map<String, String> temp_info;
    public FragmentRegisterLogin() {

    }

    @Override
    public void onAttach(Context activity) {
        try {
            callBack = (IRegisterListener) activity;
        } catch (ClassCastException e) {

        }
        super.onAttach(activity);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    public void initState(){
        if (disposable != null) {
            disposable.dispose();
        }
        Constant.MESSAGE_CODE = "";
        login();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_layout, container,
                false);
        ButterKnife.bind(FragmentRegisterLogin.this, view);
        util = Utils.getInstance();
        present = QueryPresent.getInstance(getActivity());
        present.setView(FragmentRegisterLogin.this);
        sp = getActivity().getSharedPreferences(COOKIE_KEY, 0);
        temp_info = new HashMap<>();
        login_phone_et.setHint("请输入手机号码");
        login_phone_et.setInputType(InputType.TYPE_CLASS_PHONE);
        RxView.clicks(login_login_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> nextStep());
        //  RxView.clicks(login_shut_iv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> callBack.OnFinish());
        RxView.clicks(login_register_tv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> register());
        RxView.clicks(login_login_tv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> login());
        RxView.clicks(login_time_tv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> fetchCode());//
        // RxView.clicks(login_qcode_iv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> fetchQCode());
        RxView.clicks(login_forget_tv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> forget());
        header_btn.setVisibility(View.GONE);
        header_title_icon.setVisibility(View.VISIBLE);

        RxView.clicks(update_iv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s ->  BmobUpdateAgent.forceUpdate(getContext()));
        update_tv.setText("当前版本"+util.getAppVersionName(getContext()));
        return view;
    }

    private void forget() {
        isRegisterOrForgot = true;
        callBack.OnFinish();
    }

    private void fetchQCode() {
        //   present.initRetrofitOrigin(Constant.URL_BAIBAO);
        //    present.QueryQcode(temp_info.get(STR_PHONE));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        login_phone_et.postDelayed(() -> autoLogin(),2000);

    }

    private void autoLogin(){
        if (sp.getBoolean(Constant.USER_INFO_SAVE, false)) {
            String user_phone = sp.getString(Constant.USER_INFO_PHONE, "");
            String user_pw = sp.getString(Constant.USER_INFO_PW, "");
            //KLog.v("USER_INFO_SAVE"+user_phone+"--"+user_pw);
            login_phone_et.setText(user_phone);
            login_phone_et.selectAll();
            login_password_et.setText(user_pw);
            if (!user_phone.equals("") && !user_pw.equals("")) {
               // showWaitDialog("正在登录");
                login_login_btn.setEnabled(false);
                login_login_btn.setText("正在登录");
                present.initRetrofit(Constant.URL_BAIBAO, false);
                HashMap<String, String> StringA1 = new HashMap<>();
                StringA1.put(Constant.AID_STR, Constant.AID);
                StringA1.put(STR_USER_NAME, user_phone);
                StringA1.put(STR_PW, user_pw);
                String sign1 = util.getSign(StringA1);
                StringA1.put(Constant.SIGN, sign1);
                present.QueryLogin(Constant.AID, sign1, StringA1.get(STR_USER_NAME), StringA1.get(STR_PW));
            }else{
                login_phone_et.setFocusable(true);
                login_phone_et.setFocusableInTouchMode(true);
                login_phone_et.requestFocus();
                login_auto_activity.setVisibility(View.GONE);
                login_login_activity.setVisibility(View.VISIBLE);
            }
        }else{
            login_auto_activity.setVisibility(View.GONE);
            login_login_activity.setVisibility(View.VISIBLE);
        }

    }
    private void fetchCode() {
        if (!util.verifyPhone(login_phone_et.getText().toString().trim())) {
            VToast.toast(getActivity(), "请输入正确的手机号码!");
            return;
        }
        showWaitDialog("正在获取验证码");
        login_login_btn.setEnabled(false);
        login_login_btn.setText("正在拉取验证码");
        Long msgId = System.currentTimeMillis();
        Constant.MESSAGE_CODE = util.getCode(4);

        String str_content = "【" + getContext().getString(R.string.app_name) + "】您的验证码为：" + Constant.MESSAGE_CODE + Constant.MESSAGE_CONTENT_PREFIX;
        KLog.v(Constant.MESSAGE_CODE);
        present.initRetrofitSendMessage(Constant.URL_MESSAGE);
        present.QueryCode(login_phone_et.getText().toString().trim(), msgId + "", str_content);
        login_time_tv.setEnabled(false);
    }

    /**
     * 显示登陆界面
     */
    private void login() {
        login_auto_activity.setVisibility(View.GONE);
        login_login_activity.setVisibility(View.VISIBLE);
        CUR_TYPE = TYPE_LOGIN;
        cur_step = STEP_PASSWORD;
        login_login_btn.setText("登录");
        login_time_tv.setVisibility(View.GONE);
        login_password_et.setVisibility(View.VISIBLE);
        login_invite_et.setVisibility(View.GONE);
        login_code_et.setVisibility(View.GONE);
        login_login_tv.setVisibility(View.GONE);
        login_register_tv.setVisibility(View.VISIBLE);
        login_remember_btn.setVisibility(View.VISIBLE);
        login_phone_et.setEnabled(true);
        login_login_btn.setEnabled(true);
    }

    private void register() {
        CUR_TYPE = TYPE_REGISTER;
        cur_step = STEP_PASSWORD;
        login_login_btn.setText("注册");
        login_time_tv.setVisibility(View.VISIBLE);
        login_time_tv.setText("点击获取验证码");

        login_login_tv.setVisibility(View.VISIBLE);
        login_password_et.setVisibility(View.GONE);

        login_register_tv.setVisibility(View.GONE);
        login_remember_btn.setVisibility(View.GONE);
        if (Constant.MESSAGE_CODE.equals("")) {
            login_login_btn.setEnabled(false);
            login_code_et.setVisibility(View.GONE);
            login_invite_et.setVisibility(View.GONE);
            login_phone_et.setEnabled(true);
        } else {
            login_login_btn.setEnabled(true);
            login_code_et.setVisibility(View.VISIBLE);
            login_invite_et.setVisibility(View.VISIBLE);
            login_phone_et.setEnabled(true);
        }

    }

    private void nextStep() {
        SharedPreferences.Editor editor;
        switch (cur_step) {
            case STEP_PASSWORD:
                temp_info.put(STR_PHONE, login_phone_et.getText().toString());
                if (!util.verifyPhone(login_phone_et.getText().toString())) {
                    VToast.toast(getActivity(), "请输入正确的手机号码!");
                    return;
                }
                switch (CUR_TYPE) {
                    case TYPE_REGISTER:
                        if (!login_code_et.getText().toString().trim().equals(Constant.MESSAGE_CODE)) {
                            VToast.toast(getActivity(), "短信验证码错误!");
                            return;
                        }
                        showWaitDialog("正在注册");
                        login_login_btn.setEnabled(false);
                        doLogin();
                        break;
                    case TYPE_LOGIN:
                        if (login_password_et.getText().toString().trim().equals("")) {
                            VToast.toast(getActivity(), "请输入密码!");
                            return;
                        }
                        showWaitDialog("正在登录");
                        login_login_btn.setEnabled(false);
                        login_login_btn.setText("正在登录");

                        doLogin();
                        break;
                }
                break;
        }
    }

    private void doLogin(){
        present.initRetrofit(Constant.URL_BAIBAO, false);
        HashMap<String, String> StringA1 = new HashMap<>();
        StringA1.put(Constant.AID_STR, Constant.AID);
        StringA1.put(STR_USER_NAME, login_phone_et.getText().toString());
        StringA1.put(STR_PW, login_password_et.getText().toString());
        temp_info.put(STR_PW, login_password_et.getText().toString());
        String sign1 = util.getSign(StringA1);
        StringA1.put(Constant.SIGN, sign1);
        present.QueryLogin(Constant.AID, sign1, StringA1.get(STR_USER_NAME), StringA1.get(STR_PW));
        InputMethodManager inputManager =
                (InputMethodManager) login_phone_et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(login_phone_et.getWindowToken(), 0);
    }

    @Override
    public void ResolveLoginInfo(HeoCodeResponse info) {
        KLog.v(info.toString());
        hideWaitDialog();

        if (disposable != null) {
            disposable.dispose();
        }
        login_time_tv.setEnabled(true);
        login_time_tv.setText("重新获取");

        login_login_btn.setEnabled(true);
        if(CUR_TYPE==TYPE_REGISTER){
            login_login_btn.setText("注册");
        }else {
            login_code_et.setText("");
            login_login_btn.setText("登录");
        }
        if (info.getErrcode() == null) {
            login_auto_activity.setVisibility(View.GONE);
            login_login_activity.setVisibility(View.VISIBLE);
            VToast.toast(getActivity(), "网络错误");
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        if (!info.getErrcode().equals(STATUS_SUCCESS)) {
            KLog.v("CUR_TYPE"+CUR_TYPE);
            switch(CUR_TYPE) {
                case TYPE_REGISTER:
                    editor = sp.edit();
                    editor.putString(Constant.USER_INFO_PHONE, temp_info.get(STR_PHONE));
                    editor.putString(Constant.USER_INFO_PW, temp_info.get(STR_PHONE).substring(5));
                    editor.putString(Constant.USER_INFO_INVITE_CODE, login_invite_et.getText().toString().trim());
                    editor.putBoolean(Constant.USER_INFO_SAVE, true);
                    editor.commit();
                    VToast.toast(getActivity(), "请完成实名以操作APP");
                    callBack.OnRegisterSuccess();
                    break;
                case TYPE_LOGIN:
                    if (sp.getString(Constant.USER_INFO_PHONE,"").equals(login_phone_et.getText().toString().trim())
                            && sp.getString(Constant.USER_INFO_PW,"").equals(login_password_et.getText().toString().trim())) {
                        // VToast.toast(getContext(), "请完成实名以操作APP!");
                        callBack.OnLoginSuccess();
                    } else {
                        login_auto_activity.setVisibility(View.GONE);
                        login_login_activity.setVisibility(View.VISIBLE);
                        VToast.toast(getContext(), info.getErrmsg());
                    }
                    if (info.getErrcode().equals("4012") && !sp.getString(Constant.USER_INFO_PHONE,"").equals(login_phone_et.getText().toString().trim())) {
                        login_auto_activity.setVisibility(View.GONE);
                        login_login_activity.setVisibility(View.VISIBLE);
                        VToast.toast(getContext(), info.getErrmsg());
                        return;
                    }
                    KLog.v(sp.getString(Constant.USER_INFO_PHONE,"")+"----"+ sp.getString(Constant.USER_INFO_PW,""));
                    break;
            }
            return;
        }
        KLog.v("ResolveLoginInfo");
        Constant.user_info = new HashMap<>();
        Constant.user_info.put(Constant.USER_INFO_PHONE, login_phone_et.getText().toString().trim());
        Constant.user_info.put(Constant.USER_INFO_USER_NAME, info.getUsername());
        Constant.user_info.put(Constant.USER_INFO_MERCHANT_ID, info.getMerchant_id());
        Constant.user_info.put(Constant.USER_INFO_PW, login_password_et.getText().toString().trim());
        merchant_id = info.getMerchant_id();
        editor.putString(Constant.USER_INFO_PHONE, login_phone_et.getText().toString().trim());
        editor.putString(Constant.USER_INFO_PW, login_password_et.getText().toString().trim());
        editor.putString(Constant.USER_INFO_MERCHANT_ID, info.getMerchant_id());
        editor.putString(Constant.USER_INFO_NAME, info.getUsername());
        editor.putBoolean(Constant.USER_INFO_SAVE, login_remember_btn.isChecked());
        editor.commit();

        showWaitDialog("正在获取商户信息");
        fetchMerchantInfo();
    }

    private void fetchMerchantInfo() {
        HashMap<String, String> StringA1 = new HashMap<>();
        StringA1.put(Constant.AID_STR, Constant.AID);
        StringA1.put(Constant.MERCHANT_ID, merchant_id);

        String sign1 = util.getSign(StringA1);
        StringA1.put(Constant.SIGN, sign1);

        present.initRetrofit(Constant.URL_BAIBAO, false);
        present.QueryMerchant(Constant.AID, StringA1.get(Constant.SIGN), StringA1.get(Constant.MERCHANT_ID));
    }

    @Override
    public void ResolveRegisterInfo(HeoCodeResponse info) {
        login_login_btn.setEnabled(true);
        login_login_btn.setText("注册");
        if (info == null || info.getErrcode() == null) {
            VToast.toast(getActivity(), "网络错误，请重试!");
        } else {
            KLog.v(info.toString());
            VToast.toast(getActivity(), info.getErrmsg());
            if (info.getErrcode().equals(STATUS_SUCCESS)) {
                login_code_et.setText("");
                if (disposable != null) {
                    disposable.dispose();
                }
                login_time_tv.setEnabled(true);
                login_time_tv.setText("重新获取");
                login_phone_et.setText("");
                VToast.toast(getActivity(), "请完成实名以操作APP!");
                cur_step = STEP_COMPLETE;
                isRegisterOrForgot = true;

                Constant.user_info.put(Constant.USER_INFO_ISAUTH, "0");
                callBack.OnRegisterSuccess();
            }
        }
    }

    @Override
    public void ResolveQcodeInfo(ResponseBody info) {
        login_login_btn.setEnabled(true);
        login_login_btn.setText("登录");
        String res;
        try {
            res = info.string().substring(24).trim();
            res = res.replaceAll("\\\\r\\\\n", "");
        } catch (IOException e) {
            e.printStackTrace();
            VToast.toast(getActivity(), "验证码图片解析错误");
            return;
        }
        if (res != null) {
            login_qcode_lay.setVisibility(View.VISIBLE);
            Bitmap bitmap = util.stringtoBitmap(res);
            login_qcode_iv.setImageBitmap(bitmap);
          //  timer(TIMER_QCODE);
        } else {
            VToast.toast(getActivity(), "验证码图片解析错误");
        }
    }

    @Override
    public void ResolveCodeInfo(ResponseBody info) {
        KLog.v(info.contentLength() + "");
        hideWaitDialog();
        login_time_tv.setEnabled(true);
        login_login_btn.setText("注册");
        String rst = null;
        try {
            if (info == null || info.source() == null) {
                VToast.toast(getActivity(), "网络错误，请重试!");
                return;
            }
            rst = info.string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (rst == null) {
            VToast.toast(getActivity(), "网络错误，请重试!");
        } else {
            if (rst.equals(Constant.MESSAGE_SEND_SUCCESS + "")) {
                //  VToast.toast(getActivity(), "获取成功");

                login_login_btn.setEnabled(true);
                login_time_tv.setEnabled(false);
                login_phone_et.setEnabled(false);
                login_code_et.setVisibility(View.VISIBLE);
                login_invite_et.setVisibility(View.VISIBLE);
                InputMethodManager inputManager =
                        (InputMethodManager) login_code_et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInputFromInputMethod(login_code_et.getWindowToken(), 0);
             //   timer(TIMER_CODE);
                cur_step = STEP_PASSWORD;
            }
        }
    }

    @Override
    public void ResolveForgetInfo(HeoCodeResponse info) {

    }

    @Override
    public void ResolveAlterInfo(HeoCodeResponse info) {

    }

    @Override
    public void ResolveEmailInfo(HeoCodeResponse info) {

    }

    @Override
    public void ResolveVerifyInfo(HeoCodeResponse info) {

    }

    @Override
    public void ResolveCodeVerifyInfo(HeoCodeResponse info) {

    }

    @Override
    public void ResolvePhoneChangeInfo(HeoCodeResponse info) {

    }

    @Override
    public void ResolveMerchantInfo(HeoCodeResponse info) {
        KLog.v(info.toString());
        hideWaitDialog();
        if (info.getErrcode() == null) {
            VToast.toast(getContext(), "网络错误,不能获取商户信息");
            return;
        }
        if (info.getErrcode().equals(SUCCESS)) {
            if (info.getContent() == null && info.getContent().get(0) == null) {
                KLog.v("商户账号无信息");
                return;
            }
            HeoMerchantInfoResponse merchant = info.getContent().get(0);

            Constant.user_info.put(Constant.AGENT_ID, merchant.getAgent_id());
            String sta = merchant.getState();
            Constant.user_info.put(Constant.USER_INFO_ISAUTH, sta == null ? "0" : sta);
            Constant.user_info.put(Constant.NAME, merchant.getName());
            Constant.user_info.put(Constant.CONTACT, merchant.getContact());
            Constant.user_info.put(Constant.RATE, merchant.getRate());
            Constant.user_info.put(Constant.MERCHANT_ID, merchant.getMerchant_id());
            Constant.user_info.put(Constant.USER_INFO_IDCARD, merchant.getId_card());
            Constant.user_info.put(Constant.LEVEL_NAME, merchant.getLevel_name());
            Constant.user_info.put(Constant.BANK, merchant.getBank());
            Constant.user_info.put(Constant.BANK_ACCOUNT_NAME, merchant.getBank_account_name());
            Constant.user_info.put(Constant.BANK_ACCOUNT, merchant.getBank_account());
            Constant.user_info.put(Constant.SUB_BRANCH, merchant.getSub_branch());
            Constant.user_info.put(Constant.BANKFIRM, merchant.getBankfirm());
            Constant.user_info.put(Constant.ID_CARD_NAME, merchant.getCreate_name());

            Constant.user_info.put(Constant.ACCOUNT, merchant.getUsername());
            Constant.user_info.put(Constant.USER_INFO_PW, merchant.getPassword());
            Constant.user_info.put(Constant.PHONE, merchant.getPhone());
            Constant.user_info.put(Constant.PROVINCE, merchant.getProvince());
            Constant.user_info.put(Constant.CITY, merchant.getCity());
            Constant.user_info.put(Constant.ADDRESS, merchant.getAddress());

            Constant.user_info.put(Constant.PIC_1, merchant.getImages().getPic_1());
            Constant.user_info.put(Constant.PIC_2, merchant.getImages().getPic_2());
            Constant.user_info.put(Constant.PIC_3, merchant.getImages().getPic_3());
            Constant.user_info.put(Constant.PIC_4, merchant.getImages().getPic_4());
            Constant.user_info.put(Constant.PIC_5, merchant.getImages().getPic_5());
            callBack.OnLoginSuccess();
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        if (disposable != null) {
            disposable.dispose();
        }
        if (disposable_q != null) {
            disposable_q.dispose();
        }
        if (callBack != null && !isRegisterOrForgot) {
            callBack.OnLoginCancel();
        }
    }

    /**
     * 计时器
     **/
    private void timer(int type) {
        int limit = Constant.DEFAULT_MESSAGE_TIMEOUT;
        Disposable temp_dis;
        temp_dis = Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(limit + 1)
                .map(s -> limit - s.intValue())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> complete(type))
                .subscribe(s -> next(type, s));
        switch (type) {
            case TIMER_CODE:
                if (disposable != null) {
                    disposable.dispose();
                }
                disposable = temp_dis;
                break;
            case TIMER_QCODE:
                if (disposable_q != null) {
                    disposable_q.dispose();
                }
                disposable_q = temp_dis;
                break;
        }
    }

    private void complete(int type) {
        switch (type) {
            case TIMER_CODE:
                Constant.MESSAGE_CODE = "";
                login_time_tv.setEnabled(true);
                login_time_tv.setText("重新获取");
                login_login_btn.setEnabled(false);

                break;
            case TIMER_QCODE:
                fetchQCode();
                break;
        }

    }

    private void next(int type, int s) {
        // KLog.v(s + "=type=" + type);
        if (s >= 10) {
            login_time_tv.setText(s + "秒后重发");
        } else {
            login_time_tv.setText("0" + s + "秒后重发");
        }
    }

    /**
     *
     */
    public interface IRegisterListener {
        /**
         */
        void OnLoginSuccess();
        /**
         */
        void OnRegisterSuccess();

        void OnLoginCancel();

        void OnFinish();

    }
}
