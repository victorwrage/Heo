
package com.heinsoft.heo.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heinsoft.heo.R;
import com.heinsoft.heo.bean.HeoCodeResponse;
import com.heinsoft.heo.bean.HeoMerchantInfoResponse;
import com.heinsoft.heo.present.QueryPresent;
import com.heinsoft.heo.service.AdvanceLoadX5Service;
import com.heinsoft.heo.util.Constant;
import com.heinsoft.heo.util.Utils;
import com.heinsoft.heo.util.VToast;
import com.heinsoft.heo.view.IMerchantView;
import com.heinsoft.heo.view.IUserView;
import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;
import com.tencent.smtt.sdk.QbSdk;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.update.BmobUpdateAgent;
import okhttp3.ResponseBody;


/**
 * @ClassName: DialogFragmentAddTag
 * @Description:TODO(登录界面)
 * @author: xiaoyl
 * @date: 2013-07-20 下午6:38:07
 */
public class FragmentLogin extends BaseFragment implements IUserView, IMerchantView {

    private final static int INIT_FINISHED = 1001;
    @Bind(R.id.login_user_et)
    EditText login_user_et;
    @Bind(R.id.login_password_et)
    EditText login_password_et;

    @Bind(R.id.login_remember_btn)
    CheckBox login_remember_btn;
    @Bind(R.id.login_auto_btn)
    CheckBox login_auto_btn;

    @Bind(R.id.login_login_btn)
    Button login_login_btn;

    @Bind(R.id.login_register_tv)
    TextView login_register_tv;
    @Bind(R.id.login_forget_tv)
    TextView login_forget_tv;

    @Bind(R.id.update_iv)
    ImageView update_iv;
    @Bind(R.id.update_tv)
    TextView update_tv;
    @Bind(R.id.login_show_iv)
    CheckBox login_show_iv;

    @Bind(R.id.login_step1_lay)
    RelativeLayout login_step1_lay;
    @Bind(R.id.login_step2_lay)
    RelativeLayout login_step2_lay;

