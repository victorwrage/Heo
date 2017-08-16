
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

import com.heinsoft.heo.R;
import com.heinsoft.heo.present.QueryPresent;
import com.heinsoft.heo.util.Constant;
import com.heinsoft.heo.util.Utils;
import com.jakewharton.rxbinding2.view.RxView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;


/**
 * @Description:TODO(实名提示)
 * @author: xiaoyl
 * @date: 2013-07-20 下午6:38:07
 */
public class DialogFragmentPhotoTip extends DialogFragment  {
    private static final String COOKIE_KEY = "cookie";
    private IPhotoTipListtener callBack;
    private static final String STATUS_SUCCESS = "1001";
    private static final String STATUS_FAIL = "1002";

    private Disposable disposable;

    @Bind(R.id.verify_shut_iv)
    ImageView verify_shut_iv;

    @Bind(R.id.photo_img_iv)
    ImageView photo_img_iv;
    @Bind(R.id.photo_do_btn)
    Button photo_do_btn;
    @Bind(R.id.photo_cancel_btn)
    Button photo_cancel_btn;

    @Bind(R.id.verify_shut_lay)
    RelativeLayout verify_shut_lay;



    QueryPresent present;
    Utils util;
    int cur_state;
    public DialogFragmentPhotoTip() {

    }

    @Override
    public void onAttach(Context activity) {
        try {
            callBack = (IPhotoTipListtener) activity;
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
        View view = inflater.inflate(R.layout.photo_tip_layout, container,
                false);
        ButterKnife.bind(DialogFragmentPhotoTip.this, view);
        util = Utils.getInstance();
        RxView.clicks(photo_do_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> photo());
        RxView.clicks(verify_shut_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> dismiss());
        RxView.clicks(photo_cancel_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> dismiss());
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setCancelable(false);
        int width = 300;
        int height = 200;
        if(Constant.photo_idx==2){
             width = 200;
             height = 300;
        }
        Picasso.with(getContext()).load( new File(Constant.photo_path))
              //  .resize(width,height)
              //  .centerCrop()
                .placeholder(R.drawable.card)
                .error(R.drawable.download_failed)
                .into(photo_img_iv);
      // cur_state = Integer.parseInt(Constant.user_info==null?"0": Constant.user_info.get(Constant.USER_INFO_ISAUTH));
    }

    void photo(){
        dismiss();
        callBack.doPhoto(Constant.photo_idx);
    }

    public interface IPhotoTipListtener {
        void doPhoto(int type);
    }
}
