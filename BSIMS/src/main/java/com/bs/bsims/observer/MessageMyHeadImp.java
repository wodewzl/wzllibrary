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
 * @date 2016-5-14
 * @version 2.0
 */
// 头像观察者
public class MessageMyHeadImp implements BSApplictionObserver {
    private static MessageMyHeadImp crmCustomerimp;
    // 定义一个List来封装Watcher
    private List<Watcher> list = new ArrayList<Watcher>();

    public static MessageMyHeadImp getInstance() {
        if (crmCustomerimp != null)
            return crmCustomerimp;
        else{
            crmCustomerimp = new MessageMyHeadImp();
            return crmCustomerimp;
        }
            
         
    }

    @Override
    public void add(Watcher watcher) {
        if(!list.contains(watcher))
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
