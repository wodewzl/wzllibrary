
package com.bs.bsims.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.EXTTeamFileListActivity;
import com.bs.bsims.model.SharedfilesdHomeAllFragmentB;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.view.BSImgViewFliePoint;

import java.util.ArrayList;
import java.util.List;

/**
 * @author peck
 * @Description: 1. 文档获取部门列表接口
 * @date 2015-7-4 上午9:50:04
 * @email 971371860@qq.com
 * @version V1.0
 */
public class EXTSharedfilesdGroupHomeAllAdapter extends BaseAdapter {

    private Context mContext;
    public List<SharedfilesdHomeAllFragmentB> mList;

    public EXTSharedfilesdGroupHomeAllAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<SharedfilesdHomeAllFragmentB>();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext,
                    R.layout.item_sharedfilesd_grouphome_all, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView
                    .findViewById(R.id.img_item_sharedfilesd_grouphome_all);
            holder.mBSPonit = (BSImgViewFliePoint)
                    convertView.findViewById(R.id.point_img_item_sharedfilesd_grouphome_all);
            holder.mTime = (TextView) convertView
                    .findViewById(R.id.txt_item_sharedfilesd_grouphome_all);
            holder.mColor = (TextView) convertView
                    .findViewById(R.id.txt_item_sharedfilesd_grouphome_all_color);
            holder.mNum = (TextView) convertView
                    .findViewById(R.id.txt_item_sharedfilesd_grouphome_all_num);

            // 上个月
            holder.mName1 = (TextView) convertView
                    .findViewById(R.id.txt_last_item_sharedfilesd_grouphome_all);
            holder.mTime1 = (TextView) convertView
                    .findViewById(R.id.txt_last_item_sharedfilesd_grouphome_all_month);
            holder.mColor1 = (TextView) convertView
                    .findViewById(R.id.txt_last_item_sharedfilesd_grouphome_all_color);
            holder.mNum1 = (TextView) convertView
                    .findViewById(R.id.txt_last_item_sharedfilesd_grouphome_all_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SharedfilesdHomeAllFragmentB mData = mList.get(position);

        String tempTime = mData.getTime();
        if (TextUtils.isEmpty(tempTime)) {
            tempTime = "暂无文档";
        } else {
            tempTime = DateUtils.parseDate(Long.parseLong(tempTime) * 1000);
        }
        holder.mTime.setText(mData.getName());
        holder.mTime1.setText(tempTime);
        holder.mNum.setText(mData.getRemind());
        holder.mNum1.setText(mData.getDid());

        // holder.mBSPonit.setBadgeNumber(1);
        // if (mData.getRemind().equals("1"))
        // holder.mBSPonit.setmShowFlag(false);
        // else
        holder.mBSPonit.setmShowFlag(false);

        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                /**
                 * 部门ID下的文档
                 */
                SharedfilesdHomeAllFragmentB mData = mList.get(position);
                String did = mData.getDid();
                Intent intent = new Intent();
                intent.putExtra("currentDid", did);
                intent.putExtra("actyname", mData.getName() + "文档");
                intent.setClass(mContext,
                        EXTTeamFileListActivity.class);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        private TextView mTime, mType, mColor, mNum;
        private TextView mName1, mContent1, mTime1, mType1, mColor1, mNum1;
        private ImageView image, mMoreImg;
        private BSImgViewFliePoint mBSPonit;
        private LinearLayout mMoreLy;
    }

    public void updateData(List<SharedfilesdHomeAllFragmentB> list) {
        if (null == mList) {
            mList = new ArrayList<SharedfilesdHomeAllFragmentB>();
        }
        if (null == list) {
            return;
        }
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

}
