package com.heinsoft.heo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.KeyEvent;

import com.heinsoft.heo.HeoApplication;
import com.heinsoft.heo.R;
import com.heinsoft.heo.fragment.BaseFragment;
import com.heinsoft.heo.fragment.FragmentForget;
import com.heinsoft.heo.fragment.FragmentLogin;
import com.heinsoft.heo.fragment.FragmentMain;
import com.heinsoft.heo.fragment.FragmentMessage;
import com.heinsoft.heo.fragment.FragmentPay;
import com.heinsoft.heo.fragment.FragmentRecord;
import com.heinsoft.heo.fragment.FragmentRegister;
import com.heinsoft.heo.fragment.FragmentRepayment;
import com.heinsoft.heo.fragment.FragmentSettle;
import com.heinsoft.heo.fragment.FragmentVerify;
import com.heinsoft.heo.fragment.FragmentWebview;
import com.heinsoft.heo.fragment.ListFragmentMenuSliding;
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
public class BaseActivity extends SlidingFragmentActivity  {
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
    protected static final String PAGE_10 = "page_10";

    protected FragmentLogin fragment0;
    protected FragmentMain fragment1;
    protected FragmentWebview fragment2;
    protected FragmentPay fragment3;
    protected FragmentVerify fragment4;
    protected FragmentRecord fragment5;
    protected FragmentRegister fragment6;
    protected FragmentSettle fragment7;
    protected FragmentMessage fragment8;
    protected FragmentRepayment fragment9;
    protected FragmentForget fragment10;
    private DoubleConfirm double_c;
    ListFragmentMenuSliding mFrag;
    SlidingMenu slidingMenu;
    protected Context context;
    protected int cur_page = -1;
    protected BaseFragment curFragment;
    /**
     * 双击事件
     */
    private DoubleConfirm.DoubleConfirmEvent doubleConfirmEvent = new DoubleConfirm.DoubleConfirmEvent() {
        public void doSecondConfirmEvent() {
            HeoApplication.getInstance().exitApplication();
        }

        public int getFirstConfirmTipsId() {
            return R.string.msg_exit;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBehindContentView(R.layout.activity_behind_lay);

        HeoApplication.getInstance().addActivitys(this);
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
        slidingMenu.setOnCloseListener(() -> {
             bottomToMain();
        });
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


    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        if (paramInt == KeyEvent.KEYCODE_BACK) {
            KLog.v("cur_page"+cur_page);
            switch (cur_page) {
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                    curFragment.Back();
                    return true;
                default:
                    this.double_c.onKeyPressed(paramKeyEvent, this);
                    return true;
            }
        }
        return false;
    }

    protected void closeSliding() {
        if (slidingMenu != null && slidingMenu.isMenuShowing()) {
            slidingMenu.toggle();
        }
    }
    protected void openSliding() {
        if (slidingMenu != null && !slidingMenu.isMenuShowing()) {
            slidingMenu.toggle();
        }
    }


    public void gotoPay() {

    }

    public void gotoMain() {
        KLog.v("gotoMain"+mFrag +slidingMenu);
        if(slidingMenu!=null) {
            mFrag.refreshInfo();
            this.slidingMenu.setTouchModeAbove(SlidingMenu.RIGHT);
        }
    }


    public void openCamera(int type) {

    }


    public void doPhoto(int type) {

    }


    public void showVerify() {
        closeSliding();
    }

    public void gotoLogin() {
        closeSliding();
        if(this.slidingMenu !=null) {
            this.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }


    public void gotoRecord() {
        closeSliding();
    }


    public void showWithdraw() {
        closeSliding();
    }


    public void gotoRegister() {

    }

    public void gotoVerify() {
        closeSliding();
    }

    protected void bottomToMain()
    {}

    public void gotoForgot() {

    }
}
