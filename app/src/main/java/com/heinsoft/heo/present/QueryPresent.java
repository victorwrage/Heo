package com.heinsoft.heo.present;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.heinsoft.heo.bean.HeoCodeObjResponse;
import com.heinsoft.heo.bean.HeoCodeResponse;
import com.heinsoft.heo.model.IRequestMode;
import com.heinsoft.heo.model.converter.CustomGsonConverter;
import com.heinsoft.heo.util.Constant;
import com.heinsoft.heo.view.IAgentView;
import com.heinsoft.heo.view.IInviteView;
import com.heinsoft.heo.view.IMerchantView;
import com.heinsoft.heo.view.IMessageView;
import com.heinsoft.heo.view.IPayView;
import com.heinsoft.heo.view.IUserEditView;
import com.heinsoft.heo.view.IUserView;
import com.heinsoft.heo.view.IVerifyView;
import com.heinsoft.heo.view.IView;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Administrator on 2017/4/6.
 */

public class QueryPresent implements IRequestPresent {
    private IView iView;
    private Context context;
    private IRequestMode iRequestMode;
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .build();
    private static QueryPresent instance = null;

    public void setView(Activity activity) {
        iView = (IView) activity;
    }

    public void setView(Fragment fragment) {
        iView = (IView) fragment;
    }

    private QueryPresent(Context context_) {
        context = context_;
    }

    public static QueryPresent getInstance(Context context) {
        if (instance == null) {
            synchronized (QueryPresent.class) {
                if (instance == null) {
                    return new QueryPresent(context);
                }
            }
        }
        return instance;
    }

