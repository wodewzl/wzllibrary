
package com.bs.bsims.interfaces;

import com.bs.bsims.model.PDFOutlineElementVO;

public interface EmployeeOnclickCallback {
    /**
     * 用于选中项的处理
     * 
     * @param position 选中项下标
     * @param viewType 被点击view 1 check 2 see
     */
    void employeeOnclick(int arg2, int viewType, PDFOutlineElementVO vo);
}
