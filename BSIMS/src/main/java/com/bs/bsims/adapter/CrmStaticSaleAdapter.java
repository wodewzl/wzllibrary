
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.BossStatisticsAttendanceVO;
import com.bs.bsims.model.CrmStaticsSaleVO;
import com.bs.bsims.utils.CommonUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CrmStaticSaleAdapter extends BSBaseAdapter<CrmStaticsSaleVO> {
    private Context mContext;
    private String mMode = "1";

    public CrmStaticSaleAdapter(Context context) {
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
            convertView = View.inflate(mContext, R.layout.crm_statistics_sale_adapter, null);
            holder.rank = (TextView) convertView.findViewById(R.id.rank_text);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.sort = (TextView) convertView.findViewById(R.id.sort);
            holder.money = (TextView) convertView.findViewById(R.id.money);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CrmStaticsSaleVO vo = mList.get(position);

        if (0 == position) {
            holder.img.setVisibility(View.VISIBLE);
            holder.rank.setVisibility(View.GONE);
            holder.img.setImageResource(R.drawable.static_num_01);
            holder.sort.setTextColor(mContext.getResources().getColor(R.color.red));

        } else if (1 == position) {
            holder.img.setVisibility(View.VISIBLE);
            holder.rank.setVisibility(View.GONE);
            holder.img.setImageResource(R.drawable.static_num_02);
            holder.sort.setTextColor(mContext.getResources().getColor(R.color.C13));
        } else if (2 == position) {
            holder.img.setVisibility(View.VISIBLE);
            holder.rank.setVisibility(View.GONE);
            holder.img.setImageResource(R.drawable.static_num_03);
            holder.sort.setTextColor(mContext.getResources().getColor(R.color.C10));
        } else {
            holder.img.setVisibility(View.GONE);
            holder.rank.setVisibility(View.VISIBLE);
            holder.rank.setBackgroundResource(R.drawable.static_sale_value_rank);
            // holder.rank.setBackground(CommonUtils.setBackgroundShap(mContext, 100, R.color.C3,
            // R.color.C3));
            holder.rank.setText(String.valueOf(position + 1));
            holder.sort.setTextColor(mContext.getResources().getColor(R.color.C6));
        }

        holder.name.setText(vo.getFullname());
        holder.money.setText(CommonUtils.formatDetailMoney(vo.getPayment()));
        holder.sort.setText(vo.getCompPercent());
        return convertView;
    }

    static class ViewHolder {
        private TextView rank, name, sort, type, money;
        private ImageView img;
    }

    public void updateData(List<CrmStaticsSaleVO> list, String mode) {
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
