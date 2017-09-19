package com.bs.bsims.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.bs.bsims.model.LoginUser;

public class UserManager {
	
	/** JPush的alias */
	public static final String JPUSH_ALIAS = "fjpalias";
	
	/** JPush的tags */
	public static final String JPUSH_TAGS = "fjptags";
	
	/** 应用程序标示 */
	public static final String TOKEN = "ftoken";
	
	/** 用户id */
	public static final String USER_ID = "userid";
	
	/** 用户名 */
	public static final String USER_NAME = "username";
	
	/** 用户头像 */
	public static final String HEADPIC = "headpic";
	
	/** 用户全名 */
	public static final String FULL_NAME = "fullname";
	
	/** 用户全名首字母 */
	public static final String INITIAL = "initial";
	
	/** 用户生日 */
	public static final String BIRTHDAY = "birthday";
	
	/** 电话 */
	public static final String TEL = "tel";
	
	/** QQ */
	public static final String QQ = "qq";
	
	/** Email*/
	public static final String EMAIL = "email";
	
	/** 地址 */
	public static final String ADDRESS = "address";
	
	/** 部门ID */
	public static final String DID = "did";
	
	/** 职位ID */
	public static final String PID = "pid";
	
	/** 最后登陆时间 */
	public static final String LAST_LOGIN_TIME = "lastlogintime";
	
	/** 最后登陆IP */
	public static final String LAST_LOGIN_IP = "lastloginip";
	
	/** 现在登陆时间 */
	public static final String NOW_LOGIN_TIME = "nowlogintime";
	
	/** 现在登陆IP */
	public static final String NOW_LOGIN_IP = "nowloginip";
	
	/** 关联环信用户名 */
	public static final String HX_UNAME = "hxuname";
	
	/**关联环信用户密码 */
	public static final String HX_UPASSWORD = "hxupassword";
	
	/** 用户注册时间 */
	public static final String ADD_TIME = "addtime";
	
	/** 职位名称 */
	public static final String D_NAME = "dname";
	
	/** 部门名称 */
	public static final String P_NAME = "pname";
	
	/** 员工档案权限   1,0 */
	public static final String ARCHIVESPURVIEW = "archivespurview";
	
	/**人事统计权限   1,0 */
	public static final String STATISTICSPURVIEW = "statisticspurview";
	
	
	public static final void saveLoginInfo(SharedPreferences preferences, LoginUser loginUser) {
		Editor editor = preferences.edit();
		editor.putString(UserManager.USER_ID, loginUser.getUserid());
		editor.putString(UserManager.USER_NAME, loginUser.getUsername());
		editor.putString(UserManager.HEADPIC, loginUser.getHeadpic());
		editor.putString(UserManager.FULL_NAME, loginUser.getFullname());
		editor.putString(UserManager.INITIAL, loginUser.getInitial());
		editor.putString(UserManager.BIRTHDAY, loginUser.getBirthday());
		editor.putString(UserManager.TEL, loginUser.getTel());
		editor.putString(UserManager.QQ, loginUser.getQq());
		editor.putString(UserManager.EMAIL, loginUser.getEmail());
		editor.putString(UserManager.ADDRESS, loginUser.getAddress());
		editor.putString(UserManager.DID, loginUser.getDid());
		editor.putString(UserManager.PID, loginUser.getPid());
		editor.putString(UserManager.LAST_LOGIN_TIME, loginUser.getLastlogintime());
		editor.putString(UserManager.LAST_LOGIN_IP, loginUser.getLastloginip());
		editor.putString(UserManager.NOW_LOGIN_TIME, loginUser.getNowlogintime());
		editor.putString(UserManager.NOW_LOGIN_IP, loginUser.getNowloginip());
		editor.putString(UserManager.HX_UNAME, loginUser.getHxuname());
		editor.putString(UserManager.HX_UPASSWORD, loginUser.getHxupassword());
		editor.putString(UserManager.ADD_TIME, loginUser.getAddtime());
		editor.putString(UserManager.D_NAME, loginUser.getDname());
		editor.putString(UserManager.P_NAME, loginUser.getPname());
		editor.putString(UserManager.TOKEN, loginUser.getFtoken());
		editor.putString(UserManager.JPUSH_TAGS, loginUser.getFjptags());
		editor.putString(UserManager.JPUSH_ALIAS, loginUser.getJpalias());
		editor.putString(UserManager.ARCHIVESPURVIEW, loginUser.getArchivespurview());
		editor.putString(UserManager.STATISTICSPURVIEW, loginUser.getStatisticspurview());
		
		editor.commit();
	}
	
	
	
	
	
