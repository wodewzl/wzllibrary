
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.CrmClientTrendVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.DateUtils;

import java.util.List;

public class CrmClientTrendAdapter extends BSBaseAdapter<CrmClientTrendVO> {
    private Context mContext;
    private String mCuurentTime = "";
    private String mPreviousTime = "";

    public CrmClientTrendAdapter(Context context) {
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
        CrmClientTrendVO vo = mList.get(position);
        if (convertView != null && convertView.getTag() == null)
            convertView = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.crm_client_trend_lv, null);
            holder.mTimeTv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.mText01Tv = (TextView) convertView.findViewById(R.id.text01);
            holder.mText02Tv = (TextView) convertView.findViewById(R.id.text02);
            holder.mText03Tv = (TextView) convertView.findViewById(R.id.text03);
            holder.mTypeIv = (ImageView) convertView.findViewById(R.id.head_iconbasic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mText01Tv.setText(vo.getOpname());
        if (vo.getContent().contains("：") && vo.getContent().split("：").length >= 2) {
            String content1 = vo.getContent().split("：")[0];
            String content2 = vo.getContent().split("：")[1];
            CommonUtils.setDifferentTextColor(holder.mText02Tv, content1 + "：", content2, "#00a9fe");
        } else {
            holder.mText02Tv.setText(vo.getContent());

        }

        holder.mText03Tv.setText(DateUtils.parseHour(vo.getAddtime()));
        mCuurentTime = DateUtils.parseDateDay(vo.getAddtime());
        switch (Integer.parseInt(CommonUtils.isNormalCount(vo.getIcon()))) {
            case 1:
                holder.mTypeIv.setImageResource(R.drawable.crm_bszone_customer);
                break;
            case 2:
                holder.mTypeIv.setImageResource(R.drawable.crm_bszone_person);
                break;
            case 3:
                holder.mTypeIv.setImageResource(R.drawable.crm_bszone_money);
                break;
            case 4:
                holder.mTypeIv.setImageResource(R.drawable.crm_bszone_product);
                break;
            case 5:
                holder.mTypeIv.setImageResource(R.drawable.crm_bszone_save);
                break;
            case 6:
                holder.mTypeIv.setImageResource(R.drawable.crm_bszone_getpay);
                break;

            default:
                holder.mTypeIv.setImageResource(R.drawable.crm_bszone_getpay);
                break;
        }

        if (vo.getIsshowtime() == null) {
            if (mPreviousTime.equals(mCuurentTime)) {
                vo.setIsshowtime("1");
            } else {

                if (mCuurentTime.equals(DateUtils.getCurrentDate())) {
                    vo.setIsshowtime("今天");
                } else {
                    vo.setIsshowtime(mCuurentTime);
                }
            }
        }

        if (vo.getIsshowtime() != null && !"1".equals(vo.getIsshowtime())) {
            holder.mTimeTv.setVisibility(View.VISIBLE);
            holder.mTimeTv.setText(vo.getIsshowtime());
        } else {
            holder.mTimeTv.setVisibility(View.GONE);
        }

        mPreviousTime = mCuurentTime;

        return convertView;
    }

    static class ViewHolder {
        private TextView mText01Tv, mText02Tv, mText03Tv, mTimeTv;
        private ImageView mTypeIv;
    }

    @Override
    public void updateDataFrist(List<CrmClientTrendVO> list) {
        for (int i = 0; i < this.mList.size(); i++) {
            mList.get(i).setIsshowtime(null);
        }
        mCuurentTime = "";
        mPreviousTime = "";
        super.updateDataFrist(list);
    }
}
