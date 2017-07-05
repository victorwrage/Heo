package com.heinsoft.heo.present;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.heinsoft.heo.bean.HeoCodeResponse;
import com.heinsoft.heo.bean.HeoProfitResponse;
import com.heinsoft.heo.model.IRequestMode;
import com.heinsoft.heo.model.converter.CustomGsonConverter;
import com.heinsoft.heo.util.Constant;
import com.heinsoft.heo.view.IPayView;
import com.heinsoft.heo.view.IUserEditView;
import com.heinsoft.heo.view.IUserView;
import com.heinsoft.heo.view.IVerifyView;
import com.heinsoft.heo.view.IView;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
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
import okio.Buffer;
import okio.BufferedSource;
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

    public static synchronized QueryPresent getInstance(Context context) {
        if (instance == null) {
            return new QueryPresent(context);
        }
        return instance;
    }

    public void initRetrofit(String url, boolean isXml) {

        try {
            if (isXml) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(url)
                        .client(client)
                        // .addConverterFactory(Xm.create())
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

    /**
     * 添加统一header,超时时间,http日志打印
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
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
                  /*  .client(new OkHttpClient.Builder()
//                            .addNetworkInterceptor(
//                                    new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
//                            .addNetworkInterceptor(
//                                    new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
//                            .addNetworkInterceptor(
//                                    new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build())
                    .build();*/
            iRequestMode = retrofit.create(IRequestMode.class);

        } catch (IllegalArgumentException e) {
            e.fillInStackTrace();
        }
    }


    @Override
    public void QueryRegister(HashMap<String,String> stringA) {

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
        Map<String ,String > sendParams = new HashMap<>();
        sendParams.put("uid",Constant.MESSAGE_USER_NAME);
        sendParams.put("psw",Constant.MESSAGE_PASSWORD);
        sendParams.put("mobiles",mobile);
        sendParams.put("msgid",msgid);
        sendParams.put("msg",msg);
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
    public void QueryPay(String aid, String sign, String merchant_id, String pay_money, int pay_type, int trade_type) {
        iRequestMode.QueryPay(aid, sign, merchant_id, pay_money, pay_type, trade_type)
                .onErrorReturn(s -> new HeoCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IPayView) iView).ResolvePayInfo(s));
    }

    @Override
    public void QueryMerchant(String aid, String sign, String merchant_id) {
        iRequestMode.QueryMerchantID(aid, sign, merchant_id)
                .onErrorReturn(s -> new HeoCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IUserView) iView).ResolveMerchantInfo(s));
    }

    @Override
    public void QueryProfit(String aid, String sign, String agent_id, String date_start, String date_end) {
        iRequestMode.QueryProfit(aid, sign, agent_id,date_start,date_end)
                .onErrorReturn(s -> new HeoProfitResponse())
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
    public void QueryQcode(String mobile) {
        iRequestMode.QueryQcode(mobile)
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
                .subscribe(s -> ((IUserView) iView).ResolveQcodeInfo(s));
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


}
