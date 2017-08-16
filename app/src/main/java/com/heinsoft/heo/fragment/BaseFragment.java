package com.heinsoft.heo.fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.heinsoft.heo.R;
import com.heinsoft.heo.customView.ProgressBarItem;
import com.heinsoft.heo.present.DbPresent;

import java.util.ArrayList;

public abstract class BaseFragment extends Fragment {
    protected final String COOKIE_KEY = "cookie";
    protected final String VISIBLE_MONEY_LEFT_KEY = "visible_left_money";
    protected final String VISIBLE_MONEY_RIGHT_KEY = "visible_right_money";

    protected String[] scopes = new String[]{"交易成功", "交易失败", "已撤销", "已冲正", "待支付"};

    protected ArrayList<String> pay_types = new ArrayList<>();
    protected final static String SUCCESS = "0";

    protected ArrayAdapter<String> bankAdapter;
    protected ArrayAdapter<String> proviceAdapter;
    protected ArrayAdapter<String> cityAdapter;
   // RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pay_types.add("QQ钱包T0");
        pay_types.add("QQ钱包T1");
        pay_types.add("支付宝T0");
        pay_types.add("支付宝T1");
        pay_types.add("微信T0");
        pay_types.add("微信T1");
        pay_types.add("快捷支付");

    }

    ProgressDialog progressDialog;
    protected String lock = "lock";
    protected boolean isVisible = false;
    protected DbPresent dbPresent;

    protected void showWaitDialog(String tip){
        ProgressBarItem.show(getContext(),tip,false,null);
    }
    protected void hideWaitDialog() {
        ProgressBarItem.hideProgress();
    }

    protected void startLoading(ImageView imageView) {
        imageView.setVisibility(View.VISIBLE);
        Animation rotate = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);
        imageView.startAnimation(rotate);
    }

    protected void stopLoading(ImageView imageView) {
        imageView.setVisibility(View.GONE);
        imageView.clearAnimation();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            initView();
        }
    }

    AlertDialog dialog;
    protected void showDialog(int type, String title, String tip, String posbtn, String negbtn) {
        dialog = null;
        if (negbtn == null) {
            dialog = new AlertDialog.Builder(getActivity()).setTitle(title)
                    .setMessage(tip)
                    .setPositiveButton(posbtn, (dia, which) -> confirm(type, dia))
                    .create();
        } else {
            dialog = new AlertDialog.Builder(getActivity()).setTitle(title)
                    .setMessage(tip)
                    .setPositiveButton(posbtn, (dia, which) -> confirm(type, dia))
                    .setNegativeButton(negbtn, (dia, which) -> cancel(type, dia)).create();
        }
        dialog.setCancelable(false);
        dialog.show();

    }

    protected void cancel(int type, DialogInterface dia) {
        dialog.dismiss();
    }

    protected void confirm(int type, DialogInterface dia) {
        dialog.dismiss();
    }


    protected void initView(){

    }

}
