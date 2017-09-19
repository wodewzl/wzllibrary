package com.wuzhanglong.sendmessage;

import com.wuzhanglong.library.mode.BaseVO;

import java.util.List;

/**
 * Created by ${Wuzhanglong} on 2017/8/15.
 */

public class MessageInfo extends BaseVO{
    private MessageInfo mobilelist;
    private String mobile;
    private String content;
    private List<MessageInfo> datas;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public List<MessageInfo> getDatas() {
        return datas;
    }

    public void setDatas(List<MessageInfo> datas) {
        this.datas = datas;
    }

    public MessageInfo getMobilelist() {
        return mobilelist;
    }

    public void setMobilelist(MessageInfo mobilelist) {
        this.mobilelist = mobilelist;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
