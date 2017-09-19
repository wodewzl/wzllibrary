package com.bs.bsims.receiver;

public class TaskActionReceiver {
	public static final String intentFilter = "TaskActionReceiverFilter";
	/**
	 * 查看者是否有编辑任务结束时间权限（1有0无）
	 */
	public static final String CHANGEDATE = "TaskChangeDate";
	
	/**
	 * 初审的状态改变之后
	 */
	public static final String CHANGEPRELIMINARYSTATUS = "TaskChangePreliminaryStatus";
}
