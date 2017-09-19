
package com.bs.bsims.utils;

import android.content.Context;
import android.os.Handler;

import com.bs.bsims.model.DepartmentAndEmployeeVO;
import com.bs.bsims.model.PDFOutlineElementVO;
import com.bs.bsims.model.ResultVO;

import java.util.ArrayList;
import java.util.List;

public class DepartmentOneUtis {
    private ResultVO mResultVO;
    private Context mContext;
    private Handler mHandler;
    /** 最外层数据 */
    private ArrayList<PDFOutlineElementVO> mPdfOutlinesCount = new ArrayList<PDFOutlineElementVO>();

    /** 内层数据 */
    private ArrayList<PDFOutlineElementVO> mPdfOutlines = new ArrayList<PDFOutlineElementVO>();

    public DepartmentOneUtis(Context context, ResultVO resultVo, Handler handler) {
        this.mContext = context;
        this.mResultVO = resultVo;
        this.mHandler = handler;

        getDataFromServer();
    }

    private void getDataFromServer() {
        List<DepartmentAndEmployeeVO> departmentList = mResultVO.getDepartments();
        if (departmentList != null && departmentList.size() > 0) {
            for (int i = 0; i < departmentList.size(); i++) {
                DepartmentAndEmployeeVO department = departmentList.get(i);
                int level = Integer.parseInt(department.getLevel());
                if ("0".equals(department.getBelong())) {
                    // 获取第一级的部门

                    mPdfOutlinesCount.add(new PDFOutlineElementVO(department.getDepartmentid(), department, false, true, department.getBelong(), level, false, false));
                    mPdfOutlines.add(new PDFOutlineElementVO(department.getDepartmentid(), department, false, true, department.getBelong(), level, false, false));
                } else {
                    mPdfOutlines.add(new PDFOutlineElementVO(department.getDepartmentid(), department, true, true, department.getBelong(), level, false, false));
                }
            }
        }

        // 用户
        List<DepartmentAndEmployeeVO> employeeList = mResultVO.getUsers();
        if (employeeList != null && employeeList.size() > 0) {
            for (int j = 0; j < employeeList.size(); j++) {
                DepartmentAndEmployeeVO employee = employeeList.get(j);
                mPdfOutlines.add(new PDFOutlineElementVO("user", employee, false, false, employee.getDid(), 4, false, false));
            }
        }
        mHandler.sendEmptyMessage(0);

    }
}
