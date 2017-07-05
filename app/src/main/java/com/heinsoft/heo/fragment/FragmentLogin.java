package com.heinsoft.heo.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heinsoft.heo.R;
import com.heinsoft.heo.bean.HeoCodeResponse;
import com.heinsoft.heo.present.QueryPresent;
import com.heinsoft.heo.util.Constant;
import com.heinsoft.heo.util.Utils;
import com.heinsoft.heo.util.VToast;
import com.heinsoft.heo.view.IUserView;
import com.socks.library.KLog;

import java.util.HashMap;

import butterknife.ButterKnife;
import okhttp3.ResponseBody;

public class FragmentLogin extends BaseFragment implements IUserView {
    protected static final String PAGE_9 = "page_9";
    ILoginListener listener;
    private final String STR_PHONE = "phone_num";
    private final String STR_USER_NAME = "username";
    private final String STR_PW = "password";

    private static final String COOKIE_KEY = "cookie";
    SharedPreferences sp;
    QueryPresent present;
    Utils util;
    FragmentRegisterLogin fragment;
    private boolean shouldShowLogin = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.splash_lay, container, false);
        ButterKnife.bind(FragmentLogin.this, view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ILoginListener)context;
        }catch(Exception e){
            e.fillInStackTrace();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        util = Utils.getInstance();
        present = QueryPresent.getInstance(getActivity());
        present.setView(FragmentLogin.this);
        sp = getContext().getSharedPreferences(COOKIE_KEY, 0);
        autoLogin();
    }


    private void autoLogin() {

        String user_phone = sp.getString(Constant.USER_INFO_PHONE, "");
        String user_pw = sp.getString(Constant.USER_INFO_PW, "");
        //String user_session = sp.getString(Constant.USER_INFO_SESSION_ID, "");


        KLog.v(user_phone + "--" + user_pw );
        if (user_phone.equals("") || user_pw.equals("")) {
            showLogin();
            return;
        }

        present.initRetrofit(Constant.URL_BAIBAO,false);
        HashMap<String ,String>  StringA = new HashMap<>();
        StringA.put(Constant.AID_STR,Constant.AID);
        //StringA.put("username","13413151741");
        //StringA.put("password","123456");
        StringA.put("username",user_phone);
        StringA.put("password",user_pw);
        String sign = util.getSign(StringA);
        StringA.put(Constant.SIGN,sign);
        present.QueryLogin(Constant.AID,sign,StringA.get("username"), StringA.get("password"));

    }

    @Override
    public void onStop() {
        super.onStop();
        setUserVisibleHint(false);
        KLog.v("onStop");
    }

    @Override
    public void onResume() {
        super.onResume();
        setUserVisibleHint(true);
        if (shouldShowLogin && Constant.user_info == null && fragment == null) {
            KLog.v("showLogin");
            showLogin();
        }
        KLog.v("onResume");
    }

    private void showLogin() {
        shouldShowLogin = true;//在自动登陆失败切换了界面再回来继续弹出
        if (getUserVisibleHint()) {//防止在登陆时切换了界面，致使登录窗口弹出出错
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            FragmentRegisterLogin fragment = new FragmentRegisterLogin();
            ft.add(R.id.fragment_container, fragment, PAGE_9);
            ft.show(fragment);
            ft.commit();
        }
    }

    @Override
    public void ResolveLoginInfo(HeoCodeResponse info) {
        KLog.v(info.toString());
        if (info.getErrcode() == null) {
            VToast.toast(getContext(),"网络错误");
            showLogin();
            return;
        }
        VToast.toast(getContext(),info.getErrmsg());
        if(!info.getErrcode().equals(SUCCESS)){
            showLogin();
            return;
        }
        Constant.user_info = new HashMap<>();
        Constant.user_info.put(Constant.USER_INFO_PHONE, sp.getString(Constant.USER_INFO_PHONE,""));
        Constant.user_info.put(Constant.USER_INFO_USER_NAME, info.getUsername());
        listener.AutoLoginFinished();
    }


    @Override
    public void ResolveRegisterInfo(HeoCodeResponse info) {
    }

    @Override
    public void ResolveQcodeInfo(ResponseBody info) {
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
    public void ResolveMerchantInfo(HeoCodeResponse info) {

    }

    public  interface ILoginListener {
        void AutoLoginFinished();
    }

}
