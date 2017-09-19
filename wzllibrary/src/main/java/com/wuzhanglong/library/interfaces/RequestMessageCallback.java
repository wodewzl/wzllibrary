package com.wuzhanglong.library.interfaces;

import com.wuzhanglong.library.mode.BaseVO;

public interface RequestMessageCallback {

    public abstract void sendMessage(BaseVO vo);
}
