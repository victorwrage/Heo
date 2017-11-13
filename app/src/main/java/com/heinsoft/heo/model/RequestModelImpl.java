package com.heinsoft.heo.model;


import com.heinsoft.heo.bean.HeoCodeObjResponse;
import com.heinsoft.heo.bean.HeoCodeResponse;
import com.heinsoft.heo.util.Constant;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.Query;

/**
 * Info:接口实现类
 * Created by xiaoyl
 * 创建时间:2017/4/7 9:42
 */

public class RequestModelImpl implements IRequestMode {
    IRequestMode iRequestMode;


    @Override
    public Flowable<HeoCodeResponse> QueryRegister(@Field(Constant.AID_STR) String aid, @Field(Constant.SIGN) String sign, @Field(Constant.OPENID) String openid, @Field(Constant.REFERRAL_CODE) String referral_code, @Field(Constant.NAME) String name, @Field(Constant.RATE) String rate, @Field(Constant.CONTACT) String contact, @Field(Constant.PHONE) String phone, @Field(Constant.PROVINCE) String province, @Field(Constant.CITY) String city, @Field(Constant.ADDRESS) String address, @Field(Constant.ID_CARD) String id_card, @Field(Constant.BANK) String bank, @Field(Constant.SUB_BRANCH) String sub_branch, @Field(Constant.BANK_ACCOUNT) String bank_account, @Field(Constant.BANK_ACCOUNT_NAME) String bank_account_name, @Field(Constant.BANKFIRM) String bankfirm, @Field(Constant.ACCOUNT) String account, @Field(Constant.PASSWORD) String password, @Field(Constant.PIC_1) String pic_1, @Field(Constant.PIC_2) String pic_2, @Field(Constant.PIC_3) String pic_3, @Field(Constant.PIC_4) String pic_4, @Field(Constant.PIC_5) String pic_5) {
        return iRequestMode.QueryRegister(aid, sign, openid, referral_code, name, rate, contact, phone, province, city, address, id_card, bank, sub_branch, bank_account, bank_account_name, bankfirm, account, password, pic_1, pic_2, pic_3, pic_4, pic_5);
    }

    @Override
    public Flowable<ResponseBody> QueryQcode(@Query("mobile") String mobile) {
        return iRequestMode.QueryQcode(mobile);
    }

    @Override
    public Flowable<ResponseBody> QueryMessage() {
        return iRequestMode.QueryMessage();
    }

    @Override
    public Flowable<ResponseBody> QueryCode(@FieldMap(encoded = true) Map<String, String> params) {
        return iRequestMode.QueryCode(params);
    }

    @Override
    public Flowable<ResponseBody> QueryOCRInfo(@Field("inputs") String inputs) {
        return iRequestMode.QueryOCRInfo(inputs);
    }


    @Override
    public Flowable<HeoCodeResponse> QueryLogin(@Field("aid") String aid, @Field("sign") String sign, @Field("username") String username, @Field("password") String password) {
        return iRequestMode.QueryLogin(aid, sign, username, password);
    }

    @Override
    public Flowable<HeoCodeResponse> QueryMerchantID(@Field("aid") String aid, @Field("sign") String sign, @Field("merchant_id") String merchant_id) {
        return iRequestMode.QueryMerchantID(aid, sign, merchant_id);
    }

    @Override
    public Flowable<HeoCodeObjResponse> QueryInviteCode(@Field("aid") String aid, @Field("sign") String sign, @Field("referral_code") String referral_code) {
        return iRequestMode.QueryInviteCode(aid, sign, referral_code);
    }

    @Override
    public Flowable<HeoCodeResponse> QueryUserfullInviteCode(@Field("aid") String aid, @Field("sign") String sign, @Field("agent_id") String agent_id) {
        return iRequestMode.QueryUserfullInviteCode(aid, sign, agent_id);
    }

    @Override
    public Flowable<HeoCodeResponse> QueryObtainInviteCode(@Field("aid") String aid, @Field("sign") String sign, @Field("agent_id") String agent_id, @Field("num") String num) {
        return iRequestMode.QueryObtainInviteCode(aid, sign, agent_id, num);
    }

