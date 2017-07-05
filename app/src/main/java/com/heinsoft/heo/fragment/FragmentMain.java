package com.heinsoft.heo.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heinsoft.heo.R;
import com.heinsoft.heo.adapter.GlideImageLoader;
import com.heinsoft.heo.bean.HeoCodeResponse;
import com.heinsoft.heo.bean.HeoMerchantInfoResponse;
import com.heinsoft.heo.bean.HeoProfitResponse;
import com.heinsoft.heo.present.QueryPresent;
import com.heinsoft.heo.util.Constant;
import com.heinsoft.heo.util.Utils;
import com.heinsoft.heo.util.VToast;
import com.heinsoft.heo.view.IPayView;
import com.heinsoft.heo.view.IUserView;
import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;

public class FragmentMain extends BaseFragment implements OnBannerListener, IPayView, IUserView {
    @Bind(R.id.main_tab_left_lay)
    LinearLayout main_tab_left_lay;
    @Bind(R.id.main_pay_lay)
    LinearLayout main_pay_lay;
    @Bind(R.id.main_share_rv)
    ImageView main_share_rv;

    @Bind(R.id.main_today_reward)
    TextView main_today_reward;
    @Bind(R.id.main_history_reward)
    TextView main_history_reward;

    @Bind(R.id.main_tab_right_lay)
    LinearLayout main_tab_right_lay;
    @Bind(R.id.main_banner)
    Banner main_banner;
    IMainListener listener;

