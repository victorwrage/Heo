package com.heinsoft.heo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.KeyEvent;

import com.heinsoft.heo.R;
import com.heinsoft.heo.RRSApplication;
import com.heinsoft.heo.fragment.FragmentMain;
import com.heinsoft.heo.fragment.FragmentPay;
import com.heinsoft.heo.fragment.FragmentRecord;
import com.heinsoft.heo.fragment.FragmentRegisterLogin;
import com.heinsoft.heo.fragment.FragmentVerify;
import com.heinsoft.heo.fragment.FragmentWebview;
import com.heinsoft.heo.fragment.ListFragmentMenuSliding;
import com.heinsoft.heo.util.Constant;
import com.heinsoft.heo.util.DoubleConfirm;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.socks.library.KLog;


/**
 * @ClassName: BaseActivity
 * @Description:TODO(界面的基类)
 * @author: xiaoyl
 * @date: 2013-7-10 下午2:30:06
 */
public class BaseActivity extends SlidingFragmentActivity implements ListFragmentMenuSliding.IFragmentSlindingListtener {
    protected static final String PAGE_0 = "page_0";
    protected static final String PAGE_1 = "page_1";
    protected static final String PAGE_2 = "page_2";
    protected static final String PAGE_3 = "page_3";
    protected static final String PAGE_4 = "page_4";
    protected static final String PAGE_5 = "page_5";
    protected static final String PAGE_6 = "page_6";
    protected static final String PAGE_7 = "page_7";
    protected static final String PAGE_8 = "page_8";
    protected static final String PAGE_9 = "page_9";

    protected FragmentRegisterLogin fragment0;
    protected FragmentMain fragment1;
    protected FragmentWebview fragment2;
    protected FragmentWebview fragment3;
    protected FragmentWebview fragment4;
    protected FragmentPay fragment5;
    protected FragmentVerify fragment6;
    protected FragmentWebview fragment7;
    protected FragmentRecord fragment8;
    private DoubleConfirm double_c;
    ListFragmentMenuSliding mFrag;
    SlidingMenu slidingMenu;
    protected Context context;
    protected int cur_page = -1;

    /**
     * 双击事件
     */
    private DoubleConfirm.DoubleConfirmEvent doubleConfirmEvent = new DoubleConfirm.DoubleConfirmEvent() {
        public void doSecondConfirmEvent() {
            RRSApplication.getInstance().exitApplication();
        }

        public int getFirstConfirmTipsId() {
            return R.string.msg_exit;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBehindContentView(R.layout.activity_behind_lay);

        RRSApplication.getInstance().addActivitys(this);
        context = getApplicationContext();
        this.double_c = new DoubleConfirm();
        this.double_c.setEvent(this.doubleConfirmEvent);
    }

    protected void initSliding() {
        FragmentTransaction ft = getSupportFragmentManager()
                .beginTransaction();
        if (mFrag == null) {
            mFrag = new ListFragmentMenuSliding();
        }
        ft.replace(R.id.container_behind, mFrag);
        ft.commit();

        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int width = 2 * localDisplayMetrics.widthPixels / 3;

        this.slidingMenu = getSlidingMenu();
        this.slidingMenu.setMode(SlidingMenu.RIGHT);
        //this.slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        this.slidingMenu.setShadowDrawable(R.drawable.shadow);
        //this.slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        //this.slidingMenu.setFadeDegree(0.35F);
        this.slidingMenu.setTouchModeAbove(SlidingMenu.RIGHT);
        /*this.slidingMenu.setBehindCanvasTransformer(new CanvasTransformer() {
            @Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				canvas.scale(percentOpen, 1, 0, 0);
			}
		});*/
        this.slidingMenu.setBehindWidth(width);
        invalidateOptionsMenu();
    }

    protected void refreshSliding() {
        if (mFrag != null) {
            mFrag.initView();
        }
    }

    protected void toggleSliding() {
        if (slidingMenu != null && !slidingMenu.isMenuShowing()) {
            slidingMenu.toggle();

        }
    }

    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        if (paramInt == KeyEvent.KEYCODE_BACK) {
            if (slidingMenu != null && slidingMenu.isMenuShowing()) {
                return super.onKeyDown(paramInt, paramKeyEvent);
            }
            KLog.v("onKeyDown"+cur_page);
            switch (cur_page) {
                case 0:
                case 1:
                    this.double_c.onKeyPressed(paramKeyEvent, this);
                    return true;
                case 2:
                    if(fragment2.canBack()){
                        fragment2.back();
                        return true;
                    }
                    gotoMain();
                    return true;
                case 3:
                    if(fragment3.canBack()){
                        fragment3.back();
                        return true;
                    }
                    gotoMain();
                    return true;
                case 4:
                    if(fragment4.canBack()){
                        fragment4.back();
                        return true;
                    }
                    gotoMain();
                    return true;
                case 5:
                    fragment5.step(0);
                    return true;
                case 6:
                    fragment6.back();
                    return true;
                case 7:
                    if(fragment7.canBack()){
                        fragment7.back();
                        return true;
                    }
                    gotoMain();
                    return true;
                case 8:
                    fragment8.back();
                    return true;
                default:
                    this.double_c.onKeyPressed(paramKeyEvent, this);
                    return true;
            }
        }
        return false;
    }

    protected void gotoMain() {

    }

    @Override
    public void clearDateFlag() {
        slidingMenu.toggle();
        Constant.user_info = null;
        FragmentTransaction ft = getSupportFragmentManager()
                .beginTransaction();
        ft.remove(mFrag);
        ft.commit();
        this.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        gotoLogin();
    }

    @Override
    public void verify() {
        slidingMenu.toggle();
        gotoVerify();
    }

    @Override
    public void orderRecord() {
        slidingMenu.toggle();
    }

    @Override
    public void showVerify() {

    }

    @Override
    public void withdraw() {
        slidingMenu.toggle();
    }

    @Override
    public void update() {
        slidingMenu.toggle();
    }

    protected void gotoLogin() {

    }

    protected void gotoVerify() {

    }

    protected void webviewBack() {

    }

    protected boolean webviewCanBack() {
        return true;
    }

}
