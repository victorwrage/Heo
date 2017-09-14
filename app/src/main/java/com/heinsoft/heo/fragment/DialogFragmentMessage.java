
package com.heinsoft.heo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.heinsoft.heo.R;
import com.heinsoft.heo.bean.HeoCodeResponse;
import com.heinsoft.heo.bean.MessageBean;
import com.heinsoft.heo.customView.ProgressBarItem;
import com.heinsoft.heo.view.IFragmentActivity;
import com.heinsoft.heo.present.QueryPresent;
import com.heinsoft.heo.util.Utils;
import com.heinsoft.heo.util.VToast;
import com.heinsoft.heo.view.IPayView;
import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;


/**
 * @Description:TODO(消息)
 * @author: xiaoyl
 * @date: 2013-07-20 下午6:38:07
 */
public class DialogFragmentMessage extends DialogFragment implements IPayView {
    private static final String COOKIE_KEY = "cookie";
    private IFragmentActivity listener;
    private static final String SUCCESS = "0";



    @Bind(R.id.message_detail_title)
    TextView message_detail_title;
    @Bind(R.id.message_detail_content)
    TextView message_detail_content;
    @Bind(R.id.message_date_tv)
    TextView message_date_tv;
    @Bind(R.id.message_author_tv)
    TextView message_author_tv;

    @Bind(R.id.message_confirm_btn)
    Button message_confirm_btn;


    QueryPresent present;
    Utils util;
    private MessageBean message;

    public DialogFragmentMessage() {

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
        View view = inflater.inflate(R.layout.message_layout, container,
                false);
        ButterKnife.bind(DialogFragmentMessage.this, view);
        return view;
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
        RxView.clicks(message_confirm_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> dismiss());


        message_detail_title.setText(message.getTitle());
        message_detail_content.setText(Html.fromHtml(message.getContent()));
        message_date_tv.setText(message.getSend_time());
        message_author_tv.setText(message.getSend_name());

    }

    public void setMessage(MessageBean message_){
        message = message_;
    }
    private void initDate() {
        util = Utils.getInstance();
        present =  QueryPresent.getInstance(getContext());
        present.setView(DialogFragmentMessage.this);
    }

    @Override
    public void ResolvePayInfo(HeoCodeResponse info) {

    }

    @Override
    public void ResolveQuickPayInfo(ResponseBody info) {

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
