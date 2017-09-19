package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

/** 
 * @author peck
 * @Description:  
 * @date 2015-5-15 下午12:24:04 
 * @email  971371860@qq.com
 * @version V1.0 
 */
public class UserFromServerInnerMenuVO implements Serializable {
	private static final long serialVersionUID = -353748050455794459L;
	
//	"mname":"日常事务",
//	"mview":"1",
//	"malias":"Daily",
	
	/** 用户ID */
	private String mname = "";
	private String mview = "";
	private String malias = "";
	private List<UserFromServerInnerMenuVO> menu;
	public String getMname() {
		return mname;
	}
	public void setMname(String mname) {
		this.mname = mname;
	}
	public String getMview() {
		return mview;
	}
	public void setMview(String mview) {
		this.mview = mview;
	}
	public String getMalias() {
		return malias;
	}
	public void setMalias(String malias) {
		this.malias = malias;
	}
	public List<UserFromServerInnerMenuVO> getMenu() {
		return menu;
	}
	public void setMenu(List<UserFromServerInnerMenuVO> menu) {
		this.menu = menu;
	}
}