    @Override
    public Flowable<HeoCodeResponse> QueryAssignInviteCode(@Field("aid") String aid, @Field("sign") String sign, @Field("up_agent_id") String up_agent_id, @Field("agent_id") String agent_id, @Field("refercodes") String refercodes) {
        return iRequestMode.QueryAssignInviteCode(aid, sign, up_agent_id, agent_id, refercodes);
    }

    @Override
    public Flowable<HeoCodeResponse> QueryAddAgent(@Field("aid") String aid, @Field("sign") String sign, @Field("parent_id") String parent_id, @Field("phone") String phone, @Field("agent_name") String agent_name, @Field("contact") String contact, @Field("province") String province, @Field("city") String city, @Field("rate") String rate, @Field("bank") String bank, @Field("sub_branch") String sub_branch, @Field("bank_account") String bank_account, @Field("bank_account_name") String bank_account_name, @Field("bankfirm") String bankfirm, @Field("account") String account, @Field("password") String password) {
        return iRequestMode.QueryAddAgent(aid, sign, parent_id, phone,agent_name, contact, province, city, rate, bank, sub_branch, bank_account, bank_account_name, bankfirm, account, password);
    }

    @Override
    public Flowable<HeoCodeResponse> QueryAgentInfo(@Field("aid") String aid, @Field("sign") String sign, @Field("phone") String phone) {
        return iRequestMode.QueryAgentInfo(aid, sign, phone);
    }

    @Override
    public Flowable<HeoCodeResponse> QueryMerchantOrder(@Field("aid") String aid, @Field("sign") String sign, @Field("merchant_id") String merchant_id,  @Field("date_start") String date_start, @Field("date_end") String date_end) {
        return iRequestMode.QueryMerchantOrder(aid, sign, merchant_id, date_start, date_end);
    }

   /* @Override
    public Flowable<ResponseBody> QuerySettle(@Field("aid") String aid, @Field("sign") String sign, @Field("merchant_id") String merchant_id, @Field("person_type") String person_type, @Field("phone") String phone, @Field("email") String email, @Field("name") String name, @Field("inst") String inst, @Field("address") String address, @Nullable @Field( "bus_lic") String bus_lic, @Nullable @Field( "sto_pic") String sto_pic, @Field( "idcard_face") String idcard_face, @Field( "idcard_back") String idcard_back, @Field( "bank_card_face") String bank_card_face, @Field( "bank_card_back") String bank_card_back, @Field("truename") String truename, @Field("idcard_no") String idcard_no, @Field("bank_card_no") String bank_card_no, @Field("bank_card_name") String bank_card_name, @Field("bank_card_type") String bank_card_type, @Field("bank_name") String bank_name, @Field("bank_branch") String bank_branch, @Field("bankfirm") String bankfirm) {
        return iRequestMode.QuerySettle(aid, sign, merchant_id, person_type, phone, email, name, inst, address, bus_lic, sto_pic, idcard_face, idcard_back, bank_card_face, bank_card_back, truename, idcard_no, bank_card_no, bank_card_name, bank_card_type, bank_name, bank_branch, bankfirm);
    }*/

    @Override
    public Flowable<HeoCodeResponse> QuerySettle(@FieldMap HashMap<String,String> request){
        return iRequestMode.QuerySettle(request);
    }



    @Override
    public Flowable<HeoCodeResponse> QuerySettleInfo(@Field("aid") String aid, @Field("sign") String sign, @Field("merchant_id") String merchant_id) {
        return iRequestMode.QuerySettleInfo(aid, sign, merchant_id);
    }

    @Override
    public Flowable<HeoCodeResponse> QueryAgentOrder(@Field("aid") String aid, @Field("sign") String sign, @Field("agent_id") String agent_id, @Field("date_start") String date_start, @Field("date_end") String date_end) {
        return iRequestMode.QueryAgentOrder(aid, sign, agent_id, date_start, date_end);
    }

    @Override
    public Flowable<HeoCodeResponse> QueryAgentProfit(@Field("aid") String aid, @Field("sign") String sign, @Field("agent_id") String agent_id, @Field("date_start") String date_start, @Field("date_end") String date_end) {
        return iRequestMode.QueryAgentProfit(aid, sign, agent_id, date_start, date_end);
    }

