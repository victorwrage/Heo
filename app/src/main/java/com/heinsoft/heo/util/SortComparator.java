package com.heinsoft.heo.util;

import com.heinsoft.heo.bean.HeoMerchantInfoResponse;
import com.socks.library.KLog;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Locale;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/4/27 9:37
 */

public class SortComparator implements Comparator<Object> {


    public SortComparator() {

    }

    @Override
    public int compare(Object o1, Object o2) {
        HeoMerchantInfoResponse oo1 = (HeoMerchantInfoResponse) o1;
        HeoMerchantInfoResponse oo2 = (HeoMerchantInfoResponse) o2;
        if (oo1.getCreate_name() != null && oo2.getCreate_time() != null) {
            if (ValidateFormat(oo1.getCreate_time()) >= ValidateFormat(oo2.getCreate_time())) {
                return -1;
            }
        }
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    private long ValidateFormat(String date){

        long result = -1L;
        // 指定日期格式
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2015/02/29会被接受，并转换成2015/03/01
            format.setLenient(false);
            result = format.parse(date).getTime();
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            KLog.v("ValidateFormat error");
        }
        return result;
    }
}
