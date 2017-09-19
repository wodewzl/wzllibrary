
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.BossStatisticsAttendanceVO;
import com.bs.bsims.utils.CommonUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BossStatisticsAttendanceAdapter extends BSBaseAdapter<BossStatisticsAttendanceVO> {
    private Context mContext;
    private String mMode = "1";

    public BossStatisticsAttendanceAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (mIsEmpty) {
            return super.getView(position, convertView, parent);
        }

        if (convertView != null && convertView.getTag() == null)
            convertView = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.lv_statistics_leave, null);
            holder.rank = (TextView) convertView.findViewById(R.id.rank_text);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.type = (TextView) convertView.findViewById(R.id.type);
            holder.sort = (TextView) convertView.findViewById(R.id.sort);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BossStatisticsAttendanceVO vo = mList.get(position);
        if (0 == position) {
            holder.img.setVisibility(View.VISIBLE);
            holder.rank.setVisibility(View.GONE);
            holder.img.setImageResource(R.drawable.static_num_01);
        } else if (1 == position) {
            holder.img.setVisibility(View.VISIBLE);
            holder.rank.setVisibility(View.GONE);
            holder.img.setImageResource(R.drawable.static_num_02);
        } else if (2 == position) {
            holder.img.setVisibility(View.VISIBLE);
            holder.rank.setVisibility(View.GONE);
            holder.img.setImageResource(R.drawable.static_num_03);
        } else {
            holder.img.setVisibility(View.GONE);
            holder.rank.setVisibility(View.VISIBLE);
            holder.rank.setBackgroundResource(R.drawable.static_sale_value_rank);
            holder.rank.setText(String.valueOf(position + 1));
        }

        if (vo.getDname() != null) {
            // 考勤首页下方列表
            holder.type.setText(vo.getDname());
        } else {
            // 考勤排行下方列表
            holder.type.setText(vo.getFullname());
        }

        if ("1".equals(mMode)) {
            holder.sort.setText(vo.getNum() + "人");
        } else if ("2".equals(mMode)) {
            holder.sort.setText(vo.getNum() + "次");
        } else if ("3".equals(mMode)) {
            holder.sort.setText(vo.getNum() + "小时");
        } else {
            holder.sort.setText(vo.getNum() + "元");
        }
        holder.sort.setCompoundDrawablePadding(CommonUtils.dip2px(mContext, 5));
        if ("1".equals(vo.getContrast())) {
            holder.sort.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.statistics_down), null);
        } else if ("2".equals(vo.getContrast())) {
            holder.sort.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.statistics_same), null);
        } else if ("3".equals(vo.getContrast())) {
            holder.sort.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.statistics_up), null);
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView rank, name, sort, type;
        private ImageView img;
    }

    public void updateData(List<BossStatisticsAttendanceVO> list, int sort, String mode) {
        this.mMode = mode;
        List<BossStatisticsAttendanceVO> sortList = new ArrayList<BossStatisticsAttendanceVO>();
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).isVisible())
                continue;
            sortList.add(list.get(i).getList().get(sort));
        }
        sortList(sortList);
        super.updateData(sortList);
    }

    public void updateData(List<BossStatisticsAttendanceVO> list, String mode) {
        this.mMode = mode;
        super.updateData(list);
    }

    // 全部部门时sort 为默认最后一个
    public void updateData(List<BossStatisticsAttendanceVO> list, String mode, int sort) {
        this.mMode = mode;
        super.updateData(list);
    }

    public void sortList(List<BossStatisticsAttendanceVO> list) {
        Comparator comp = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                float one = 0;
                float two = 0;
                BossStatisticsAttendanceVO p1 = (BossStatisticsAttendanceVO) o1;
                BossStatisticsAttendanceVO p2 = (BossStatisticsAttendanceVO) o2;
                one = Float.parseFloat(p1.getNum());
                two = Float.parseFloat(p2.getNum());
                if (one < two) {
                    return 1;
                } else if (one == two) {
                    return 0;
                } else {
                    return -1;
                }
            }
        };
        Collections.sort(list, comp);
    }
}
