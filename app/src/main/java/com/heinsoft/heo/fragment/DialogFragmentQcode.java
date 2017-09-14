
package com.heinsoft.heo.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heinsoft.heo.view.IFragmentActivity;
import com.heinsoft.heo.util.VToast;
import com.jakewharton.rxbinding2.view.RxView;
import com.heinsoft.heo.R;
import com.heinsoft.heo.present.QueryPresent;
import com.heinsoft.heo.util.Utils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @ClassName: DialogFragmentAddTag
 * @Description:TODO(二维码界面)
 * @author: xiaoyl
 * @date: 2013-07-20 下午6:38:07
 */
public class DialogFragmentQcode extends DialogFragment {
    private IFragmentActivity listener;


    @Bind(R.id.my_qcode_shut_tv)
    TextView my_qcode_shut_tv;
    @Bind(R.id.my_qcode_later_tv)
    TextView my_qcode_later_tv;

    @Bind(R.id.my_qcode_pay_tv)
    TextView my_qcode_pay_tv;
    @Bind(R.id.my_qcode_money_tv)
    TextView my_qcode_money_tv;

    @Bind(R.id.my_qcode_shut_lay)
    RelativeLayout my_qcode_shut_lay;

    @Bind(R.id.my_qcode_shut_iv)
    ImageView my_qcode_shut_iv;
    @Bind(R.id.my_qcode_qcode_iv)
    ImageView my_qcode_qcode_iv;

    String pay_type,pay_money;
    SharedPreferences sp;
    QueryPresent present;
    Utils util;
    Bitmap qcode;
    private Map<String, String> temp_info;

    public DialogFragmentQcode() {

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
        View view = inflater.inflate(R.layout.my_qcode_layout, container,
                false);
        ButterKnife.bind(DialogFragmentQcode.this, view);
        util = Utils.getInstance();

        RxView.clicks(my_qcode_shut_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> shut());
        RxView.clicks(my_qcode_later_tv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> save());
        return view;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (qcode != null) {
            my_qcode_qcode_iv.setImageBitmap(qcode);
        }
        my_qcode_pay_tv.setText(pay_type==null?"":pay_type);
        my_qcode_money_tv.setText(pay_money==null?"":"￥"+pay_money);
    }

    private void save() {
        util.saveImageToGallery(getContext(), qcode);
        VToast.toast(getContext(),"二维码已经保存");
        dismiss();
    }

    private void shut() {
        dismiss();
    }

    public void setQcode(Bitmap bitmap,String pay_type,String pay_money) {
        this.pay_type = pay_type;
        this.pay_money = pay_money;
        qcode = bitmap;
    }

}