    SharedPreferences sp;
    QueryPresent present;
    Utils util;
    String merchant_id;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INIT_FINISHED:
                    VToast.toast(getContext(), "你好 " + ((Constant.user_info == null) ? login_user_et.getText().toString() : Constant.user_info.get(Constant.NAME)));
                    hidSoftInput();
                    listener.gotoMain();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_layout, container,
                false);
        ButterKnife.bind(FragmentLogin.this, view);
        return view;
    }

    private void Forget() {
        listener.gotoForgot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
        initView();

    }

    @Override
    public void RefreshState() {
        super.RefreshState();
        login_step2_lay.setVisibility(View.GONE);
        login_user_et.setText(sp.getString(Constant.USER_INFO_USER_NAME, ""));
        login_password_et.setText(sp.getString(Constant.USER_INFO_PW, ""));
        login_remember_btn.setChecked(sp.getBoolean(Constant.USER_INFO_REMEMBER, true));
        login_auto_btn.setChecked(sp.getBoolean(Constant.USER_INFO_AUTO_LOGIN, true));
    }

    @Override
    public void Back() {
        super.Back();
    }

    private void initView() {

        login_step1_lay.setVisibility(View.VISIBLE);
        login_step2_lay.setVisibility(View.VISIBLE);
        login_password_et.setOnEditorActionListener((textView, i, keyEvent) -> {
            switch (i) {
                case EditorInfo.IME_ACTION_DONE:
                    Login();
                    break;
            }
            return false;
        });

        login_remember_btn.setChecked(sp.getBoolean(Constant.USER_INFO_REMEMBER, true));
        login_auto_btn.setChecked(sp.getBoolean(Constant.USER_INFO_AUTO_LOGIN, true));

        if (login_remember_btn.isChecked()) {
            login_user_et.setText(sp.getString(Constant.USER_INFO_USER_NAME, ""));
            login_password_et.setText(sp.getString(Constant.USER_INFO_PW, ""));
        }
        RxView.clicks(login_login_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Login());
        RxView.clicks(login_register_tv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Register());
        RxView.clicks(login_forget_tv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Forget());
        RxView.clicks(update_iv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> BmobUpdateAgent.forceUpdate(getContext()));
        update_tv.setText("当前版本" + util.getAppVersionName(getContext()));
        BmobUpdateAgent.update(getContext());
        login_show_iv.setOnCheckedChangeListener((compoundButton, b) -> {
            visiblePw(b);
        });

        login_auto_btn.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                login_remember_btn.setChecked(true);
            }
        });
        AutoLogin();
    }

    private void AutoLogin() {
        if (login_auto_btn.isChecked()) {
            if (login_user_et.getText().toString().trim().equals("") || login_password_et.getText().toString().trim().equals("")) {
                login_step2_lay.setVisibility(View.GONE);
                return;
            }

            present.initRetrofit(Constant.URL_BAIBAO, false);
            HashMap<String, String> StringA1 = new HashMap<>();
            StringA1.put(Constant.AID_STR, Constant.AID);
            StringA1.put(Constant.USER_INFO_USERNAME, login_user_et.getText().toString().trim());
            StringA1.put(Constant.USER_INFO_PASSWORD, login_password_et.getText().toString().trim());
            String sign1 = util.getSign(StringA1);
            StringA1.put(Constant.SIGN, sign1);
            present.QueryLogin(Constant.AID, sign1, StringA1.get(Constant.USER_INFO_USERNAME), StringA1.get(Constant.USER_INFO_PASSWORD));
        } else {
            login_step2_lay.setVisibility(View.GONE);
        }
    }

    private void Register() {
        listener.gotoRegister();
    }

    private void Login() {
        if (login_user_et.getText().toString().trim().equals("")) {
            VToast.toast(getContext(), "请输入帐号");
            return;
        }
        if (login_password_et.getText().toString().trim().equals("")) {
            VToast.toast(getContext(), "请输入密码");
            return;
        }

        showWaitDialog("正在登录");
        present.initRetrofit(Constant.URL_BAIBAO, false);
        HashMap<String, String> StringA1 = new HashMap<>();
        StringA1.put(Constant.AID_STR, Constant.AID);
        StringA1.put(Constant.USER_INFO_USERNAME, login_user_et.getText().toString().trim());
        StringA1.put(Constant.USER_INFO_PASSWORD, login_password_et.getText().toString().trim());
        String sign1 = util.getSign(StringA1);
        StringA1.put(Constant.SIGN, sign1);
        present.QueryLogin(Constant.AID, sign1, StringA1.get(Constant.USER_INFO_USERNAME), StringA1.get(Constant.USER_INFO_PASSWORD));
    }

    private void visiblePw(Boolean b) {
        if (b) {
            login_password_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            login_password_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        login_password_et.postInvalidate();
        CharSequence charSequence = login_password_et.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }

    private void initDate() {
        util = Utils.getInstance();
        present = QueryPresent.getInstance(getActivity());
        present.setView(FragmentLogin.this);
        sp = getActivity().getSharedPreferences(COOKIE_KEY, 0);
    }

    @Override
    public void ResolveLoginInfo(HeoCodeResponse info) {
        KLog.v(info.toString());
        hideWaitDialog();

        if (info.getErrcode() == null) {
            login_step2_lay.setVisibility(View.GONE);
            VToast.toast(getActivity(), "网络错误");
            return;
        }
        if (!info.getErrcode().equals(SUCCESS)) {
            KLog.v(sp.getString(Constant.USER_INFO_USER_NAME, ""));
            if (sp.getString(Constant.USER_INFO_USER_NAME, "").equals(login_user_et.getText().toString().trim())
                    && sp.getString(Constant.USER_INFO_PW, "").equals(login_password_et.getText().toString().trim())) {
                executor.execute(() -> {
                    saveInfo();
                    preinitX5WebCore();
                    //预加载x5内核
                    Intent intent = new Intent(getContext(), AdvanceLoadX5Service.class);
                    getContext().startService(intent);
                    handler.sendEmptyMessage(INIT_FINISHED);
                });
                return;
            }
            login_step2_lay.setVisibility(View.GONE);
            VToast.toast(getContext(), info.getErrmsg());
            return;
        }

        Constant.user_info = new HashMap<>();
        Constant.user_info.put(Constant.USER_INFO_PHONE, login_user_et.getText().toString().trim());
        Constant.user_info.put(Constant.USER_INFO_USER_NAME, info.getUsername());
        Constant.user_info.put(Constant.USER_INFO_NAME, info.getName());
        Constant.user_info.put(Constant.USER_INFO_MERCHANT_ID, info.getMerchant_id());
        Constant.user_info.put(Constant.USER_INFO_PW, login_password_et.getText().toString().trim());
        merchant_id = info.getMerchant_id();

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

    }

    private void saveInfo(){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constant.USER_INFO_USER_NAME, login_user_et.getText().toString().trim());
        editor.putString(Constant.USER_INFO_PW, login_password_et.getText().toString().trim());
        if (login_remember_btn.isChecked()) {
            editor.putBoolean(Constant.USER_INFO_REMEMBER, login_remember_btn.isChecked());
        } else {
            editor.putBoolean(Constant.USER_INFO_REMEMBER, false);
        }
        if (login_auto_btn.isChecked()) {
            editor.putBoolean(Constant.USER_INFO_AUTO_LOGIN, true);
        } else {
            editor.putBoolean(Constant.USER_INFO_AUTO_LOGIN, false);
        }
        editor.commit();
    }

    @Override
    public void ResolveCodeInfo(ResponseBody info) {
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
    public void ResolveResetPasswordInfo(HeoCodeResponse info) {

    }

    @Override
    public void ResolveSettleInfo(HeoCodeResponse info) {

    }

    @Override
    public void ResolveSettle(HeoCodeResponse info) {

    }

    @Override
    public void ResolveMerchantInfo(HeoCodeResponse info) {
        KLog.v(info.toString());
        hideWaitDialog();
        if (info.getErrcode() == null) {
            login_step2_lay.setVisibility(View.GONE);
            VToast.toast(getContext(), "网络错误,不能获取商户信息");
            return;
        }
        if (info.getErrcode().equals(SUCCESS)) {
            if (info.getContent() == null && info.getContent().get(0) == null) {
                login_step2_lay.setVisibility(View.GONE);
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

            executor.execute(() -> {
                saveInfo();
                preinitX5WebCore();
                //预加载x5内核
                Intent intent = new Intent(getContext(), AdvanceLoadX5Service.class);
                getContext().startService(intent);
                handler.sendEmptyMessage(INIT_FINISHED);
            });
        } else {
            login_step2_lay.setVisibility(View.GONE);
        }
    }

    private void preinitX5WebCore() {
        if (!QbSdk.isTbsCoreInited()) {
            QbSdk.preInit(getContext(), null);// 设置X5初始化完成的回调接口
        }
    }

}