	public static final LoginUser getLoginUser(SharedPreferences preferences) {
		LoginUser loginUser = new LoginUser();
		
		loginUser.setUserid(preferences.getString(UserManager.USER_ID, ""));
		loginUser.setUsername(preferences.getString(UserManager.USER_NAME, ""));
		loginUser.setHeadpic(preferences.getString(UserManager.HEADPIC, ""));
		loginUser.setFullname(preferences.getString(UserManager.FULL_NAME, ""));
		loginUser.setInitial(preferences.getString(UserManager.INITIAL, ""));
		loginUser.setBirthday(preferences.getString(UserManager.BIRTHDAY, ""));
		loginUser.setTel(preferences.getString(UserManager.TEL, ""));
		loginUser.setQq(preferences.getString(UserManager.QQ, ""));
		loginUser.setEmail(preferences.getString(UserManager.EMAIL, ""));
		loginUser.setAddress(preferences.getString(UserManager.ADDRESS, ""));
		loginUser.setDid(preferences.getString(UserManager.DID, ""));
		loginUser.setPid(preferences.getString(UserManager.PID, ""));
		loginUser.setLastlogintime(preferences.getString(UserManager.LAST_LOGIN_TIME, ""));
		loginUser.setLastloginip(preferences.getString(UserManager.LAST_LOGIN_IP, ""));
		loginUser.setNowlogintime(preferences.getString(UserManager.NOW_LOGIN_TIME, ""));
		loginUser.setNowloginip(preferences.getString(UserManager.NOW_LOGIN_IP, ""));
		loginUser.setHxuname(preferences.getString(UserManager.HX_UNAME, ""));
		loginUser.setHxupassword(preferences.getString(UserManager.HX_UPASSWORD, ""));
		loginUser.setAddtime(preferences.getString(UserManager.ADD_TIME, ""));
		loginUser.setDname(preferences.getString(UserManager.D_NAME, ""));
		loginUser.setPname(preferences.getString(UserManager.P_NAME, ""));
		loginUser.setFtoken(preferences.getString(UserManager.TOKEN, ""));
		loginUser.setFjptags(preferences.getString(UserManager.JPUSH_TAGS, ""));
		loginUser.setJpalias(preferences.getString(UserManager.JPUSH_ALIAS, ""));
		loginUser.setArchivespurview(preferences.getString(UserManager.ARCHIVESPURVIEW, "0"));
		loginUser.setStatisticspurview(preferences.getString(UserManager.STATISTICSPURVIEW, "0"));
		return loginUser;
	}
	

	
	
	
	
	public static final void saveUser4reflect(SharedPreferences preferences, LoginUser loginUser) {
		Editor edit = preferences.edit();
		
		Class<? extends LoginUser> clazz = loginUser.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			String fieldName = field.getName();
			String firstLetter = fieldName.substring(0,1).toUpperCase(); 	//获得字段第一个字母大写 
			String getMethodName = "get" + firstLetter + fieldName.substring(1); //转换成字段的get方法 
			
			try {
				Method getMethod = clazz.getMethod(getMethodName, new Class[] {});
				Object methodResult = getMethod.invoke(loginUser, new Object[] {}); //这个对象字段get方法的值 
				//System.out.println(getMethod.getName() + " .... " + methodResult);
				
				//获取字段的定义类型
				/* 字段全部是String类型，所以不用区分
				Type genericType = field.getGenericType();
				if(genericType == Integer.TYPE) 
					edit.putInt(fieldName, Integer.parseInt(methodResult+""));
				else 
					edit.putString(fieldName, methodResult.toString());
				*/
				
				edit.putString(fieldName, methodResult.toString());
				
				edit.commit();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} 
		}
		
	}




	public static final LoginUser getLoginUser4reflect(SharedPreferences preferences) {
		LoginUser loginUser = new LoginUser();
		
		Class<? extends LoginUser> clazz = loginUser.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			
			field.setAccessible(true);
			String fieldName = field.getName();
			String fieldValue = preferences.getString(fieldName, "");
			
			String firstLetter = fieldName.substring(0, 1).toUpperCase(); 	//获得字段第一个字母大写 
			String setMethodName = "set" + firstLetter + fieldName.substring(1); //转换成字段的set方法 
			//System.out.println(setMethodName + "()");
			try {
				Method setMethod = clazz.getMethod(setMethodName, new Class[]{String.class} );
				Object methodResult = setMethod.invoke(loginUser, new Object[]{fieldValue} ); //这个对象字段set方法的值 
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			
		}
		return loginUser;
	}
	
	
	
}
