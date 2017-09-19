package com.beisheng.synews.mode;

import android.content.Context;
import android.database.SQLException;

import com.beisheng.synews.application.AppApplication;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ChannelManage {
	public static ChannelManage channelManage;
	private Context mContext;
	/**
	 * 默认的用户选择频道列表
	 */
	public static List<ChannelItem.ListvBean.ListBeanX> defaultUserChannels;
	/**
	 * 默认的其他频道列表
	 */
	public static List<ChannelItem.ListvBean.ListBeanX> defaultOtherChannels;
	/**
	 * 默认的城市频道列表
	 */
	public static List<ChannelItem.ListvBean.ListBeanX> defaultCityChannels;
	/**
	 * 默认的部门频道列表
	 */
	public static List<ChannelItem.ListvBean.ListBeanX> defaultBranchChannels;

	// 生活列表
	public static List<CommunityVO> communityvos;
	// 生活列表
	public static List<CommunityVO> communityvosDef;
	/** 判断数据库中是否存在用户数据 */
	private boolean userExist = false;

	/**
	 * 初始化频道管理类
	 * 
	 * @param
	 * @throws SQLException
	 */
	public static ChannelManage getManage() throws SQLException {
		if (channelManage == null)
			synchronized (ChannelManage.class) {
				if (channelManage == null) {
					channelManage = new ChannelManage();
				}
			}
		return channelManage;
	}

	public static ChannelManage getManage(Context context) throws SQLException {
		if (channelManage == null)
			synchronized (ChannelManage.class) {
				if (channelManage == null) {
					channelManage = new ChannelManage();
				}
			}
		return channelManage;
	}

	public List<ChannelItem.ListvBean.ListBeanX> getUserChannel() {
		try {
			if (defaultUserChannels == null || defaultUserChannels.size() == 0) {
				FileInputStream stream = AppApplication.getInstance().getApplicationContext().openFileInput("data.userchannel");
				ObjectInputStream ois = new ObjectInputStream(stream);
				defaultUserChannels = (List<ChannelItem.ListvBean.ListBeanX>) ois.readObject();
			}

			if (defaultUserChannels == null) {
				return defaultUserChannels = new ArrayList<ChannelItem.ListvBean.ListBeanX>();

			} else {
				return defaultUserChannels;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultUserChannels = new ArrayList<ChannelItem.ListvBean.ListBeanX>();
	}

	public void saveUserChannel(List<ChannelItem.ListvBean.ListBeanX> channelItem) {
		if (channelItem == null) {
			defaultUserChannels = new ArrayList<ChannelItem.ListvBean.ListBeanX>();
		}
		Context context = AppApplication.getInstance().getApplicationContext();
		try {
			FileOutputStream stream = context.openFileOutput("data.userchannel", context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(stream);
			oos.writeObject(channelItem);// td is an Instance of TableData;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<ChannelItem.ListvBean.ListBeanX> getOtherChannel() {
		try {
			if (defaultOtherChannels == null || defaultOtherChannels.size() == 0) {
				FileInputStream stream = AppApplication.getInstance().getApplicationContext().openFileInput("data.otherchannel");
				ObjectInputStream ois = new ObjectInputStream(stream);
				defaultOtherChannels = (List<ChannelItem.ListvBean.ListBeanX>) ois.readObject();
			}

			if (defaultOtherChannels == null) {
				return defaultOtherChannels = new ArrayList<ChannelItem.ListvBean.ListBeanX>();

			} else {
				return defaultOtherChannels;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultOtherChannels = new ArrayList<ChannelItem.ListvBean.ListBeanX>();
	}

	public void saveOtherChannel(List<ChannelItem.ListvBean.ListBeanX> channelItem) {
		Context context = AppApplication.getInstance().getApplicationContext();
		try {
			FileOutputStream stream = context.openFileOutput("data.otherchannel", context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(stream);
			oos.writeObject(channelItem);// td is an Instance of TableData;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<ChannelItem.ListvBean.ListBeanX> getCityChannel() {
		try {
			if (defaultCityChannels == null || defaultCityChannels.size() == 0) {
				FileInputStream stream = AppApplication.getInstance().getApplicationContext().openFileInput("data.citychannel");
				ObjectInputStream ois = new ObjectInputStream(stream);
				defaultCityChannels = (List<ChannelItem.ListvBean.ListBeanX>) ois.readObject();
			}

			if (defaultCityChannels == null) {
				return defaultCityChannels = new ArrayList<ChannelItem.ListvBean.ListBeanX>();

			} else {
				return defaultCityChannels;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// if (defaultOtherChannels == null) {
		return defaultCityChannels = new ArrayList<ChannelItem.ListvBean.ListBeanX>();
		// }
		// return null;
	}

	public void saveCityChannel(List<ChannelItem.ListvBean.ListBeanX> channelItem) {
		Context context = AppApplication.getInstance().getApplicationContext();
		try {
			FileOutputStream stream = context.openFileOutput("data.citychannel", context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(stream);
			oos.writeObject(channelItem);// td is an Instance of TableData;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<ChannelItem.ListvBean.ListBeanX> getBranchChannel() {
		try {
			if (defaultBranchChannels == null || defaultBranchChannels.size() == 0) {
				FileInputStream stream = AppApplication.getInstance().getApplicationContext().openFileInput("data.branchchannel");
				ObjectInputStream ois = new ObjectInputStream(stream);
				defaultBranchChannels = (List<ChannelItem.ListvBean.ListBeanX>) ois.readObject();
			}
			if (defaultBranchChannels == null) {
				return defaultBranchChannels = new ArrayList<ChannelItem.ListvBean.ListBeanX>();

			} else {
				return defaultBranchChannels;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// if (defaultOtherChannels == null) {
		return defaultBranchChannels = new ArrayList<ChannelItem.ListvBean.ListBeanX>();
		// }
		// return null;
	}

	public void saveBranchChannel(List<ChannelItem.ListvBean.ListBeanX> channelItem) {
		Context context = AppApplication.getInstance().getApplicationContext();
		try {
			FileOutputStream stream = context.openFileOutput("data.branchchannel", context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(stream);
			oos.writeObject(channelItem);// td is an Instance of TableData;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<CommunityVO> getCommunity() {
		Context context = AppApplication.getInstance().getApplicationContext();

		try {
			if (communityvos == null || communityvos.size() == 0) {
				FileInputStream stream = context.openFileInput("data.communityvos");
				ObjectInputStream ois = new ObjectInputStream(stream);
				communityvos = (List<CommunityVO>) ois.readObject();
			}
			return communityvos;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return communityvos = new ArrayList<CommunityVO>();
	}

	public void saveCommunity(List<CommunityVO> communityVOs) {
		Context context = AppApplication.getInstance().getApplicationContext();
		try {
			FileOutputStream stream = context.openFileOutput("data.communityvos", context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(stream);
			oos.writeObject(communityVOs);// td is an Instance of TableData;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<CommunityVO> getCommunityDef() {
		Context context = AppApplication.getInstance().getApplicationContext();
		try {
			if (communityvosDef == null || communityvosDef.size() == 0) {
				FileInputStream stream = context.openFileInput("data.communityvosDef");
				ObjectInputStream ois = new ObjectInputStream(stream);
				communityvosDef = (List<CommunityVO>) ois.readObject();
			}
			return communityvosDef;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return communityvosDef = new ArrayList<CommunityVO>();
	}

	public void saveCommunityDef(List<CommunityVO> communityVOs) {
		Context context = AppApplication.getInstance().getApplicationContext();
		try {
			FileOutputStream stream = context.openFileOutput("data.communityvosDef", context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(stream);
			oos.writeObject(communityVOs);// td is an Instance of TableData;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