    @Override
    public Flowable<HeoCodeResponse> QueryAgentWithDraw(@Field("aid") String aid, @Field("sign") String sign, @Field("agent_id") String agent_id, @Field("money") String money) {
        return iRequestMode.QueryAgentWithDraw(aid, sign, agent_id, money);
    }

    @Override
    public Flowable<HeoCodeResponse> QueryEditMerchant(@Field("aid") String aid, @Field("sign") String sign, @Field("merchant_id") String merchant_id, @Field("name") String name, @Field("contact") String contact, @Field("phone") String phone, @Field("province") String province, @Field("city") String city, @Field("address") String address, @Field("id_card") String id_card) {
        return iRequestMode.QueryEditMerchant(aid, sign, merchant_id, name, contact, phone, province, city, address, id_card);
    }

    @Override
    public Flowable<HeoCodeResponse> QueryEditMerchantBank(@Field("aid") String aid, @Field("sign") String sign, @Field("merchant_id") String merchant_id, @Field("bank") String bank, @Field("sub_branch") String sub_branch, @Field("bank_account") String bank_account, @Field("bank_account_name") String bank_account_name, @Field("bankfirm") String bankfirm) {
        return iRequestMode.QueryEditMerchantBank(aid, sign, merchant_id, bank, sub_branch, bank_account, bank_account_name, bankfirm);
    }

    @Override
    public Flowable<HeoCodeResponse> QueryEditMerchantPic(@Field("aid") String aid, @Field("sign") String sign, @Field("merchant_id") String merchant_id, @Field("pic_1") String pic_1, @Field("pic_2") String pic_2, @Field("pic_3") String pic_3, @Field("pic_4") String pic_4, @Field("pic_5") String pic_5) {
        return iRequestMode.QueryEditMerchantPic(aid, sign, merchant_id, pic_1, pic_2, pic_3, pic_4, pic_5);
    }

    @Override
    public Flowable<HeoCodeResponse> QueryProfit(@Field("aid") String aid, @Field("sign") String sign, @Field("agent_id") String agent_id, @Field("date_start") String date_start, @Field("date_end") String date_end) {
        return iRequestMode.QueryProfit(aid, sign, agent_id, date_start, date_end);
    }

    @Override
    public Flowable<HeoCodeResponse> QueryBank(@Field("aid") String aid, @Field("sign") String sign) {
        return iRequestMode.QueryBank(aid, sign);
    }

    @Override
    public Flowable<HeoCodeResponse> QueryBankBranch(@Field("aid") String aid, @Field("sign") String sign, @Field("bankname") String bankname) {
        return iRequestMode.QueryBankBranch(aid, sign, bankname);
    }


    @Override
    public Flowable<HeoCodeResponse> QueryForget(@Field("mobile") String mobile, @Field("code") String code, @Field("user_pass") String user_pass) {
        return iRequestMode.QueryForget(mobile, code, user_pass);
    }

    @Override
    public Flowable<HeoCodeResponse> QueryAlter(@Field("userID") String userID, @Field("TOKEN") String TOKEN, @Field("old_password") String old_password, @Field("new_password") String new_password) {
        return iRequestMode.QueryAlter(userID, TOKEN, old_password,new_password);
    }

    @Override
    public Flowable<HeoCodeResponse> QueryEmail(@Field("userID") String userID, @Field("TOKEN") String TOKEN, @Field("email") String email) {
        return iRequestMode.QueryEmail(userID,TOKEN,email);
    }

    @Override
    public Flowable<HeoCodeResponse> QueryVerify(@Field("userID") String userID, @Field("TOKEN") String TOKEN, @Field("real_name") String real_name, @Field("id_card_num") String id_card_num) {
        return iRequestMode.QueryVerify(userID, TOKEN, real_name, id_card_num);
    }

    @Override
    public Flowable<HeoCodeResponse> QueryCodeVerify(@Query("mobile") String mobile, @Query("code") String code) {
        return iRequestMode.QueryCodeVerify(mobile, code);
    }

    @Override
    public Flowable<HeoCodeResponse> QueryChangePhone(@Query("mobile") String mobile, @Query("code") String code, @Query("useID") String useID, @Query("TOKEN") String TOKEN) {
        return iRequestMode.QueryChangePhone(mobile, code, useID, TOKEN);
    }

