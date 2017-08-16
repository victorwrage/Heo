
package com.heinsoft.heo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heinsoft.heo.R;
import com.heinsoft.heo.present.QueryPresent;
import com.heinsoft.heo.util.Constant;
import com.heinsoft.heo.util.Utils;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;


/**
 * @Description:TODO(实名认证)
 * @author: xiaoyl
 * @date: 2013-07-20 下午6:38:07
 */
public class DialogFragmentVerifyTip extends DialogFragment  {
    private static final String COOKIE_KEY = "cookie";
    private IVerifyTipListtener callBack;
    private static final String STATUS_SUCCESS = "1001";
    private static final String STATUS_FAIL = "1002";

    private Disposable disposable;

    @Bind(R.id.verify_shut_iv)
    ImageView verify_shut_iv;
    @Bind(R.id.verify_do_btn)
    Button verify_do_btn;

    @Bind(R.id.verify_shut_lay)
    RelativeLayout verify_shut_lay;


    @Bind(R.id.verify_tip_tv)
    TextView verify_tip_tv;

    QueryPresent present;
    Utils util;
    int cur_state;
    public DialogFragmentVerifyTip() {

    }

    @Override
    public void onAttach(Context activity) {
        try {
            callBack = (IVerifyTipListtener) activity;
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
        View view = inflater.inflate(R.layout.verify_tip_layout, container,
                false);
        ButterKnife.bind(DialogFragmentVerifyTip.this, view);
        util = Utils.getInstance();
        RxView.clicks(verify_do_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s ->verify());
        RxView.clicks(verify_shut_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> dismiss());
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setCancelable(false);
        cur_state = Integer.parseInt(Constant.user_info==null?"0": Constant.user_info.get(Constant.USER_INFO_ISAUTH));
        switch(cur_state){
            case 0:
                verify_do_btn.setText("立即认证");
                break;
            case 1://已通过

                break;
            case 2:
                verify_do_btn.setText("立即认证");
                verify_tip_tv.setText("您的实名认证已失效");
                verify_tip_tv.setTextColor(getResources().getColor(R.color.gray));
                break;
            case 3:
                verify_do_btn.setText("更新资料");
                verify_tip_tv.setText("您的实名认证未通过，请提交正确的信息");
                verify_tip_tv.setTextColor(getResources().getColor(R.color.burlywood));
                break;
        }
    }

    void verify(){
        dismiss();
        callBack.doVerify();
    }

    public interface IVerifyTipListtener {
        void doVerify();
    }
}
