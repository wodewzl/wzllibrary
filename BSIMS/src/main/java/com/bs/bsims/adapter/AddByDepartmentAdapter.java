
package com.bs.bsims.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.interfaces.EmployeeOnclickCallback;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.model.PDFOutlineElementVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class AddByDepartmentAdapter extends ArrayAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    public List<PDFOutlineElementVO> mfilelist;
    private Bitmap mIconCollapse;
    private Bitmap mIconExpand;
    final int TYPE_1 = 1;
    final int TYPE_2 = 2;
    final int TYPE_3 = 3;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    // Activity activity;
    private boolean mShowUser;
    private boolean mShowFirst = false;

    public List<EmployeeVO> checkboxlist = new ArrayList<EmployeeVO>();

    private EmployeeOnclickCallback employeeOnclickCallback;
    private List<PDFOutlineElementVO> mOneList = new ArrayList<PDFOutlineElementVO>();

    public AddByDepartmentAdapter(Context context, EmployeeOnclickCallback activity, int textViewResourceId, List<PDFOutlineElementVO> objects, boolean showUser) {

        super(context, textViewResourceId, objects);
        this.mContext = context;
        this.mfilelist = objects;
        // this.activity = activity;
        this.mIconCollapse = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_contacts_department_fragment_arrow_default);
        this.mIconExpand = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_contacts_department_fragment_arrow_selected);
        this.mShowUser = showUser;
        options = CommonUtils.initImageLoaderOptions();

        try {
            employeeOnclickCallback = activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
            throw new RuntimeException("activity impl employeeOnclickCallback error");
        }

    }

    public AddByDepartmentAdapter(Context context, EmployeeOnclickCallback activity, int textViewResourceId, boolean showUser) {

        super(context, textViewResourceId);
        this.mContext = context;
        // this.activity = activity;
        this.mIconCollapse = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_contacts_department_fragment_arrow_default);
        this.mIconExpand = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_contacts_department_fragment_arrow_selected);
        this.mShowUser = showUser;
        options = CommonUtils.initImageLoaderOptions();

        try {
            employeeOnclickCallback = activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
            throw new RuntimeException("activity impl employeeOnclickCallback error");
        }

    }

    public AddByDepartmentAdapter(Context context, EmployeeOnclickCallback activity, int textViewResourceId, boolean showUser, boolean showFirst) {

        super(context, textViewResourceId);
        this.mContext = context;
        // this.activity = activity;
        this.mIconCollapse = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_contacts_department_fragment_arrow_default);
        this.mIconExpand = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_contacts_department_fragment_arrow_selected);
        this.mShowUser = showUser;
        this.mShowFirst = showFirst;
        options = CommonUtils.initImageLoaderOptions();

        try {
            employeeOnclickCallback = activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
            throw new RuntimeException("activity impl employeeOnclickCallback error");
        }

    }

    public int getCount() {
        return mfilelist.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    // 每个convert view都会调用此方法，获得当前所需要的view样式
    @Override
    public int getItemViewType(int position) {

        PDFOutlineElementVO p = (PDFOutlineElementVO) mfilelist.get(position);
  /*      if (p.getLevel() == 4) {
            return TYPE_3;
        } else {
            return TYPE_2;
        }
*/
        if (p.getLevel() == 3) {
            if (p.getId().equals("user")) {
                return TYPE_3;
            } else {
                return TYPE_2;
            }
        } else if (p.getLevel() == 1) {
            return TYPE_2;
        } else if (p.getLevel() == 2) {
            if (p.getId().equals("user")) {
                return TYPE_3;
            } else {
                return TYPE_2;
            }
        } else {
            return TYPE_3;
        }

    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder1 holder1 = null;
        ViewHolder2 holder2 = null;
        ViewHolder3 holder3 = null;
        int type = getItemViewType(position);

        mInflater = LayoutInflater.from(mContext);
        // 按当前所需的样式，确定new的布局
        switch (type) {
            case TYPE_1:

                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_contacts_department_txt, parent, false);
                    holder1 = new ViewHolder1();
                }
                else if (convertView.getTag() != holder1) {
                    convertView = mInflater.inflate(R.layout.item_contacts_department_txt, parent, false);
                    holder1 = new ViewHolder1();
                }
                else {
                    holder1 = (ViewHolder1) convertView.getTag();
                }
                holder1.txt_contacts_department_type = (TextView) convertView.findViewById(R.id.txt_contacts_department_type);
                convertView.setTag(holder1);
                break;

            case TYPE_2:
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_contacts_many_treeview, null);
                    holder2 = new ViewHolder2();
                }
                else if (convertView.getTag() != holder2) {
                    convertView = mInflater.inflate(R.layout.item_contacts_many_treeview, null);
                    holder2 = new ViewHolder2();
                }
                else {
                    holder2 = (ViewHolder2) convertView.getTag();
                }

                holder2.text = (TextView) convertView.findViewById(R.id.txt_contacts_department_name);
                holder2.icon = (ImageView) convertView.findViewById(R.id.img_contacts_department_arrow);
                holder2.line_check = (LinearLayout) convertView.findViewById(R.id.line_public_many_employee_check);
                holder2.line_public_many_employee_select = (LinearLayout) convertView.findViewById(R.id.line_public_many_employee_select);
                holder2.line_check_right = (LinearLayout) convertView.findViewById(R.id.line_public_many_employee_check_right);
                holder2.line_see = (LinearLayout) convertView.findViewById(R.id.line_contacts_department_see_person);
                // holder2.line_all=(LinearLayout)convertView.findViewById(R.id.line_public_many_employee_all);
                holder2.checkBox_up_check = (CheckBox) convertView.findViewById(R.id.checkBox_up_check);
                holder2.checkBox_up_check_right = (CheckBox) convertView.findViewById(R.id.checkBox_up_check_right);
                holder2.divider = convertView.findViewById(R.id.view_contats_department_person_line);

                if (!mShowUser) {
                    // holder2.icon.setVisibility(View.GONE);
                    holder2.divider.setVisibility(View.GONE);
                }
                convertView.setTag(holder2);
                break;

            case TYPE_3:
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_contacts_department_tree_view, null);
                    holder3 = new ViewHolder3();
                }
                else if (convertView.getTag() != holder2) {
                    convertView = mInflater.inflate(R.layout.item_contacts_department_tree_view, null);
                    holder3 = new ViewHolder3();
                }
                else {
                    holder3 = (ViewHolder3) convertView.getTag();
                }
                holder3.txt_contats_department_person_name = (TextView) convertView.findViewById(R.id.txt_contats_department_person_name);
                holder3.txt_contats_department_person_phone = (TextView) convertView.findViewById(R.id.txt_contats_department_person_phone);
                holder3.txt_contats_department_person_image = (BSCircleImageView) convertView.findViewById(R.id.txt_contats_department_person_image);
                holder3.txt_contats_department_person_image_layout = (LinearLayout) convertView.findViewById(R.id.txt_contats_department_person_image_layout);
                holder3.line_contats_department_person_seeDetail = (LinearLayout) convertView.findViewById(R.id.line_contats_department_person_seeDetail);
                holder3.line_new_check = (LinearLayout) convertView.findViewById(R.id.line_new_check);
                holder3.checkBox_new_check = (CheckBox) convertView.findViewById(R.id.checkBox_new_check);
                holder3.layout3 = (LinearLayout) convertView.findViewById(R.id.layout3);
                convertView.setTag(holder3);
                break;

            default:
                break;
        }

        if (parent.getChildCount() == position) {

        }

        final PDFOutlineElementVO pdfoutlineelement = mfilelist.get(position);

        // 设置资源
        switch (type) {
            case TYPE_1:

                holder1.txt_contacts_department_type.setText(pdfoutlineelement.getDepartmentandwmployee().getDname());
                break;

            case TYPE_2:

                int level2 = pdfoutlineelement.getLevel();

                // holder2.line_all.setPadding(5 * level2,
                // holder2.line_all.getPaddingTop(), 0,
                // holder2.line_all.getPaddingBottom());
                // if (level2 == 1) {
                // holder2.line_public_many_employee_select.setPadding(20,
                // holder2.line_check.getPaddingTop(), 15, holder2.line_check.getPaddingBottom());
                // } else {
                // holder2.line_public_many_employee_select.setPadding(20 * (level2 + 1),
                // holder2.line_check.getPaddingTop(), 15, holder2.line_check.getPaddingBottom());
                // }

                if (level2 == 1) {
                    holder2.line_public_many_employee_select.setPadding(30, 0, 30, 0);
                } else {
                    holder2.line_public_many_employee_select.setPadding(30 * (level2 + 1), 0, 30, 0);
                }

                holder2.text.setText(pdfoutlineelement.getDepartmentandwmployee().getDname());
                if (pdfoutlineelement.isMhasChild() && (pdfoutlineelement.isExpanded() == false)) {
                    holder2.icon.setImageBitmap(mIconCollapse);
                } else if (pdfoutlineelement.isMhasChild() && (pdfoutlineelement.isExpanded() == true)) {
                    holder2.icon.setImageBitmap(mIconExpand);
                } else if (!pdfoutlineelement.isMhasChild()) {
                    holder2.icon.setImageBitmap(mIconCollapse);
                    holder2.icon.setVisibility(View.GONE);
                }

                if (mShowFirst) {
                    holder2.icon.setVisibility(View.GONE);
                    holder2.divider.setVisibility(View.GONE);
                    // holder2.line_check.setVisibility(View.GONE);
                    // holder2.line_check_right.setVisibility(View.VISIBLE);
                    //
                    // if (level2 == 1) {
                    // holder2.line_check_right.setPadding(0,
                    // holder2.line_check_right.getPaddingTop(), 30,
                    // holder2.line_check_right.getPaddingBottom());
                    // } else {
                    // holder2.line_check_right.setPadding(0 * (level2 + 1),
                    // holder2.line_check_right.getPaddingTop(), 30,
                    // holder2.line_check_right.getPaddingBottom());
                    // }
                    //
                    // holder2.checkBox_up_check_right.setChecked(pdfoutlineelement.isSelect());
                    // holder2.line_check_right.setOnClickListener(new OnClickListener() {
                    //
                    // @Override
                    // public void onClick(View paramView) {
                    // // if (mOneList.size() > 0) {
                    // // mOneList.get(0).setSelect(false);
                    // // }
                    // // mOneList.clear();
                    // if (!mOneList.contains(pdfoutlineelement)) {
                    // if (!mShowUser) {
                    // if (mfilelist.size() > 0) {
                    // for (int i = 0; i < mfilelist.size(); i++) {
                    // if (mfilelist.get(i).isSelect()) {
                    // mfilelist.get(i).setSelect(false);
                    // }
                    // }
                    // }
                    // }
                    // }
                    // mOneList.clear();
                    // employeeOnclickCallback.employeeOnclick(position, 1);
                    // // if (!mOneList.contains(pdfoutlineelement) &&
                    // // pdfoutlineelement.isSelect()) {
                    // // mOneList.add(pdfoutlineelement);
                    // // }
                    // mOneList.add(pdfoutlineelement);
                    // }
                    // });
                }
                holder2.checkBox_up_check.setChecked(pdfoutlineelement.isSelect());

                holder2.line_check.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View paramView) {
                        // if (mOneList.size() > 0) {
                        // mOneList.get(0).setSelect(false);
                        // }
                        // mOneList.clear();
                        if (!mOneList.contains(pdfoutlineelement)) {
                            if (!mShowUser) {
                                if (mfilelist.size() > 0) {
                                    for (int i = 0; i < mfilelist.size(); i++) {
                                        if (mfilelist.get(i).isSelect()) {
                                            mfilelist.get(i).setSelect(false);
                                        }
                                    }
                                }
                            }
                        }
                        mOneList.clear();
                        employeeOnclickCallback.employeeOnclick(position, 1, pdfoutlineelement);
                        // if (!mOneList.contains(pdfoutlineelement) &&
                        // pdfoutlineelement.isSelect()) {
                        // mOneList.add(pdfoutlineelement);
                        // }
                        mOneList.add(pdfoutlineelement);
                    }
                });

                holder2.line_see.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View paramView) {
                        employeeOnclickCallback.employeeOnclick(position, 2, pdfoutlineelement);

                    }
                });

                break;
            case TYPE_3:

                // if (!mShowUser) {
                // holder3.line_contats_department_person_seeDetail.setVisibility(View.GONE);
                // holder3.layout3.setVisibility(View.GONE);
                // }
                int level3 = pdfoutlineelement.getUser_level();

                // if (pdfoutlineelement.isSearch()) {
                // holder3.line_new_check.setPadding(30, holder3.line_new_check.getPaddingTop(), 15,
                // holder3.line_new_check.getPaddingBottom());
                //
                // } else {
                // holder3.line_new_check.setPadding(30 * (level3 + 1),
                // holder3.line_new_check.getPaddingTop(), 15,
                // holder3.line_new_check.getPaddingBottom());
                //
                // }

                if (pdfoutlineelement.isSearch()) {
                    holder3.line_new_check.setPadding(30, 0, 30, 0);

                } else {
                    holder3.line_new_check.setPadding(30 * (level3 + 1), 0, 30, 0);

                }

                String path = pdfoutlineelement.getDepartmentandwmployee().getHeadpic();
                holder3.txt_contats_department_person_image.setTag(path);

                holder3.txt_contats_department_person_name.setText(pdfoutlineelement.getDepartmentandwmployee().getFullname());

                Drawable drawable;
                if ("男".equals(pdfoutlineelement.getDepartmentandwmployee().getSex())) {
                    drawable = mContext.getResources().getDrawable(R.drawable.sex_man);
                } else {
                    drawable = mContext.getResources().getDrawable(R.drawable.sex_woman);
                }

                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); // 设置边界
                holder3.txt_contats_department_person_name.setCompoundDrawables(null, null, drawable, null);
                holder3.txt_contats_department_person_name.setCompoundDrawablePadding(10);

                imageLoader.displayImage(pdfoutlineelement.getDepartmentandwmployee().getHeadpic(), holder3.txt_contats_department_person_image, options);
                // holder3.txt_contats_department_person_phone.setText(pdfoutlineelement.getDepartmentandwmployee().getTel());
                holder3.txt_contats_department_person_phone.setText(pdfoutlineelement.getDepartmentandwmployee().getDname() + "/" + pdfoutlineelement.getDepartmentandwmployee().getPname());
                holder3.checkBox_new_check.setChecked(pdfoutlineelement.isSelect());

                holder3.line_contats_department_person_seeDetail.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View paramView) {
                        employeeOnclickCallback.employeeOnclick(position, 1, pdfoutlineelement);

                    }
                });
                break;
        }

        return convertView;
    }

    class ViewHolder1 {

        // 类别
        TextView txt_contacts_department_type;

    }

    public class ViewHolder2 {

        // 部门
        TextView text;
        ImageView icon;
        LinearLayout line_check, line_check_right, line_public_many_employee_select;
        LinearLayout line_see;
        // LinearLayout line_all;
        CheckBox checkBox_up_check;
        CheckBox checkBox_up_check_right;
        View divider;
    }

    public class ViewHolder3 {

        // 姓名
        TextView txt_contats_department_person_name;
        // 电话
        TextView txt_contats_department_person_phone;
        // 头像
        BSCircleImageView txt_contats_department_person_image;
        // 头像布局
        LinearLayout txt_contats_department_person_image_layout;
        // 布局最外层
        LinearLayout line_contats_department_person_seeDetail, layout3;

        LinearLayout line_new_check;
        CheckBox checkBox_new_check;

    }

}