    public void initRetrofit(String url, boolean isXml) {

        try {
            if (isXml) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(url)
                        .client(client)
                       //  .addConverterFactory(Xm.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
                iRequestMode = retrofit.create(IRequestMode.class);
            } else {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
                iRequestMode = retrofit.create(IRequestMode.class);
            }

        } catch (IllegalArgumentException e) {
            e.fillInStackTrace();
        }
    }

    public void initRetrofitSendMessage(String url) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .client(genericClient())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            iRequestMode = retrofit.create(IRequestMode.class);


        } catch (IllegalArgumentException e) {
            e.fillInStackTrace();
        }
    }

    public void initRetrofitMerchant(String url) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .client(genericMerchantClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            iRequestMode = retrofit.create(IRequestMode.class);


        } catch (IllegalArgumentException e) {
            e.fillInStackTrace();
        }
    }

    /**
     * 添加统一header,超时时间,http日志打印
     *
     * @return
     */
    public static OkHttpClient genericClient() {
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request.Builder requestBuilder = request.newBuilder();
                        request = requestBuilder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=GBK"),
                                URLDecoder.decode(bodyToString(request.body()), "UTF-8")))
                                .build();
                        return chain.proceed(request);
                    }
                })
                //  .addInterceptor(logging)
                .connectTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        return httpClient;
    }

    /**
     * 添加统一header,超时时间,http日志打印
     *
     * @return
     */
    public static OkHttpClient genericMerchantClient() {

        OkHttpClient httpClient = new OkHttpClient.Builder()
                 .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request.Builder requestBuilder = request.newBuilder();
                        String encode = bodyToString(request.body());
                        request = requestBuilder.post(RequestBody.create(MediaType.parse("multipart/form-data;charset=UTF-8"),
                                 URLDecoder.decode(encode,"UTF-8")))
                                .build();
                        return chain.proceed(request);
                    }
                })
                .addNetworkInterceptor(
                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
                .addNetworkInterceptor(
                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .addNetworkInterceptor(
                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        return httpClient;
    }

    private static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    public void initRetrofit2(String url, boolean isXml) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(CustomGsonConverter.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(new OkHttpClient.Builder()
                            .addNetworkInterceptor(
                                    new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
                            .addNetworkInterceptor(
                                    new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                            .addNetworkInterceptor(
                                    new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build())
                    .build();
            iRequestMode = retrofit.create(IRequestMode.class);

        } catch (IllegalArgumentException e) {
            e.fillInStackTrace();
        }
    }


    @Override
    public void QueryRegister(HashMap<String, String> stringA) {

        iRequestMode.QueryRegister(Constant.AID,
                stringA.get(Constant.SIGN),
                stringA.get(Constant.OPENID),
                stringA.get(Constant.REFERRAL_CODE),
                stringA.get(Constant.NAME),
                stringA.get(Constant.RATE),
                stringA.get(Constant.CONTACT),
                stringA.get(Constant.PHONE),
                stringA.get(Constant.PROVINCE),
                stringA.get(Constant.CITY),
                stringA.get(Constant.ADDRESS),
                stringA.get(Constant.ID_CARD),

                stringA.get(Constant.BANK),
                stringA.get(Constant.SUB_BRANCH),
                stringA.get(Constant.BANK_ACCOUNT),
                stringA.get(Constant.BANK_ACCOUNT_NAME),
                stringA.get(Constant.BANKFIRM),
                stringA.get(Constant.ACCOUNT),
                stringA.get(Constant.PASSWORD),
                stringA.get(Constant.PIC_1),
                stringA.get(Constant.PIC_2),
                stringA.get(Constant.PIC_3),
                stringA.get(Constant.PIC_4),
                stringA.get(Constant.PIC_5))
                .onErrorReturn(s -> new HeoCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IUserView) iView).ResolveRegisterInfo(s));
    }

    @Override
    public void QueryOCRInfo(String inputs) {
        iRequestMode.QueryOCRInfo(inputs)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IVerifyView) iView).ResolveOCRInfo(s));
    }

    @Override
    public void QueryCode(String mobile, String msgid, String msg) {
        Map<String, String> sendParams = new HashMap<>();
        sendParams.put("uid", Constant.MESSAGE_USER_NAME);
        sendParams.put("psw", Constant.MESSAGE_PASSWORD);
        sendParams.put("mobiles", mobile);
        sendParams.put("msgid", msgid);
        sendParams.put("msg", msg);
        iRequestMode.QueryCode(sendParams)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IUserView) iView).ResolveCodeInfo(s));
    }

    @Override
    public void QueryMessage() {
        iRequestMode.QueryMessage()
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IMessageView) iView).ResolveMessageInfo(s));
    }

    @Override
    public void QueryPay(String aid, String sign, String merchant_id, String pay_money, int pay_type, int trade_type) {
        iRequestMode.QueryPay(aid, sign, merchant_id, pay_money, pay_type, trade_type)
                .onErrorReturn(s -> new HeoCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IPayView) iView).ResolvePayInfo(s));
    }



    @Override
    public void QueryScoreQuickPay(String aid, String sign, String merchant_id, String pay_money, String bank_account, String phone, String true_name, String id_card, String cvv2, String indate, String trantp) {
        iRequestMode.QueryScoreQuickPay(aid, sign, merchant_id, pay_money, bank_account, phone, true_name, id_card, cvv2, indate, trantp)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IPayView) iView).ResolveScoreQuickPayInfo(s));
    }

    @Override
    public void QueryScoreQuickPayConfirm(String aid, String sign, String account, String system_orderId, String orderId, String smsCode, String bank_account, String pay_money, String tranTp,String extact) {
        iRequestMode.QueryScoreQuickPayConfirm(aid, sign, account, system_orderId, orderId, smsCode, bank_account, pay_money, tranTp,extact)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IPayView) iView).ResolveScoreQuickPayConfirmInfo(s));
    }

    @Override
    public void QueryBankName(String aid, String sign) {
        iRequestMode.QueryBankName(aid,sign)
                .onErrorReturn(s ->new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IPayView) iView).ResolveBankName(s));

    }

    @Override
    public void QueryAddCard(String aid, String sign, String merchant_id, String truename, String id_card, String card_account, int card_type, String bank, String phone, String indate, String cvv2) {
        iRequestMode.QueryAddCard(aid, sign, merchant_id, truename, id_card, card_account, card_type, bank, phone, indate, cvv2)
                .onErrorReturn(s ->new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IPayView) iView).ResolveAddCardInfo(s));
    }

    @Override
    public void QueryCardPackage(String aid, String sign, String merchant_id) {
        iRequestMode.QueryCardPackage(aid, sign, merchant_id)
                .onErrorReturn(s ->new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IPayView) iView).ResolveCardPackageInfo(s));
    }

    @Override
    public void QueryQuickPay(String aid, String sign, String merchant_id, String pay_money, String bank_account, String mobile, String name, String id_card, String cvv2, String vd, String trantp) {
        iRequestMode.QueryQuickPay(aid, sign, merchant_id, pay_money, bank_account, mobile, name, id_card, cvv2, vd, trantp)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IPayView) iView).ResolveQuickPayInfo(s));
    }

    @Override
    public void QueryOpenCredit(String aid, String sign, String bank_account, String trantp, String merchant) {
        iRequestMode.QueryOpenCredit(aid, sign, bank_account, trantp, merchant)
                .onErrorReturn(s ->new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s ->((IPayView) iView).ResolveOpenCreditInfo(s));
    }

    @Override
    public void QueryQuickPayConfirm(String aid, String sign, String merchant_id, String order_id, String orderNo, String smCode) {
        iRequestMode.QueryQuickPayConfirm(aid, sign, merchant_id, order_id, orderNo, smCode)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IPayView) iView).ResolveQuickPayConfirmInfo(s));
    }

    @Override
    public void QueryMerchant(String aid, String sign, String merchant_id) {
        iRequestMode.QueryMerchantID(aid, sign, merchant_id)
                .onErrorReturn(s -> new HeoCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IMerchantView) iView).ResolveMerchantInfo(s));
    }

    @Override
    public void QueryProfit(String aid, String sign, String agent_id, String date_start, String date_end) {
        iRequestMode.QueryProfit(aid, sign, agent_id, date_start, date_end)
                .onErrorReturn(s -> new HeoCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IPayView) iView).ResolveProfitInfo(s));
    }

    @Override
    public void QueryEditMerchant(String aid, String sign, String merchant_id, String name, String contact, String phone, String province, String city, String address, String id_card) {
        iRequestMode.QueryEditMerchant(aid, sign, merchant_id, name, contact, phone, province, city, address, id_card)
                .onErrorReturn(s -> new HeoCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IUserEditView) iView).ResolveEditMerchantInfo(s));
    }

    @Override
    public void QuerySettle(String aid, String sign, String merchant_id, String person_type, String phone, String email, String name, String inst, String address, String bus_lic, String sto_pic, String idcard_face, String idcard_back, String bank_card_face, String bank_card_back, String truename, String idcard_no, String bank_card_no, String bank_card_name, String bank_card_type, String bank_name, String bank_branch, String bankfirm) {

        HashMap<String,String > map = new HashMap<>();
        map.put("aid",aid);
        map.put("sign",sign);
        map.put("merchant_id",merchant_id);
        map.put("person_type",person_type);
        map.put("phone",phone);
        map.put("email",email);
        map.put("name",name);
        map.put("inst",inst);
        map.put("address",address);
        if(bus_lic!=null) {
            map.put("bus_lic",bus_lic);
            map.put("sto_pic",sto_pic);
        }
        map.put("idcard_face",idcard_face);
        map.put("idcard_back", idcard_back);
        map.put("bank_card_face",bank_card_face);
        map.put("bank_card_back",bank_card_back);
        map.put("truename",truename);
        map.put("idcard_no",idcard_no);
        map.put("bank_card_no",bank_card_no);
        map.put("bank_card_name",bank_card_name);
        map.put("bank_card_type",bank_card_type);
        map.put("bank_name",bank_name);
        map.put("bank_branch",bank_branch);
        map.put("bankfirm",bankfirm);


        iRequestMode.QuerySettle(map)
                .onErrorReturn(s -> new HeoCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IMerchantView) iView).ResolveSettle(s));
    }

    @Override
    public void QuerySettleInfo(String aid, String sign, String merchant_id) {
        iRequestMode.QuerySettleInfo(aid, sign, merchant_id)
                .onErrorReturn(s -> new HeoCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IMerchantView) iView).ResolveSettleInfo(s));
    }

    @Override
    public void QueryEditMerchantBank(String aid, String sign, String merchant_id, String bank, String sub_branch, String bank_account, String bank_account_name, String bankfirm) {
        iRequestMode.QueryEditMerchantBank(aid, sign, merchant_id, bank, sub_branch, bank_account, bank_account_name, bankfirm)
                .onErrorReturn(s -> new HeoCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IUserEditView) iView).ResolveEditMerchantBankInfo(s));
    }

    @Override
    public void QueryEditMerchantPic(String aid, String sign, String merchant_id, String pic_1, String pic_2, String pic_3, String pic_4, String pic_5) {
        iRequestMode.QueryEditMerchantPic(aid, sign, merchant_id, pic_1, pic_2, pic_3, pic_4, pic_5)
                .onErrorReturn(s -> new HeoCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IUserEditView) iView).ResolveEditMerchantPicInfo(s));
    }

    @Override
    public void QueryBank(String aid, String sign) {
        iRequestMode.QueryBank(aid, sign)
                .onErrorReturn(s -> new HeoCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IVerifyView) iView).ResolveBankInfo(s));
    }

    @Override
    public void QueryBankBranch(String aid, String sign, String bankname) {
        iRequestMode.QueryBankBranch(aid, sign, bankname)
                .onErrorReturn(s -> new HeoCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IVerifyView) iView).ResolvBankBranchInfo(s));
    }

    @Override
    public void QueryInviteCode(String aid, String sign, String referral_code) {
        iRequestMode.QueryInviteCode(aid, sign, referral_code)
                .onErrorReturn(s -> new HeoCodeObjResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IInviteView) iView).ResolveInviteInfo(s));
    }

    @Override
    public void QueryUsefullInviteCode(String aid, String sign, String agent_id) {
        iRequestMode.QueryUserfullInviteCode(aid, sign, agent_id)
                .onErrorReturn(s -> new HeoCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IInviteView) iView).ResolveUsefullInviteInfo(s));
    }

    @Override
    public void QueryObtainInviteCode(String aid, String sign, String agent_id, String num) {
        iRequestMode.QueryObtainInviteCode(aid, sign, agent_id, num)
                .onErrorReturn(s -> new HeoCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IInviteView) iView).ResolveObtainInviteInfo(s));
    }

    @Override
    public void QueryAssignInviteCode(String aid, String sign, String up_agent_id, String agent_id, String refercodes) {
        iRequestMode.QueryAssignInviteCode(aid, sign, up_agent_id, agent_id, refercodes)
                .onErrorReturn(s -> new HeoCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IInviteView) iView).ResolveAssignInviteInfo(s));
    }

    @Override
    public void QueryAddAgent(String aid, String sign, String parent_id, String phone, String agent_name, String contact, String province, String city, String rate, String bank, String sub_branch, String bank_account, String bank_account_name, String bankfirm, String account, String password) {
        iRequestMode.QueryAddAgent(aid, sign, parent_id, phone, agent_name, contact, province, city, rate, bank, sub_branch, bank_account, bank_account_name, bankfirm, account, password)
                .onErrorReturn(s -> new HeoCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IAgentView) iView).ResolveAddAgentInfo(s));
    }

    @Override
    public void QueryAgentInfo(String aid, String sign, String phone) {
        iRequestMode.QueryAgentInfo(aid, sign, phone)
                .onErrorReturn(s -> new HeoCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IAgentView) iView).ResolveAgentInfo(s));
    }

    @Override
    public void QueryAgentOrder(String aid, String sign, String agent_id, String date_start, String date_end) {
        iRequestMode.QueryAgentOrder(aid, sign, agent_id, date_start, date_end)
                .onErrorReturn(s -> new HeoCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IPayView) iView).ResolveAgentOrderInfo(s));
    }

    @Override
    public void QueryAgentProfit(String aid, String sign, String agent_id, String date_start, String date_end) {
        iRequestMode.QueryAgentProfit(aid, sign, agent_id, date_start, date_end)
                .onErrorReturn(s -> new HeoCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IPayView) iView).ResolveAgentProfitInfo(s));
    }

    @Override
    public void QueryAgentWithDraw(String aid, String sign, String agent_id, String money) {
        iRequestMode.QueryAgentWithDraw(aid, sign, agent_id, money)
                .onErrorReturn(s -> new HeoCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IPayView) iView).ResolveAgentWithdrawInfo(s));
    }

    @Override
    public void QueryMerchantOrder(String aid, String sign, String merchant_id, String date_start, String date_end) {
        iRequestMode.QueryMerchantOrder(aid, sign, merchant_id, date_start, date_end)
                .onErrorReturn(s -> new HeoCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IPayView) iView).ResolveMerchantOrderInfo(s));
    }


    @Override
    public void QueryVerify(String mobile) {

    }

    @Override
    public void QueryLogin(String aid, String sign, String username, String password) {
        iRequestMode.QueryLogin(aid, sign, username, password)
                .onErrorReturn(s -> new HeoCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IUserView) iView).ResolveLoginInfo(s));
    }


    @Override
    public void QueryForget(String mobile, String code, String user_pass) {
        iRequestMode.QueryForget(mobile, code, user_pass)
                .onErrorReturn(s -> new HeoCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IUserView) iView).ResolveForgetInfo(s));
    }

    @Override
    public void QueryAlter(String useID, String TOKEN, String old_password, String new_password) {
        iRequestMode.QueryAlter(useID, TOKEN, old_password, new_password)
                .onErrorReturn(s -> new HeoCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IUserView) iView).ResolveAlterInfo(s));
    }

    @Override
    public void QueryEmail(String useID, String TOKEN, String email) {
        iRequestMode.QueryEmail(useID, TOKEN, email)
                .onErrorReturn(s -> new HeoCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IUserView) iView).ResolveEmailInfo(s));
    }


    @Override
    public void QueryCodeVerify(String mobile, String code) {
        iRequestMode.QueryCodeVerify(mobile, code)
                .onErrorReturn(s -> new HeoCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IUserView) iView).ResolveCodeVerifyInfo(s));
    }

    @Override
    public void QueryPhoneChange(String mobile, String code, String useID, String TOKEN) {
        iRequestMode.QueryChangePhone(mobile, code, useID, TOKEN)
                .onErrorReturn(s -> new HeoCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IUserView) iView).ResolvePhoneChangeInfo(s));
    }

    @Override
    public void QueryResetPassword(String aid, String sign, String account, String newpass) {
        iRequestMode.QueryResetPassword(aid, sign, account, newpass)
                .onErrorReturn(s -> new HeoCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IUserView) iView).ResolveResetPasswordInfo(s));
    }

}
