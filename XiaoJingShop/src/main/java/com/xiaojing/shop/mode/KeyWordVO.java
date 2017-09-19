package com.xiaojing.shop.mode;

import com.wuzhanglong.library.mode.BaseVO;

/**
 * Created by ${Wuzhanglong} on 2017/5/3.
 */

public class KeyWordVO extends BaseVO{
    private KeyWordVO datas;
    private String [] list;

    public KeyWordVO getDatas() {
        return datas;
    }

    public void setDatas(KeyWordVO datas) {
        this.datas = datas;
    }

    public String[] getList() {
        return list;
    }

    public void setList(String[] list) {
        this.list = list;
    }
}
