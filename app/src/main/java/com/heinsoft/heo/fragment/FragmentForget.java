package com.heinsoft.heo.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heinsoft.heo.R;
import com.heinsoft.heo.bean.HeoCodeResponse;
import com.heinsoft.heo.present.QueryPresent;
import com.heinsoft.heo.util.Constant;
import com.heinsoft.heo.util.Utils;
import com.heinsoft.heo.util.VToast;
import com.heinsoft.heo.view.IUserView;
import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class FragmentForget extends BaseFragment implements IUserView {


    @Bind(R.id.forget_user_et)
    EditText forget_user_et;
    @Bind(R.id.forget_code_et)
    EditText forget_code_et;
    @Bind(R.id.forget_password_et)
    EditText forget_password_et;
    @Bind(R.id.forget_password2_et)
    EditText forget_password2_et;

    @Bind(R.id.forget_step1_btn)
    Button forget_step1_btn;
    @Bind(R.id.forget_step2_btn)
    Button forget_step2_btn;
    @Bind(R.id.forget_step3_btn)
    Button forget_step3_btn;

    @Bind(R.id.header_title)
    TextView header_title;
    @Bind(R.id.header_btn_lay)
    LinearLayout header_btn_lay;
    @Bind(R.id.forget_step1_lay)
    LinearLayout forget_step1_lay;
    @Bind(R.id.forget_step2_lay)
    LinearLayout forget_step2_lay;
    @Bind(R.id.forget_step3_lay)
    LinearLayout forget_step3_lay;

    @Bind(R.id.forget_show1_iv)
    CheckBox forget_show1_iv;
    @Bind(R.id.forget_show2_iv)
    CheckBox forget_show2_iv;

    @Bind(R.id.forget_tip_tv)
    TextView forget_tip_tv;
    @Bind(R.id.foget_code_again)
    TextView foget_code_again;
    private Disposable disposable;
    SharedPreferences sp;
    QueryPresent present;
    Utils util;
    String phone, vertify, vcode, password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forget, container, false);
        ButterKnife.bind(FragmentForget.this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
        initView();
    }

    private void initView() {

        RxView.clicks(forget_step1_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Forward());
        RxView.clicks(forget_step2_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Forward());
        RxView.clicks(forget_step3_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Forward());
        RxView.clicks(header_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Back());
        RxView.clicks(foget_code_again).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Code());

        forget_show1_iv.setOnCheckedChangeListener((compoundButton, b) -> {
            visiblePw1(b);
        });
        forget_show2_iv.setOnCheckedChangeListener((compoundButton, b) -> {
            visiblePw2(b);
        });
        refreshState();
    }

    private void visiblePw1(Boolean b) {
        if (b) {
            forget_password_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            forget_password_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        forget_password_et.postInvalidate();
        CharSequence charSequence = forget_password_et.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }

    private void visiblePw2(Boolean b) {
        if (b) {
            forget_password2_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            forget_password2_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        forget_password2_et.postInvalidate();
        CharSequence charSequence = forget_password2_et.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }

    private void Code() {
        showWaitDialog("请稍等");

        Long msgId = System.currentTimeMillis();
        Constant.MESSAGE_CODE = util.getCode(4);

        String str_content = "【" + getContext().getString(R.string.app_name) + "】，您的验证码为：" + Constant.MESSAGE_CODE + Constant.MESSAGE_CONTENT_PREFIX;
        KLog.v(Constant.MESSAGE_CODE);
        present.initRetrofitSendMessage(Constant.URL_MESSAGE);
        present.QueryCode(forget_user_et.getText().toString().trim(), msgId + "", str_content);
    }


    public void refreshState() {

        forget_user_et.setText(sp.getString("user_name", ""));
        forget_code_et.setText("");
        forget_password_et.setText("");
        forget_password2_et.setText("");
        forget_step1_lay.setVisibility(View.VISIBLE);
        forget_step2_lay.setVisibility(View.GONE);
        forget_step3_lay.setVisibility(View.GONE);
        header_title.setText("重置密码");
        password = null;
        phone = null;
        vertify = null;
        vcode = null;
    }

    private void Forward() {
        if (forget_step1_lay.getVisibility() == View.VISIBLE) {
            if (!util.verifyPhone(forget_user_et.getText().toString().trim())) {
                VToast.toast(getContext(), "请输入正确的手机号");
                return;
            }
            phone = forget_user_et.getText().toString().trim();
            if (vertify == null) {
                Code();
            } else {
                forget_step2_lay.setVisibility(View.VISIBLE);
                forget_step1_lay.setVisibility(View.GONE);
            }
        } else if (forget_step2_lay.getVisibility() == View.VISIBLE) {
            if (!forget_code_et.getText().toString().trim().equals(Constant.MESSAGE_CODE)) {
                VToast.toast(getContext(), "请输入正确的验证码");
                return;
            }
            vcode = forget_code_et.getText().toString().trim();
            forget_step3_lay.setVisibility(View.VISIBLE);
            forget_step2_lay.setVisibility(View.GONE);
        } else {
            if (forget_password_et.getText().toString().trim().equals("")) {
                VToast.toast(getContext(), "请输入密码");
                return;
            }
            if (!forget_password_et.getText().toString().trim().equals(forget_password2_et.getText().toString().trim())) {
                VToast.toast(getContext(), "两次密码输入不一致");
                return;
            }
            password = forget_password_et.getText().toString().trim();
            Submit();
        }
    }

    private void Submit() {
        showWaitDialog("请稍等");
        KLog.v("vcode="+vcode);
        present.initRetrofit(Constant.URL_BAIBAO, false);
        HashMap<String, String> StringA1 = new HashMap<>();
        StringA1.put(Constant.AID_STR, Constant.AID);
        StringA1.put(Constant.ACCOUNT, forget_user_et.getText().toString().trim());
        StringA1.put(Constant.NEWPASSWORD, password);

        String sign1 = util.getSign(StringA1);
        StringA1.put(Constant.SIGN, sign1);
        present.QueryResetPassword(StringA1.get(Constant.AID_STR),StringA1.get(Constant.SIGN),StringA1.get(Constant.ACCOUNT),StringA1.get(Constant.NEWPASSWORD));
    }

    public void Back() {
        super.Back();

        if (forget_step3_lay.getVisibility() == View.VISIBLE) {
            forget_step2_lay.setVisibility(View.VISIBLE);
            forget_step3_lay.setVisibility(View.GONE);
        } else if (forget_step2_lay.getVisibility() == View.VISIBLE) {
            forget_step1_lay.setVisibility(View.VISIBLE);
            forget_step2_lay.setVisibility(View.GONE);
        } else {
           /* if(Constant.InDirectTo==1){
                Constant.InDirectTo = 0;
                listener.gotoProfile();
                return;
            }*/
            listener.gotoLogin();
        }
    }

    private void initDate() {
        sp = getContext().getSharedPreferences(COOKIE_KEY, 0);
        present = QueryPresent.getInstance(getContext());
        present.initRetrofit(Constant.URL_BAIBAO, false);
        present.setView(FragmentForget.this);
        util = Utils.getInstance();
    }

    /**
     * 计时器
     **/
    private void timer() {
        int limit = Constant.DEFAULT_MESSAGE_TIMEOUT;
        Disposable temp_dis;
        temp_dis = Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(limit + 1)
                .map(s -> limit - s.intValue())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> complete())
                .subscribe(s -> next(s));

        if (disposable != null) {
            disposable.dispose();
        }
        disposable = temp_dis;
    }

    private void complete() {

        foget_code_again.setEnabled(true);
        foget_code_again.setText("重新获取");

    }

    private void next(int s) {
        if (s >= 10) {
            foget_code_again.setText("(" + s + ")\n重新获取");
        } else {
            foget_code_again.setText("(0" + s + ")\n重新获取");
        }
    }

    @Override
    public void ResolveLoginInfo(HeoCodeResponse info) {

    }

    @Override
    public void ResolveRegisterInfo(HeoCodeResponse info) {

    }

    @Override
    public void ResolveCodeInfo(ResponseBody info) {
        KLog.v(info.contentLength() + "");
        hideWaitDialog();

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
                forget_step2_lay.setVisibility(View.VISIBLE);
                forget_step1_lay.setVisibility(View.GONE);
                forget_tip_tv.setText("验证码已发送至您的手机:" + util.getPhoneEncrypt(phone));
                timer();
                header_title.setText("验证手机号码");
            }else{

                VToast.toast(getContext(), "发送验证码失败");
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
    public void ResolveResetPasswordInfo(HeoCodeResponse info) {
        hideWaitDialog();
        if(info.getErrcode()==null){
            VToast.toast(getContext(), "网络错误，请重试!");
            return;
        }

        if (info.getErrcode().equals(SUCCESS)) {
            VToast.toast(getContext(), "修改成功，请使用新密码登录");
            listener.gotoLogin();
        }else{
            VToast.toast(getContext(), info.getErrmsg());
        }
    }

}
