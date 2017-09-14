package com.heinsoft.heo.util;

import com.heinsoft.heo.bean.MessageBean;

import java.util.ArrayList;
import java.util.Map;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/5/13 14:43
 */

public class Constant {

    public static final String URL_BAIBAO = "http://www.o2obaibao.com/bsystem/";//百宝
    public static final String CONSTANT_MERCHANT_ID = "617061910264433";//
    public static final String URL_MESSAGE = "http://202.104.149.61/";//短信
   // public static final String URL_MESSAGE = "http://c.kf10000.com/";//短信
    public static final String PUBLIC_KEY = "z4sgErHeEww5uwhTrjF6kM5qdKjpifQg6bp2iRAumnvRx7GV57laelZmX1e405QD";//公钥
    public static final String AID = "17060518292214";//AID

    public static final String PUBLIC_BMOB_KEY = "c740095be78b8d373d247ceb0eab3142";//BMOB APP KEY

    public static final String PUBLIC_OCR_KEY = "Z4vmVGAxTnVrjF32oVTPXDDd";//baidu ocr api key
    public static final String PUBLIC_OCR_SECRET = "S9ioW1vGA9k4fHIDK43rmAXQZjdiZMcx";//secret


    public static final String MESSAGE_USER_NAME = "5234TSGF2";//账号
    public static final String USER_INFO_REMEMBER = "remember";
 public static final String USER_INFO_AUTO_LOGIN = "auto_login";
 public static  String MESSAGE_PASSWORD = "852741";//密码

    public static  String TOP_AGENT_ID = "495687";//顶级代理商ID

    public static final String MESSAGE_CONTENT_PREFIX = "。如非本人操作,请忽略本短信";//短信内容
    public static  String MESSAGE_UPDATE_TIP = "";//

    public static  String MESSAGE_CODE = "";	//生成的验证码
    public static final int MESSAGE_SEND_SUCCESS = 100;	//成功
    public static final int MESSAGE_SEND_FAIL = MESSAGE_SEND_SUCCESS+1;	//失败
    public static final int MESSAGE_SEND_PW_ERR = MESSAGE_SEND_FAIL+1;	//密码不对
    public static final int MESSAGE_SEND_NUM_ERR = MESSAGE_SEND_PW_ERR+1;	//接收号码格式错误
    public static final int MESSAGE_SEND_CONTENT_ERR = MESSAGE_SEND_NUM_ERR+1;	//敏感内容,会同时返回
    public static final int MESSAGE_SEND_TOO_FREQUENCY = MESSAGE_SEND_CONTENT_ERR+1;	//频率过快
    public static final int MESSAGE_SEND_LIMIT = MESSAGE_SEND_TOO_FREQUENCY+1;	//限制发送


    public static final int DEFAULT_TIMEOUT = 100;//超时时间(S)

    public static final int DEFAULT_MESSAGE_TIMEOUT = 120;//短信验证码超时时间(S)


    public static Map<String,String> user_info;
    public static String photo_path;
    public static int photo_idx;
    public static final String USER_INFO_ID = "user_id";//缓存
    public static final String USER_INFO_MERCHANT_ID = "merchant_id";//缓存
    public static final String USER_INFO_PHONE = "user_phone";//缓存
    public static final String UP_AGENT_ID = "up_agent_id";//缓存
    public static final String REFERCODES = "refercodes";//缓存
    public static final String USER_INFO_TOKEN = "token";//缓存
    public static final String USER_INFO_EMAIL = "user_email";//缓存
    public static final String USER_INFO_IDCARD = "id_card_num";//缓存
    public static final String USER_INFO_NAME = "real_name";//缓存
    public static final String USER_INFO_SAVE = "save_info";//缓存
    public static final String USER_INFO_EMAIL_STATUS = "email_status";//缓存
    public static final String USER_INFO_ISAUTH = "is_auth";//缓存
    public static final String USER_INFO_USER_NAME = "user_name";//缓存
    public static final String USER_INFO_USERNAME = "username";//缓存
    public static final String USER_INFO_SHOPPER_ID = "shopper_id";//缓存
    public static final String USER_INFO_SESSION_ID = "user_session";//缓存
    public static final String USER_INFO_PW = "user_pw";//缓存
    public static final String USER_INFO_PASSWORD = "password";//缓存
    public static final String USER_INFO_INVITE_CODE = "invite_code";//缓存

    public static final int FRAGMENT_CLEAR_DATE = 1000;//重新加载数据

    /**---------------数据提交字段集合----------------- **/
    public static final String MACHINE_ID = "machine_id";


    public static final String SIGN = "sign";
    public static final String AID_STR = "aid";
    public static final String PARENT_ID = "parent_id";
    public static final String MERCHANT_ID = "merchant_id";
    public static final String OPENID = "openid";
    public static final String REFERRAL_CODE = "referral_code";
    public static final String NAME = "name";
    public static final String RATE = "rate";
    public static final String CONTACT = "contact";
    public static final String PHONE = "phone";
    public static final String PROVINCE = "province";
    public static final String CITY = "city";
    public static final String ADDRESS = "address";
    public static final String AGENT_ID = "agent_id";
    public static final String AGENT_NAME = "agent_name";
    public static final String MY_AGENT_ID = "my_agent_id";
    public static final String MONEY = "money";
    public static final String MY_AGENT_NAME = "my_agent_name";
    public static final String PROFIT_BALANCE = "profit_balance";
    public static final String PROFIT_HISTORY = "profit_history";
    public static final String NUM = "num";
    public static final String ID_CARD = "id_card";
    public static final String ID_CARD_NAME = "id_card_name";
    public static final String BANK = "bank";
    public static final String LEVEL_NAME = "level_name";
    public static final String SUB_BRANCH = "sub_branch";
    public static final String BANK_ACCOUNT = "bank_account";
    public static final String BANK_ACCOUNT_NAME = "bank_account_name";
    public static final String BANKFIRM = "bankfirm";
    public static final String ACCOUNT = "account";
    public static final String PASSWORD = "password";
   public static final String NEWPASSWORD = "newpass";
    public static final String PIC_1 = "pic_1";
    public static final String PIC_2 = "pic_2";
    public static final String PIC_3 = "pic_3";
    public static final String PIC_4 = "pic_4";
    public static final String PIC_5 = "pic_5";

   public static ArrayList<MessageBean> message;
   public static String ERRCODE = "errcode";
   public static String ERRMSG = "errmsg";


}
