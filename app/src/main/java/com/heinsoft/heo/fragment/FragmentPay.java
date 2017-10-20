package com.heinsoft.heo.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.zxing.WriterException;
import com.heinsoft.heo.R;
import com.heinsoft.heo.bean.HeoCodeResponse;
import com.heinsoft.heo.bean.PreparPayResultBean;
import com.heinsoft.heo.present.QueryPresent;
import com.heinsoft.heo.util.Constant;
import com.heinsoft.heo.util.Utils;
import com.heinsoft.heo.util.VToast;
import com.heinsoft.heo.view.IPayView;
import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;

public class FragmentPay extends BaseFragment implements IPayView {

    ImageView header_btn, pay_qcode_iv;
    TextView header_title, pay_set_cash, pay_save_qcode, pay_cash_tv, pay_btn_tv;
    LinearLayout pay_step_2, pay_generate_qcode, header_btn_lay, pay_quick_pay_extra, scan_code_payment;
    EditText pay_cvn2_et, pay_available_et, bank_account_et, pay_mobile_et, pay_name_et, pay_id_et;
    LinearLayout pay_step_1;
    FrameLayout webview_open;
    Spinner pay_tunnel_cs;

    QueryPresent present;
    Utils util;
    SharedPreferences sp;

    private final String COOKIE_KEY = "cookie";

    View popupWindowView;
    FragmentPayWebview fragment;
//    FragmentWebview fragment;

    DialogFragmentQcode fragmentQcode;
    Bitmap qrCodeBitmap;
    private PopupWindow popupWindow;

    @Bind(R.id.btn_txt0)
    TextView btn_txt0;
    @Bind(R.id.btn_txt1)
    TextView btn_txt1;
    @Bind(R.id.btn_txt2)
    TextView btn_txt2;
    @Bind(R.id.btn_txt3)
    TextView btn_txt3;
    @Bind(R.id.btn_txt4)
    TextView btn_txt4;
    @Bind(R.id.btn_txt5)
    TextView btn_txt5;
    @Bind(R.id.btn_txt6)
    TextView btn_txt6;
    @Bind(R.id.btn_txt7)
    TextView btn_txt7;
    @Bind(R.id.btn_txt8)
    TextView btn_txt8;
    @Bind(R.id.btn_txt9)
    TextView btn_txt9;
    @Bind(R.id.btn_dot)
    TextView btn_dot;
    @Bind(R.id.btn_del)
    TextView btn_del;
    @Bind(R.id.btn_confirm)
    TextView btn_confirm;
    @Bind(R.id.btn_cancel)
    TextView btn_cancel;
    @Bind(R.id.tv_digit)
    TextView tv_digit;

