package com.heinsoft.heo.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.apigateway.ApiInvokeException;
import com.alibaba.apigateway.ApiRequest;
import com.alibaba.apigateway.ApiResponse;
import com.alibaba.apigateway.ApiResponseCallback;
import com.alibaba.apigateway.client.ApiGatewayClient;
import com.alibaba.apigateway.enums.HttpMethod;
import com.alibaba.apigateway.service.RpcService;
import com.baidu.ocr.sdk.OCR;
import com.google.gson.Gson;
import com.heinsoft.heo.R;
import com.heinsoft.heo.bean.DataValueBean;
import com.heinsoft.heo.bean.HeoCodeResponse;
import com.heinsoft.heo.bean.NumBean;
import com.heinsoft.heo.present.QueryPresent;
import com.heinsoft.heo.util.Constant;
import com.heinsoft.heo.util.Utils;
import com.heinsoft.heo.util.VToast;
import com.heinsoft.heo.view.IUserEditView;
import com.heinsoft.heo.view.IUserView;
import com.heinsoft.heo.view.IVerifyView;
import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;
import com.squareup.picasso.Picasso;

import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;
import okhttp3.ResponseBody;


public class FragmentVerify extends BaseFragment implements IUserView, IVerifyView, IUserEditView {
    IVerifyListtener listener;

    @Bind(R.id.header_btn_lay)
    LinearLayout header_btn_lay;

    @Bind(R.id.verify_commit_tv)
    TextView verify_commit_tv;

    @Bind(R.id.verify_card_card1)
    LinearLayout verify_card_card1;
    @Bind(R.id.verify_card_card2)
    LinearLayout verify_card_card2;
    @Bind(R.id.verify_card_card3)
    LinearLayout verify_card_card3;
    @Bind(R.id.verify_bank_card2)
    LinearLayout verify_bank_card2;
    @Bind(R.id.verify_bank_card1)
    LinearLayout verify_bank_card1;

    @Bind(R.id.verify_add_lay)
    LinearLayout verify_add_lay;
    @Bind(R.id.verify_invite_lay)
    LinearLayout verify_invite_lay;
    @Bind(R.id.verify_account_name_lay)
    LinearLayout verify_account_name_lay;
    @Bind(R.id.verify_pw_lay)
    LinearLayout verify_pw_lay;

    @Bind(R.id.verify_do_lay)
    LinearLayout verify_do_lay;

    @Bind(R.id.verify_step1)
    LinearLayout verify_step1;
    @Bind(R.id.verify_step2)
    LinearLayout verify_step2;
    @Bind(R.id.verify_step3)
    LinearLayout verify_step3;

    @Bind(R.id.verify_card_card1_finished)
    ImageView verify_card_card1_finished;
    @Bind(R.id.verify_card_card2_finished)
    ImageView verify_card_card2_finished;
    @Bind(R.id.verify_card_card3_finished)
    ImageView verify_card_card3_finished;
    @Bind(R.id.verify_bank_card1_finished)
    ImageView verify_bank_card1_finished;
    @Bind(R.id.verify_bank_card2_finished)
    ImageView verify_bank_card2_finished;

    @Bind(R.id.verify_card_card1_thumb)
    ImageView verify_card_card1_thumb;
    @Bind(R.id.verify_card_card2_thumb)
    ImageView verify_card_card2_thumb;
    @Bind(R.id.verify_card_card3_thumb)
    ImageView verify_card_card3_thumb;
    @Bind(R.id.verify_bank_card1_thumb)
    ImageView verify_bank_card1_thumb;
    @Bind(R.id.verify_bank_card2_thumb)
    ImageView verify_bank_card2_thumb;

    @Bind(R.id.verify_name_et)
    EditText verify_name_et;
    @Bind(R.id.verify_num_et)
    EditText verify_num_et;
    @Bind(R.id.verify_add_et)
    EditText verify_add_et;

    @Bind(R.id.verify_bank_name_et)
    EditText verify_bank_name_et;
    @Bind(R.id.verify_owner_et)
    EditText verify_owner_et;
    @Bind(R.id.verify_bank_cardnum_et)
    EditText verify_bank_cardnum_et;
    @Bind(R.id.verify_b_bank_et)
    EditText verify_b_bank_et;
    @Bind(R.id.verify_b_bank_num_et)
    EditText verify_b_bank_num_et;

    @Bind(R.id.verify_account_name_et)
    EditText verify_account_name_et;
    @Bind(R.id.verify_pw_et)
    EditText verify_pw_et;
    @Bind(R.id.verify_contact_et)
    EditText verify_contact_et;
    @Bind(R.id.verify_merchant_name_et)
    EditText verify_merchant_name_et;
    @Bind(R.id.verify_contact_phone_et)
    EditText verify_contact_phone_et;
    @Bind(R.id.verify_province_et)
    EditText verify_province_et;
    @Bind(R.id.verify_city_et)
    EditText verify_city_et;
    @Bind(R.id.verify_now_add_et)
    EditText verify_now_add_et;
    @Bind(R.id.verify_invite_et)
    EditText verify_invite_et;

