package com.heinsoft.heo.fragment;

import android.content.Context;
import android.content.DialogInterface;
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
import com.heinsoft.heo.bean.HeoCodeObjResponse;
import com.heinsoft.heo.bean.HeoCodeResponse;
import com.heinsoft.heo.bean.HeoMerchantInfoResponse;
import com.heinsoft.heo.present.QueryPresent;
import com.heinsoft.heo.util.Constant;
import com.heinsoft.heo.util.Utils;
import com.heinsoft.heo.util.VToast;
import com.heinsoft.heo.view.IAgentView;
import com.heinsoft.heo.view.IInviteView;
import com.heinsoft.heo.view.IPayView;
import com.heinsoft.heo.view.IUserView;
import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;

public class FragmentMain extends BaseFragment implements OnBannerListener, IPayView, IUserView, IAgentView, IInviteView {
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
    @Bind(R.id.main_level_tv)
    TextView main_level_tv;

    @Bind(R.id. main_level_lay)
    LinearLayout  main_level_lay;
    @Bind(R.id.main_tab_right_lay)
    LinearLayout main_tab_right_lay;
    @Bind(R.id.main_banner)
    Banner main_banner;
    IMainListener listener;

    QueryPresent present;
    Utils util;
    SharedPreferences sp;
    Boolean fetchHistory = false;