    String qcode;
    int pay_type = 0;
    int pay_t;
    View view;
    private String account, system_orderId, orderId, smsCode, bank_account, pay_money, trantp;
    private String order_id, orderNo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.pay_lay, container, false);

        return view;
    }

    @Override
    public void Back() {
        super.Back();
        if (pay_step_2.getVisibility() == View.VISIBLE) {
            pay_step_1.setVisibility(View.VISIBLE);
            pay_step_2.setVisibility(View.GONE);
            webview_open.setVisibility(View.GONE);
        } else if (webview_open.getVisibility() == View.VISIBLE) {
            pay_step_1.setVisibility(View.VISIBLE);
            pay_step_2.setVisibility(View.GONE);
            webview_open.setVisibility(View.GONE);
        } else{
            listener.gotoMain();
        }
    }

    private void pay() {
        switch (pay_type) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                generate();
                break;
            case 6:
            case 7:
//                VToast.toast(getContext(),"暂未开通");
                scoreQuickPay();
                break;
            case 8:
            case 9:
//                VToast.toast(getContext(), "暂未开通");
                quickPay();
                break;

        }
    }

    private void scoreQuickPay() {

        if (Float.parseFloat(pay_cash_tv.getText().toString()) == 0) {
            VToast.toast(getContext(), "支付金额不能为0");
            return;
        }
        if (pay_cvn2_et.getText().toString().equals("")) {
            VToast.toast(getContext(), "请输入信用卡背面的CVN2有效码");
            return;
        }
        if (pay_available_et.getText().toString().equals("")) {
            VToast.toast(getContext(), "请输入信用卡有效期");
            return;
        }
        if (bank_account_et.getText().toString().equals("")) {
            VToast.toast(getContext(), "请输入银行卡号");
            return;
        }
        if (pay_mobile_et.getText().toString().equals("")) {
            VToast.toast(getContext(), "请输入银行卡绑定的手机号");
            return;
        }
        if (pay_name_et.getText().toString().equals("")) {
            VToast.toast(getContext(), "请输入银行卡绑定的姓名");
            return;
        }
        if (pay_id_et.getText().toString().equals("")) {
            VToast.toast(getContext(), "请输入银行卡绑定的姓名");
            return;
        }
        showWaitDialog("请稍等");
        present.initRetrofit2(Constant.URL_BAIBAO, false);
        HashMap<String, String> StringA1 = new HashMap<>();
        StringA1.put(Constant.AID_STR, Constant.AID);
        StringA1.put("pay_money", pay_cash_tv.getText().toString());
        int trade_t = 0;
        switch (pay_type) {
            case 7:
                trade_t = 1;
                break;
            case 6:
                trade_t = 0;
                break;
            default:
                VToast.toast(getContext(), "不是有积分快捷支付类型");
                break;
        }
        StringA1.put(Constant.MERCHANT_ID, Constant.user_info.get(Constant.USER_INFO_MERCHANT_ID));
        StringA1.put("bank_account", bank_account_et.getText().toString().trim());
        StringA1.put("phone", pay_mobile_et.getText().toString().trim());
        StringA1.put("true_name", pay_name_et.getText().toString().trim());
        StringA1.put("id_card", pay_id_et.getText().toString().trim());
        StringA1.put("cvv2", pay_cvn2_et.getText().toString().trim());
        StringA1.put("indate", pay_available_et.getText().toString().trim());
        StringA1.put("trantp", trade_t + "");
        String sign1 = util.getSign(StringA1);
        StringA1.put(Constant.SIGN, sign1);

        present.QueryScoreQuickPay(StringA1.get(Constant.AID_STR), StringA1.get(Constant.SIGN), StringA1.get(Constant.MERCHANT_ID), StringA1.get("pay_money"),
                StringA1.get("bank_account"), StringA1.get("phone"), StringA1.get("true_name"), StringA1.get("id_card"), StringA1.get("cvv2"),
                StringA1.get("indate"), StringA1.get("trantp"));
    }

    private void scoreQuickPayConfirm() {
        showWaitDialog("正在确认支付结果");
        present.initRetrofit(Constant.URL_BAIBAO, false);
        PreparPayResultBean bean = new PreparPayResultBean();
        bean.setTrue_name(pay_name_et.getText().toString().trim());
        bean.setId_card(pay_id_et.getText().toString().trim());
        bean.setCvv2(pay_cvn2_et.getText().toString().trim());
        bean.setIndate(pay_available_et.getText().toString().trim());
        bean.setPhone(pay_mobile_et.getText().toString().trim());
        String extact = new Gson().toJson(bean);
        HashMap<String, String> StringA1 = new HashMap<>();
        StringA1.put(Constant.AID_STR, Constant.AID);
//        StringA1.put(Constant.MERCHANT_ID, Constant.user_info.get(Constant.USER_INFO_MERCHANT_ID));
        StringA1.put("bank_account", bank_account);
        StringA1.put("account", account);
        StringA1.put("system_orderId", system_orderId);
        StringA1.put("orderId", orderId);
        StringA1.put("smsCode", smsCode);
        StringA1.put("pay_money", pay_money);
        StringA1.put("trantp", trantp);
        StringA1.put("extact", extact);

        String sign1 = util.getSign(StringA1);
        StringA1.put(Constant.SIGN, sign1);

        present.QueryScoreQuickPayConfirm(StringA1.get(Constant.AID_STR), StringA1.get(Constant.SIGN), StringA1.get("account"), StringA1.get("system_orderId"),
                StringA1.get("orderId"), StringA1.get("smsCode"), StringA1.get("bank_account"), StringA1.get("pay_money"), StringA1.get("trantp"), StringA1.get("extact"));
    }

    private void quickPay() {

        if (Float.parseFloat(pay_cash_tv.getText().toString()) == 0) {
            VToast.toast(getContext(), "支付金额不能为0");
            return;
        }
        if (pay_cvn2_et.getText().toString().equals("")) {
            VToast.toast(getContext(), "请输入信用卡背面的CVN2有效码");
            return;
        }
        if (pay_available_et.getText().toString().equals("")) {
            VToast.toast(getContext(), "请输入信用卡的有效期(年月，如2202)");
            return;
        }
        if (pay_mobile_et.getText().toString().equals("")) {
            VToast.toast(getContext(), "请输入信用卡绑定的手机号");
            return;
        }
        if (pay_name_et.getText().toString().equals("")) {
            VToast.toast(getContext(), "请输入信用卡持卡人姓名");
            return;
        }
        if (pay_id_et.getText().toString().equals("")) {
            VToast.toast(getContext(), "请输入信用卡绑定的身份证号");
            return;
        }
        showWaitDialog("请稍等");
        present.initRetrofit2(Constant.URL_BAIBAO, false);
        HashMap<String, String> StringAl = new HashMap<>();
        StringAl.put("pay_money", pay_cash_tv.getText().toString().trim());
        StringAl.put("bank_account", bank_account_et.getText().toString().trim());
        StringAl.put("mobile", pay_mobile_et.getText().toString().trim());
        StringAl.put("name", pay_name_et.getText().toString().trim());
        StringAl.put("id_card", pay_id_et.getText().toString().trim());
        StringAl.put("cvv2", pay_cvn2_et.getText().toString().trim());
        StringAl.put("vd", pay_available_et.getText().toString().trim());
        int trade_t = 0;
        switch (pay_type) {
            case 8:
                trade_t = 0;
                break;
            case 9:
                trade_t = 1;
                break;
            default:
                VToast.toast(getContext(), "不是无积分快捷支付");
        }
        StringAl.put("trantp", trade_t + "");
        StringAl.put(Constant.AID_STR, Constant.AID);
        StringAl.put(Constant.MERCHANT_ID, Constant.user_info.get(Constant.USER_INFO_MERCHANT_ID));
        String sign1 = util.getSign(StringAl);
        StringAl.put(Constant.SIGN, sign1);

        Constant.user_info.put(Constant.TRANTP,trade_t+"");

        Log.v("Heo","id="+StringAl.get(Constant.AID_STR)+"&sign="+ StringAl.get(Constant.SIGN)+"&merchant_id="+StringAl.get(Constant.MERCHANT_ID)+"pay_money="+
                StringAl.get("pay_money")+"&bank_account="+ StringAl.get("bank_account")+"&mobile="+StringAl.get("mobile")+"&name="+StringAl.get("name")+"&id_card="+
                StringAl.get("id_card")+"&cvv2="+ StringAl.get("cvv2")+"&vd="+ StringAl.get("vd")+"&trantp="+ StringAl.get("trantp"));

        present.QueryQuickPay(StringAl.get(Constant.AID_STR), StringAl.get(Constant.SIGN), StringAl.get(Constant.MERCHANT_ID),
                StringAl.get("pay_money"), StringAl.get("bank_account"), StringAl.get("mobile"), StringAl.get("name"),
                StringAl.get("id_card"), StringAl.get("cvv2"), StringAl.get("vd"), StringAl.get("trantp"));

    }

    private void quickPayConfirm() {
        showWaitDialog("正在确认支付结果");
        present.initRetrofit(Constant.URL_BAIBAO, false);
        HashMap<String, String> StringAl = new HashMap<>();
        StringAl.put(Constant.AID_STR, Constant.AID);
        StringAl.put(Constant.MERCHANT_ID, Constant.user_info.get(Constant.USER_INFO_MERCHANT_ID));
        StringAl.put("order_id", order_id);
        StringAl.put("orderNo", orderNo);
        StringAl.put("smCode", smsCode);
        String sign1 = util.getSign(StringAl);
        StringAl.put(Constant.SIGN, sign1);

        present.QueryQuickPayConfirm(StringAl.get(Constant.AID_STR), StringAl.get(Constant.SIGN), StringAl.get(Constant.MERCHANT_ID),
                StringAl.get("order_id"), StringAl.get("orderNo"), StringAl.get("smCode"));
    }

    private void generate() {

        if (Float.parseFloat(pay_cash_tv.getText().toString()) == 0) {
            VToast.toast(getContext(), "支付金额不能为0");
            return;
        }
        showWaitDialog("正在生成二维码");
        pay_generate_qcode.setEnabled(false);
        pay_t = 2;
        int trade_t = 0;
        switch (pay_type) {
            case 7:
                pay_t = 3;
                trade_t = 1;
                break;
            case 6:
                pay_t = 3;
                trade_t = 0;
                break;
            case 5:
                pay_t = 1;
                trade_t = 1;
                break;
            case 4:
                pay_t = 0;
                trade_t = 0;
                break;
            case 3:
                pay_t = 1;
                trade_t = 1;
                break;
            case 2:
                pay_t = 1;
                trade_t = 0;
                break;
            case 1:
                pay_t = 2;
                trade_t = 1;
                break;
            case 0:
                pay_t = 2;
                trade_t = 0;
                break;
        }
        present.initRetrofit(Constant.URL_BAIBAO, false);
        HashMap<String, String> StringA1 = new HashMap<>();

        StringA1.put(Constant.AID_STR, Constant.AID);
        StringA1.put("pay_money", pay_cash_tv.getText().toString());
        StringA1.put("pay_type", pay_t + "");
        StringA1.put("trade_type", trade_t + "");
        String merchant_id = Constant.user_info.get(Constant.USER_INFO_MERCHANT_ID);
        KLog.v("merchant_id" + merchant_id + "----" + sp.getString(Constant.USER_INFO_MERCHANT_ID, ""));
        StringA1.put(Constant.MERCHANT_ID, merchant_id);
        String sign1 = util.getSign(StringA1);
        StringA1.put(Constant.SIGN, sign1);
        present.QueryPay(Constant.AID, sign1, merchant_id, pay_cash_tv.getText().toString(), pay_t, trade_t);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
        initView();

    }


    private void initDate() {
        util = Utils.getInstance();
        present = QueryPresent.getInstance(getContext());
        present.setView(FragmentPay.this);
        sp = getContext().getSharedPreferences(COOKIE_KEY, Context.MODE_PRIVATE);
    }

    private void initView() {
        header_btn = (ImageView) view.findViewById(R.id.header_btn);
        pay_qcode_iv = (ImageView) view.findViewById(R.id.pay_qcode_iv);
        header_title = (TextView) view.findViewById(R.id.header_title);
        pay_set_cash = (TextView) view.findViewById(R.id.pay_set_cash);
        pay_save_qcode = (TextView) view.findViewById(R.id.pay_save_qcode);
        pay_cash_tv = (TextView) view.findViewById(R.id.pay_cash_tv);
        pay_btn_tv = (TextView) view.findViewById(R.id.pay_btn_tv);
        header_btn_lay = (LinearLayout) view.findViewById(R.id.header_btn_lay);
        pay_quick_pay_extra = (LinearLayout) view.findViewById(R.id.pay_quick_pay_extra);
        scan_code_payment = (LinearLayout) view.findViewById(R.id.scan_code_payment);
        pay_cvn2_et = (EditText) view.findViewById(R.id.pay_cvn2_et);
        pay_available_et = (EditText) view.findViewById(R.id.pay_available_et);
        bank_account_et = (EditText) view.findViewById(R.id.bank_account_et);
        pay_mobile_et = (EditText) view.findViewById(R.id.pay_mobile_et);
        pay_name_et = (EditText) view.findViewById(R.id.pay_name_et);
        pay_id_et = (EditText) view.findViewById(R.id.pay_id_et);

        pay_tunnel_cs = (Spinner) view.findViewById(R.id.pay_tunnel_cs);
        bankAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, pay_types);
        bankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        pay_tunnel_cs.setAdapter(bankAdapter);
        pay_tunnel_cs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pay_type = position;
                switch (position) {
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                        pay_btn_tv.setText("确认支付");
                        pay_quick_pay_extra.setVisibility(View.VISIBLE);
                        scan_code_payment.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        pay_btn_tv.setText("生成二维码");
                        pay_quick_pay_extra.setVisibility(View.GONE);
                        scan_code_payment.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        pay_step_1 = (LinearLayout) view.findViewById(R.id.pay_step_1);
        pay_step_2 = (LinearLayout) view.findViewById(R.id.pay_step_2);
        webview_open= (FrameLayout) view.findViewById(R.id.open_pay);
        pay_generate_qcode = (LinearLayout) view.findViewById(R.id.pay_generate_qcode);

        popupWindowView = View.inflate(getContext(), R.layout.pop_password, null);
        ButterKnife.bind(FragmentPay.this, popupWindowView);

        RxView.clicks(header_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Back());
        RxView.clicks(pay_set_cash).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> setCash());
        RxView.clicks(pay_save_qcode).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> save());
        RxView.clicks(pay_generate_qcode).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> pay());
        RxView.clicks(pay_cash_tv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> showPopupWindow());

        String merchant_id = Constant.user_info.get(Constant.USER_INFO_MERCHANT_ID);
        KLog.v("merchant_id" + merchant_id);
    }

    @Override
    public void RefreshState() {
        super.RefreshState();
        pay_tunnel_cs.setSelection(0);
        pay_cash_tv.setText("0");

        pay_cvn2_et.setText("");
        pay_available_et.setText("");
        bank_account_et.setText("");
        pay_mobile_et.setText("");
        pay_name_et.setText("");
        pay_id_et.setText("");
    }

    private void code() {
        String pay_type_str = "";
        switch (pay_t) {
            case 0:
                pay_type_str = "微信支付";
                break;
            case 1:
                pay_type_str = "支付宝支付";
                break;
            case 2:
                pay_type_str = "QQ钱包支付";
                break;
        }

        try {
            Utils.LogoConfig logoConfig = util.new LogoConfig();
            Bitmap logoBitmap = logoConfig.modifyLogo(
                    BitmapFactory.decodeResource(getResources(),
                            R.mipmap.icon), BitmapFactory
                            .decodeResource(getResources(),
                                    R.mipmap.icon));
            qrCodeBitmap = util.createCode(qcode, logoBitmap);

            Bitmap topBmp = util.getImage(qrCodeBitmap.getWidth(), 40, pay_type_str, 36, getResources().getColor(R.color.chutou_txt));
            Bitmap bottomBmp = util.getImage(qrCodeBitmap.getWidth(), 40, "￥" + pay_cash_tv.getText().toString(), 36, getResources().getColor(R.color.black));

            Bitmap allBmp = util.addTopBmp(topBmp, bottomBmp, qrCodeBitmap);

            fragmentQcode = new DialogFragmentQcode();
            fragmentQcode.setQcode(allBmp, pay_type_str, pay_cash_tv.getText().toString());
            fragmentQcode.setCancelable(false);
            fragmentQcode.show(getFragmentManager(), "");
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void confirm2(String pw) {
        super.confirm2(pw);
        smsCode = pw;
        switch (pay_type) {
            case 6:
            case 7:
                scoreQuickPayConfirm();
                break;
            case 8:
            case 9:
                quickPayConfirm();

        }

    }

    private void save() {
        util.saveImageToGallery(getContext(), qrCodeBitmap);
        VToast.toast(getContext(), "二维码已经保存");
    }

    private void setCash() {
        pay_step_1.setVisibility(View.GONE);
        pay_step_2.setVisibility(View.VISIBLE);
        webview_open.setVisibility(View.GONE);
        showPopupWindow();

    }

    @Override
    protected void confirm(int type, DialogInterface dia) {
        super.confirm(type, dia);
        Log.v("Heo","type="+type);
        switch (type) {
            case 1:
                openCredit();
                break;
        }
    }


    private void openCredit(){
        showWaitDialog("正在加载");
        present.initRetrofit(Constant.URL_BAIBAO,false);
        HashMap<String,String> StringAl=new HashMap<>();
        StringAl.put(Constant.AID_STR,Constant.AID);
        StringAl.put(Constant.MERCHANT_ID,Constant.user_info.get(Constant.MERCHANT_ID));
        StringAl.put("bank_account",bank_account_et.getText().toString());
        StringAl.put("trantp",Constant.user_info.get(Constant.TRANTP));
        String sign1=util.getSign(StringAl);
        StringAl.put(Constant.SIGN,sign1);

        present.QueryOpenCredit(StringAl.get(Constant.AID_STR),StringAl.get(Constant.SIGN),StringAl.get("bank_account"),
                StringAl.get("trantp"),StringAl.get(Constant.MERCHANT_ID));
    }

    private void printTest(HashMap<String, String> string) {

        Log.v("Heo", string.get(Constant.AID_STR));
        Log.v("Heo", string.get(Constant.SIGN));
        Log.v("Heo", string.get("bank_account"));
        Log.v("Heo", string.get("trantp"));
        Log.v("Heo", string.get(Constant.MERCHANT_ID));
    }

    private void showPopupWindow() {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);

            popupWindow.setAnimationStyle(R.style.AnimationBottomFade);
            ColorDrawable dw = new ColorDrawable(0xffffffff);
            popupWindow.setBackgroundDrawable(dw);
            passwordLis();
        }
        tv_digit.setText("0");
        popupWindow.showAtLocation(View.inflate(getContext(), R.layout.pay_lay, null),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }


    private void passwordLis() {
        RxView.clicks(btn_txt1).subscribe(s -> textBtn('1'));
        RxView.clicks(btn_txt2).subscribe(s -> textBtn('2'));
        RxView.clicks(btn_txt3).subscribe(s -> textBtn('3'));
        RxView.clicks(btn_txt4).subscribe(s -> textBtn('4'));
        RxView.clicks(btn_txt5).subscribe(s -> textBtn('5'));
        RxView.clicks(btn_txt6).subscribe(s -> textBtn('6'));
        RxView.clicks(btn_txt7).subscribe(s -> textBtn('7'));
        RxView.clicks(btn_txt8).subscribe(s -> textBtn('8'));
        RxView.clicks(btn_txt9).subscribe(s -> textBtn('9'));
        RxView.clicks(btn_txt0).subscribe(s -> textBtn('0'));
        RxView.clicks(btn_dot).subscribe(s -> textBtn('.'));
        RxView.clicks(btn_del).subscribe(s -> del());
        RxView.clicks(btn_confirm).subscribe(s -> confirm());
        RxView.clicks(btn_cancel).subscribe(s -> clear());
    }

    private void clear() {
        tv_digit.setText("0");
    }

    /**
     * 显示并格式化输入
     *
     * @param paramChar
     */
    private void textBtn(char paramChar) {
        StringBuilder sb = new StringBuilder();
        String val = tv_digit.getText().toString();

        if (val.indexOf(".") == val.length() - 3 && val.length() > 3) {//小数点后面保留两位
            return;
        }
        if (paramChar == '.' && val.indexOf(".") != -1) {//只出现一次小数点
            return;
        }
        if (paramChar == '0' && val.charAt(0) == '0' && val.indexOf(".") == -1) {//no 0000
            return;
        }
        if (val.length() > 30) {//最大长度
            return;
        }
        sb.append(val.toCharArray()).append(paramChar);

        if (sb.length() > 1 && sb.charAt(0) == '0' && sb.charAt(1) != '.') {
            sb.deleteCharAt(0);
        }
        tv_digit.setText(sb.toString());
    }

    /**
     * 退格
     */
    private void del() {
        char[] chars = tv_digit.getText().toString().toCharArray();

        if (chars.length == 1) {
            tv_digit.setText("0");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(chars);
        sb.deleteCharAt(sb.length() - 1);
        if (sb.charAt(sb.length() - 1) == '.') {
            sb.deleteCharAt(sb.length() - 1);
        }
        tv_digit.setText(sb.toString());
    }


    /**
     * 确定
     */
    private void confirm() {
        popupWindow.dismiss();
        String d = tv_digit.getText().toString();

        if (d.indexOf(".") == -1) {
            d += ".00";
        }
        if (d.indexOf(".") == d.length() - 1) {
            d += "00";
        }
        if (d.equals("0.0") || d.equals("0.00")) {
            d = "0";
        }
        pay_cash_tv.setText(d);
    }

    @Override
    public void ResolvePayInfo(HeoCodeResponse info) {
        hideWaitDialog();
        pay_generate_qcode.setEnabled(true);
        KLog.v(info.toString());
        if (info.getErrmsg() != null) {
            VToast.toast(getContext(), info.getErrmsg());
        } else {
            VToast.toast(getContext(), "网络错误,请重试");
        }
        if (info.getMerchant_id() == null) {
            return;
        }
        qcode = info.getQrcode();
        code();
    }

    @Override
    public void ResolveScoreQuickPayInfo(ResponseBody info) {


        if (info.source() == null) {
            hideWaitDialog();
            return;
        }
        JSONObject jsonObject = null;
        try {
            String res = info.string();
            KLog.v(res);
            jsonObject = new JSONObject(res);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            hideWaitDialog();
            return;
        }
        if (jsonObject.optInt(Constant.ERRCODE) == 0) {

            orderId = jsonObject.optString("orderId");
            system_orderId = jsonObject.optString("system_orderId");
            bank_account = jsonObject.optString("bank_account");
            account = jsonObject.optString("account");
            pay_money = jsonObject.optString("pay_money");
            trantp = jsonObject.optString("tranTp");
            if (!system_orderId.equals("")) {
                showPopupWindow2(R.layout.pay_lay);
            } else {
                KLog.e("没有返回订单号");
                VToast.toast(getContext(), "没有返回订单号");
            }

        } else {
            VToast.toast(getContext(), jsonObject.optString(Constant.ERRMSG));
        }
        hideWaitDialog();
    }


    @Override
    public void ResolveScoreQuickPayConfirmInfo(ResponseBody info) {

        if (info.source() == null) {
            hideWaitDialog();
            return;
        }
        JSONObject jsonObject = null;
        try {
            String res = info.string();
            KLog.v(res);
            jsonObject = new JSONObject(res);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            hideWaitDialog();
            return;
        }
        if (jsonObject.optInt(Constant.ERRCODE) == 0) {
            String errmsg = jsonObject.optString("errmsg");
            if (errmsg.equals("success")) {
                RefreshState();
                VToast.toast(getContext(), "支付成功！");
            }

        } else {
            VToast.toast(getContext(), jsonObject.optString(Constant.ERRMSG));
        }

        hideWaitDialog();
    }

    @Override
    public void ResolveQuickPayInfo(ResponseBody info) {

        if (info.source() == null) {
            hideWaitDialog();
            return;
        }

        JSONObject jsonObject = null;
        try {
            String res = info.string();
            KLog.v(res);
            jsonObject = new JSONObject(res);
        } catch (IOException e) {
            e.printStackTrace();
            hideWaitDialog();
            return;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject.optInt(Constant.ERRCODE) == 0) {
            order_id = jsonObject.optString("order_id");
            orderNo = jsonObject.optString("orderNo");
            if (!order_id.equals("")) {
                showPopupWindow2(R.layout.pay_lay);
            }  else {
                KLog.v("没有返回订单号");
                VToast.toast(getContext(), "没有返回订单号");
            }
        } else if (jsonObject.optInt(Constant.ERRCODE)==ERROR_REGISTER){
            showDialog(QUERY_ELSE,"银行卡未开通快捷支付","是否开通快捷支付","是","否");
        } else {
            VToast.toast(getContext(), jsonObject.optString(Constant.ERRMSG));
        }

        hideWaitDialog();
    }

    @Override
    public void ResolveOpenCreditInfo(ResponseBody info) {
        if (info.source()==null){
            hideWaitDialog();
            return;
        }
        JSONObject jsonObject=null;
        try {
            String res=info.string();
            KLog.v(res);
            jsonObject=new JSONObject(res);
        } catch (IOException e) {
            e.printStackTrace();
            hideWaitDialog();
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (jsonObject.optInt(Constant.ERRCODE)==0){
            String load=jsonObject.optString(Constant.URL);
            Uri loadurl=Uri.parse(load);
            String uri=loadurl.toString().trim();
            FragmentTransaction ft=getFragmentManager().beginTransaction();
            pay_step_1.setVisibility(View.GONE);
            pay_step_2.setVisibility(View.GONE);
            webview_open.setVisibility(View.VISIBLE);
            if (fragment == null) {
                fragment = new FragmentPayWebview();
//                fragment=new FragmentWebview();
                fragment.setTitle("开通快捷支付");
            }
            ft.replace(R.id.open_pay,fragment);
            fragment.loadUrl(uri);
            ft.commit();
        } else {
            VToast.toast(getContext(),jsonObject.optString(Constant.ERRMSG));
        }
        hideWaitDialog();
    }

    @Override
    public void ResolveQuickPayConfirmInfo(ResponseBody info) {
        if (info.source() == null) {
            hideWaitDialog();
            return;
        }
        JSONObject jsonObject = null;
        try {
            String res = info.string();
            KLog.v(res);
            jsonObject = new JSONObject(res);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (jsonObject.optInt(Constant.ERRCODE)==0){
            if (jsonObject.optString(Constant.ERRMSG).equals("success")){
                VToast.toast(getContext(),"支付成功");
                Back();
            }
        }else {
            VToast.toast(getContext(),jsonObject.optString(Constant.ERRMSG));
        }
        hideWaitDialog();
    }

    @Override
    public void ResolveProfitInfo(HeoCodeResponse info) {

    }

    @Override
    public void ResolveAgentOrderInfo(HeoCodeResponse info) {

    }

    @Override
    public void ResolveAgentProfitInfo(HeoCodeResponse info) {

    }

    @Override
    public void ResolveAgentWithdrawInfo(HeoCodeResponse info) {

    }

    @Override
    public void ResolveMerchantOrderInfo(HeoCodeResponse info) {

    }

}
