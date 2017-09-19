
package com.bs.bsims.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.ContactPersonActivity;
import com.bs.bsims.activity.EXTWorkAttendanceDetailActivity;
import com.bs.bsims.model.PDFOutlineElementVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

@SuppressWarnings("rawtypes")
public class DepartmentAdapter extends ArrayAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<PDFOutlineElementVO> mfilelist;
    private Bitmap mIconCollapse;
    private Bitmap mIconExpand;
    final int TYPE_1 = 1;
    final int TYPE_2 = 2;
    final int TYPE_3 = 3;
    ImageLoader imageLoader;
    DisplayImageOptions options;

    @SuppressWarnings("unchecked")
    public DepartmentAdapter(Context context, int textViewResourceId, List<PDFOutlineElementVO> objects) {

        super(context, textViewResourceId, objects);
        this.imageLoader = ImageLoader.getInstance();
        options = CommonUtils.initImageLoaderOptions();
        this.mContext = context;
        this.mfilelist = objects;
        this.mIconCollapse = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_contacts_department_fragment_arrow_default);
        this.mIconExpand = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_contacts_department_fragment_arrow_selected);

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
        if (p.getLevel() == 4) {
            return TYPE_3;
        } else {
            return TYPE_2;
        }
        /*
         * if (p.getLevel() == 3) { return TYPE_2; } else if (p.getLevel() == 1) { return TYPE_2; }
         * else if (p.getLevel() == 2) { if (p.getId().equals("usertwo")) { return TYPE_3; } else {
         * return TYPE_2; } } else { return TYPE_3; }
         */

    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder1 holder1 = null;
        ViewHolder2 holder2 = null;
        ViewHolder3 holder3 = null;
        int type = getItemViewType(position);

        if (convertView == null) {
            mInflater = LayoutInflater.from(mContext);
            // 按当前所需的样式，确定new的布局
            switch (type) {
                case TYPE_1:

                    convertView = mInflater.inflate(R.layout.item_contacts_department_txt, parent, false);
                    holder1 = new ViewHolder1();
                    holder1.txt_contacts_department_type = (TextView) convertView.findViewById(R.id.txt_contacts_department_type);
                    convertView.setTag(holder1);
                    break;

                case TYPE_2:

                    convertView = mInflater.inflate(R.layout.item_contacts_department_supertreeview, null);
                    holder2 = new ViewHolder2();
                    holder2.text = (TextView) convertView.findViewById(R.id.txt_contacts_department_name);
                    holder2.icon = (ImageView) convertView.findViewById(R.id.img_contacts_department_arrow);
                    convertView.setTag(holder2);
                    break;

                case TYPE_3:

                    convertView = mInflater.inflate(R.layout.item_contacts_single_last, null);
                    holder3 = new ViewHolder3();
                    holder3.txt_contats_department_person_name = (TextView) convertView.findViewById(R.id.txt_contats_department_person_name);
                    holder3.txt_contats_department_person_phone = (TextView) convertView.findViewById(R.id.txt_contats_department_person_phone);
                    holder3.txt_contats_department_person_image = (BSCircleImageView) convertView.findViewById(R.id.txt_contats_department_person_image);
                    holder3.txt_contats_department_person_image_layout = (LinearLayout) convertView.findViewById(R.id.txt_contats_department_person_image_layout);
                    holder3.line_contats_department_person_seeDetail = (LinearLayout) convertView.findViewById(R.id.line_contats_department_person_seeDetail);

                    convertView.setTag(holder3);

                    break;

                default:
                    break;
            }

        } else {
            switch (type) {

                case TYPE_1:
                    holder1 = (ViewHolder1) convertView.getTag();
                    break;

                case TYPE_2:
                    holder2 = (ViewHolder2) convertView.getTag();
                    break;

                case TYPE_3:
                    holder3 = (ViewHolder3) convertView.getTag();
                    break;

            }

        }

        final PDFOutlineElementVO pdfoutlineelement = mfilelist.get(position);

        // 设置资源
        switch (type) {
            case TYPE_1:

                holder1.txt_contacts_department_type.setText(pdfoutlineelement.getDepartmentandwmployee().getDname());
                break;

            case TYPE_2:

                int level2 = pdfoutlineelement.getLevel();
                if (level2 == 1) {
                    holder2.icon.setPadding(30, holder2.icon.getPaddingTop(), 0, holder2.icon.getPaddingBottom());
                } else {
                    holder2.icon.setPadding(30 * (level2 + 1), holder2.icon.getPaddingTop(), 0, holder2.icon.getPaddingBottom());
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

                break;
            case TYPE_3:

                int level3 = pdfoutlineelement.getUser_level();

                if (pdfoutlineelement.isSearch()) {
                    holder3.txt_contats_department_person_image_layout.setPadding(30, holder3.txt_contats_department_person_image_layout.getPaddingTop(), 0,
                            holder3.txt_contats_department_person_image_layout.getPaddingBottom());
                } else {
                    holder3.txt_contats_department_person_image_layout.setPadding(30 * (level3 + 1), holder3.txt_contats_department_person_image_layout.getPaddingTop(), 0,
                            holder3.txt_contats_department_person_image_layout.getPaddingBottom());
                }
                imageLoader.displayImage(pdfoutlineelement.getDepartmentandwmployee().getHeadpic(), holder3.txt_contats_department_person_image, options);

                holder3.txt_contats_department_person_name.setText(pdfoutlineelement.getDepartmentandwmployee().getFullname());

                if (pdfoutlineelement.getDepartmentandwmployee().getAwork() != null) {
                    holder3.txt_contats_department_person_phone.setTextColor(Color.parseColor("#ff0000"));
                    holder3.txt_contats_department_person_phone.setText("考工: " + pdfoutlineelement.getDepartmentandwmployee().getAwork() + "元");
                } else {
                    holder3.txt_contats_department_person_phone.setText(pdfoutlineelement.getDepartmentandwmployee().getTel());
                }
                holder3.line_contats_department_person_seeDetail.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        Intent intent = new Intent();
                        if (pdfoutlineelement.getDepartmentandwmployee().getAwork() != null) {
                            intent.setClass(mContext, EXTWorkAttendanceDetailActivity.class);
                        } else if (pdfoutlineelement.getDepartmentandwmployee().getTel() != null) {
                            intent.setClass(mContext, ContactPersonActivity.class);
                        } else {

                        }
                        intent.putExtra("uid", pdfoutlineelement.getDepartmentandwmployee().getUserid());
                        intent.putExtra("userid", pdfoutlineelement.getDepartmentandwmployee().getUserid());
                        // intent.putExtra("personid",
                        // pdfoutlineelement.getDepartmentandwmployee().getUserid());
                        mContext.startActivity(intent);
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
        LinearLayout line_contats_department_person_seeDetail;

    }

}
