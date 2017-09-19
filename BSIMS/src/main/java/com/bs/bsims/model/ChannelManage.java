package com.bs.bsims.model;

import java.util.ArrayList;
import java.util.List;

public class ChannelManage {
	public static ChannelManage channelManage;

	public static CrmProductVo mCrmProductVo;
	/**
	 * Ĭ�ϵ��û�ѡ��Ƶ���б�
	 * */
	public static List<ChannelItem> defaultUserChannels;

	/**
	 * Ĭ�ϵ�����Ƶ���б�
	 * */
	public static List<ChannelItem> defaultOtherChannels;

	public ChannelManage(CrmProductVo mCrmProductVo) {
		this.mCrmProductVo = mCrmProductVo;
	}

	public static ArrayList<ChannelItem> getDefaultUserChannels() {
		defaultUserChannels = new ArrayList<ChannelItem>();
		if (null != mCrmProductVo.getInfo().getSelected()) {
			List<CrmProductVo> list = mCrmProductVo.getInfo().getSelected();
			for (int i = 0; i < list.size(); i++) {
				defaultUserChannels.add(new ChannelItem(Integer.parseInt(list.get(i).getId()), list.get(i).getName(), 1));
			}

		}

		return (ArrayList<ChannelItem>) defaultUserChannels;
	}

	public static void setDefaultUserChannels(List<ChannelItem> defaultUserChannels) {
		ChannelManage.defaultUserChannels = defaultUserChannels;
	}

	public static ArrayList<ChannelItem> getDefaultOtherChannels() {
		defaultOtherChannels = new ArrayList<ChannelItem>();
		if (null != mCrmProductVo.getInfo().getUnselected()) {
			List<CrmProductVo> list = mCrmProductVo.getInfo().getUnselected();
			for (int i = 0; i < list.size(); i++) {
				defaultOtherChannels.add(new ChannelItem(Integer.parseInt(list.get(i).getId()), list.get(i).getName(), 0));
			}

		}

		return (ArrayList<ChannelItem>) defaultOtherChannels;
	}

	public static void setDefaultOtherChannels(List<ChannelItem> defaultOtherChannels) {
		ChannelManage.defaultOtherChannels = defaultOtherChannels;
	}

	public CrmProductVo getmCrmProductVo() {
		return mCrmProductVo;
	}

	public void setmCrmProductVo(CrmProductVo mCrmProductVo) {
		this.mCrmProductVo = mCrmProductVo;
	}
}
