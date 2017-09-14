package com.heinsoft.heo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.BankCardParams;
import com.baidu.ocr.sdk.model.BankCardResult;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.heinsoft.heo.R;
import com.heinsoft.heo.bean.HeoCodeResponse;
import com.heinsoft.heo.bean.HeoMerchantInfoResponse;
import com.heinsoft.heo.present.QueryPresent;
import com.heinsoft.heo.util.Constant;
import com.heinsoft.heo.util.Utils;
import com.heinsoft.heo.util.VToast;
import com.heinsoft.heo.view.IMerchantView;
import com.heinsoft.heo.view.IVerifyView;
import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;
import okhttp3.ResponseBody;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/8/28 10:28
 */

public class FragmentSettle extends BaseFragment implements OnTabSelectListener, IVerifyView, IMerchantView {
    private final int OCR_CODE_OCR_CAED = 1011;
    private int type;
    private static int REQUEST_THUMBNAIL = 1;// 请求缩略图信号标识
    private String[] actor_type = new String[]{"法人注册", "非法人注册"};
    private String[] industry_type = new String[]{"餐饮", "超市", "住宿", "其它"};
    private String[] account_type = new String[]{"个人", "企业"};
    private String cur_branch_bank;
    private String cur_branch_bank_firm;

    private String qrcode_url;
    private String path;
    private ArrayList<String> bank_list = new ArrayList<>();
    private ArrayList<String> bank_branch_list = new ArrayList<>();
    private ArrayList<HeoMerchantInfoResponse> bank_branch_data = new ArrayList<>();
    ArrayAdapter branch_bank_adapter;
    protected ArrayAdapter<String> actorAdapter;
    protected ArrayAdapter<String> industryAdapter;
    protected ArrayAdapter<String> accountAdapter;
    private final String PIC1 = "pic1";
    private final String PIC2 = "pic2";
    private final String PIC3 = "pic3";
    private final String PIC4 = "pic4";
    private final String PIC5 = "pic5";
    private final String PIC6 = "pic6";
    @Bind(R.id.settle_name_et)
    EditText settle_name_et;
    @Bind(R.id.settle_card_et)
    EditText settle_card_et;
    @Bind(R.id.settle_account_et)
    EditText settle_account_et;
    @Bind(R.id.settle_bank_num_et)
    EditText settle_bank_num_et;

    @Bind(R.id.settle_branch_et)
    EditText settle_branch_et;
    @Bind(R.id.settle_branch_num_et)
    EditText settle_branch_num_et;

    @Bind(R.id.settle_b_bank_lv)
    ListView settle_b_bank_lv;

    @Bind(R.id.actor_phone_et)
    EditText actor_phone_et;
    @Bind(R.id.settle_email_et)
    EditText settle_email_et;
    @Bind(R.id.settle_merchant_et)
    EditText settle_merchant_et;
    @Bind(R.id.settle_add_et)
    EditText settle_add_et;
    @Bind(R.id.settle_qrcode_iv)
    ImageView settle_qrcode_iv;

    @Bind(R.id.settle_pic1_iv)
    ImageView settle_pic1_iv;
    @Bind(R.id.settle_pic2_iv)
    ImageView settle_pic2_iv;
    @Bind(R.id.settle_pic3_iv)
    ImageView settle_pic3_iv;
    @Bind(R.id.settle_pic4_iv)
    ImageView settle_pic4_iv;
    @Bind(R.id.settle_pic5_iv)
    ImageView settle_pic5_iv;
    @Bind(R.id.settle_pic6_iv)
    ImageView settle_pic6_iv;

    @Bind(R.id.actor_actor_sp)
    Spinner actor_actor_sp;
    @Bind(R.id.settle_account_type_sp)
    Spinner settle_account_type_sp;
    @Bind(R.id.settle_type_sp)
    Spinner settle_type_sp;
    @Bind(R.id.settle_bank_name_sp)
    Spinner settle_bank_name_sp;

