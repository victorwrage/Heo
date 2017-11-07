package com.heinsoft.heo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.heinsoft.heo.R;

import java.util.List;

/**
 * Created by Thinkpadx240 on 2017/11/3.
 */

public class BankAccountListAdapter extends BaseAdapter {

    private Context mContex;
    private List<String> card,bank;

    public BankAccountListAdapter(Context context,List<String> card_data, List<String> bank_data) {
        mContex=context;
        card=card_data;
        bank=bank_data;
    }

    @Override
    public int getCount() {
        return card.size();
    }

    @Override
    public Object getItem(int i) {
        return card.get(i)+bank.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        return null;
    }
}
