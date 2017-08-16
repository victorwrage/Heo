package com.heinsoft.heo.model;

import com.heinsoft.heo.bean.HeoCodeObjResponse;
import com.heinsoft.heo.bean.HeoCodeResponse;
import com.heinsoft.heo.util.Constant;

import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by xyl on 2017/4/6.
 */

public interface IRequestMode {

    @FormUrlEncoded
    @POST("index.php?g=Api&m=Merchant&a=register")
    Flowable<HeoCodeResponse> QueryRegister(@Field(Constant.AID_STR) String aid, @Field(Constant.SIGN) String sign, @Field(Constant.OPENID) String openid, @Field(Constant.REFERRAL_CODE) String referral_code,
                                            @Field(Constant.NAME) String name, @Field(Constant.RATE) String rate, @Field(Constant.CONTACT) String contact, @Field(Constant.PHONE) String phone, @Field(Constant.PROVINCE) String province, @Field(Constant.CITY) String city, @Field(Constant.ADDRESS) String address
            , @Field(Constant.ID_CARD) String id_card, @Field(Constant.BANK) String bank, @Field(Constant.SUB_BRANCH) String sub_branch, @Field(Constant.BANK_ACCOUNT) String bank_account, @Field(Constant.BANK_ACCOUNT_NAME) String bank_account_name, @Field(Constant.BANKFIRM) String bankfirm
            , @Field(Constant.ACCOUNT) String account, @Field(Constant.PASSWORD) String password, @Field(Constant.PIC_1) String pic_1, @Field(Constant.PIC_2) String pic_2, @Field(Constant.PIC_3) String pic_3, @Field(Constant.PIC_4) String pic_4, @Field(Constant.PIC_5) String pic_5);

    @GET("index.php?g=Api&m=Api&a=getImgCode")
    Flowable<ResponseBody> QueryQcode(@Query("mobile") String mobile);

    @FormUrlEncoded
    @POST("sdk/SMS?cmd=send")
    Flowable<ResponseBody> QueryCode(@FieldMap(encoded = true) Map<String, String> params);


    @FormUrlEncoded
    @POST("rest/160601/ocr/ocr_idcard.json")
    Flowable<ResponseBody> QueryOCRInfo(@Field("inputs") String inputs);

    @FormUrlEncoded
    @POST("index.php?g=Api&m=Public&a=login")
    Flowable<HeoCodeResponse> QueryLogin(@Field("aid") String aid, @Field("sign") String sign,
                                         @Field("username") String username, @Field("password") String password);
    @FormUrlEncoded
    @POST("index.php?g=Api&m=Merchant&a=getInfo")
    Flowable<HeoCodeResponse> QueryMerchantID(@Field("aid") String aid, @Field("sign") String sign,
                                              @Field("merchant_id") String merchant_id);

    @FormUrlEncoded
    @POST("index.php?g=Api&m=Refercode&a=check")
    Flowable<HeoCodeObjResponse> QueryInviteCode(@Field("aid") String aid, @Field("sign") String sign,
                                                 @Field("referral_code") String referral_code);

    @FormUrlEncoded
    @POST("index.php?g=Api&m=Agent&a=usefulcode")
    Flowable<HeoCodeResponse> QueryUserfullInviteCode(@Field("aid") String aid, @Field("sign") String sign,
                                              @Field("agent_id") String agent_id);

    @FormUrlEncoded
    @POST("index.php?g=Api&m=Refercode&a=add")
    Flowable<HeoCodeResponse> QueryObtainInviteCode(@Field("aid") String aid, @Field("sign") String sign,
                                              @Field("agent_id") String agent_id, @Field("num") String num);

    @FormUrlEncoded
    @POST("index.php?g=Api&m=Refercode&a=allot")
    Flowable<HeoCodeResponse> QueryAssignInviteCode(@Field("aid") String aid, @Field("sign") String sign,
                                                    @Field("up_agent_id") String up_agent_id, @Field("agent_id") String agent_id, @Field("refercodes") String refercodes);

    @FormUrlEncoded
    @POST("index.php?g=Api&m=Agent&a=add")
    Flowable<HeoCodeResponse> QueryAddAgent(@Field("aid") String aid, @Field("sign") String sign,
                                            @Field("parent_id") String parent_id, @Field("phone") String phone, @Field("agent_name")String agent_name, @Field("contact") String contact,
     @Field("province") String province, @Field("city") String city, @Field("rate") String rate, @Field("bank") String bank, @Field("sub_branch") String sub_branch,
                                            @Field("bank_account") String bank_account,
                                            @Field("bank_account_name") String bank_account_name, @Field("bankfirm") String bankfirm,
                                            @Field("account") String account, @Field("password") String password);

    @FormUrlEncoded
    @POST("index.php?g=Api&m=Agent&a=getInfo")
    Flowable<HeoCodeResponse> QueryAgentInfo(@Field("aid") String aid, @Field("sign") String sign,
                                                    @Field("phone") String phone);


    @FormUrlEncoded
    @POST("index.php?g=Api&m=Order&a=getInfo")
    Flowable<HeoCodeResponse> QueryMerchantOrder(@Field("aid") String aid, @Field("sign") String sign,
                                                    @Field("merchant_id") String merchant_id,
                                                 @Field("date_start") String date_start, @Field("date_end") String date_end);


