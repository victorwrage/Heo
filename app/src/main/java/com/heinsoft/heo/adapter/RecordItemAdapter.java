package com.heinsoft.heo.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.heinsoft.heo.R;
import com.heinsoft.heo.bean.HeoMerchantInfoResponse;

import java.util.ArrayList;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/5/17 14:28
 */
public class RecordItemAdapter extends RecyclerView.Adapter<RecordItemAdapter.MyViewHolder> {
    Context context;
    ArrayList<HeoMerchantInfoResponse> items;


    public RecordItemAdapter(ArrayList<HeoMerchantInfoResponse> items, Context context, Fragment f) {
        this.items = items;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int VIEW_TYPE) {
        return new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.evaluate_item_lay, viewGroup,
                false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int i) {

        HeoMerchantInfoResponse item = items.get(i);

        holder.record_merchant_id.setText(item.getMerchant_id());
      //  holder.record_comment.setText("");
        holder.record_order_id.setText(item.getOrder_id());
        int sta = Integer.parseInt(item.getState());
        int id = R.color.green;
        String str = "";
        switch(sta){
            case 0:
                str = "支付成功";
                id = R.color.green;
                break;
            case 1:
                str= "支付失败";
                id = R.color.red;
                break;
            case 2:
                str ="已撤销";
                id = R.color.gray;
                break;
            case 3:
                str="已冲正";
                id = R.color.chutou_check_txt;
                break;
            case 4:
                str ="待支付";
                id = R.color.chutou_txt;
                break;
        }
        holder.record_state.setText(str);
        holder.record_state.setTextColor(context.getResources().getColor(id));

     //   holder.record_cashier.setText(item.getCashier());
        holder.record_time.setText(item.getCreate_time());
        holder.record_money.setText(item.getPay_money());
        holder.record_t0.setText(item.getT0_fee());

        sta = Integer.parseInt(item.getSource());
        switch(sta){
            case 0:
                holder.record_tunnel.setText("微信支付");
                holder.record_tunnel.setTextColor(context.getResources().getColor(R.color.green));
                break;
            case 1:
                holder.record_tunnel.setText("支付宝支付");
                holder.record_tunnel.setTextColor(context.getResources().getColor(R.color.dodgerblue));
                break;
            case 2:
                holder.record_tunnel.setText("快捷支付");
                holder.record_tunnel.setTextColor(context.getResources().getColor(R.color.tab_item_d));
                break;
            case 3:
                holder.record_tunnel.setText("QQ钱包支付");
                holder.record_tunnel.setTextColor(context.getResources().getColor(R.color.orangered));
                break;
        }
      //  holder.record_comment.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
     //   RxView.clicks(holder.record_item_lay_).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> ellipse(holder));
        if (i % 2 == 0) {
            holder.record_item_lay_.setCardBackgroundColor(context.getResources().getColor(R.color.snow));
        } else {
            holder.record_item_lay_.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        }

    }

    private void ellipse(MyViewHolder holder) {
      //  holder.record_comment.setEllipsize(TextUtils.TruncateAt.valueOf("START"));
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView record_merchant_id,record_order_id,record_state,record_cashier,record_time,record_money,record_t0,record_tunnel;
       // EllipsizeTextView record_comment;

        CardView record_item_lay_;

        public MyViewHolder(View view) {
            super(view);
            record_merchant_id = (TextView) view.findViewById(R.id.record_merchant_id);
    //        record_comment = (EllipsizeTextView) view.findViewById(R.id.record_comment);
            record_order_id = (TextView) view.findViewById(R.id.record_order_id);
            record_state = (TextView) view.findViewById(R.id.record_state);
            record_cashier = (TextView) view.findViewById(R.id.record_cashier);
            record_time = (TextView) view.findViewById(R.id.record_time);
            record_money = (TextView) view.findViewById(R.id.record_money);
            record_t0 = (TextView) view.findViewById(R.id.record_t0);
            record_tunnel = (TextView) view.findViewById(R.id.record_tunnel);
            record_item_lay_ = (CardView) view.findViewById(R.id.record_item_lay_);

        }
    }

}
