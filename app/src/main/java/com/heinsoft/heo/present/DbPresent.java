package com.heinsoft.heo.present;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.heinsoft.heo.view.IView;


/**
 * Created by Administrator on 2017/4/6.
 */

public class DbPresent implements IDbPresent {
    private IView iView;
    private Context context;
    private IDbPresent iRequestMode;

    private static DbPresent instance = null;

    public void setView(Activity activity) {
        iView = (IView) activity;
    }

    public void setView(Fragment fragment) {
        iView = (IView) fragment;
    }

    private DbPresent(Context context_) {
        context = context_;
    }

    public static synchronized DbPresent getInstance(Context context) {
        if (instance == null) {
            return new DbPresent(context);
        }
        return instance;
    }



}