    @FormUrlEncoded
    @POST("index.php?g=Api&m=Agent&a=order")
    Flowable<HeoCodeResponse> QueryAgentOrder(@Field("aid") String aid, @Field("sign") String sign,
                                                 @Field("agent_id") String agent_id,
                                                 @Field("date_start") String date_start, @Field("date_end") String date_end);

    @FormUrlEncoded
    @POST("index.php?g=Api&m=Profit&a=index")
    Flowable<HeoCodeResponse> QueryAgentProfit(@Field("aid") String aid, @Field("sign") String sign,
                                              @Field("agent_id") String agent_id,
                                              @Field("date_start") String date_start, @Field("date_end") String date_end);

    @FormUrlEncoded
    @POST("index.php?g=Api&m=Agent&a=withdraw")
    Flowable<HeoCodeResponse> QueryAgentWithDraw(@Field("aid") String aid, @Field("sign") String sign,
                                               @Field("agent_id") String agent_id,
                                               @Field("money") String money);



    @FormUrlEncoded
    @POST("index.php?g=Api&m=Merchant&a=editInfo")
    Flowable<HeoCodeResponse> QueryEditMerchant(@Field("aid") String aid, @Field("sign") String sign,
                                              @Field("merchant_id") String merchant_id,@Field("name") String name,@Field("contact") String contact,
    @Field("phone") String phone,@Field("province") String province,@Field("city") String city,@Field("address") String address,@Field("id_card") String id_card);


    @FormUrlEncoded
    @POST("index.php?g=Api&m=Merchant&a=editBankInfo")
    Flowable<HeoCodeResponse> QueryEditMerchantBank(@Field("aid") String aid, @Field("sign") String sign,
                                                @Field("merchant_id") String merchant_id,@Field("bank") String bank,@Field("sub_branch") String sub_branch,
                                                @Field("bank_account") String bank_account,@Field("bank_account_name") String bank_account_name,@Field("bankfirm") String bankfirm);

    @FormUrlEncoded
    @POST("index.php?g=Api&m=Merchant&a=photo")
    Flowable<HeoCodeResponse> QueryEditMerchantPic(@Field("aid") String aid, @Field("sign") String sign,
                                                @Field("merchant_id") String merchant_id,@Field("pic_1") String pic_1,@Field("pic_2") String pic_2,
                                                @Field("pic_3") String pic_3,@Field("pic_4") String pic_4,@Field("pic_5") String pic_5);


    @FormUrlEncoded
    @POST("index.php?g=Api&m=Profit&a=index")
    Flowable<HeoCodeResponse> QueryProfit(@Field("aid") String aid, @Field("sign") String sign,
                                                @Field("agent_id") String agent_id,
                                                @Field("date_start") String date_start,
                                                @Field("date_end") String date_end);

    @FormUrlEncoded
    @POST("index.php?g=Api&m=Bank&a=main")
    Flowable<HeoCodeResponse> QueryBank(@Field("aid") String aid, @Field("sign") String sign);

    @FormUrlEncoded
    @POST("index.php?g=Api&m=Bank&a=branch")
    Flowable<HeoCodeResponse> QueryBankBranch(@Field("aid") String aid, @Field("sign") String sign, @Field("bankname") String bankname);

    @FormUrlEncoded
    @POST("index.php?g=Api&m=Api&a=newPass")
    Flowable<HeoCodeResponse> QueryForget(@Field("mobile") String mobile, @Field("code") String code, @Field("user_pass") String user_pass);

    @FormUrlEncoded
    @POST("index.php?g=Api&m=Api&a=newPass")
    Flowable<HeoCodeResponse> QueryAlter(@Field("userID") String userID, @Field("TOKEN") String TOKEN, @Field("old_password") String old_password, @Field("new_password") String new_password);

    @FormUrlEncoded
    @POST("index.php?g=Api&m=Api&a=bindEmail")
    Flowable<HeoCodeResponse> QueryEmail(@Field("userID") String userID, @Field("TOKEN") String TOKEN, @Field("email") String email);

    @FormUrlEncoded
    @POST("index.php?g=Api&m=Api&a=realAuth")
    Flowable<HeoCodeResponse> QueryVerify(@Field("userID") String userID, @Field("TOKEN") String TOKEN, @Field("real_name") String real_name, @Field("id_card_num") String id_card_num);

    @GET("index.php?g=Api&m=Api&a=isRCode")
    Flowable<HeoCodeResponse> QueryCodeVerify(@Query("mobile") String mobile, @Query("code") String code);

    @GET("index.php?g=Api&m=Api&a=changeMobile")
    Flowable<HeoCodeResponse> QueryChangePhone(@Query("mobile") String mobile, @Query("code") String code, @Query("useID") String useID, @Query("TOKEN") String TOKEN);

    @GET("index.php?g=Api&m=Pays&a=qrpay")
    Flowable<HeoCodeResponse> QueryPay(@Query("aid") String aid, @Query("sign") String sign, @Query("merchant_id") String merchant_id, @Query("pay_money") String pay_money,
                                       @Query("pay_type") int pay_type, @Query("trade_type") int trade_type);


}
