package com.bs.bsims.model;

import java.io.Serializable;

/**相关人
 * @author Administrator
 * @Deprecated
 * 使用 @link DetailTell
 */

public class RelativePepoleVO implements Serializable{

	private static final long serialVersionUID = 8439894470494620697L;

	/**uid*/
	private String userid = "";
	
	/**头像*/
	private String headpic = "";
	
	/**名字*/
	private String fullname = "";
	
	/**是否已读*/
	private String isread = "";


	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getHeadpic() {
		return headpic;
	}

	public void setHeadpic(String headpic) {
		this.headpic = headpic;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getIsread() {
		return isread;
	}

	public void setIsread(String isread) {
		this.isread = isread;
	}
	
}
