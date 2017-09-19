/**
 * 
 */

package com.bs.bsims.observer;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-5-14
 * @version 2.0
 */

// 全局观察者对象
public interface BSHtmlObserver {
	// 在其接口中定义一个用来增加观察者的方法
	public void add(Watcher watcher);

	// 再定义一个用来删除观察者权利的方法
	public void remove(Watcher watcher);

	// 再定义一个可以实现行为变现并向观察者传输信息的方法
	public void notifyWatcher(Object content, String str);

	public interface Watcher {
		// 再定义一个用来获取更新信息接收的方法
		public void updateNotify(Object content, String str);

	}

}