    QueryPresent present;
    Utils util;
    SharedPreferences sp;
    Boolean fetchHistory = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_lay, container, false);
        ButterKnife.bind(FragmentMain.this, view);
        RxView.clicks(main_tab_left_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> card());
        RxView.clicks(main_tab_right_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> scan());
        RxView.clicks(main_pay_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> pay());
        RxView.clicks(main_share_rv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> share());

        RxView.clicks(main_today_reward).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> getTodayProfit());
        RxView.clicks(main_history_reward).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> getHistoryProfit());

        util = Utils.getInstance();
        present = QueryPresent.getInstance(getContext());
        present.setView(FragmentMain.this);
        sp = getContext().getSharedPreferences(COOKIE_KEY, Context.MODE_PRIVATE);

        initShare();
        return view;
    }

    private void initShare() {

    }

    private void share() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "锄头信用，您忠实的理财管家");
        shareIntent.setType("text/plain");
        //设置分享列表的标题，并且每次都显示分享列表
        startActivity(Intent.createChooser(shareIntent, "分享到"));

      //  VToast.toast(getContext(), "暂未集成");
        //Intent shareintent = new Intent(getActivity(),ShareActivity.class);
        //startActivity(shareintent);
    }

    private void pay() {
        listener.gotoPay();
    }

    private void scan() {

    }

    private void card() {

    }

    public void fetchMerchantInfo() {
        HashMap<String, String> StringA1 = new HashMap<>();
        StringA1.put(Constant.AID_STR, Constant.AID);
        StringA1.put(Constant.MERCHANT_ID, Constant.user_info.get(Constant.MERCHANT_ID));

        String sign1 = util.getSign(StringA1);
        StringA1.put(Constant.SIGN, sign1);

        present.initRetrofit(Constant.URL_BAIBAO, false);
        present.QueryMerchant(Constant.AID, StringA1.get(Constant.SIGN), StringA1.get(Constant.MERCHANT_ID));
    }

    public void initState() {
        if (main_banner == null) {
            return;
        }
        initDate();
    }

    private void initDate() {
        initBanner();
        main_banner.startAutoPlay();
        getTodayProfit();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
    }

    private void getTodayProfit() {
        if (Constant.user_info == null || Constant.user_info.get(Constant.AGENT_ID) == null) {
            VToast.toast(getContext(), "请先实名");
            main_today_reward.setText("没有记录");
            main_history_reward.setText("没有记录");
            return;
        }
        main_today_reward.setEnabled(false);
        main_history_reward.setEnabled(false);
        main_today_reward.setText("正在刷新");
        main_history_reward.setText("正在刷新");

        fetchHistory = false;

        HashMap<String, String> StringA1 = new HashMap<>();
        StringA1.put(Constant.AID_STR, Constant.AID);
        StringA1.put(Constant.AGENT_ID, Constant.user_info.get(Constant.AGENT_ID));

        long l = 24 * 60 * 60 * 1000; //每天的毫秒数
        String start_t = util.currentDate(util.getTodayZero() - l, "yyyy-MM-dd");
        String end_t = util.currentDate(System.currentTimeMillis(), "yyyy-MM-dd");

        StringA1.put("date_start", start_t);
        StringA1.put("date_end", end_t);
        String sign1 = util.getSign(StringA1);
        StringA1.put(Constant.SIGN, sign1);
        present.initRetrofit(Constant.URL_BAIBAO, false);
        present.QueryProfit(Constant.AID, StringA1.get(Constant.SIGN), StringA1.get(Constant.AGENT_ID), StringA1.get("date_start"), StringA1.get("date_end"));
    }

    private void getHistoryProfit() {
        if (Constant.user_info == null || Constant.user_info.get(Constant.AGENT_ID) == null) {
            VToast.toast(getContext(), "请先实名");
            return;
        }

        main_history_reward.setEnabled(false);
        main_history_reward.setText("正在刷新");

        HashMap<String, String> StringA1 = new HashMap<>();

        StringA1.put(Constant.AID_STR, Constant.AID);

        StringA1.put(Constant.AGENT_ID, Constant.user_info.get(Constant.AGENT_ID));

        long l = 30 * 24 * 60 * 60 * 1000; //每天的毫秒数
        String start_t = util.currentDate(util.getTodayZero() - l, "yyyy-MM-dd");
        String end_t = util.currentDate(System.currentTimeMillis(), "yyyy-MM-dd");

        StringA1.put("date_start", start_t);
        StringA1.put("date_end", end_t);

        String sign1 = util.getSign(StringA1);
        StringA1.put(Constant.SIGN, sign1);

        present.initRetrofit(Constant.URL_BAIBAO, false);
        present.QueryProfit(Constant.AID, StringA1.get(Constant.SIGN), StringA1.get(Constant.AGENT_ID), StringA1.get("date_start"), StringA1.get("date_end"));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (IMainListener) context;
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    /**
     * 准备轮播
     */
    private void initBanner() {
        List<Integer> bannerImageList = new ArrayList<>();
        bannerImageList.add(R.drawable.banner01);
        bannerImageList.add(R.drawable.banner02);
        bannerImageList.add(R.drawable.banner03);
        //设置banner样式
        main_banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        main_banner.setImageLoader(new GlideImageLoader());
        //设置banner动画效果
        main_banner.setBannerAnimation(Transformer.ZoomOut);
        //设置自动轮播，默认为true
        main_banner.isAutoPlay(true);
        //设置轮播时间
        main_banner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        main_banner.setIndicatorGravity(BannerConfig.RIGHT);
        //设置图片集合
        main_banner.setImages(bannerImageList);
        main_banner.start();
        main_banner.setOnBannerListener(this);
    }

    @Override
    public void OnBannerClick(int position) {

    }

    @Override
    public void ResolvePayInfo(HeoCodeResponse info) {

    }

    @Override
    public void ResolveProfitInfo(HeoProfitResponse info) {
        KLog.v(info.toString());
        if (!fetchHistory) {
            main_today_reward.setEnabled(true);
        } else {
            main_history_reward.setEnabled(true);
        }
        if (info.getErrcode() == null) {
            if (fetchHistory) {
                main_history_reward.setText("网络错误");
            } else {
                main_today_reward.setText("网络错误");
            }
            if (!fetchHistory) {
                fetchHistory = true;
                getHistoryProfit();
            }
            return;
        }
        if (info.getErrcode().equals(SUCCESS)) {
            if (fetchHistory) {
                main_history_reward.setText(info.getTotal_money() == null ? "没有记录" : info.getTotal_money());
            } else {
                main_today_reward.setText(info.getTotal_money() == null ? "没有记录" : info.getTotal_money());
            }
        } else {
            if (fetchHistory) {
                main_history_reward.setText(info.getErrmsg());
            } else {
                main_today_reward.setText(info.getErrmsg());
            }
        }
        if (!fetchHistory) {
            fetchHistory = true;
            getHistoryProfit();
        }
        //	VToast.toast(getContext(),info.getErrmsg());
    }

    @Override
    public void ResolveLoginInfo(HeoCodeResponse info) {

    }

    @Override
    public void ResolveRegisterInfo(HeoCodeResponse info) {

    }

    @Override
    public void ResolveQcodeInfo(ResponseBody info) {

    }

    @Override
    public void ResolveCodeInfo(ResponseBody info) {

    }

    @Override
    public void ResolveForgetInfo(HeoCodeResponse info) {

    }

    @Override
    public void ResolveAlterInfo(HeoCodeResponse info) {

    }

    @Override
    public void ResolveEmailInfo(HeoCodeResponse info) {

    }

    @Override
    public void ResolveVerifyInfo(HeoCodeResponse info) {

    }

    @Override
    public void ResolveCodeVerifyInfo(HeoCodeResponse info) {

    }

    @Override
    public void ResolvePhoneChangeInfo(HeoCodeResponse info) {

    }

    @Override
    public void ResolveMerchantInfo(HeoCodeResponse info) {
        KLog.v(info.toString());
        hideWaitDialog();
        if (info.getErrcode() == null) {
            VToast.toast(getContext(), "网络错误,不能获取商户信息");
            return;
        }
        if (info.getErrcode().equals(SUCCESS)) {
            if (info.getContent() == null && info.getContent().get(0) == null) {
                KLog.v("商户账号无信息");
                return;
            }
            HeoMerchantInfoResponse merchant = info.getContent().get(0);

            Constant.user_info.put(Constant.AGENT_ID, merchant.getAgent_id());
            String sta = merchant.getState();
            Constant.user_info.put(Constant.USER_INFO_ISAUTH, sta == null ? "0" : sta);
            Constant.user_info.put(Constant.NAME, merchant.getName());
            Constant.user_info.put(Constant.CONTACT, merchant.getContact());
            Constant.user_info.put(Constant.RATE, merchant.getRate());
            Constant.user_info.put(Constant.MERCHANT_ID, merchant.getMerchant_id());
            Constant.user_info.put(Constant.USER_INFO_IDCARD, merchant.getId_card());

            Constant.user_info.put(Constant.BANK, merchant.getBank());
            Constant.user_info.put(Constant.BANK_ACCOUNT_NAME, merchant.getBank_account_name());
            Constant.user_info.put(Constant.BANK_ACCOUNT, merchant.getBank_account());
            Constant.user_info.put(Constant.SUB_BRANCH, merchant.getSub_branch());
            Constant.user_info.put(Constant.BANKFIRM, merchant.getBankfirm());
            Constant.user_info.put(Constant.ID_CARD_NAME, merchant.getCreate_name());

            Constant.user_info.put(Constant.ACCOUNT, merchant.getUsername());
            Constant.user_info.put(Constant.USER_INFO_PW, merchant.getPassword());
            Constant.user_info.put(Constant.PHONE, merchant.getPhone());
            Constant.user_info.put(Constant.PROVINCE, merchant.getProvince());
            Constant.user_info.put(Constant.CITY, merchant.getCity());
            Constant.user_info.put(Constant.ADDRESS, merchant.getAddress());

            Constant.user_info.put(Constant.PIC_1, merchant.getImages().getPic_1());
            Constant.user_info.put(Constant.PIC_2, merchant.getImages().getPic_2());
            Constant.user_info.put(Constant.PIC_3, merchant.getImages().getPic_3());
            Constant.user_info.put(Constant.PIC_4, merchant.getImages().getPic_4());
            Constant.user_info.put(Constant.PIC_5, merchant.getImages().getPic_5());
            listener.updateMerchant();
        }
    }

    public interface IMainListener {
        void gotoPay();
        void updateMerchant();
    }
}
