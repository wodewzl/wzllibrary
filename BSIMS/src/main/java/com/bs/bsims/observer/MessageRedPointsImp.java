/**
 * 
 */

package com.bs.bsims.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-7-8
 * @version 2.0
 */

// IM聊天接受的消息红点
public class MessageRedPointsImp implements BSApplictionObserver {

    private static MessageRedPointsImp redPotionsimp;
    // 定义一个List来封装Watcher
    private List<Watcher> list = new ArrayList<Watcher>();

    public static MessageRedPointsImp getInstance() {
        if (redPotionsimp != null)
            return redPotionsimp;
        else {
            redPotionsimp = new MessageRedPointsImp();
            return redPotionsimp;
        }

    }

    public void add(Watcher watcher) {
        if (!list.contains(watcher))
            list.add(watcher);
    }

    @Override
    public void remove(Watcher watcher) {
        // TODO Auto-generated method stub
        if (list == null || list.size() < 1)
            return;
        list.remove(watcher);
    }

    @Override
    public void notifyWatcher(Object content) {
        // TODO Auto-generated method stub
        if (list == null || list.size() < 1)
            return;
        for (Watcher watcher : list) {
            watcher.updateNotify(content);
        }
    }

}
