package com.heinsoft.heo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heinsoft.heo.R;
import com.heinsoft.heo.bean.CardBean;
import com.heinsoft.heo.util.Utils;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;

/**
 * Info: 消息
 * Created by xiaoyl
 * 创建时间:2017/8/07 10:15
 */
public class CardItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<CardBean> items;
    Utils util;

    ICardAdapter listener;
    public CardItemAdapter(ArrayList<CardBean> items, Context context) {
        this.items = items;
        this.context = context;
        util = Utils.getInstance();
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int VIEW_TYPE) {

        return new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.card_lay_item, viewGroup,
                false));
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder_, int i) {
        MyViewHolder holder = (MyViewHolder) holder_;
        CardBean item = items.get(i);
        holder.payment_num_tv.setText(item.getCardnum());
        holder.payment_item_name_tv.setText(item.getCardname());
        holder.payment_item_icon.setImageResource(item.getCardicon());
        if(item.getIs_plan()){
            holder.payment_item_plan_tv.setText("已计划");
        }else{
            holder.payment_item_plan_tv.setText("未计划");
        }

        RxView.clicks(holder.card_item_lay).subscribe(s -> gotoPlan(holder, i));
    }


    private void gotoPlan(MyViewHolder holder, int i) {listener.gotoPlan(i);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setListener(ICardAdapter fragmentRepayment) {
        listener = fragmentRepayment;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView payment_num_tv, payment_item_name_tv, payment_item_plan_tv;
        LinearLayout card_item_lay;
        ImageView payment_item_icon;

        public MyViewHolder(View view) {
            super(view);
            payment_num_tv = (TextView) view.findViewById(R.id.payment_num_tv);
            payment_item_name_tv = (TextView) view.findViewById(R.id.payment_item_name_tv);
            payment_item_plan_tv = (TextView) view.findViewById(R.id.payment_item_plan_tv);

            card_item_lay = (LinearLayout) view.findViewById(R.id.card_item_lay);
            payment_item_icon = (ImageView) view.findViewById(R.id.payment_item_icon);

        }
    }

    public interface ICardAdapter{

        void gotoPlan(int position);

    }
}
