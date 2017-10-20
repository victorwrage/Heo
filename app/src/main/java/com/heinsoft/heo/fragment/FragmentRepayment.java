package com.heinsoft.heo.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heinsoft.heo.R;
import com.heinsoft.heo.adapter.CardItemAdapter;
import com.heinsoft.heo.bean.CardBean;
import com.heinsoft.heo.bean.HeoCodeResponse;
import com.heinsoft.heo.customView.RecyclerViewWithEmpty;
import com.heinsoft.heo.present.QueryPresent;
import com.heinsoft.heo.util.Utils;
import com.heinsoft.heo.util.VToast;
import com.heinsoft.heo.view.IPayView;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.gmariotti.recyclerview.adapter.AlphaAnimatorAdapter;
import okhttp3.ResponseBody;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/8/25 10:28
 */

public class FragmentRepayment extends BaseFragment implements IPayView, CardItemAdapter.ICardAdapter {

    @Bind(R.id.header_btn_lay)
    LinearLayout header_btn_lay;
    @Bind(R.id.header_title)
    TextView header_title;

    @Bind(R.id.repay_bank_list)
    RecyclerViewWithEmpty repay_bank_list;

    @Bind(R.id.repayment_new_plan_btn)
    Button repayment_new_plan_btn;
    @Bind(R.id.repayment_plan_submit_btn)
    Button repayment_plan_submit_btn;
    @Bind(R.id.repayment_repay_btn)
    Button repayment_repay_btn;

    ArrayList<CardBean> date;
    CardItemAdapter adapter;

    @Bind(R.id.repayment_step1)
    LinearLayout repayment_step1;
    @Bind(R.id.repayment_step2)
    LinearLayout repayment_step2;
    @Bind(R.id.repayment_step3)
    LinearLayout repayment_step3;
    @Bind(R.id.repayment_step4)
    LinearLayout repayment_step4;
    @Bind(R.id.repayment_step5)
    LinearLayout repayment_step5;
    @Bind(R.id.repayment_step6)
    LinearLayout repayment_step6;

    @Bind(R.id.repayment_add_lay)
    LinearLayout repayment_add_lay;

    QueryPresent present;
    Utils util;
    SharedPreferences sp;

    @Bind(R.id.empty_lay)
    LinearLayout empty_lay;
    @Bind(R.id.empty_iv)
    ImageView empty_iv;
    @Bind(R.id.empty_tv)
    TextView empty_tv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repayment, container, false);
        ButterKnife.bind(FragmentRepayment.this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
        initView();
    }


    private void initDate() {
        util = Utils.getInstance();
        present = QueryPresent.getInstance(getContext());
        present.setView(FragmentRepayment.this);
        sp = getContext().getSharedPreferences(COOKIE_KEY, Context.MODE_PRIVATE);

        date = new ArrayList<>();
      /*  date.add(test(R.drawable.payment_icon1, "中国建设银行", "**** **** **** **** 567",true));
        date.add(test(R.drawable.payment_icon2, "中国工商银行", "**** **** **** **** 254",false));*/

        adapter = new CardItemAdapter(date, getContext());


    }

    private CardBean test(int icon, String name, String num,Boolean plan) {
        CardBean card = new CardBean();
        card.setCardicon(icon);
        card.setCardname(name);
        card.setIs_plan(plan);
        card.setCardnum(num);
        return card;
    }


    protected void setEmptyStatus(boolean isOffLine) {
        if (isOffLine) {
            empty_iv.setImageResource(R.drawable.network_error);
            empty_tv.setText("(=^_^=)，粗错了，点我刷新试试~");
          //  message_empty.setEnabled(true);
        //    RxView.clicks(empty_iv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> emptyClick());
        } else {
          //  message_empty.setEnabled(false);
            empty_iv.setImageResource(R.drawable.smile);
            empty_tv.setText("暂无信用卡");
        }
    }

    private void initView() {
        RxView.clicks(header_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Back());
        RxView.clicks(repayment_plan_submit_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> PlanSubmit());
        RxView.clicks(repayment_new_plan_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> NewPlan());
        RxView.clicks(repayment_repay_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Repay());
        RxView.clicks(repayment_add_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> AddCard());
        header_title.setText("还款计划");

        repay_bank_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        //	recycle_view.setItemAnimator(new SlideInOutLeftItemAnimator(recycle_view));
        AlphaAnimatorAdapter animatorApdapter = new AlphaAnimatorAdapter(adapter, repay_bank_list);
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        repay_bank_list.setEmptyView(empty_lay);

        repay_bank_list.setAdapter(animatorApdapter);
        adapter.setListener(FragmentRepayment.this);
        setEmptyStatus(false);

    }

    private void AddCard() {
        VToast.toast(getContext(),"暂未开通");
    }

    private void Repay() {

    }

    private void NewPlan() {

    }

    private void PlanSubmit() {

    }

    @Override
    public void Back() {
        super.Back();
        listener.gotoMain();
    }

    @Override
    public void RefreshState() {
        super.RefreshState();
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

    }

    @Override
    public void ResolveMerchantOrderInfo(HeoCodeResponse info) {

    }


    @Override
    public void gotoPlan(int position) {
        repayment_step1.setVisibility(View.GONE);
        if (date.get(position).getIs_plan()) {
            repayment_step6.setVisibility(View.VISIBLE);
        } else {
            repayment_step5.setVisibility(View.VISIBLE);
        }
    }
}
