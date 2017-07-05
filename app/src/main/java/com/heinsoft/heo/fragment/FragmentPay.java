package com.heinsoft.heo.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.heinsoft.heo.R;
import com.heinsoft.heo.bean.HeoCodeResponse;
import com.heinsoft.heo.bean.HeoProfitResponse;
import com.heinsoft.heo.present.QueryPresent;
import com.heinsoft.heo.util.Constant;
import com.heinsoft.heo.util.Utils;
import com.heinsoft.heo.util.VToast;
import com.heinsoft.heo.view.IPayView;
import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FragmentPay extends BaseFragment implements IPayView {
    IPayListener listener;
    ImageView header_btn, pay_qcode_iv;
    TextView header_title, pay_set_cash, pay_save_qcode, pay_cash_tv;
    LinearLayout pay_step_2, pay_generate_qcode,header_btn_lay;

    RelativeLayout pay_step_1;
    Spinner pay_tunnel_cs;

    QueryPresent present;
    Utils util;
    SharedPreferences sp;
    private final String STR_PHONE = "phone_num";
    private final String STR_USER_NAME = "username";
    private final String STR_PW = "password";
    private final String COOKIE_KEY = "cookie";
    private int cur_step = 1;
    View popupWindowView;
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
    private static final int IMAGE_HALFWIDTH = 40;//宽度值，影响中间图片大小
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pay_lay, container, false);

        header_btn = (ImageView) view.findViewById(R.id.header_btn);
        pay_qcode_iv = (ImageView) view.findViewById(R.id.pay_qcode_iv);
        header_title = (TextView) view.findViewById(R.id.header_title);
        pay_set_cash = (TextView) view.findViewById(R.id.pay_set_cash);
        pay_save_qcode = (TextView) view.findViewById(R.id.pay_save_qcode);
        pay_cash_tv = (TextView) view.findViewById(R.id.pay_cash_tv);
        header_btn_lay = (LinearLayout) view.findViewById(R.id.header_btn_lay);

        pay_tunnel_cs = (Spinner) view.findViewById(R.id.pay_tunnel_cs);
        spinner_adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, pay_types);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        pay_tunnel_cs.setAdapter(spinner_adapter);
        pay_tunnel_cs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pay_type = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        pay_step_1 = (RelativeLayout) view.findViewById(R.id.pay_step_1);
        pay_step_2 = (LinearLayout) view.findViewById(R.id.pay_step_2);
        pay_generate_qcode = (LinearLayout) view.findViewById(R.id.pay_generate_qcode);

        util = Utils.getInstance();
        present = QueryPresent.getInstance(getContext());
        present.setView(FragmentPay.this);
        sp = getContext().getSharedPreferences(COOKIE_KEY, Context.MODE_PRIVATE);

        popupWindowView = View.inflate(getContext(), R.layout.pop_password, null);
        ButterKnife.bind(FragmentPay.this, popupWindowView);

        RxView.clicks(header_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> step(0));
        RxView.clicks(pay_set_cash).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> setCash());
        RxView.clicks(pay_save_qcode).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> save());
        RxView.clicks(pay_generate_qcode).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> generate());
        RxView.clicks(pay_cash_tv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> showPopupWindow());

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            initView();
        }
    }

    public void step(int step) {
        KLog.v(cur_step+"cur_step");
        switch (step) {
            case 0:
                listener.finishPay();
                break;
            case 1:
                pay_cash_tv.setText("0.00");
                pay_step_1.setVisibility(View.VISIBLE);
                pay_step_2.setVisibility(View.GONE);
                break;
        }
    }

    private void generate() {
        if( Float.parseFloat(pay_cash_tv.getText().toString())==0){
            VToast.toast(getContext(),"支付金额不能为0");
            return;
        }
        showWaitDialog("正在生成二维码");
        pay_generate_qcode.setEnabled(false);
        pay_t = 0;
        int trade_t = 0;
        switch (pay_type) {
            case 0:
                pay_t = 0;
                trade_t = 0;
                break;
            case 1:
                pay_t = 0;
                trade_t = 1;
                break;
            case 2:
                pay_t = 1;
                trade_t = 0;
                break;
            case 3:
                pay_t = 1;
                trade_t = 1;
                break;
            case 4:
                pay_t = 2;
                trade_t = 0;
                break;
            case 5:
                pay_t = 2;
                trade_t = 1;
                break;
        }
        present.initRetrofit(Constant.URL_BAIBAO, false);
        HashMap<String, String> StringA1 = new HashMap<>();

        StringA1.put(Constant.AID_STR, Constant.AID);
        StringA1.put("pay_money", pay_cash_tv.getText().toString());
        StringA1.put("pay_type", pay_t+"");
        StringA1.put("trade_type", trade_t+"");
        String merchant_id = sp.getString(Constant.USER_INFO_MERCHANT_ID, "");
        if (merchant_id.equals("")) {//test
          //  merchant_id = Constant.CONSTANT_MERCHANT_ID;
        }
        StringA1.put(Constant.MERCHANT_ID, merchant_id);
        String sign1 = util.getSign(StringA1);
        StringA1.put(Constant.SIGN, sign1);
        present.QueryPay(Constant.AID, sign1, merchant_id, pay_cash_tv.getText().toString(), pay_t, trade_t);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        step(1);
    }


    private void code() {
        cur_step = 2;
        String pay_type_str = "";
        switch(pay_t){
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
            qrCodeBitmap = createCode("锄头信用", BitmapFactory.decodeResource(getResources(),R.mipmap.icon),BarcodeFormat.QR_CODE);
            pay_qcode_iv.setImageBitmap(qrCodeBitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        try {
            Bitmap qrCodeBitmap =createCode(qcode, BitmapFactory.decodeResource(getResources(),R.mipmap.icon),BarcodeFormat.QR_CODE);
            fragmentQcode = new DialogFragmentQcode();
            fragmentQcode.setQcode(qrCodeBitmap,pay_type_str,pay_cash_tv.getText().toString());
            fragmentQcode.setCancelable(false);
            fragmentQcode.show(getFragmentManager(), "");
        } catch (WriterException e) {

        }
    }

    private void save() {
        util.saveImageToGallery(getContext(), qrCodeBitmap);
    }

    private void setCash() {
        pay_step_1.setVisibility(View.GONE);
        pay_step_2.setVisibility(View.VISIBLE);
        showPopupWindow();
        cur_step = 2;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (IPayListener) context;
        } catch (Exception e) {
            e.fillInStackTrace();
        }
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
        if (d.indexOf(".") == d.length()-1) {
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
        //    VToast.toast(getContext(), info.getErrmsg());
        }else{
            VToast.toast(getContext(), "网络错误,请重试");
        }
        if(info.getMerchant_id() == null){
            cur_step=1;
            return;
        }
        qcode = info.getQrcode();
        code();
    }

    @Override
    public void ResolveProfitInfo(HeoProfitResponse info) {

    }

    /**
     * 生成二维码
     * @param string 二维码中包含的文本信息
     * @param mBitmap logo图片
     * @param format  编码格式
     * [url=home.php?mod=space&uid=309376]@return[/url] Bitmap 位图
     * @throws WriterException
     */
    public Bitmap createCode(String string,Bitmap mBitmap, BarcodeFormat format)
            throws WriterException {
        Matrix m = new Matrix();
        float sx = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getWidth();
        float sy = (float) 2 * IMAGE_HALFWIDTH
                / mBitmap.getHeight();
        m.setScale(sx, sy);//设置缩放信息
        //将logo图片按martix设置的信息缩放
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0,
                mBitmap.getWidth(), mBitmap.getHeight(), m, false);
        MultiFormatWriter writer = new MultiFormatWriter();
        Hashtable<EncodeHintType, String> hst = new Hashtable<EncodeHintType, String>();
        hst.put(EncodeHintType.CHARACTER_SET, "UTF-8");//设置字符编码
        BitMatrix matrix = writer.encode(string, format, 400, 400, hst);//生成二维码矩阵信息
        int width = matrix.getWidth();//矩阵高度
        int height = matrix.getHeight();//矩阵宽度
        int halfW = width / 2;
        int halfH = height / 2;
        int[] pixels = new int[width * height];//定义数组长度为矩阵高度*矩阵宽度，用于记录矩阵中像素信息
        for (int y = 0; y < height; y++) {//从行开始迭代矩阵
            for (int x = 0; x < width; x++) {//迭代列
                if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
                        && y > halfH - IMAGE_HALFWIDTH
                        && y < halfH + IMAGE_HALFWIDTH) {//该位置用于存放图片信息
//记录图片每个像素信息
                    pixels[y * width + x] = mBitmap.getPixel(x - halfW
                            + IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);              } else {
                    if (matrix.get(x, y)) {//如果有黑块点，记录信息
                        pixels[y * width + x] = 0xff000000;//记录黑块信息
                    }
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    public interface IPayListener {
        void finishPay();
    }
}
