package com.bs.bsims.jpush;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class ScreenService extends Service {

	private String screenOffAction = "android.intent.action.SCREEN_OFF";
	private String screenOnAction = "android.intent.action.SCREEN_ON";
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		
		//屏幕广播
		IntentFilter filterOn = new IntentFilter(screenOnAction);
		registerReceiver(screenOnReceiver, filterOn);
		
		//屏幕广播
		IntentFilter filterOff = new IntentFilter(screenOffAction);
		registerReceiver(screenOffReceiver, filterOff);
		
		
		return START_REDELIVER_INTENT;
	}
	
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		unregisterReceiver(screenOnReceiver);
		unregisterReceiver(screenOffReceiver);
	}
	
	
	private BroadcastReceiver screenOnReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("屏幕打开");
		}
	};
	
	
	private BroadcastReceiver screenOffReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("屏幕关闭");
		}
	};
	
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	
	
}
