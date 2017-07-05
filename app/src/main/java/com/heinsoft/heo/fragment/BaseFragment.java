package com.heinsoft.heo.fragment;


import android.app.ProgressDialog;
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
    protected long isFar = 5000;//5公里
   // protected String[] scopes = new String[]{"全部目的地", "只看近的", "只看远的"};

    protected ArrayList<String> pay_types = new ArrayList<>();
    protected final static String SUCCESS = "0";

    protected ArrayAdapter<String> spinner_adapter;
   // RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pay_types.add("微信T0");
        pay_types.add("微信T1");
        pay_types.add("支付宝T0");
        pay_types.add("支付宝T1");
        pay_types.add("QQ钱包T0");
        pay_types.add("QQ钱包T1");

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

    protected void initView(){

    }

}
