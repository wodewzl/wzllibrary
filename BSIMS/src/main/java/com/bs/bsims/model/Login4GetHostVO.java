package com.bs.bsims.model;

import java.io.Serializable;

import com.bs.bsims.utils.CustomLog;

/**
 * @author peck
 * @Description: 第一次登录成功后返回的array值
 * @date 2015-5-15 上午10:09:23
 * @email 971371860@qq.com
 * @version V1.0
 */
public class Login4GetHostVO implements Serializable {

	private static final long serialVersionUID = -8748828846739650050L;

	public Login4GetHostVO() {
		// TODO Auto-generated constructor stub
		super();
	}

	// "array":{
	// "userid":"49",
	// "isinpost":"1",
	// "islogin":"1",
	// "siteurl":"http:\/\/cp.beisheng.wang\/",
	// "ftoken":"RBDTZXGUMNDKkEwMkZFN0UyMTA1RgO0O0OO0O0O",
	// "return":"true"
	// },
	private Login4GetHostVO array;
	
	/** 用户ID */
	private String userid = "";
	/** 用户ID */
	private String isinpost = "";
	public Login4GetHostVO getArray() {
        return array;
    }

    public void setArray(Login4GetHostVO array) {
        this.array = array;
    }

    /** 用户ID */
	private String islogin = "";
	/** 用户ID */
	private String siteurl = "";
	/** 用户ID */
	private String ftoken = "";

	// /** 用户ID */
	// private String return = "";
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getIsinpost() {
		return isinpost;
	}

	public void setIsinpost(String isinpost) {
		this.isinpost = isinpost;
	}

	public String getIslogin() {
		return islogin;
	}

	public void setIslogin(String islogin) {
		this.islogin = islogin;
	}

	public String getSiteurl() {
		CustomLog.e("Login4GetHostVO 70siteurl", siteurl);
		return siteurl;
	}

	public void setSiteurl(String siteurl) {
		this.siteurl = siteurl+"/";
	}

	public String getFtoken() {
		return ftoken;
	}

	public void setFtoken(String ftoken) {
		this.ftoken = ftoken;
	}

//	@Override
//	public String toString() {
//		// TODO Auto-generated method stub
//		return userid + "\t" + isinpost + "\t" + islogin + "\t" + siteurl
//				+ "\t" + ftoken;
//	}
}