    @Bind(R.id.settle_save_btn)
    Button settle_save_btn;
    @Bind(R.id.settle_step1_btn)
    Button settle_step1_btn;
    @Bind(R.id.settle_step2_btn)
    Button settle_step2_btn;
    @Bind(R.id.settle_step3_btn)
    Button settle_step3_btn;

    @Bind(R.id.settle_work_lay)
    RelativeLayout settle_work_lay;
    @Bind(R.id.settle_qrcode_lay)
    RelativeLayout settle_qrcode_lay;
    @Bind(R.id.settle_step1_lay)
    LinearLayout settle_step1_lay;
    @Bind(R.id.settle_step2_lay)
    LinearLayout settle_step2_lay;
    @Bind(R.id.settle_step3_lay)
    LinearLayout settle_step3_lay;
    @Bind(R.id.settle_shop_lay)
    LinearLayout settle_shop_lay;
    @Bind(R.id.header_edit_lay)
    LinearLayout header_edit_lay;

    @Bind(R.id.tl_1)
    SegmentTabLayout tl_1;
    private HashMap<String, Boolean> isLocalBMPUploadMap = new HashMap<>();
    @Bind(R.id.header_btn_lay)
    LinearLayout header_btn_lay;
    @Bind(R.id.header_title)
    TextView header_title;
    HashMap<String, String> pics = new HashMap<>();
    private final String[] mTitles = {"个体工商户"
            , "企业商户"};
    private Boolean isAutoWrite = false;
    boolean isSearching = false;