    private final String PIC1 = "pic1";
    private final String PIC2 = "pic2";
    private final String PIC3 = "pic3";
    private final String PIC4 = "pic4";
    private final String PIC5 = "pic5";
    private int cur_step = 1;

    private static final int OCR_SUCESS = 100;
    private static final int OCR_FAIL = OCR_SUCESS + 1;
    private HashMap<String, Boolean> isLocalBMPUploadMap = new HashMap<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case OCR_SUCESS:
                    DataValueBean item = (DataValueBean) msg.obj;
                    verify_name_et.setText(item.getName());
                    verify_num_et.setText(item.getNum());
                    verify_add_et.setText(item.getAddress());
                    break;
                case OCR_FAIL:
                    VToast.toast(getContext(), "获取信息失败,请手动输入");
                    break;
            }
        }
    };
    Map<String, String> temp_info;
    QueryPresent present;
    Utils util;
    SharedPreferences sp;

    //  UploadManager uploadManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.verify_card_lay, container, false);
        ButterKnife.bind(FragmentVerify.this, view);

        OCR.getInstance().initWithToken(getActivity().getApplicationContext(), Constant.PUBLIC_OCR_TOKEN);//初始化OCR
        ApiGatewayClient.init(getContext(), false);
        util = Utils.getInstance();
        temp_info = new HashMap<>();
        present = QueryPresent.getInstance(getContext());
        present.setView(FragmentVerify.this);
        sp = getContext().getSharedPreferences(COOKIE_KEY, Context.MODE_PRIVATE);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RxView.clicks(verify_do_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> doVerify());
        RxView.clicks(verify_card_card1).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> photograph(0));
        RxView.clicks(verify_card_card2).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> photograph(1));
        RxView.clicks(verify_card_card3).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> photograph(2));
        RxView.clicks(verify_bank_card1).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> photograph(3));
        RxView.clicks(verify_bank_card2).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> photograph(4));
        RxView.clicks(header_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> listener.finishVerify());
        initVisible();
        autoWriteInfo();
        showWidget();
    }

    private void initVisible() {
        isLocalBMPUploadMap.put("0", false);
        isLocalBMPUploadMap.put("1", false);
        isLocalBMPUploadMap.put("2", false);
        isLocalBMPUploadMap.put("3", false);
        isLocalBMPUploadMap.put("4", false);

        verify_card_card1.setVisibility(View.VISIBLE);
        verify_card_card2.setVisibility(View.VISIBLE);
        verify_card_card3.setVisibility(View.VISIBLE);
        verify_bank_card1.setVisibility(View.VISIBLE);
        verify_bank_card2.setVisibility(View.VISIBLE);

        verify_bank_card1_finished.setVisibility(View.GONE);
        verify_bank_card2_finished.setVisibility(View.GONE);
        verify_card_card1_finished.setVisibility(View.GONE);
        verify_card_card2_finished.setVisibility(View.GONE);
        verify_card_card3_finished.setVisibility(View.GONE);

        verify_name_et.setEnabled(true);
        verify_num_et.setEnabled(true);
        verify_add_et.setEnabled(true);
        verify_bank_name_et.setEnabled(true);
        verify_owner_et.setEnabled(true);
        verify_bank_cardnum_et.setEnabled(true);
        verify_b_bank_et.setEnabled(true);
        verify_b_bank_num_et.setEnabled(true);
        verify_account_name_et.setEnabled(true);
        verify_merchant_name_et.setEnabled(true);
        verify_contact_et.setEnabled(true);
        verify_contact_phone_et.setEnabled(true);
        verify_province_et.setEnabled(true);
        verify_city_et.setEnabled(true);
        verify_now_add_et.setEnabled(true);
        verify_invite_et.setEnabled(true);
        verify_step1.setVisibility(View.VISIBLE);
        verify_step2.setVisibility(View.VISIBLE);
        verify_step3.setVisibility(View.VISIBLE);
        verify_add_lay.setVisibility(View.VISIBLE);
        verify_invite_lay.setVisibility(View.VISIBLE);
        verify_account_name_lay.setVisibility(View.VISIBLE);
        verify_pw_lay.setVisibility(View.VISIBLE);
        verify_commit_tv.setText("提交");
    }

    private void showWidget() {
        int verify_state = Integer.parseInt(Constant.user_info == null ? "0" : Constant.user_info.get(Constant.USER_INFO_ISAUTH));
        switch (verify_state) {
            case 0://未审核
                verify_commit_tv.setText("下一步");
                verify_step1.setVisibility(View.VISIBLE);
                verify_step2.setVisibility(View.GONE);
                verify_step3.setVisibility(View.GONE);

            //    verify_account_name_lay.setVisibility(View.GONE);
           //     verify_pw_lay.setVisibility(View.GONE);
                break;
            case 1://
                verify_commit_tv.setText("返回");
                verify_card_card1.setVisibility(View.GONE);
                verify_card_card2.setVisibility(View.GONE);
                verify_card_card3.setVisibility(View.GONE);
                verify_bank_card1.setVisibility(View.GONE);
                verify_bank_card2.setVisibility(View.GONE);

                verify_account_name_lay.setVisibility(View.GONE);
                verify_pw_lay.setVisibility(View.GONE);
                verify_invite_lay.setVisibility(View.GONE);
                verify_add_lay.setVisibility(View.GONE);

                verify_name_et.setEnabled(false);
                verify_num_et.setEnabled(false);
                verify_add_et.setEnabled(false);
                verify_bank_name_et.setEnabled(false);
                verify_owner_et.setEnabled(false);
                verify_bank_cardnum_et.setEnabled(false);
                verify_b_bank_et.setEnabled(false);
                verify_b_bank_num_et.setEnabled(false);
                verify_merchant_name_et.setEnabled(false);
                verify_contact_et.setEnabled(false);

                verify_account_name_et.setEnabled(false);
                verify_contact_phone_et.setEnabled(false);
                verify_province_et.setEnabled(false);
                verify_city_et.setEnabled(false);
                verify_now_add_et.setEnabled(false);

                verify_step1.setVisibility(View.VISIBLE);
                verify_step2.setVisibility(View.VISIBLE);
                verify_step3.setVisibility(View.VISIBLE);
                break;
            case 2://
                break;
            case 3://
                cur_step = 1;
//                verify_card_card1_finished.setVisibility(temp_info.get(PIC1).equals("") ? View.GONE : View.VISIBLE);
//                verify_card_card2_finished.setVisibility(temp_info.get(PIC2).equals("") ? View.GONE : View.VISIBLE);
//                verify_card_card3_finished.setVisibility(temp_info.get(PIC3).equals("") ? View.GONE : View.VISIBLE);
//                verify_bank_card1_finished.setVisibility(temp_info.get(PIC4).equals("") ? View.GONE : View.VISIBLE);
//                verify_bank_card2_finished.setVisibility(temp_info.get(PIC5).equals("") ? View.GONE : View.VISIBLE);

                Picasso.with(getContext()).load(temp_info.get(PIC1))
                        .placeholder(R.drawable.card)
                        .error(R.drawable.download_failed)
                        .into(verify_card_card1_thumb);
                Picasso.with(getContext()).load(temp_info.get(PIC2))
                        .placeholder(R.drawable.card)
                        .error(R.drawable.download_failed)
                        .into(verify_card_card2_thumb);
                Picasso.with(getContext()).load(temp_info.get(PIC3))
                        .placeholder(R.drawable.card)
                        .error(R.drawable.download_failed)
                        .into(verify_card_card3_thumb);
                Picasso.with(getContext()).load(temp_info.get(PIC4))
                        .placeholder(R.drawable.card)
                        .error(R.drawable.download_failed)
                        .into(verify_bank_card1_thumb);
                Picasso.with(getContext()).load(temp_info.get(PIC5))
                        .placeholder(R.drawable.card)
                        .error(R.drawable.download_failed)
                        .into(verify_bank_card2_thumb);

                verify_step1.setVisibility(View.VISIBLE);
                verify_step2.setVisibility(View.VISIBLE);
                verify_step3.setVisibility(View.VISIBLE);

                verify_add_lay.setVisibility(View.GONE);
                verify_invite_lay.setVisibility(View.GONE);
             //   verify_account_name_lay.setVisibility(View.GONE);
              //  verify_pw_lay.setVisibility(View.GONE);
                verify_commit_tv.setText("提交");
                break;
        }
    }

    private void autoWriteInfo() {

        verify_name_et.setText(Constant.user_info == null ? "" : Constant.user_info.get(Constant.NAME));
        verify_num_et.setText(Constant.user_info == null ? "" : Constant.user_info.get(Constant.USER_INFO_IDCARD));

        verify_bank_name_et.setText(Constant.user_info == null ? "" : Constant.user_info.get(Constant.BANK));
        verify_owner_et.setText(Constant.user_info == null ? "" : Constant.user_info.get(Constant.BANK_ACCOUNT_NAME));
        verify_bank_cardnum_et.setText(Constant.user_info == null ? "" : Constant.user_info.get(Constant.BANK_ACCOUNT));
        verify_b_bank_et.setText(Constant.user_info == null ? "" : Constant.user_info.get(Constant.SUB_BRANCH));
        verify_b_bank_num_et.setText(Constant.user_info == null ? "" : Constant.user_info.get(Constant.BANKFIRM));

        verify_account_name_et.setText(Constant.user_info == null ? "" : Constant.user_info.get(Constant.USER_INFO_INVITE_CODE));
        verify_pw_et.setText(Constant.user_info == null ? "" : Constant.user_info.get(Constant.USER_INFO_PW));
        verify_contact_et.setText(Constant.user_info == null ? "" : Constant.user_info.get(Constant.CONTACT));
        verify_contact_phone_et.setText(Constant.user_info == null ? "" : Constant.user_info.get(Constant.PHONE));
        verify_province_et.setText(Constant.user_info == null ? "" : Constant.user_info.get(Constant.PROVINCE));
        verify_merchant_name_et.setText(Constant.user_info == null ? "" : Constant.user_info.get(Constant.NAME));
        verify_city_et.setText(Constant.user_info == null ? "" : Constant.user_info.get(Constant.CITY));
        verify_now_add_et.setText(Constant.user_info == null ? "" : Constant.user_info.get(Constant.ADDRESS));

        temp_info.put(PIC1, Constant.user_info == null ? "" : Constant.user_info.get(Constant.PIC_1));
        temp_info.put(PIC2, Constant.user_info == null ? "" : Constant.user_info.get(Constant.PIC_2));
        temp_info.put(PIC3, Constant.user_info == null ? "" : Constant.user_info.get(Constant.PIC_3));
        temp_info.put(PIC4, Constant.user_info == null ? "" : Constant.user_info.get(Constant.PIC_4));
        temp_info.put(PIC5, Constant.user_info == null ? "" : Constant.user_info.get(Constant.PIC_5));

    }

    private void photograph(int type) {
        Constant.photo_idx = type;
        switch (type) {
            case 0:
                if (isLocalBMPUploadMap.get(type + "")) {
                    Constant.photo_path = (String) verify_card_card1_thumb.getTag();
                    showPhotoTip();
                } else {
                    listener.openCamera(type);
                }
                break;
            case 1:
                if (isLocalBMPUploadMap.get(type + "")) {
                    Constant.photo_path = (String) verify_card_card2_thumb.getTag();
                    showPhotoTip();
                } else {
                    listener.openCamera(type);
                }
                break;
            case 2:
                if (isLocalBMPUploadMap.get(type + "")) {
                    Constant.photo_path = (String) verify_card_card3_thumb.getTag();
                    showPhotoTip();
                } else {
                    listener.openCamera(type);
                }
                break;
            case 3:
                if (isLocalBMPUploadMap.get(type + "")) {
                    Constant.photo_path = (String) verify_bank_card1_thumb.getTag();
                    showPhotoTip();
                } else {
                    listener.openCamera(type);
                }
                break;
            case 4:
                if (isLocalBMPUploadMap.get(type + "")) {
                    Constant.photo_path = (String) verify_bank_card2_thumb.getTag();
                    showPhotoTip();
                } else {
                    listener.openCamera(type);
                }
                break;
        }
    }

    /**
     *
     */
    private void showPhotoTip() {
        DialogFragmentPhotoTip dialogFragmentPhotoTip = new DialogFragmentPhotoTip();
        dialogFragmentPhotoTip.show(getFragmentManager(), "");
    }

    private void doVerify() {

        int verify_state = Integer.parseInt(Constant.user_info == null ? "0" : Constant.user_info.get(Constant.USER_INFO_ISAUTH));
        switch (verify_state) {
            case 0://未审核
                if (!doEmptyVerify()) {
                    return;
                }
                cur_step++;
                switch (cur_step) {
                    case 2:
                        verify_step1.setVisibility(View.GONE);
                        verify_step2.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        verify_step2.setVisibility(View.GONE);
                        verify_step3.setVisibility(View.VISIBLE);
                        verify_commit_tv.setText("提交");
                        break;
                    case 4:
                        submitInfo();
                        break;
                }
                break;
            case 1://已审核
                listener.finishVerify();

                break;
            case 2://已作废
                break;
            case 3://未通过
                if (!doEmptyVerify2()) {
                    return;
                }
                switch (cur_step) {
                    case 1:
                        verifyMerchantInfo();
                        break;
                    case 2:
                        verifyMerchantBankInfo();
                        break;
                    case 3:
                        verifyMerchantPicInfo();
                        break;
                }
                break;
        }
    }

    private void verifyMerchantInfo() {
        verify_commit_tv.setText("正在提交商户资料");
        verify_do_lay.setEnabled(false);
        showWaitDialog("正在提交商户资料");
        HashMap<String, String> StringA2 = new HashMap<>();
        StringA2.put(Constant.AID_STR, Constant.AID);
        StringA2.put(Constant.MERCHANT_ID, Constant.user_info.get(Constant.MERCHANT_ID));
        StringA2.put(Constant.NAME, verify_name_et.getText().toString().trim());
        StringA2.put(Constant.CONTACT, verify_contact_et.getText().toString().trim());
        StringA2.put(Constant.PHONE, verify_contact_phone_et.getText().toString().trim());
        StringA2.put(Constant.PROVINCE, verify_province_et.getText().toString().trim());
        StringA2.put(Constant.CITY, verify_city_et.getText().toString().trim());
        StringA2.put(Constant.ADDRESS, verify_now_add_et.getText().toString().trim());
        StringA2.put(Constant.ID_CARD, verify_num_et.getText().toString().trim());

        String sign0 = util.getSign(StringA2);
        StringA2.put(Constant.SIGN, sign0);
        present.initRetrofit(Constant.URL_BAIBAO, false);
        present.QueryEditMerchant(Constant.AID, StringA2.get(Constant.SIGN), StringA2.get(Constant.MERCHANT_ID), StringA2.get(Constant.NAME), StringA2.get(Constant.CONTACT),
                StringA2.get(Constant.PHONE), StringA2.get(Constant.PROVINCE), StringA2.get(Constant.CITY), StringA2.get(Constant.ADDRESS),
                StringA2.get(Constant.ID_CARD));
    }

    private void verifyMerchantBankInfo() {
        showWaitDialog("正在提交银行卡信息");
        verify_commit_tv.setText("提交中");
        verify_do_lay.setEnabled(false);

        HashMap<String, String> StringA2 = new HashMap<>();
        StringA2.put(Constant.AID_STR, Constant.AID);
        StringA2.put(Constant.MERCHANT_ID, Constant.user_info.get(Constant.MERCHANT_ID));
        StringA2.put(Constant.BANK, verify_bank_name_et.getText().toString().trim());
        StringA2.put(Constant.SUB_BRANCH, verify_b_bank_et.getText().toString().trim());
        StringA2.put(Constant.BANK_ACCOUNT, verify_bank_cardnum_et.getText().toString().trim());
        StringA2.put(Constant.BANK_ACCOUNT_NAME, verify_owner_et.getText().toString().trim());
        StringA2.put(Constant.BANKFIRM, verify_b_bank_num_et.getText().toString().trim());

        String sign0 = util.getSign(StringA2);
        StringA2.put(Constant.SIGN, sign0);

        present.initRetrofit(Constant.URL_BAIBAO, false);
        present.QueryEditMerchantBank(Constant.AID, StringA2.get(Constant.SIGN), StringA2.get(Constant.MERCHANT_ID), StringA2.get(Constant.BANK), StringA2.get(Constant.SUB_BRANCH),
                StringA2.get(Constant.BANK_ACCOUNT), StringA2.get(Constant.BANK_ACCOUNT_NAME), StringA2.get(Constant.BANKFIRM));
    }

    private void verifyMerchantPicInfo() {
        showWaitDialog("正在提交认证图片信息");
        verify_commit_tv.setText("提交中");
        verify_do_lay.setEnabled(false);

        HashMap<String, String> StringA2 = new HashMap<>();
        StringA2.put(Constant.AID_STR, Constant.AID);
        StringA2.put(Constant.MERCHANT_ID, Constant.user_info.get(Constant.MERCHANT_ID));
        StringA2.put(Constant.PIC_1, temp_info.get(PIC1));
        StringA2.put(Constant.PIC_2, temp_info.get(PIC2));
        StringA2.put(Constant.PIC_3, temp_info.get(PIC3));
        StringA2.put(Constant.PIC_4, temp_info.get(PIC4));
        StringA2.put(Constant.PIC_5, temp_info.get(PIC5));

        String sign0 = util.getSign(StringA2);
        StringA2.put(Constant.SIGN, sign0);

        present.initRetrofit(Constant.URL_BAIBAO, false);
        present.QueryEditMerchantPic(Constant.AID, StringA2.get(Constant.SIGN), StringA2.get(Constant.MERCHANT_ID), StringA2.get(Constant.PIC_1), StringA2.get(Constant.PIC_2),
                StringA2.get(Constant.PIC_3), StringA2.get(Constant.PIC_4), StringA2.get(Constant.PIC_5));
    }


    private void submitInfo() {
        verify_commit_tv.setText("正在提交");
        verify_do_lay.setEnabled(false);
        showWaitDialog("正在提交,请稍等");
        HashMap<String, String> StringA2 = new HashMap<>();
        StringA2.put(Constant.AID_STR, Constant.AID);
        //   StringA2.put(Constant.MERCHANT_ID, "123456789123456");
        StringA2.put(Constant.OPENID, Constant.OPENID);
        StringA2.put(Constant.REFERRAL_CODE, verify_invite_et.getText().toString().trim());
        StringA2.put(Constant.NAME, verify_name_et.getText().toString().trim());
        StringA2.put(Constant.RATE, "49");
        StringA2.put(Constant.CONTACT, verify_contact_et.getText().toString().trim());
        StringA2.put(Constant.PHONE, verify_contact_phone_et.getText().toString().trim());
        StringA2.put(Constant.PROVINCE, verify_province_et.getText().toString().trim());
        StringA2.put(Constant.CITY, verify_city_et.getText().toString().trim());
        StringA2.put(Constant.ADDRESS, verify_now_add_et.getText().toString().trim());
        //    StringA2.put(Constant.AGENT_ID, "无");
        StringA2.put(Constant.ID_CARD, verify_num_et.getText().toString().trim());
        StringA2.put(Constant.BANK, verify_bank_name_et.getText().toString().trim());
        StringA2.put(Constant.SUB_BRANCH, verify_b_bank_et.getText().toString().trim());
        StringA2.put(Constant.BANK_ACCOUNT, verify_bank_cardnum_et.getText().toString().trim());
        StringA2.put(Constant.BANK_ACCOUNT_NAME, verify_owner_et.getText().toString().trim());
        StringA2.put(Constant.BANKFIRM, "");
        //  StringA2.put(Constant.ACCOUNT, verify_account_name_et.getText().toString().trim());
        //   StringA2.put(Constant.PASSWORD, verify_pw_et.getText().toString().trim());
        StringA2.put(Constant.ACCOUNT, Constant.user_info == null ? verify_contact_phone_et.getText().toString().trim() : Constant.user_info.get(Constant.PHONE));
        StringA2.put(Constant.PASSWORD, Constant.user_info == null ? verify_contact_phone_et.getText().toString().trim().substring(6) : Constant.user_info.get(Constant.PHONE).substring(6));
        StringA2.put(Constant.PIC_1, temp_info.get(PIC1));
        StringA2.put(Constant.PIC_2, temp_info.get(PIC2));
        StringA2.put(Constant.PIC_3, temp_info.get(PIC3));
        StringA2.put(Constant.PIC_4, temp_info.get(PIC4));
        StringA2.put(Constant.PIC_5, temp_info.get(PIC5));

        String sign0 = util.getSign(StringA2);
        StringA2.put(Constant.SIGN, sign0);
        present.initRetrofit(Constant.URL_BAIBAO, false);
        present.QueryRegister(StringA2);
    }


    public void initState() {
        KLog.v("initState");
        if (verify_card_card1 == null) {
            return;
        }
        initVisible();
        autoWriteInfo();
        showWidget();
        InputMethodManager inputManager =
                (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(verify_name_et.getWindowToken(), 0);
    }

    public void back() {
        KLog.v(cur_step + "");
        int verify_state = Integer.parseInt(Constant.user_info == null ? "0" : Constant.user_info.get(Constant.USER_INFO_ISAUTH));
        switch (verify_state) {
            case 0:
                cur_step--;
                switch (cur_step) {
                    case 0:
                        listener.finishVerify();
                        break;
                    case 1:
                        verify_step1.setVisibility(View.VISIBLE);
                        verify_step2.setVisibility(View.GONE);
                        break;
                    case 2:
                        verify_step1.setVisibility(View.GONE);
                        verify_step2.setVisibility(View.VISIBLE);
                        verify_step3.setVisibility(View.GONE);
                        break;
                    case 3:
                        verify_step2.setVisibility(View.GONE);
                        verify_step3.setVisibility(View.VISIBLE);
                        verify_commit_tv.setText("提交");
                        break;
                }
                break;
            case 1:
            case 2:
            case 3:
                listener.finishVerify();
                break;
        }

    }

    private Boolean doEmptyVerify() {
        switch (cur_step) {
            case 1:
                if (temp_info.get(PIC1) == null) {
                    VToast.toast(getContext(), "请上传身份证正面照片");
                    return false;
                }
                if (temp_info.get(PIC2) == null) {
                    VToast.toast(getContext(), "请上传身份证反面照片");
                    return false;
                }
                if (temp_info.get(PIC3) == null) {
                    VToast.toast(getContext(), "请上传手持身份证照片");
                    return false;
                }
                if (verify_name_et.getText().toString().trim().equals("")) {
                    VToast.toast(getContext(), "请输入姓名");
                    return false;
                }
                if (!util.IDCardValidate(verify_num_et.getText().toString().trim()).equals("")) {
                    VToast.toast(getContext(), "请输入正确的身份证");
                    return false;
                }
                break;
            case 2:
                if (temp_info.get(PIC4) == null) {
                    VToast.toast(getContext(), "请上传银行卡正面照片");
                    return false;
                }
                if (temp_info.get(PIC5) == null) {
                    VToast.toast(getContext(), "请上传银行卡反面照片");
                    return false;
                }
                if (verify_bank_cardnum_et.getText().toString().trim().equals("")) {
                    VToast.toast(getContext(), "请输入银行卡卡号");
                    return false;
                }
                if (verify_owner_et.getText().toString().trim().equals("")) {
                    VToast.toast(getContext(), "请输入持卡人姓名");
                    return false;
                }
                if (verify_b_bank_et.getText().toString().trim().equals("")) {
                    VToast.toast(getContext(), "请输入分行名称");
                    return false;
                }
                break;
            case 3:
                if (verify_account_name_et.getText().toString().trim().equals("")) {
                    VToast.toast(getContext(), "请输入帐户名");
                    return false;
                }
                if (verify_pw_et.getText().toString().trim().equals("")) {
                    VToast.toast(getContext(), "请输入账户密码");
                    return false;
                }
                if (verify_contact_et.getText().toString().trim().equals("")) {
                    VToast.toast(getContext(), "请输入联系人姓名");
                    return false;
                }
                if (!util.verifyPhone(verify_contact_phone_et.getText().toString().trim())) {
                    VToast.toast(getContext(), "请输入正确的联系人电话");
                    return false;
                }
                if (verify_province_et.getText().toString().trim().equals("")) {
                    VToast.toast(getContext(), "请输入联系人所在省份");
                    return false;
                }
                if (verify_city_et.getText().toString().trim().equals("")) {
                    VToast.toast(getContext(), "请输入联系人所在城市");
                    return false;
                }
                if (verify_now_add_et.getText().toString().trim().equals("")) {
                    VToast.toast(getContext(), "请输入联系人当前住址");
                    return false;
                }
                if (verify_invite_et.getText().toString().trim().equals("")) {
                    VToast.toast(getContext(), "请输入邀请码");
                    return false;
                }
                break;
            default:
                return true;
        }
        return true;
    }

    private Boolean doEmptyVerify2() {

        if (verify_name_et.getText().toString().trim().equals("")) {
            VToast.toast(getContext(), "请输入姓名");
            return false;
        }
        if (!util.IDCardValidate(verify_num_et.getText().toString().trim()).equals("")) {
            VToast.toast(getContext(), "请输入正确的身份证");
            return false;
        }
        if (verify_bank_cardnum_et.getText().toString().trim().equals("")) {
            VToast.toast(getContext(), "请输入银行卡卡号");
            return false;
        }
        if (verify_owner_et.getText().toString().trim().equals("")) {
            VToast.toast(getContext(), "请输入持卡人姓名");
            return false;
        }
        if (verify_b_bank_et.getText().toString().trim().equals("")) {
            VToast.toast(getContext(), "请输入分行名称");
            return false;
        }
        if (verify_contact_et.getText().toString().trim().equals("")) {
            VToast.toast(getContext(), "请输入联系人姓名");
            return false;
        }
        if (!util.verifyPhone(verify_contact_phone_et.getText().toString().trim())) {
            VToast.toast(getContext(), "请输入正确的联系人电话");
            return false;
        }
        if (verify_province_et.getText().toString().trim().equals("")) {
            VToast.toast(getContext(), "请输入联系人所在省份");
            return false;
        }
        if (verify_city_et.getText().toString().trim().equals("")) {
            VToast.toast(getContext(), "请输入联系人所在城市");
            return false;
        }
        if (verify_now_add_et.getText().toString().trim().equals("")) {
            VToast.toast(getContext(), "请输入联系人当前住址");
            return false;
        }


        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (IVerifyListtener) context;
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    public void uploadImg(int type, String path) {
        KLog.v(path);
        showWaitDialog("正在上传,请稍等");
        upTobmob(type, path);
      /*  switch (type) {
            case 0:
                showWaitDialog("正在验证&上传,请稍等");
                new Thread(() -> trygetCardNum(type, path)).start();
                break;
            default:
                showWaitDialog("正在上传,请稍等");
                upTobmob(type, path);
                break;
        }*/
    }

    private void upTobmob(int type, String path) {
        BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.uploadblock(getContext(), new UploadFileListener() {
            @Override
            public void onProgress(Integer value) {
                KLog.v(value + "%");
                // showWaitDialog("请稍等，正在上传到服务器:" + value + "%");
            }

            @Override
            public void onSuccess() {
                hideWaitDialog();
                isLocalBMPUploadMap.put(type + "", true);
                String ret_path = bmobFile.getFileUrl(getContext());
                KLog.v(ret_path);
                File local_file = new File(path);
                VToast.toast(getContext(), "上传文件成功");
                switch (type) {
                    case 0:
                        //   verify_card_card1_finished.setVisibility(View.VISIBLE);
                        temp_info.put(PIC1, ret_path);
                        verify_card_card1_thumb.setTag(local_file.getAbsolutePath());
                        Picasso.with(getContext()).load(local_file).
                                into(verify_card_card1_thumb);
                        break;
                    case 1:
                        // verify_card_card2_finished.setVisibility(View.VISIBLE);
                        temp_info.put(PIC2, ret_path);
                        verify_card_card2_thumb.setTag(local_file.getAbsolutePath());
                        Picasso.with(getContext()).load(local_file).
                                into(verify_card_card2_thumb);
                        break;
                    case 2:
                        //   verify_card_card3_finished.setVisibility(View.VISIBLE);
                        verify_card_card3_thumb.setTag(local_file.getAbsolutePath());
                        Picasso.with(getContext()).load(local_file).
                                into(verify_card_card3_thumb);
                        temp_info.put(PIC3, ret_path);
                        break;
                    case 3:
                        //  verify_bank_card1_finished.setVisibility(View.VISIBLE);
                        verify_bank_card1_thumb.setTag(local_file.getAbsolutePath());
                        Picasso.with(getContext()).load(local_file).
                                into(verify_bank_card1_thumb);
                        temp_info.put(PIC4, ret_path);
                        break;
                    case 4:
                        //   verify_bank_card2_finished.setVisibility(View.VISIBLE);
                        verify_bank_card2_thumb.setTag(local_file.getAbsolutePath());
                        Picasso.with(getContext()).load(local_file).
                                into(verify_bank_card2_thumb);
                        temp_info.put(PIC5, ret_path);
                        break;
                }
            }

            @Override
            public void onFailure(int i, String s) {
                hideWaitDialog();
                KLog.v("i" + i + "--" + s);
                VToast.toast(getContext(), "上传文件失败：" + s);
            }
        });
    }

    private void trygetCardNum(int type, String filePath) {
        if (!util.isNetworkConnected(getContext())) {
            VToast.toast(getContext(), "没有网络");
            return;
        }
        String imgBase64;
        try {
            File file = new File(filePath);
            byte[] content = new byte[(int) file.length()];
            FileInputStream finputstream = new FileInputStream(file);
            finputstream.read(content);
            finputstream.close();
            imgBase64 = new String(Base64.encodeBase64(content));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        // 获取服务
        RpcService rpcService = ApiGatewayClient.getRpcService();
        final ApiRequest apiRequest = new ApiRequest();
        // 设置请求地址、Path及Method
        apiRequest.setAddress("https://dm-51.data.aliyun.com");
        apiRequest.setPath("/rest/160601/ocr/ocr_idcard.json");
        apiRequest.setMethod(HttpMethod.POST);
        // 按照文档设置二进制形式Body，支持设置Query参数、Header参数、Form形式Body
        apiRequest.setStringBody("{\"inputs\":[{\"image\":{\"dataType\":50,\"dataValue\":\"" + imgBase64 + "\"},\"configure\":{\"dataType\":50,\"dataValue\":\"{\\\"side\\\":\\\"face\\\"}\"}}]}");
        // 设置支持自签等形式的证书，如果服务端证书合法请勿设置该值，仅在开发测试或者非常规场景下设置。
        apiRequest.setTrustServerCertificate(true);
        // 设置超时
        apiRequest.setTimeout(10000);
        rpcService.call(apiRequest, new ApiResponseCallback() {
            @Override
            public void onSuccess(ApiResponse apiResponse) {
                String s = apiResponse.getStringBody();
                KLog.v(s);
                Gson gson = new Gson();
                switch (type) {
                    case 0:
                        NumBean result = gson.fromJson(s, NumBean.class);
                        String dataValue = result.getOutputs().get(0).getOutputValue().getDataValue();
                        DataValueBean dataValueBean = gson.fromJson(dataValue, DataValueBean.class);
                        Message msg = new Message();
                        msg.what = OCR_SUCESS;
                        msg.obj = dataValueBean;
                        handler.sendMessage(msg);
                        upTobmob(type, filePath);
                        break;
                    default:
                        handler.sendEmptyMessage(OCR_FAIL);
                        upTobmob(type, filePath);
                        break;
                }
            }

            @Override
            public void onException(ApiInvokeException e) {
                handler.sendEmptyMessage(OCR_FAIL);
                upTobmob(type, filePath);
            }
        });
    }


    @Override
    public void ResolveLoginInfo(HeoCodeResponse info) {

    }


    @Override
    public void ResolveRegisterInfo(HeoCodeResponse info) {
        hideWaitDialog();
        verify_commit_tv.setText("提交");
        verify_do_lay.setEnabled(true);
        if (info.getErrcode() == null) {
            VToast.toast(getContext(), "网络错误");
            return;
        }
        VToast.toast(getContext(), info.getErrmsg());
        if (info.getErrcode().equals(SUCCESS)) {
            listener.finishVerify();
        }
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

    }

    @Override
    protected void initView() {
        super.initView();
        int verify_state = Integer.parseInt(Constant.user_info == null ? "0" : Constant.user_info.get(Constant.USER_INFO_ISAUTH));
        switch (verify_state) {
            case 0:
                cur_step = 2;
                break;
        }
        back();
    }

    @Override
    public void ResolveOCRInfo(ResponseBody info) {
        hideWaitDialog();
        if (info.contentLength() == 0) return;
        try {
            KLog.v(info.string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ResolveEditMerchantInfo(HeoCodeResponse info) {
        KLog.v(info.toString());
        hideWaitDialog();
        verify_commit_tv.setText("提交");
        verify_do_lay.setEnabled(true);
        if (info.getErrcode() == null) {
            VToast.toast(getContext(), "网络错误");
            return;
        }
        if (info.getErrcode().equals(SUCCESS)) {
            cur_step = 2;
            verifyMerchantBankInfo();
        } else {
            VToast.toast(getContext(), info.getErrmsg());
        }
    }

    @Override
    public void ResolveEditMerchantBankInfo(HeoCodeResponse info) {
        KLog.v(info.toString());
        hideWaitDialog();
        verify_commit_tv.setText("提交");
        verify_do_lay.setEnabled(true);
        if (info.getErrcode() == null) {
            VToast.toast(getContext(), "网络错误");
            return;
        }
        if (info.getErrcode().equals(SUCCESS)) {
            cur_step = 3;
            verifyMerchantPicInfo();
        } else {
            VToast.toast(getContext(), info.getErrmsg());
        }
    }

    @Override
    public void ResolveEditMerchantPicInfo(HeoCodeResponse info) {
        KLog.v(info.toString());
        hideWaitDialog();
        verify_commit_tv.setText("提交");
        verify_do_lay.setEnabled(true);
        if (info.getErrcode() == null) {
            VToast.toast(getContext(), "网络错误");
            return;
        }
        if (info.getErrcode().equals(SUCCESS)) {
            cur_step = 1;
            VToast.toast(getContext(), "修改成功");
            listener.updateSuccess();
        } else {
            VToast.toast(getContext(), info.getErrmsg());
        }
    }

    public interface IVerifyListtener {

        void finishVerify();

        void updateSuccess();

        void openCamera(int type);
    }
}