
package com.bs.bsims.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.StatisticsArticlesVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
/**
 * 用品详情界面List Adapter
 */
public class StatisticsArticlesDetailAdapter extends BaseAdapter {

    private Context mContext;
    public List<StatisticsArticlesVO> mList;
    public ImageLoader mImageLoader;
    public DisplayImageOptions mOptions;

    public StatisticsArticlesDetailAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<StatisticsArticlesVO>();
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.lv_statistics_articles_detail, null);
            holder.icon = (BSCircleImageView) convertView.findViewById(R.id.head_icon);
            holder.depart = (TextView) convertView.findViewById(R.id.depart);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.states = (TextView) convertView.findViewById(R.id.states);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.unit = (TextView) convertView.findViewById(R.id.unit);
            holder.unit_price = (TextView) convertView.findViewById(R.id.unit_price);
            holder.articels = (TextView) convertView.findViewById(R.id.articels);
            holder.total_price = (TextView) convertView.findViewById(R.id.total_price);
            holder.money = (TextView) convertView.findViewById(R.id.money);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        StatisticsArticlesVO vo = mList.get(position);
        mImageLoader.displayImage(vo.getU_headpic(), holder.icon, mOptions);
        holder.icon.setUserId(vo.getGh_user());//HL:获取头像对应的用户ID，以便响应跳转
        holder.name.setText(vo.getU_fullname());
        holder.depart.setText(vo.getDept_name());
        if ("1".equals(vo.getState())) {
            holder.states.setText("入");
        } else {
            holder.states.setText("出");
        }
        holder.price.setText(vo.getGh_num());
        holder.unit.setText(vo.getG_company());
        holder.unit_price.setText(vo.getGh_price() + "元/" + vo.getG_company());
        holder.articels.setText(vo.getDate());

        double d = Double.parseDouble(vo.getGh_price());
        int in = Integer.parseInt(vo.getGh_num());
        double b = Double.parseDouble(vo.getGh_price()) * Integer.parseInt(vo.getGh_num());
        holder.total_price.setText(CommonUtils.countNumberSplitUnit(b + "").split(",")[0]);
        holder.money.setText(CommonUtils.countNumberSplitUnit(b + "").split(",")[1]);
        return convertView;
    }

    static class ViewHolder {
        private TextView name, depart, states, price, unit, unit_price, articels, total_price, money;
        private BSCircleImageView icon;

    }

    public void updateData(List<StatisticsArticlesVO> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataFrist(List<StatisticsArticlesVO> list) {
        list.addAll(mList);
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataLast(List<StatisticsArticlesVO> list) {
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

}