    @Override
    public Flowable<HeoCodeResponse> QueryPay(@Query("aid") String aid, @Query("sign") String sign, @Query("merchant_id") String merchant_id, @Query("pay_money") String pay_money, @Query("pay_type") int pay_type, @Query("trade_type") int trade_type) {
        return iRequestMode.QueryPay(aid, sign, merchant_id, pay_money, pay_type, trade_type);
    }

    @Override
    public Flowable<ResponseBody> QueryScoreQuickPay(@Query("aid") String aid, @Query("sign") String sign, @Query("merchant_id") String merchant_id, @Query("pay_money") String pay_money, @Query("bank_account") String bank_account, @Query("phone") String phone, @Query("true_name") String true_name, @Query("id_card") String id_card, @Query("cvv2") String cvv2, @Query("indate") String indate, @Query("trantp") String trantp) {
        return iRequestMode.QueryScoreQuickPay(aid, sign, merchant_id, pay_money, bank_account, phone, true_name, id_card, cvv2, indate, trantp);
    }

    @Override
    public Flowable<ResponseBody> QueryScoreQuickPayConfirm(@Query("aid") String aid, @Query("sign") String sign, @Query("account") String account, @Query("system_orderId") String system_orderId, @Query("orderId") String orderId, @Query("smsCode") String smsCode, @Query("bank_account") String bank_account, @Query("pay_money") String pay_money, @Query("trantp") String trantp,@Query("extact") String extact) {
        return iRequestMode.QueryScoreQuickPayConfirm(aid, sign, account, system_orderId, orderId, smsCode, bank_account,pay_money,trantp,extact );
    }

    @Override
    public Flowable<ResponseBody> QueryBankName(@Query("aid") String aid, @Query("sign") String sign) {
        return iRequestMode.QueryBankName(aid,sign);
    }

    @Override
    public Flowable<ResponseBody> QueryAddCard(@Query("aid") String aid, @Query("sign") String sign, @Query("merchant_id") String merchant_id, @Query("truename") String truename, @Query("id_card") String id_card, @Query("card_account") String card_account, @Query("card_type") int card_type, @Query("bank") String bank, @Query("phone") String phone, @Query("indate") String indate, @Query("cvv2") String cvv2) {
        return iRequestMode.QueryAddCard(aid, sign, merchant_id, truename, id_card, card_account, card_type, bank, phone, indate, cvv2);
    }

    @Override
    public Flowable<ResponseBody> QueryCardPackage(@Query("aid") String aid, @Query("sign") String sign, @Query("merchant_id") String merchant_id) {
        return iRequestMode.QueryCardPackage(aid, sign, merchant_id);
    }

    @Override
    public Flowable<ResponseBody> QueryQuickPay(@Query("aid") String aid, @Query("sign") String sign, @Query("merchant_id") String merchant_id, @Query("pay_money") String pay_money, @Query("bank_account") String bank_account, @Query("mobile") String mobile, @Query("name") String name, @Query("id_card") String id_card, @Query("cvv2") String cvv2, @Query("vd") String vd, @Query("trantp") String trantp) {
        return iRequestMode.QueryQuickPay(aid,sign,merchant_id,pay_money,bank_account,mobile,name,id_card,cvv2,vd,trantp);
    }

    @Override
    public Flowable<ResponseBody> QueryOpenCredit(@Query("aid") String aid, @Query("sign") String sign, @Query("bank_account") String bank_account, @Query("trantp") String trantp, @Query("merchant_id") String merchant_id) {
        return iRequestMode.QueryOpenCredit(aid, sign, bank_account, trantp, merchant_id);
    }

    @Override
    public Flowable<ResponseBody> QueryQuickPayConfirm(@Query("aid") String aid, @Query("sign") String sign, @Query("merchant_id") String merchant_id, @Query("order_id") String order_id, @Query("orderNo") String orderNo, @Query("smCode") String smCode) {
        return iRequestMode.QueryQuickPayConfirm(aid,sign,merchant_id,order_id,orderNo,smCode);
    }

    @Override
    public Flowable<HeoCodeResponse> QueryResetPassword(@Field("aid") String aid, @Field("sign") String sign, @Field("account") String account, @Field("newpass") String newpass) {
        return iRequestMode.QueryResetPassword(aid, sign, account, newpass);
    }

}
