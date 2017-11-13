
package com.heinsoft.heo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.heinsoft.heo.R;
import com.heinsoft.heo.bean.HeoCodeResponse;
import com.heinsoft.heo.customView.ProgressBarItem;
import com.heinsoft.heo.view.IFragmentActivity;
import com.heinsoft.heo.present.QueryPresent;
import com.heinsoft.heo.util.Constant;
import com.heinsoft.heo.util.Utils;
import com.heinsoft.heo.util.VToast;
import com.heinsoft.heo.view.IPayView;
import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;


/**
 * @Description:TODO(提现)
 * @author: xiaoyl
 * @date: 2013-07-20 下午6:38:07
 */
public class DialogFragmentWithdraw extends DialogFragment implements IPayView {
    private static final String COOKIE_KEY = "cookie";
    private IFragmentActivity listener;
    private static final String SUCCESS = "0";



    @Bind(R.id.withdraw_cash_tv)
    EditText withdraw_cash_tv;
    @Bind(R.id.withdraw_do_btn)
    Button withdraw_do_btn;

    @Bind(R.id.verify_shut_lay)
    RelativeLayout verify_shut_lay;

    QueryPresent present;
    Utils util;

    public DialogFragmentWithdraw() {

    }

    @Override
    public void onAttach(Context activity) {
        try {
            listener = (IFragmentActivity) activity;
        } catch (ClassCastException e) {
        }
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.withdraw_layout, container,
                false);
        ButterKnife.bind(DialogFragmentWithdraw.this, view);
        return view;
    }

    private void withdraw() {
        if(withdraw_cash_tv.getText().toString().equals("")){
            VToast.toast(getContext(),"请输入一个金额");
            return;
        }
        if(Double.parseDouble(withdraw_cash_tv.getText().toString())<=100){
            VToast.toast(getContext(),"提现金额应大于100");
            return;
        }
        showWaitDialog("请稍候");
        present.initRetrofit(Constant.URL_BAIBAO,false);

        HashMap<String, String> StringA1 = new HashMap<>();

        StringA1.put(Constant.AID_STR, Constant.AID);
        StringA1.put(Constant.AGENT_ID, Constant.user_info.get(Constant.MY_AGENT_ID));
        StringA1.put(Constant.MONEY,  withdraw_cash_tv.getText().toString());

        String sign1 = util.getSign(StringA1);
        StringA1.put(Constant.SIGN, sign1);
        present.QueryAgentWithDraw(StringA1.get(Constant.AID_STR),StringA1.get(Constant.SIGN),StringA1.get(Constant.AGENT_ID),StringA1.get(Constant.MONEY));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setCancelable(false);
        initDate();
        initView();
    }

    protected void showWaitDialog(String tip){
        ProgressBarItem.show(getContext(),tip,false,null);
    }
    protected void hideWaitDialog() {
        ProgressBarItem.hideProgress();
    }

    private void initView() {
        RxView.clicks(verify_shut_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> dismiss());
        RxView.clicks(withdraw_do_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> withdraw());
        withdraw_cash_tv.setHint("可提现金额:"+Constant.user_info.get(Constant.PROFIT_BALANCE));

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               InputMethodManager inputManager =
                                       (InputMethodManager) withdraw_cash_tv.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(withdraw_cash_tv, 0);
                           }
                       },
                500);
    }

    private void initDate() {
        util = Utils.getInstance();
        present =  QueryPresent.getInstance(getContext());
        present.setView(DialogFragmentWithdraw.this);
    }

    @Override
    public void ResolvePayInfo(HeoCodeResponse info) {

    }

    @Override
    public void ResolveScoreQuickPayInfo(ResponseBody info) {

    }

    @Override
        public void ResolveScoreQuickPayConfirmInfo(ResponseBody info) {

    }

    @Override
    public void ResolveBankName(ResponseBody info) {

    }

    @Override
    public void ResolveAddCardInfo(ResponseBody info) {

    }

    @Override
    public void ResolveCardPackageInfo(ResponseBody info) {

    }

    @Override
    public void ResolveQuickPayInfo(ResponseBody info) {

    }

    @Override
    public void ResolveOpenCreditInfo(ResponseBody info) {

    }

    @Override
    public void ResolveQuickPayConfirmInfo(ResponseBody info) {

    }

    @Override
    public void ResolveProfitInfo(HeoCodeResponse info) {

    }

    @Override
    public void ResolveAgentOrderInfo(HeoCodeResponse info) {

    }

    @Override
    public void ResolveAgentProfitInfo(HeoCodeResponse info) {

    }

    @Override
    public void ResolveAgentWithdrawInfo(HeoCodeResponse info) {
        KLog.v(info.toString());
        hideWaitDialog();
        if (info.getErrcode() == null) {
            VToast.toast(getContext(), "网络错误");
            return;
        }
        VToast.toast(getContext(),info.getErrmsg());
        if (info.getErrcode().equals(SUCCESS)) {
            dismiss();
            return;
        }
    }

    @Override
    public void ResolveMerchantOrderInfo(HeoCodeResponse info) {

    }

}
