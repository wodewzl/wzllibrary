
package com.bs.bsims.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.ContactPersonActivity;
import com.bs.bsims.activity.GaoDeMapPersonTrajectoryActivity;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class GaodeLocationItemApdater extends BaseAdapter {

    private Context mContext;
    private List<EmployeeVO> mList;
    private String bossSelectTime = "";

    public String getBossSelectTime() {
        return bossSelectTime;
    }

    public void setBossSelectTime(String bossSelectTime) {
        this.bossSelectTime = bossSelectTime;
    }

    public GaodeLocationItemApdater(Context c) {

        mContext = c;
        this.mList = new ArrayList<EmployeeVO>();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // ImageView imageView;
        ViewHolder holder;
        View view;
        if (convertView == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.gaode_hoscroll_gridview_item,
                    null);
            Gallery.LayoutParams params = new Gallery.LayoutParams(
                    (CommonUtils.getScreenWidth(mContext) - CommonUtils.getScreenWidth(mContext) / 8),
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.department = (TextView) view.findViewById(R.id.department);
            holder.people_detail = (TextView) view.findViewById(R.id.people_detail);
            holder.people_locus = (TextView) view.findViewById(R.id.people_locus);
            holder.leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
            holder.rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
            view.setLayoutParams(params);
            view.setTag(holder);

        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();

        }

        final EmployeeVO vo = mList.get(position);
        holder.name.setText(vo.getFullname());
        holder.department.setText("部门：" + vo.getDname() + "/" + vo.getPname());
        holder.leftLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(mContext, ContactPersonActivity.class);
                intent.putExtra("imageUrl", vo.getHeadpic());
                intent.putExtra("username", vo.getFullname());
                intent.putExtra("uid", vo.getUserid());
                intent.putExtra("userid", vo.getUserid());
                intent.putExtra("cornet", vo.getCornet());
                mContext.startActivity(intent);
            }
        });
        holder.rightLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(mContext,
                        GaoDeMapPersonTrajectoryActivity.class);
                intent.putExtra("sid", vo.getUserid());
                intent.putExtra("username", vo.getFullname());
                intent.putExtra("date", bossSelectTime);
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    class ViewHolder {

        TextView name, department, people_detail, people_locus;
        LinearLayout leftLayout, rightLayout;
    }

    public void updateData(List<EmployeeVO> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }
}
