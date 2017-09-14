
package com.heinsoft.heo.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
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
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


/**
 * @ClassName: DialogFragmentAddTag
 * @Description:TODO(注册界面)
 * @author: xiaoyl
 * @date: 2013-07-20 下午6:38:07
 */
public class FragmentRegister extends BaseFragment implements IUserView {


    private Disposable disposable;
    @Bind(R.id.register_user_et)
    EditText register_user_et;
    @Bind(R.id.register_password_et)
    EditText register_password_et;
    @Bind(R.id.register_invite_et)
    EditText register_invite_et;

    @Bind(R.id.register_agree_btn)
    CheckBox register_agree_btn;
    @Bind(R.id.register_show_iv)
    CheckBox register_show_iv;

    @Bind(R.id.register_code_et)
    EditText register_code_et;
    @Bind(R.id.register_code_again)
    TextView register_code_again;
    @Bind(R.id.register_tip_tv)
    TextView register_tip_tv;

    @Bind(R.id.register_step1_btn)
    Button register_step1_btn;
    @Bind(R.id.register_step2_btn)
    Button register_step2_btn;

    @Bind(R.id.register_step1_lay)
    LinearLayout register_step1_lay;
    @Bind(R.id.register_step2_lay)
    LinearLayout register_step2_lay;

    @Bind(R.id.header_title)
    TextView header_title;
    @Bind(R.id.header_btn_lay)
    LinearLayout header_btn_lay;

    SharedPreferences sp;
    QueryPresent present;
    Utils util;


    public void RefreshState(){
        register_code_et.setText("");
        register_password_et.setText("");
        register_invite_et.setText("");
        register_user_et.setText("");
        register_code_again.setText("获取验证码");

        register_step1_lay.setVisibility(View.VISIBLE);
        register_step2_lay.setVisibility(View.GONE);
    }

    @Override
    public void Back() {
        super.Back();
        if(register_step2_lay.getVisibility()==View.VISIBLE){
            register_step1_lay.setVisibility(View.VISIBLE);
            register_step2_lay.setVisibility(View.GONE);
            return;
        }
        if (disposable != null) {
            disposable.dispose();
        }
        Constant.MESSAGE_CODE = "";
        listener.gotoLogin();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_layout, container,
                false);
        ButterKnife.bind(FragmentRegister.this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
        initView();
    }

    private void initView() {
        RxView.clicks(register_step1_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Code());
        RxView.clicks(register_step2_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Register());
        RxView.clicks(header_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Back());
        header_title.setText("注册");
        register_show_iv.setOnCheckedChangeListener((compoundButton, b) -> {
            visiblePw(b);
        });

    }

    private void Register() {
        if(!register_code_et.getText().toString().trim().equals( Constant.MESSAGE_CODE )){
            VToast.toast(getContext(),"短信验证码错误");
            return;
        }
        hidSoftInput();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constant.USER_INFO_USER_NAME, register_user_et.getText().toString().trim());
        editor.putString(Constant.USER_INFO_PW, register_password_et.getText().toString().trim());
        editor.putString(Constant.USER_INFO_INVITE_CODE, register_invite_et.getText().toString().trim());
        editor.commit();
        VToast.toast(getContext(),"注册成功");
        listener.gotoLogin();
    }

    private void initDate() {
        util = Utils.getInstance();
        present = QueryPresent.getInstance(getActivity());
        present.setView(FragmentRegister.this);
        sp = getActivity().getSharedPreferences(COOKIE_KEY, 0);
    }

    private void visiblePw(Boolean b) {
        if (b) {
            register_password_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            register_password_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        register_password_et.postInvalidate();
        CharSequence charSequence = register_password_et.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }

    private void Code() {
        if (!util.verifyPhone(register_user_et.getText().toString().trim())) {
            VToast.toast(getActivity(), "请输入正确的手机号码!");
            return;
        }
        if (register_password_et.getText().toString().trim().equals("")) {
            VToast.toast(getActivity(), "请输入密码!");
            return;
        }
        if( Constant.MESSAGE_CODE.equals("")) {
            showWaitDialog("请稍等");

            Long msgId = System.currentTimeMillis();
            Constant.MESSAGE_CODE = util.getCode(4);

            String str_content = "【" + getContext().getString(R.string.app_name) + "】，您的验证码为：" + Constant.MESSAGE_CODE + Constant.MESSAGE_CONTENT_PREFIX;
            KLog.v(Constant.MESSAGE_CODE);
            present.initRetrofitSendMessage(Constant.URL_MESSAGE);
            present.QueryCode(register_user_et.getText().toString().trim(), msgId + "", str_content);
        }else{
            register_step1_lay.setVisibility(View.GONE);
            register_step2_lay.setVisibility(View.VISIBLE);
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
                register_tip_tv.setText("验证码已发送至您的手机"+util.getPhoneEncrypt(register_user_et.getText().toString().trim()));
                register_step1_lay.setVisibility(View.GONE);
                register_step2_lay.setVisibility(View.VISIBLE);
                register_code_again.setEnabled(false);
                timer();
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

    private void complete( ) {
        register_code_again.setEnabled(true);
        Constant.MESSAGE_CODE = "";

    }

    private void next(int s) {
        // KLog.v(s + "=type=" + type);
        if (s >= 10) {
            register_code_again.setText(s + "重新获取");
        } else {
            register_code_again.setText("0" + s  + "重新获取");
        }
    }

}