    private int merchant_type = 0;
    private Bitmap qrCodeBitmap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settle, container, false);
        ButterKnife.bind(FragmentSettle.this, view);
        return view;
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
        present.setView(FragmentSettle.this);
        sp = getContext().getSharedPreferences(COOKIE_KEY, Context.MODE_PRIVATE);
    }

    private void initView() {
        initMerchantSettleInfo();

        tl_1.setTabData(mTitles);
        tl_1.setOnTabSelectListener(FragmentSettle.this);
        settle_shop_lay.setVisibility(View.VISIBLE);
        RxView.clicks(header_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Back());
        RxView.clicks(settle_step1_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Step2());
        RxView.clicks(settle_step2_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Step3());
        RxView.clicks(settle_step3_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Submit());
        RxView.clicks(settle_pic1_iv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Photo(1));
        RxView.clicks(settle_pic2_iv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Photo(2));
        RxView.clicks(settle_pic3_iv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Photo(3));
        RxView.clicks(settle_pic4_iv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Photo(4));
        RxView.clicks(settle_pic5_iv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Photo(5));
        RxView.clicks(settle_pic6_iv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Photo(6));
        //  RxView.clicks(settle_save_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Save());
        RxView.clicks(header_edit_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Save());
        /**角色类型**/
        actorAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, actor_type);
        actorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actor_actor_sp.setAdapter(actorAdapter);
        actor_actor_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        actor_actor_sp.setSelection(0);
        /**行业类型**/
        industryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, industry_type);
        industryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        settle_type_sp.setAdapter(industryAdapter);
        settle_type_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        settle_type_sp.setSelection(0);

        /**账户类型**/
        accountAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, account_type);
        accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        settle_account_type_sp.setAdapter(accountAdapter);
        settle_account_type_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        settle_account_type_sp.setSelection(0);
        /**银行**/
        branch_bank_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, bank_branch_list);
        settle_b_bank_lv.setVisibility(View.GONE);
        settle_b_bank_lv.setAdapter(branch_bank_adapter);
        settle_b_bank_lv.setOnItemClickListener((parent, view1, position, id) -> {
            cur_branch_bank = bank_branch_list.get(position);
            cur_branch_bank_firm = bank_branch_data.get(position).getBankcode();
            settle_branch_et.setText(cur_branch_bank);
            settle_b_bank_lv.setVisibility(View.GONE);
            isAutoWrite = true;
        });

        settle_branch_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isAutoWrite) {
                    isAutoWrite = false;
                    return;
                }
                if (!isSearching && s.length() != 0 && count != 0) {
                    initBranckBank();
                    isSearching = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bankAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, bank_list);
        bankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        settle_bank_name_sp.setAdapter(bankAdapter);
        settle_bank_name_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        header_title.setText("商户入驻");

    }

    public Boolean isVerifyed() {
        return qrCodeBitmap == null ? false : true;
    }

    private void Save() {
        if (qrCodeBitmap == null) {
            VToast.toast(getContext(), "图片未下载");
            return;
        }
        util.saveImageToGallery(getContext(), qrCodeBitmap);
        VToast.toast(getContext(), "二维码已经保存");
    }

    private void test() {
       /* pics.put(PIC1,  util.UrlEnco(util.Bitmap2StrByBase64(BitmapFactory.decodeFile("/storage/emulated/0/chutou/1504057192426.jpg"))));
        pics.put(PIC2,  util.UrlEnco(util.Bitmap2StrByBase64(BitmapFactory.decodeFile("/storage/emulated/0/chutou/1504057192426.jpg"))));
        pics.put(PIC3,  util.UrlEnco(util.Bitmap2StrByBase64(BitmapFactory.decodeFile("/storage/emulated/0/chutou/1504057192426.jpg"))));
        pics.put(PIC4,  util.UrlEnco(util.Bitmap2StrByBase64(BitmapFactory.decodeFile("/storage/emulated/0/chutou/1504057192426.jpg"))));
        pics.put(PIC5,  util.UrlEnco(util.Bitmap2StrByBase64(BitmapFactory.decodeFile("/storage/emulated/0/chutou/1504057192426.jpg"))));
        pics.put(PIC6,  util.UrlEnco(util.Bitmap2StrByBase64(BitmapFactory.decodeFile("/storage/emulated/0/chutou/1504057192426.jpg"))));*/
        // KLog.v(util.Bitmap2StrByBase64(BitmapFactory.decodeFile("/storage/emulated/0/chutou/1504057192426.jpg")));


    }

    private void Photo(int i) {
        type = i;
        Intent intent = new Intent(getContext(), CameraActivity.class);
        path = util.getSaveFile(getContext()).getAbsolutePath();
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, path);
        switch (i) {
            case 1:
            case 2:
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, OCR_CODE_OCR_CAED);
                break;
            case 3:
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                startActivityForResult(intent, OCR_CODE_OCR_CAED);
                break;
            case 4:
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
                startActivityForResult(intent, OCR_CODE_OCR_CAED);
                break;
            case 5:
            case 6:
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_BANK_CARD);
                startActivityForResult(intent, OCR_CODE_OCR_CAED);
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        KLog.v("resultCode" + resultCode);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == OCR_CODE_OCR_CAED) {
                KLog.v("onActivityResult" + path);
                uploadImg(type, path);
            }
            if (requestCode == REQUEST_THUMBNAIL) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                String path_thumb = util.saveImageToGallery(getContext(), bitmap);
                uploadImg(type, path_thumb);
            }
        }
    }

    public void uploadImg(int type, String path) {
        KLog.v(path);
        showWaitDialog("请稍等");
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        KLog.v("uploadImg" + bitmap.getWidth());
        switch (type) {
            case 3:
                executor.execute(() -> {
                    baiduOcr(type, path);
                });
                break;
            case 5:
                executor.execute(() -> {
                    baiduOcr(type, path);
                });
                break;
            default:
                upTobmob(type, path);
                break;
        }


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
                //VToast.toast(getContext(), "上传文件成功");
                switch (type) {
                    case 1:
                        //   verify_card_card1_finished.setVisibility(View.VISIBLE);
                        settle_pic1_iv.setTag(path);
                        pics.put(PIC1, ret_path);
                        Picasso.with(getContext()).load(ret_path)
                                .placeholder(R.drawable.verify_icon1)
                                .error(R.drawable.download_failed)
                                .into(settle_pic1_iv);
                        break;
                    case 2:
                        // verify_card_card2_finished.setVisibility(View.VISIBLE);
                        pics.put(PIC2, ret_path);
                        settle_pic2_iv.setTag(path);
                        Picasso.with(getContext()).load(ret_path)
                                .placeholder(R.drawable.verify_icon2)
                                .error(R.drawable.download_failed)
                                .into(settle_pic2_iv);
                        break;
                    case 3:
                        //   verify_card_card3_finished.setVisibility(View.VISIBLE);
                        pics.put(PIC3, ret_path);
                        settle_pic3_iv.setTag(path);
                        Picasso.with(getContext()).load(ret_path)
                                .placeholder(R.drawable.verify_icon1)
                                .error(R.drawable.download_failed).
                                into(settle_pic3_iv);

                        break;
                    case 4:
                        //  verify_bank_card1_finished.setVisibility(View.VISIBLE);
                        pics.put(PIC4, ret_path);
                        settle_pic4_iv.setTag(path);
                        Picasso.with(getContext()).load(ret_path)
                                .placeholder(R.drawable.verify_icon2)
                                .error(R.drawable.download_failed).
                                into(settle_pic4_iv);

                        break;
                    case 5:
                        //   verify_bank_card2_finished.setVisibility(View.VISIBLE);
                        pics.put(PIC5, ret_path);
                        settle_pic5_iv.setTag(path);
                        Picasso.with(getContext()).load(ret_path)
                                .placeholder(R.drawable.verify_icon1)
                                .error(R.drawable.download_failed).
                                into(settle_pic5_iv);
                        break;
                    case 6:
                        //   verify_bank_card2_finished.setVisibility(View.VISIBLE);
                        pics.put(PIC6, ret_path);
                        settle_pic6_iv.setTag(path);
                        Picasso.with(getContext()).load(ret_path)
                                .placeholder(R.drawable.verify_icon2)
                                .error(R.drawable.download_failed).
                                into(settle_pic6_iv);
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

    private void baiduOcr(int type, String filePath) {
        File u_file = new File(filePath);
        KLog.v("baiduOcr" + u_file.getAbsolutePath());

        switch (type) {
            case 3:
                IDCardParams id_param = new IDCardParams();
                id_param.setImageFile(u_file);
                id_param.setIdCardSide(IDCardParams.ID_CARD_SIDE_FRONT);
                id_param.setDetectDirection(true);
                OCR.getInstance().recognizeIDCard(id_param, new OnResultListener<IDCardResult>() {
                    @Override
                    public void onResult(IDCardResult result) {
                        KLog.v(result.toString());
                        KLog.v(result.getName().getWords());
                        //hideWaitDialog();
                        if (result.getName() != null && !result.getName().getWords().equals("")) {
                            settle_name_et.setText(result.getName().getWords());
                        }
                        if (result.getIdNumber() != null && !result.getIdNumber().getWords().equals("")) {
                            settle_card_et.setText(result.getIdNumber().getWords());
                        }
                       /* if(result.getAddress()!=null && !result.getAddress().getWords().equals("")) {
                            verify_add_et.setText(result.getAddress().getWords());
                        }*/
                        upTobmob(type, filePath);
                        VToast.toast(getContext(), "请检查获取的信息是否正确");
                    }

                    @Override
                    public void onError(OCRError error) {
                        KLog.v(error.toString());
                        upTobmob(type, filePath);
                        //hideWaitDialog();
                        //    VToast.toast(getContext(), "获取信息失败,请手动输入");
                    }
                });
                break;
            case 5:
                BankCardParams param = new BankCardParams();
                param.setImageFile(new File(filePath));
                OCR.getInstance().recognizeBankCard(param, new OnResultListener<BankCardResult>() {
                    @Override
                    public void onResult(BankCardResult result) {
                        hideWaitDialog();
                        if (result.getBankCardNumber() != null && !result.getBankCardNumber().equals("")) {
                            settle_bank_num_et.setText(result.getBankCardNumber());
                        }
                        upTobmob(type, filePath);
                    }

                    @Override
                    public void onError(OCRError error) {
                        KLog.v(error.toString());
                        hideWaitDialog();
                        upTobmob(type, filePath);
                        //     VToast.toast(getContext(), "获取信息失败,请手动输入");
                    }
                });
                break;
        }

    }

    private void Submit() {
        if (settle_name_et.getText().toString().trim().equals("")) {
            VToast.toast(getContext(), "请输入您的姓名");
            return;
        }
        if (!util.IDCardValidate(settle_card_et.getText().toString().trim()).equals("")) {
            VToast.toast(getContext(), "请输入正确的身份证");
            return;
        }
        if (settle_account_et.getText().toString().trim().equals("")) {
            VToast.toast(getContext(), "请输入您的开户名称");
            return;
        }
        if (settle_bank_num_et.getText().toString().trim().equals("")) {
            VToast.toast(getContext(), "请输入您的银行卡号");
            return;
        }
       /* if(settle_bank_name_et.getText().toString().trim().equals("")){
            VToast.toast(getContext(),"请输入银行名称");
            return;
        }*/
        if (settle_branch_et.getText().toString().trim().equals("")) {
            VToast.toast(getContext(), "请输入分行名称");
            return;
        }
/*        if(settle_branch_num_et.getText().toString().trim().equals("")){
            VToast.toast(getContext(),"请输入联行号");
            return;
        }*/
        showWaitDialog("请稍等");
        present.initRetrofit(Constant.URL_BAIBAO, false);
        HashMap<String, String> StringA = new HashMap<>();
        StringA.put("aid", Constant.AID);
        StringA.put("merchant_id", Constant.user_info.get(Constant.MERCHANT_ID));
        StringA.put("person_type", actor_actor_sp.getSelectedItemPosition() + "");
        StringA.put("phone", actor_phone_et.getText().toString().trim());
        StringA.put("email", settle_email_et.getText().toString().trim());
        StringA.put("name", settle_merchant_et.getText().toString().trim());
        StringA.put("inst", industry_type[settle_type_sp.getSelectedItemPosition()]);
        StringA.put("address", settle_add_et.getText().toString().trim());
        if (merchant_type == 0) {
            StringA.put("bus_lic", pics.get(PIC1));
            StringA.put("sto_pic", pics.get(PIC2));
        }
        StringA.put("idcard_face", pics.get(PIC3));
        StringA.put("idcard_back", pics.get(PIC4));
        StringA.put("bank_card_face", pics.get(PIC5));
        StringA.put("bank_card_back", pics.get(PIC6));

        StringA.put("truename", settle_name_et.getText().toString().trim());
        StringA.put("idcard_no", settle_card_et.getText().toString().trim());
        StringA.put("bank_card_no", settle_bank_num_et.getText().toString().trim());
        StringA.put("bank_card_name", settle_account_et.getText().toString().trim());
        StringA.put("bank_card_type", account_type[settle_account_type_sp.getSelectedItemPosition()]);
        StringA.put("bank_name", bank_list.get(settle_bank_name_sp.getSelectedItemPosition()));
        StringA.put("bank_branch", settle_branch_et.getText().toString().trim());
        StringA.put("bankfirm", cur_branch_bank_firm);

        String sign = util.getSign(StringA);
        StringA.put("sign", sign);

        present.QuerySettle(StringA.get(Constant.AID_STR), StringA.get(Constant.SIGN), StringA.get(Constant.MERCHANT_ID),
                StringA.get("person_type"), StringA.get(Constant.PHONE), StringA.get("email"),
                StringA.get("name"), StringA.get("inst"), StringA.get("address"), StringA.get("bus_lic"), StringA.get("sto_pic")
                , StringA.get("idcard_face"), StringA.get("idcard_back"), StringA.get("bank_card_face"), StringA.get("bank_card_back"), StringA.get("truename"),
                StringA.get("idcard_no"), StringA.get("bank_card_no"), StringA.get("bank_card_name"), StringA.get("bank_card_type"),
                StringA.get("bank_name"), StringA.get("bank_branch")
                , StringA.get("bankfirm"));

    }

    private void Step3() {
        if (pics.get(PIC1) == null && merchant_type == 0) {
            VToast.toast(getContext(), "请上传营业执照");
            return;
        }
        if (pics.get(PIC2) == null && merchant_type == 0) {
            VToast.toast(getContext(), "请上传门店照片");
            return;
        }
        if (pics.get(PIC3) == null) {
            VToast.toast(getContext(), "请上传身份证正面照片");
            return;
        }
        if (pics.get(PIC4) == null) {
            VToast.toast(getContext(), "请上传身份证反面照片");
            return;
        }
        if (pics.get(PIC5) == null) {
            VToast.toast(getContext(), "请上传银行卡正面照片");
            return;
        }
        if (pics.get(PIC6) == null) {
            VToast.toast(getContext(), "请上传银行卡反面照片");
            return;
        }
        settle_step2_lay.setVisibility(View.GONE);
        settle_step3_lay.setVisibility(View.VISIBLE);
    }

    private void Step2() {
        if (!util.verifyPhone(actor_phone_et.getText().toString().trim())) {
            VToast.toast(getContext(), "请输入正确手机号");
            return;
        }
        if (!util.isEmail(settle_email_et.getText().toString().trim())) {
            VToast.toast(getContext(), "请输入正确邮箱");
            return;
        }
        if (settle_merchant_et.getText().toString().trim().equals("")) {
            VToast.toast(getContext(), "请输入商户号");
            return;
        }
        if (settle_add_et.getText().toString().trim().equals("")) {
            VToast.toast(getContext(), "请输入常用地址");
            return;
        }
        hidSoftInput();
        settle_step1_lay.setVisibility(View.GONE);
        settle_step2_lay.setVisibility(View.VISIBLE);
    }

    private void initMerchantSettleInfo() {
        showWaitDialog("请稍后");
        present.initRetrofit(Constant.URL_BAIBAO, false);
        HashMap<String, String> StringA = new HashMap<>();
        StringA.put(Constant.AID_STR, Constant.AID);
        StringA.put(Constant.MERCHANT_ID, Constant.user_info.get(Constant.MERCHANT_ID));
        String sign = util.getSign(StringA);
        StringA.put(Constant.SIGN, sign);
        present.QuerySettleInfo(StringA.get(Constant.AID_STR), StringA.get(Constant.SIGN), StringA.get(Constant.MERCHANT_ID));
    }

    private void initBankName() {
        showWaitDialog("请稍后");
        present.initRetrofit(Constant.URL_BAIBAO, false);
        HashMap<String, String> StringA = new HashMap<>();
        StringA.put(Constant.AID_STR, Constant.AID);
        String sign = util.getSign(StringA);
        StringA.put(Constant.SIGN, sign);
        present.QueryBank(StringA.get(Constant.AID_STR), StringA.get(Constant.SIGN));
    }


    private void initBranckBank() {
        // showWaitDialog("请稍等");
        present.initRetrofit(Constant.URL_BAIBAO, false);
        HashMap<String, String> StringA = new HashMap<>();
        StringA.put(Constant.AID_STR, Constant.AID);
        StringA.put("bankname", settle_branch_et.getText().toString());
        String sign = util.getSign(StringA);
        StringA.put(Constant.SIGN, sign);
        present.QueryBankBranch(StringA.get(Constant.AID_STR), StringA.get(Constant.SIGN), StringA.get("bankname"));
    }

    @Override
    public void Back() {
        if (settle_step3_lay.getVisibility() == View.VISIBLE) {
            settle_step2_lay.setVisibility(View.VISIBLE);
            settle_step3_lay.setVisibility(View.GONE);
            return;
        }
        if (settle_step2_lay.getVisibility() == View.VISIBLE) {
            settle_step1_lay.setVisibility(View.VISIBLE);
            settle_step2_lay.setVisibility(View.GONE);
            return;
        }
        if (qrCodeBitmap != null) {
            new MaterialDialog.Builder(getContext())
                    .title("提示")
                    .content("(商户入驻)信息还未提交，确定退出？")
                    .positiveText(R.string.btn_confirm)
                    .negativeText(R.string.btn_cancel)
                    .autoDismiss(true)
                    .onPositive((materialDialog, dialogAction) -> {
                        qrCodeBitmap = null;
                        qrcode_url = null;
                        listener.gotoMain();
                        super.Back();
                    })
                    .show();
            return;
        }else{
            listener.gotoMain();
            super.Back();
        }

    }

    @Override
    public void RefreshState() {
        super.RefreshState();
        settle_account_et.setText("");
        actor_phone_et.setText("");
        settle_add_et.setText("");
        settle_bank_num_et.setText("");
        settle_branch_et.setText("");
        settle_card_et.setText("");
        settle_email_et.setText("");
        settle_name_et.setText("");
        settle_pic1_iv.setImageResource(R.drawable.verify_icon1);
        settle_pic2_iv.setImageResource(R.drawable.verify_icon2);
        settle_pic3_iv.setImageResource(R.drawable.verify_icon1);
        settle_pic4_iv.setImageResource(R.drawable.verify_icon2);
        settle_pic5_iv.setImageResource(R.drawable.verify_icon1);
        settle_pic6_iv.setImageResource(R.drawable.verify_icon2);
        settle_account_type_sp.setSelection(0);
        settle_bank_name_sp.setSelection(0);
        settle_type_sp.setSelection(0);
        actor_actor_sp.setSelection(0);
        tl_1.setCurrentTab(0);
        merchant_type = 0;
        header_edit_lay.setVisibility(View.GONE);
        settle_work_lay.setVisibility(View.VISIBLE);
        settle_qrcode_lay.setVisibility(View.GONE);
        initMerchantSettleInfo();
    }

    @Override
    public void onTabSelect(int position) {
        merchant_type = position;
        if (position == 0) {
            settle_shop_lay.setVisibility(View.VISIBLE);
        } else {
            settle_shop_lay.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTabReselect(int position) {

    }

    @Override
    public void ResolveOCRInfo(ResponseBody info) {

    }

    @Override
    public void ResolveBankInfo(HeoCodeResponse info) {
        hideWaitDialog();
        if (info.getErrcode() == null) {
            VToast.toast(getContext(), "获取银行名称失败!");
            new MaterialDialog.Builder(getContext())
                    .title("提示")
                    .content("银行列表获取失败，是否重试？")
                    .positiveText(R.string.btn_confirm)
                    .negativeText(R.string.btn_cancel)
                    .autoDismiss(true)
                    .cancelListener(dialogInterface -> Back())
                    .onPositive((materialDialog, dialogAction) -> {
                        initBankName();
                    })
                    .show();
            return;
        }
        if (info.getErrcode().equals(SUCCESS)) {
            for (HeoMerchantInfoResponse name : info.getContent()) {
                bank_list.add(name.getName());
            }

            bankAdapter.notifyDataSetChanged();
            //  initMerchantSettleInfo();
        }
    }

    @Override
    public void ResolvBankBranchInfo(HeoCodeResponse info) {
        isSearching = false;
        if (info.getErrcode() == null) {
            VToast.toast(getContext(), "获取分行数据失败!");
            return;
        }
        if (info.getErrcode().equals(SUCCESS)) {
            if (info.getContent().size() == 0) {
                settle_b_bank_lv.setVisibility(View.GONE);
                return;
            }
            bank_branch_data.clear();
            bank_branch_list.clear();
            for (HeoMerchantInfoResponse name : info.getContent()) {
                bank_branch_data.add(name);
                bank_branch_list.add(name.getBankname());
            }
            settle_b_bank_lv.setVisibility(View.VISIBLE);
            cur_branch_bank = bank_branch_list.get(0);
            cur_branch_bank_firm = bank_branch_data.get(0).getBankcode();
            branch_bank_adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void ResolveSettleInfo(HeoCodeResponse info) {
        hideWaitDialog();
        KLog.v(info.toString());
        if (info.getErrcode() == null) {
            VToast.toast(getContext(), "网络错误");
            new MaterialDialog.Builder(getContext())
                    .title("提示")
                    .content("获取入驻信息失败！")
                    .positiveText(R.string.btn_retry)
                    .negativeText(R.string.btn_cancel)
                    .autoDismiss(true)
                    .onPositive((dialog1, which) -> {
                        initMerchantSettleInfo();
                    })
                    .onNegative((dialog12, which) -> {
                        listener.gotoMain();
                    })
                    .cancelable(false)
                    .show();
            return;
        }
        if (info.getErrcode().equals(SUCCESS)) {
            int status = Integer.parseInt(info.getInfo().getStatus());
            switch (status) {
                case 0:
                    new MaterialDialog.Builder(getContext())
                            .title("提示")
                            .content("入驻信息正在审核中！")
                            .positiveText(R.string.btn_confirm)
                            .autoDismiss(true)
                            .cancelable(false)
                            .onAny((dialog1, which) -> {
                                listener.gotoMain();
                            })
                            .show();
                    break;
                case 1:
                    qrcode_url = info.getInfo().getFixed_qrcode();
                    HideAll();
                    ShowCode();
                    header_edit_lay.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    initBankName();
                    break;
            }
        } else {
            initBankName();
          /*   VToast.toast(getContext(), info.getErrmsg());
               new MaterialDialog.Builder(getContext())
                    .title("提示")
                    .content("获取入驻信息失败！")
                    .positiveText(R.string.btn_retry)
                    .negativeText(R.string.btn_cancel)
                    .autoDismiss(true)
                    .onPositive((dialog1, which) -> {
                        initMerchantSettleInfo();
                        super.Back();
                    })
                    .onNegative((dialog12, which) -> {
                        listener.gotoMain();
                    })
                    .cancelable(false)
                    .show();*/
        }
    }

    private void HideAll() {
        settle_work_lay.setVisibility(View.GONE);
        settle_qrcode_lay.setVisibility(View.VISIBLE);
    }

    private void ShowCode() {

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                qrCodeBitmap = bitmap;
                settle_qrcode_iv.setImageBitmap(qrCodeBitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                settle_qrcode_iv.setImageResource(R.drawable.download_failed);
                new MaterialDialog.Builder(getContext())
                        .title("提示")
                        .content("获取入驻信息失败！")
                        .positiveText(R.string.btn_retry)
                        .negativeText(R.string.btn_cancel)
                        .autoDismiss(true)
                        .onPositive((dialog1, which) -> {
                            ShowCode();
                        })
                        .onNegative((dialog12, which) -> {
                            listener.gotoMain();
                        })
                        .cancelable(false)
                        .show();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.with(getContext()).load(qrcode_url).into(target);
    }

    @Override
    public void ResolveSettle(HeoCodeResponse info) {
        hideWaitDialog();
        if (info.getErrcode() == null) {
            VToast.toast(getContext(), "网络错误");
            return;
        }
        VToast.toast(getContext(), info.getErrmsg());
        if (info.getErrcode().equals(SUCCESS)) {
            listener.gotoMain();
        }
    }

    @Override
    public void ResolveMerchantInfo(HeoCodeResponse info) {
    }
}
