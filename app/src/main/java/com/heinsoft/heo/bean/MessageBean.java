package com.heinsoft.heo.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity(indexes = {
        @Index(value = "idx, send_time DESC", unique = true)
})
/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/5/13 14:47
 */

public class MessageBean {


    String id;
    @Id
    Long idx;
    String send_name;
    String send_time;
    String title;
    String content;

    Boolean is_read = false;

@Generated(hash = 812675972)
public MessageBean(String id, Long idx, String send_name, String send_time,
        String title, String content, Boolean is_read) {
    this.id = id;
    this.idx = idx;
    this.send_name = send_name;
    this.send_time = send_time;
    this.title = title;
    this.content = content;
    this.is_read = is_read;
}

@Generated(hash = 1588632019)
public MessageBean() {
}

public String getId() {
    return this.id;
}

public void setId(String id) {
    this.id = id;
}

public Long getIdx() {
    return this.idx;
}

public void setIdx(Long idx) {
    this.idx = idx;
}

public String getSend_name() {
    return this.send_name;
}

public void setSend_name(String send_name) {
    this.send_name = send_name;
}

public String getSend_time() {
    return this.send_time;
}

public void setSend_time(String send_time) {
    this.send_time = send_time;
}

public String getTitle() {
    return this.title;
}

public void setTitle(String title) {
    this.title = title;
}

public String getContent() {
    return this.content;
}

public void setContent(String content) {
    this.content = content;
}

public Boolean getIs_read() {
    return this.is_read;
}

public void setIs_read(Boolean is_read) {
    this.is_read = is_read;
}

}
