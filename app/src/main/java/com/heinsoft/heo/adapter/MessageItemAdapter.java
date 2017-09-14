package com.heinsoft.heo.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.heinsoft.heo.R;
import com.heinsoft.heo.bean.MessageBean;
import com.heinsoft.heo.fragment.FragmentMessage;
import com.heinsoft.heo.util.Utils;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;

/**
 * Info: 消息
 * Created by xiaoyl
 * 创建时间:2017/8/07 10:15
 */
public class MessageItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<MessageBean> items;
    Utils util;
    IMessageAdapter listerner;
    public MessageItemAdapter(ArrayList<MessageBean> items, Context context) {
        this.items = items;
        this.context = context;
        util = Utils.getInstance();
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int VIEW_TYPE) {

        return new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.message_lay_item, viewGroup,
                false));
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder_, int i) {
        MyViewHolder holder = (MyViewHolder) holder_;
        MessageBean item = items.get(i);
        holder.message_item_type.setText(item.getTitle());
        holder.message_item_date.setText(item.getSend_time());
    //    holder.message_item_content.setText("");
        if(item.getIs_read()){
            holder.message_item_read.setVisibility(View.GONE);
        }else{
            holder.message_item_read.setVisibility(View.VISIBLE);
        }

        RxView.clicks(holder.message_item_lay_).subscribe(s -> setRead(holder, i));
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setLiterner(FragmentMessage fragmentMessage) {
        listerner = fragmentMessage;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView message_item_type, message_item_date, message_item_content;
        CardView message_item_lay_;
        ImageView message_item_icon,message_item_read;

        public MyViewHolder(View view) {
            super(view);
            message_item_type = (TextView) view.findViewById(R.id.message_item_type);
            message_item_date = (TextView) view.findViewById(R.id.message_item_date);
            message_item_content = (TextView) view.findViewById(R.id.message_item_content);

            message_item_lay_ = (CardView) view.findViewById(R.id.message_item_lay_);
            message_item_icon = (ImageView) view.findViewById(R.id.message_item_icon);
            message_item_read = (ImageView) view.findViewById(R.id.message_item_read);
        }
    }

    private void setRead(MyViewHolder holder, int position) {

        listerner.GotoDetail(position);
       // item.setIs_read(true);
    }

    public interface IMessageAdapter{

        void GotoDetail(int position);

    }
}
