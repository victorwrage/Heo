package com.heinsoft.heo.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heinsoft.heo.R;
import com.heinsoft.heo.adapter.ChoiceSpinnerAdapter;
import com.heinsoft.heo.adapter.RecordItemListAdapter;
import com.heinsoft.heo.bean.HeoCodeResponse;
import com.heinsoft.heo.bean.HeoMerchantInfoResponse;
import com.heinsoft.heo.customView.CustomSinnper;
import com.heinsoft.heo.present.QueryPresent;
import com.heinsoft.heo.util.Constant;
import com.heinsoft.heo.util.SortComparator;
import com.heinsoft.heo.util.Utils;
import com.heinsoft.heo.util.VToast;
import com.heinsoft.heo.view.IPayView;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FragmentRecord extends BaseFragment  implements IPayView{
    IRecordListener listener;

    QueryPresent present;
    Utils util;
    SharedPreferences sp;

    RecordItemListAdapter adapter;

    @Bind(R.id.recycle_view)
    ListView recycle_view;

    @Bind(R.id.spinner_order)
    CustomSinnper spinner_order;

    @Bind(R.id.header_btn_lay)
    LinearLayout header_btn_lay;
    @Bind(R.id.header_count)
    TextView header_count;
    @Bind(R.id.empty_iv)
    ImageView empty_iv;
    @Bind(R.id.empty_tv)
    TextView empty_tv;

    @Bind(R.id.empty_lay)
    RelativeLayout empty_lay;
    ArrayList<HeoMerchantInfoResponse> temp_data;

    ArrayList<HeoMerchantInfoResponse> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.record_lay, container, false);

        ButterKnife.bind(FragmentRecord.this, view);

        util = Utils.getInstance();
        present = QueryPresent.getInstance(getContext());
        present.setView(FragmentRecord.this);
        sp = getContext().getSharedPreferences(COOKIE_KEY, Context.MODE_PRIVATE);

        data = new ArrayList<>();
        temp_data = new ArrayList<>();
        adapter = new RecordItemListAdapter(data, getActivity(), FragmentRecord.this);
        empty_iv = (ImageView) view.findViewById(R.id.empty_iv);
        empty_tv = (TextView) view.findViewById(R.id.empty_tv);
        empty_lay = (RelativeLayout) view.findViewById(R.id.empty_lay);

		spinner_order = (CustomSinnper) view.findViewById(R.id.spinner_order);
		ChoiceSpinnerAdapter adapter_scope = new ChoiceSpinnerAdapter(getActivity(),scopes);
        spinner_order.setAdapter(adapter_scope);
        spinner_order.setOnItemSeletedListener((parent,v, position,  id)->changeOrder(position));

     //   recycle_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        //	recycle_view.setItemAnimator(new SlideInOutLeftItemAnimator(recycle_view));
     //   AlphaAnimatorAdapter animatorApdapter = new AlphaAnimatorAdapter(adapter, recycle_view);
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        recycle_view.setEmptyView(empty_lay);
        recycle_view.setAdapter(adapter);

        RxView.clicks(header_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> listener.finishRecord());

        return view;
    }

    public void initState(){
        if(data !=null){
            data.clear();
        }
        if(temp_data!=null){
            temp_data.clear();
        }
        if(adapter!=null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            fetchFromNetWork();
        }
    }

    protected void changeOrder(int position) {
        if(data!=null){
            data.clear();
        }
        for(HeoMerchantInfoResponse t_p:temp_data){
            if(Integer.parseInt(t_p.getState()) == position){
                data.add(t_p);
            }
        }
        SortComparator disComparator = new SortComparator();
        Collections.sort(data, disComparator);

        header_count.setText("当月:"+data.size()+"条记录");
        adapter.notifyDataSetChanged();
    }

    private void fetchFromNetWork() {
        showWaitDialog("正在获取交易记录");
        HashMap<String, String> StringA1 = new HashMap<>();

        StringA1.put(Constant.AID_STR, Constant.AID);
        StringA1.put(Constant.MERCHANT_ID, Constant.user_info.get(Constant.MERCHANT_ID));
/*
        String start_t = util.getOldDate(-30);
        String end_t = util.getOldDate(-1);*/

        Calendar c = Calendar.getInstance();
        String start_t = util.getOldDate(1 - c.get(Calendar.DAY_OF_MONTH));

        String end_t = util.getOldDate(1);

        StringA1.put("date_start", start_t);
        StringA1.put("date_end", end_t);

        String merchant_id = sp.getString(Constant.USER_INFO_MERCHANT_ID, "");

        StringA1.put(Constant.MERCHANT_ID, merchant_id);
        String sign1 = util.getSign(StringA1);
        StringA1.put(Constant.SIGN, sign1);

        present.initRetrofit(Constant.URL_BAIBAO, false);
        present.QueryMerchantOrder(Constant.AID, StringA1.get(Constant.SIGN), StringA1.get(Constant.MERCHANT_ID), StringA1.get("date_start"), StringA1.get("date_end"));

    }

    protected void setEmptyStatus(boolean isOffLine) {
        if (isOffLine) {
            empty_iv.setImageResource(R.drawable.netword_error);
            empty_tv.setText("(=^_^=)，粗错了，点我刷新试试~");
            empty_lay.setEnabled(true);
            RxView.clicks(empty_iv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> emptyClick());
        } else {
            empty_lay.setEnabled(false);
            empty_iv.setImageResource(R.drawable.null_ico);
            empty_tv.setText("木有交易记录");
        }
    }

    protected void emptyClick() {
        showWaitDialog("正在努力加载...");
        fetchFromNetWork();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        fetchFromNetWork();
    }

    @Override
    protected void initView() {
        super.initView();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (IRecordListener) context;
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    @Override
    public void ResolvePayInfo(HeoCodeResponse info) {

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
        hideWaitDialog();
        if(data!=null){
            data.clear();
        }
        setEmptyStatus(false);
        if(info.getErrcode()==null){
            VToast.toast(getContext(),"网络错误");
            setEmptyStatus(true);
            return;
        }
        if (info.getErrcode().equals(SUCCESS)) {
            if(info.getContent()!=null && info.getContent().size()>0) {
                temp_data.addAll(info.getContent());
                changeOrder(0);
                adapter.notifyDataSetChanged();
            }else{
                setEmptyStatus(false);
                header_count.setText("当月:0条记录");
            }
        }else{
            header_count.setText("当月:0条记录");
        }
    }

    public void back() {
        listener.finishRecord();
    }


    public interface IRecordListener {
        void finishRecord();
    }
}
