package com.bs.bsims.model;

import java.io.Serializable;
import java.util.List;

/** 
 * @author peck
 * @Description:  考勤详情 返回的信息  最内层的数据 数据从最内部的1开始分配数字
 * @date 2015-5-30 下午8:34:38 
 * @email  971371860@qq.com
 * @version V1.0 
 */

public class WorkAttendanceDetailVO3 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 28727233933597080L;
	
//	 "1": {
//        "days": "3",
//        "awork": "-483.9",
//        "list": [
//            {
//                "stime": "2015-05-22 17:00",
//                "etime": "2015-05-29 04:00",
//                "hours": "33.0"
//            }
//        ]
//    },
	
	private String awork = "";
	private List<WorkAttendanceDetailVO1> list;

}
