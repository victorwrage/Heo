package com.heinsoft.heo.present;


import java.util.HashMap;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/4/7 9:46
 */

public interface IRequestPresent {
    void QueryRegister(HashMap<String ,String> params);
    void QueryOCRInfo(String inputs);
    void QueryCode(String mobile, String msgid,  String msg);
    void QueryPay(String aid, String sign,String merchant_id,  String pay_money, int pay_type,int trade_type);
    void QueryMerchant(String aid, String sign,String merchant_id);
    void QueryProfit(String aid, String sign,String agent_id,String date_start,String date_end);

    void QueryEditMerchant( String aid,String sign,
                           String merchant_id, String name,String contact,
                         String phone,String province,String city, String address, String id_card);

    void QueryEditMerchantBank(String aid,String sign,
                              String merchant_id, String bank, String sub_branch,
                               String bank_account, String bank_account_name,String bankfirm);
    void QueryEditMerchantPic(String aid,  String sign,
                             String merchant_id,String pic_1, String pic_2,
                               String pic_3,String pic_4, String pic_5);

    void QueryBank(String aid,String sign);

    void QueryBankBranch(String aid,String sign,String bankname);


    void QueryInviteCode(String aid,String sign ,String referral_code);
    void QueryUsefullInviteCode(String aid,String sign ,String agent_id);
    void QueryObtainInviteCode(String aid,String sign,String agent_id,String num);
    void QueryAssignInviteCode(String aid,String sign,String up_agent_id,String agent_id,String refercodes);

    void QueryAddAgent(String aid,String sign,String parent_id,String phone,String agent_name,String contact,String province,String city,String rate,
                       String  bank,String sub_branch,String bank_account,String bank_account_name,String bankfirm,String account,String password);

    void QueryAgentInfo(String aid,String sign,String phone);

    void QueryAgentOrder(String aid,String sign,String agent_id,String date_start,String date_end);
    void QueryAgentProfit(String aid,String sign,String agent_id,String date_start,String date_end);
    void QueryAgentWithDraw(String aid,String sign,String agent_id,String money);
    void QueryMerchantOrder(String aid,String sign,String merchant_id,String date_start,String date_end);



    void QueryQcode(String mobile);
    void QueryVerify(String mobile);
    void QueryLogin(String aid, String sign,String username, String password);
    void QueryForget(String mobile, String code, String user_pass);
    void QueryAlter(String useID, String TOKEN, String old_password, String new_password);
    void QueryEmail(String useID, String TOKEN, String email);

    void QueryCodeVerify(String mobile, String code);
    void QueryPhoneChange(String mobile, String code, String useID, String TOKEN);


}
