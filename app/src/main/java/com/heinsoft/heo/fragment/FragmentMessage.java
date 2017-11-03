package com.heinsoft.heo.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heinsoft.heo.R;
import com.heinsoft.heo.adapter.MessageItemAdapter;
import com.heinsoft.heo.bean.MessageBean;
import com.heinsoft.heo.customView.RecyclerViewWithEmpty;
import com.heinsoft.heo.present.QueryPresent;
import com.heinsoft.heo.util.Constant;
import com.heinsoft.heo.util.Utils;
import com.heinsoft.heo.view.IView;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.gmariotti.recyclerview.adapter.AlphaAnimatorAdapter;

public class FragmentMessage extends BaseFragment implements MessageItemAdapter.IMessageAdapter,IView {

    QueryPresent present;
    Utils util;
    SharedPreferences sp;
    ArrayList<MessageBean> data;
    @Bind(R.id.header_btn_lay)
    LinearLayout header_btn_lay;
    @Bind(R.id.header_title)
    TextView header_title;
    @Bind(R.id.header_edit_lay)
    LinearLayout header_edit_lay;
    @Bind(R.id.message_delete_lay)
    LinearLayout message_delete_lay;

    @Bind(R.id.message_list_lay)
    RelativeLayout message_list_lay;
    @Bind(R.id.message_detail_lay)
    LinearLayout message_detail_lay;

    @Bind(R.id.message_detail_title)
    TextView message_detail_title;
    @Bind(R.id.message_detail_date)
    TextView message_detail_date;
    @Bind(R.id.message_detail_content)
    TextView message_detail_content;
    @Bind(R.id.message_detail_author)
    TextView message_detail_author;

    @Bind(R.id.message_img_content)
    ImageView message_img_content;

    @Bind(R.id.message_list_rv)
    RecyclerViewWithEmpty message_list_rv;

    @Bind(R.id.message_empty)
    RelativeLayout message_empty;
    MessageItemAdapter adapter;
    @Bind(R.id.empty_iv)
    ImageView empty_iv;
    @Bind(R.id.empty_tv)
    TextView empty_tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(FragmentMessage.this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
        initView();
    }

    private void initView() {

        //  header_edit_lay.setVisibility(View.VISIBLE);
        RxView.clicks(header_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Back());
        RxView.clicks(header_edit_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Edit());
        RxView.clicks(message_delete_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Delete());
        header_title.setText("消息");
        if (Constant.message == null) {
            Constant.message = new ArrayList<>();
        }
        data.addAll(Constant.message);
        adapter = new MessageItemAdapter(data, getContext());
        message_list_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        //	recycle_view.setItemAnimator(new SlideInOutLeftItemAnimator(recycle_view));
        AlphaAnimatorAdapter animatorApdapter = new AlphaAnimatorAdapter(adapter, message_list_rv);
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        message_list_rv.setEmptyView(message_empty);
        message_list_rv.setAdapter(animatorApdapter);
        adapter.setLiterner(FragmentMessage.this);

        setEmptyStatus(false);
        adapter.notifyDataSetChanged();
    }

    private void Delete() {

    }

    private void Edit() {
        if (data.size() == 0) {
            return;
        }
    }


    public void RefreshState() {
        message_list_lay.setVisibility(View.VISIBLE);
        message_detail_lay.setVisibility(View.GONE);
        if (data != null) {
            data = new ArrayList<>();
        }

        data.clear();
        data.addAll(Constant.message);
        adapter.notifyDataSetChanged();
        header_title.setText("消息");
    }

    public void Back() {
        if (message_detail_lay.getVisibility() == View.VISIBLE) {
            message_list_lay.setVisibility(View.VISIBLE);
            message_detail_lay.setVisibility(View.GONE);
            return;
        }
        super.Back();
        listener.gotoMain();
    }


    private void initDate() {
        data = new ArrayList<>();
        util = Utils.getInstance();
        present = QueryPresent.getInstance(getContext());
        present.setView(FragmentMessage.this);
        sp = getContext().getSharedPreferences(COOKIE_KEY, Context.MODE_PRIVATE);
    }


    protected void setEmptyStatus(boolean isOffLine) {
        if (isOffLine) {
            empty_iv.setImageResource(R.drawable.network_error);
            empty_tv.setText("(=^_^=)，粗错了，点我刷新试试~");
            message_empty.setEnabled(true);
            RxView.clicks(empty_iv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> emptyClick());
        } else {
            message_empty.setEnabled(false);
            empty_iv.setImageResource(R.drawable.null_ico);
            empty_tv.setText("没有消息");
        }
    }

    private void fetchFromNetWork() {
        present.initRetrofit(Constant.URL_BAIBAO, false);
        // present.QueryMessage(Constant.user_info.get(Constant.USER_INFO_ID),"1");
    }

    protected void emptyClick() {
        showWaitDialog("正在努力加载...");
        //  fetchFromNetWork();
    }


    @Override
    public void GotoDetail(int position) {
        data.get(position).setIs_read(true);
        Constant.message.get(position);
        adapter.notifyDataSetChanged();
        listener.NotifyMessage(Constant.message.get(position));
        message_detail_lay.setVisibility(View.VISIBLE);
        message_list_lay.setVisibility(View.GONE);
        message_detail_title.setText((data.get(position).getTitle()==null || data.get(position).getTitle().equals(""))? "无标题":data.get(position).getTitle());
        message_detail_date.setText(data.get(position).getSend_time());
        message_detail_author.setText((data.get(position).getSend_name()==null || data.get(position).getSend_name().equals(""))? "系统":data.get(position).getSend_name());
       /* if (data.get(position).getImg() != null) {
            message_img_content.setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(data.get(position).getImg())
                    .placeholder(R.drawable.verify_icon1)
                    .error(R.drawable.download_failed)
                    .into(message_img_content);
        } else {
            message_img_content.setVisibility(View.GONE);
        }*/
        message_detail_content.setText(Html.fromHtml(data.get(position).getContent()) == null ? "暂无内容" : Html.fromHtml(data.get(position).getContent()));

    }
}