    String invite_arr[];
    String invite_str;
    Double today_money = 0.00, history_money = 0.00;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_lay, container, false);
        ButterKnife.bind(FragmentMain.this, view);
        RxView.clicks(main_tab_left_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> card());
        RxView.clicks(main_tab_right_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> scan());
        RxView.clicks(main_pay_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> pay());
        RxView.clicks(main_share_rv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> share());

        RxView.clicks(main_today_reward).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> visible(main_today_reward, 0));
        RxView.clicks(main_history_reward).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> visible(main_history_reward, 1));

        util = Utils.getInstance();
        present = QueryPresent.getInstance(getContext());
        present.setView(FragmentMain.this);
        sp = getContext().getSharedPreferences(COOKIE_KEY, Context.MODE_PRIVATE);
        if (Constant.user_info != null) {
            Constant.user_info.put(Constant.MY_AGENT_ID, "");
        }
        visibleLeftMoney();
        visibleRightMoney();
        return view;
    }



    private void share() {
        if (Integer.parseInt(Constant.user_info == null ? "0" : Constant.user_info.get(Constant.USER_INFO_ISAUTH)) != 1) {
            listener.showVerify();
            return;
        }
        KLog.v(Constant.user_info.get(Constant.MY_AGENT_ID));
        if (Constant.user_info.get(Constant.MY_AGENT_ID).equals("")) {
            showDialog(0, "提示", "您还不是代理商，是否立即成为代理商？", "是", "否");
        } else {
            generateAgentCode();
        }
    }

    private void generateAgentCode() {
        showWaitDialog("正在生成邀请码");
        present.initRetrofit(Constant.URL_BAIBAO, false);
        HashMap<String, String> StringA2 = new HashMap<>();
        StringA2.put(Constant.AID_STR, Constant.AID);
        StringA2.put(Constant.AGENT_ID, Constant.TOP_AGENT_ID);
        StringA2.put(Constant.NUM, "1");

        String sign0 = util.getSign(StringA2);
        StringA2.put(Constant.SIGN, sign0);
        present.QueryObtainInviteCode(StringA2.get(Constant.AID_STR),
                StringA2.get(Constant.SIGN), StringA2.get(Constant.AGENT_ID), StringA2.get(Constant.NUM));
    }

    private void addAgent() {
        showWaitDialog("正在提交代理商资料");
        present.initRetrofit(Constant.URL_BAIBAO, false);
        HashMap<String, String> StringA2 = new HashMap<>();
        StringA2.put(Constant.AID_STR, Constant.AID);
        StringA2.put(Constant.PARENT_ID, Constant.user_info.get(Constant.AGENT_ID));
        StringA2.put(Constant.AGENT_NAME, Constant.user_info.get(Constant.NAME));
        StringA2.put(Constant.PHONE, Constant.user_info.get(Constant.PHONE));
        StringA2.put(Constant.CONTACT, Constant.user_info.get(Constant.CONTACT));
        StringA2.put(Constant.PROVINCE, Constant.user_info.get(Constant.PROVINCE));
        StringA2.put(Constant.CITY, Constant.user_info.get(Constant.CITY) == null ? "中国" : Constant.user_info.get(Constant.CITY));
        // StringA2.put(Constant.RATE, util.add(Double.parseDouble(Constant.user_info.get(Constant.RATE)), Double.parseDouble("0.5")) + "");
        StringA2.put(Constant.RATE, Integer.parseInt(Constant.user_info.get(Constant.RATE) + 5) + "");
        StringA2.put(Constant.BANK, Constant.user_info.get(Constant.BANK));
        StringA2.put(Constant.SUB_BRANCH, Constant.user_info.get(Constant.SUB_BRANCH));
        StringA2.put(Constant.BANK_ACCOUNT, Constant.user_info.get(Constant.BANK_ACCOUNT));
        StringA2.put(Constant.BANK_ACCOUNT_NAME, Constant.user_info.get(Constant.BANK_ACCOUNT_NAME));
        StringA2.put(Constant.BANKFIRM, Constant.user_info.get(Constant.BANKFIRM));
        StringA2.put(Constant.ACCOUNT, Constant.user_info.get(Constant.ACCOUNT));
        StringA2.put(Constant.PASSWORD, Constant.user_info.get(Constant.USER_INFO_PW));

        String sign0 = util.getSign(StringA2);
        StringA2.put(Constant.SIGN, sign0);
        present.QueryAddAgent(StringA2.get(Constant.AID_STR),
                StringA2.get(Constant.SIGN), StringA2.get(Constant.PARENT_ID), StringA2.get(Constant.PHONE), StringA2.get(Constant.AGENT_NAME), StringA2.get(Constant.CONTACT),
                StringA2.get(Constant.PROVINCE), StringA2.get(Constant.CITY), StringA2.get(Constant.RATE), StringA2.get(Constant.BANK),
                StringA2.get(Constant.SUB_BRANCH), StringA2.get(Constant.BANK_ACCOUNT), StringA2.get(Constant.BANK_ACCOUNT_NAME), StringA2.get(Constant.BANKFIRM),
                StringA2.get(Constant.ACCOUNT), StringA2.get(Constant.PASSWORD));
    }

    private void doShare() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, Constant.user_info.get(Constant.NAME) + " 邀请你体验【锄头信用】，邀请码为:" + invite_arr[0]
                + "\r\n下载地址:http://t.cn/RKIEb8h");
        shareIntent.setType("text/plain");

        startActivity(Intent.createChooser(shareIntent, "分享邀请码"));
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

    public void assignInviteCode() {
        showWaitDialog("正在分配邀请码到代理商");
        HashMap<String, String> StringA1 = new HashMap<>();
        StringA1.put(Constant.AID_STR, Constant.AID);
        StringA1.put(Constant.UP_AGENT_ID, Constant.TOP_AGENT_ID);
        StringA1.put(Constant.AGENT_ID, Constant.user_info.get(Constant.MY_AGENT_ID));

        String refer = invite_str.replaceAll("-", ",");
        StringA1.put(Constant.REFERCODES, refer);

        String sign1 = util.getSign(StringA1);
        StringA1.put(Constant.SIGN, sign1);

        present.initRetrofit(Constant.URL_BAIBAO, false);
        present.QueryAssignInviteCode(Constant.AID, StringA1.get(Constant.SIGN), StringA1.get(Constant.UP_AGENT_ID)
                , StringA1.get(Constant.AGENT_ID), StringA1.get(Constant.REFERCODES));
    }

    @Override
    protected void confirm(int type, DialogInterface dia) {
        super.confirm(type, dia);
        switch (type) {
            case 0:
                addAgent();
                break;
        }
    }

    @Override
    protected void cancel(int type, DialogInterface dia) {
        super.cancel(type, dia);
    }

    public void initState() {
        if (main_banner == null) {
            return;
        }
        today_money = 0.0;
        history_money = 0.0;
        if (main_today_reward != null) {
            main_today_reward.setText("0.0");
            main_history_reward.setText("0.0");
        }
        initDate();
    }

    private void initDate() {
        initBanner();
        main_banner.startAutoPlay();

        initAgentInfo();
        switch (Integer.parseInt(Constant.user_info == null ? "0" : Constant.user_info.get(Constant.USER_INFO_ISAUTH))) {
            case 1:
                main_level_lay.setVisibility(View.VISIBLE);
                main_level_tv.setText(Constant.user_info.get(Constant.LEVEL_NAME));
                break;
            default:
                main_level_lay.setVisibility(View.GONE);
                break;
        }
    }

    private void initAgentInfo() {
        if (Constant.user_info == null) return;

        HashMap<String, String> StringA1 = new HashMap<>();
        StringA1.put(Constant.AID_STR, Constant.AID);
        StringA1.put(Constant.PHONE, Constant.user_info.get(Constant.PHONE));

        String sign1 = util.getSign(StringA1);
        StringA1.put(Constant.SIGN, sign1);

        present.initRetrofit(Constant.URL_BAIBAO, false);
        present.QueryAgentInfo(Constant.AID, StringA1.get(Constant.SIGN), StringA1.get(Constant.PHONE));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
    }

    private void getTodayProfit() {
        if (Constant.user_info == null || Constant.user_info.get(Constant.MY_AGENT_ID) == null) {
            main_today_reward.setText("0.0");
            main_history_reward.setText("0.0");
            return;
        }
        main_today_reward.setEnabled(false);
        main_history_reward.setEnabled(false);

        fetchHistory = false;

        HashMap<String, String> StringA1 = new HashMap<>();
        StringA1.put(Constant.AID_STR, Constant.AID);
        StringA1.put(Constant.AGENT_ID, Constant.user_info.get(Constant.MY_AGENT_ID));

        String start_t = util.getOldDate(-2);
        String end_t = util.getOldDate(-1);

        StringA1.put("date_start", start_t);
        StringA1.put("date_end", end_t);
        String sign1 = util.getSign(StringA1);
        StringA1.put(Constant.SIGN, sign1);
        present.initRetrofit(Constant.URL_BAIBAO, false);
        present.QueryProfit(Constant.AID, StringA1.get(Constant.SIGN), StringA1.get(Constant.AGENT_ID), StringA1.get("date_start"), StringA1.get("date_end"));
    }

    private void getTodayAgentTrade() {

        HashMap<String, String> StringA1 = new HashMap<>();
        StringA1.put(Constant.AID_STR, Constant.AID);

        String start_t = util.getOldDate(0);
        String end_t = util.getOldDate(0);
        StringA1.put("date_start", start_t);
        StringA1.put("date_end", end_t);

        StringA1.put(Constant.AGENT_ID, Constant.user_info.get(Constant.MY_AGENT_ID));
        String sign1 = util.getSign(StringA1);
        StringA1.put(Constant.SIGN, sign1);

        present.initRetrofit(Constant.URL_BAIBAO, false);
        present.QueryAgentOrder(Constant.AID, StringA1.get(Constant.SIGN), StringA1.get(Constant.AGENT_ID), StringA1.get("date_start"), StringA1.get("date_end"));

    }
    private void getTodayAgentProfit() {

        HashMap<String, String> StringA1 = new HashMap<>();
        StringA1.put(Constant.AID_STR, Constant.AID);

        String start_t = util.getOldDate(-1);
        String end_t = util.getOldDate(-1);
        StringA1.put("date_start", start_t);
        StringA1.put("date_end", end_t);

        StringA1.put(Constant.AGENT_ID, Constant.user_info.get(Constant.MY_AGENT_ID));
        String sign1 = util.getSign(StringA1);
        StringA1.put(Constant.SIGN, sign1);

        present.initRetrofit(Constant.URL_BAIBAO, false);
        present.QueryAgentProfit(Constant.AID, StringA1.get(Constant.SIGN), StringA1.get(Constant.AGENT_ID), StringA1.get("date_start"), StringA1.get("date_end"));

    }

    private void getHistoryProfit() {
        if (Constant.user_info == null || Constant.user_info.get(Constant.MY_AGENT_ID) == null) {
            main_history_reward.setText("0.0");
            return;
        }

        HashMap<String, String> StringA1 = new HashMap<>();
        StringA1.put(Constant.AID_STR, Constant.AID);
        StringA1.put(Constant.AGENT_ID, Constant.user_info.get(Constant.MY_AGENT_ID));


        Calendar c = Calendar.getInstance();
        String start_t = util.getOldDate(0 - c.get(Calendar.DAY_OF_MONTH) + 1);
        /*if (0 - c.get(Calendar.DAY_OF_MONTH) == -31) {
            start_t = util.getOldDate(0 - c.get(Calendar.DAY_OF_MONTH) + 1);
        }*/

        String end_t = util.getOldDate(0);

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
        bannerImageList.add(R.drawable.b01);
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

    /**
     * 显示金额
     */
    private void visibleLeftMoney() {
        if (sp.getBoolean(VISIBLE_MONEY_LEFT_KEY, true)) {
            main_today_reward.setTag("0");
        } else {
            main_today_reward.setTag("1");
        }
        visible(main_today_reward, 0);
    }

    /**
     * 显示金额
     */
    private void visibleRightMoney() {
        if (sp.getBoolean(VISIBLE_MONEY_RIGHT_KEY, true)) {
            main_history_reward.setTag("0");
        } else {
            main_history_reward.setTag("1");
        }
        visible(main_history_reward, 1);
    }

    private void visible(TextView tv, int tv_id) {
        KLog.v("visible"+tv_id+"----"+tv.getTag()+"---"+history_money);
        if (Integer.parseInt(tv.getTag() + "") == 0) {
            switch (tv_id) {
                case 0:
                    sp.edit().putBoolean(VISIBLE_MONEY_LEFT_KEY, false).commit();
                    break;
                case 1:
                    sp.edit().putBoolean(VISIBLE_MONEY_RIGHT_KEY, false).commit();
                    break;
            }
            tv.setText("***");
            tv.setTag("1");
        } else {
            tv.setTag("0");
            switch (tv_id) {
                case 0:
                    tv.setText(today_money + "");
                    sp.edit().putBoolean(VISIBLE_MONEY_LEFT_KEY, true).commit();
                    break;
                case 1:
                    tv.setText(history_money + "");
                    sp.edit().putBoolean(VISIBLE_MONEY_RIGHT_KEY, true).commit();
                    break;
            }
        }
    }

    @Override
    public void ResolveProfitInfo(HeoCodeResponse info) {
        KLog.v(info.toString());

        if (info.getErrcode() == null) {
            history_money = 0.0;
            return;
        }
        if (info.getErrcode().equals(SUCCESS)) {
            Double total_money = 0.0;
            for (HeoMerchantInfoResponse i_m : info.getContent()) {
                total_money = util.add(total_money, Double.parseDouble(i_m.getPayoff_money()));
            }
            history_money = total_money;
            main_history_reward.setText(total_money + "");
        } else {
            main_history_reward.setText("0.0");
        }
        visible(main_history_reward, 1);
    }

    @Override
    public void ResolveAgentOrderInfo(HeoCodeResponse info) {
        KLog.v(info.toString());
        if (info.getErrcode() == null) {
            VToast.toast(getContext(), "网络错误");
            return;
        }
        if (info.getErrcode().equals(SUCCESS)) {
            Double t_m = 0.0;
            for (HeoMerchantInfoResponse i_m : info.getContent()) {
                if (i_m.getState().equals("0")) {
                    KLog.v("ResolveAgentOrderInfo"+Double.parseDouble(i_m.getPay_money()));
                    t_m = util.add(t_m, Double.parseDouble(i_m.getPay_money()));
                }
            }
            today_money = t_m;
            visible(main_today_reward, 0);
        }
    }

    @Override
    public void ResolveAgentProfitInfo(HeoCodeResponse info) {
        KLog.v(info.toString());
        if (info.getErrcode() == null) {
            VToast.toast(getContext(), "网络错误");
            return;
        }
        if (info.getErrcode().equals(SUCCESS)) {
            Double t_m = 0.00;
            for (HeoMerchantInfoResponse i_m : info.getContent()) {
                if (i_m.getState().equals("0")) {
                 //   KLog.v("ResolveAgentOrderInfo"+Double.parseDouble(i_m.getPayoff_money()));
                    t_m = util.add(t_m, Double.parseDouble(i_m.getPayoff_money()));
                }
            }
            today_money = t_m;
            visible(main_today_reward, 0);
        }
    }

    @Override
    public void ResolveAgentWithdrawInfo(HeoCodeResponse info) {

    }

    @Override
    public void ResolveMerchantOrderInfo(HeoCodeResponse info) {

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

    @Override
    public void ResolveAddAgentInfo(HeoCodeResponse info) {
        KLog.v(info.toString());
        hideWaitDialog();
        if (info.getErrcode() == null) {
            VToast.toast(getContext(), "网络错误");
            return;
        }
        if (info.getErrcode().equals(SUCCESS)) {
            KLog.v("ResolveAddAgentInfo" + info.getAgent_id() + "--" + info.getAgent_name());
            Constant.user_info.put(Constant.MY_AGENT_ID, info.getAgent_id());
            Constant.user_info.put(Constant.MY_AGENT_NAME, info.getAgent_name());
            generateAgentCode();
        } else {
            VToast.toast(getContext(), info.getErrmsg());
        }
    }

    @Override
    public void ResolveInviteInfo(HeoCodeObjResponse info) {

    }

    @Override
    public void ResolveUsefullInviteInfo(HeoCodeResponse info) {

    }

    @Override
    public void ResolveObtainInviteInfo(HeoCodeResponse info) {
        KLog.v(info.toString());
        hideWaitDialog();
        if (info.getErrcode() == null) {
            VToast.toast(getContext(), "网络错误");
            return;
        }
        if (info.getErrcode().equals(SUCCESS)) {
            invite_arr = info.getRange().split("-");
            invite_str = info.getRange();
            assignInviteCode();
        } else {
            VToast.toast(getContext(), info.getErrmsg());
        }
    }

    @Override
    public void ResolveAssignInviteInfo(HeoCodeResponse info) {
        KLog.v(info.toString());
        hideWaitDialog();
        if (info.getErrcode() == null) {
            VToast.toast(getContext(), "网络错误");
            return;
        }
        if (info.getErrcode().equals(SUCCESS)) {
            doShare();
        } else {
            VToast.toast(getContext(), info.getErrmsg());
        }
    }

    @Override
    public void ResolveAgentInfo(HeoCodeResponse info) {
        KLog.v(info.toString());
        hideWaitDialog();
        if (info.getErrcode() == null) {
            VToast.toast(getContext(), "网络错误");
            return;
        }
        if (info.getErrcode().equals(SUCCESS)) {
            KLog.v("ResolveAddAgentInfo" + info.getContent().get(0).getAgent_id());
            Constant.user_info.put(Constant.MY_AGENT_ID, info.getContent().get(0).getAgent_id());
            Constant.user_info.put(Constant.MY_AGENT_NAME, info.getContent().get(0).getName());
            Constant.user_info.put(Constant.PROFIT_BALANCE, info.getContent().get(0).getProfit_balance());
            Constant.user_info.put(Constant.PROFIT_HISTORY, info.getContent().get(0).getProfit_history());
            history_money = Double.parseDouble(Constant.user_info.get(Constant.PROFIT_HISTORY));
            KLog.v("history_money"+history_money);
            visible(main_history_reward, 1);
           // getHistoryProfit();
            getTodayAgentProfit();
        } else {
            Constant.user_info.put(Constant.MY_AGENT_NAME, "");
            Constant.user_info.put(Constant.MY_AGENT_ID, "");
            //  VToast.toast(getContext(), info.getErrmsg());
        }
    }


    public interface IMainListener {
        void gotoPay();

        void showVerify();

        void updateMerchant();
    }
}
