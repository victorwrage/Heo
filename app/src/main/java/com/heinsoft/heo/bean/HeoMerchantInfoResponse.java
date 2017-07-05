package com.heinsoft.heo.bean;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/5/13 14:47
 */

public class HeoMerchantInfoResponse {
    String errcode;

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getCreate_name() {
        return create_name;
    }

    public void setCreate_name(String create_name) {
        this.create_name = create_name;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getUpdate_name() {
        return update_name;
    }

    public void setUpdate_name(String update_name) {
        this.update_name = update_name;
    }

    public String getTop_agent_id() {
        return top_agent_id;
    }

    public void setTop_agent_id(String top_agent_id) {
        this.top_agent_id = top_agent_id;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getSub_branch() {
        return sub_branch;
    }

    public void setSub_branch(String sub_branch) {
        this.sub_branch = sub_branch;
    }

    public String getBank_account() {
        return bank_account;
    }

    public void setBank_account(String bank_account) {
        this.bank_account = bank_account;
    }

    public String getBank_account_name() {
        return bank_account_name;
    }

    public void setBank_account_name(String bank_account_name) {
        this.bank_account_name = bank_account_name;
    }

    public String getBankfirm() {
        return bankfirm;
    }

    public void setBankfirm(String bankfirm) {
        this.bankfirm = bankfirm;
    }

    public String getSingle() {
        return single;
    }

    public void setSingle(String single) {
        this.single = single;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getSum_day() {
        return sum_day;
    }

    public void setSum_day(String sum_day) {
        this.sum_day = sum_day;
    }

    public String getSum_month() {
        return sum_month;
    }

    public void setSum_month(String sum_month) {
        this.sum_month = sum_month;
    }

    public OCRImages getImages() {
        return images;
    }

    public void setImages(OCRImages images) {
        this.images = images;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    String errmsg;
    String merchant_id;
    String name;
    String rate;
    String contact;//
    String phone;//
    String province;
    String city;//
    String address;//
    String agent_id;//
    String state;//
    String create_time;//
    String create_name;//
    String update_time;//
    String update_name;//
    String top_agent_id;//
    String id_card;//
    String bank;//
    String sub_branch;//
    String bank_account;//
    String bank_account_name;//
    String bankfirm;//
    String single;//
    String day;//
    String month;//
    String sum_day;//
    String sum_month;//
    OCRImages images;//
    String username;//
    String password;//


    public class OCRImages{
        String pic_1;
        String pic_2;
        String pic_3;
        String pic_4;

        public String getPic_1() {
            return pic_1;
        }

        public void setPic_1(String pic_1) {
            this.pic_1 = pic_1;
        }

        public String getPic_2() {
            return pic_2;
        }

        public void setPic_2(String pic_2) {
            this.pic_2 = pic_2;
        }

        public String getPic_3() {
            return pic_3;
        }

        public void setPic_3(String pic_3) {
            this.pic_3 = pic_3;
        }

        public String getPic_4() {
            return pic_4;
        }

        public void setPic_4(String pic_4) {
            this.pic_4 = pic_4;
        }

        public String getPic_5() {
            return pic_5;
        }

        public void setPic_5(String pic_5) {
            this.pic_5 = pic_5;
        }

        String pic_5;
    }

    @Override
    public String toString() {
        return "errcode:"+errcode+"errmsg:"+ errmsg +"merchant_id:"+merchant_id+"name:"+name+"username"+username
                +"rate"+rate+"state"+state+"password"+password +"pic1\n"+ images.pic_1+"pic2\n"+ images.pic_2
                +"pic3\n"+ images.pic_3+"pic4\n"+ images.pic_4;
    }
}
