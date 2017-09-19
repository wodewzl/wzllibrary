
package com.bs.bsims.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.download.domain.DownloadFile;
import com.bs.bsims.utils.DateUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author peck
 * @Description:
 * @date 2015-6-13 下午2:21:17
 * @email 971371860@qq.com
 * @version V1.1
 * @Description: 2. 文档列表页面 一级部门ID下的文档
 * @date 2015-7-7 下午4:43:18 V1.1
 */

public class EXTTeamFileListAdapter extends BaseAdapter {

    private Context mContext;
    public List<DownloadFile> mList;
    private ImageLoader mImageLoader;
    private Map<String, String> valueMap;
    private DisplayImageOptions mOptions;

    public EXTTeamFileListAdapter(Context mContext) {
        super();
        this.mContext = mContext;
        this.mList = new ArrayList<DownloadFile>();
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
                    R.layout.item_sharedfilesd_grouphome_down, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView
                    .findViewById(R.id.img_item_sharedfilesd_grouphome_all);
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
        DownloadFile mData = mList.get(position);

        // String tempTime = mData.getOpen();
        String tempTime = mData.getTime();
        if (TextUtils.isEmpty(tempTime)) {
            tempTime = "暂无文档";
        } else {
            tempTime = DateUtils.parseDate(Long.parseLong(tempTime) * 1000);
        }

        /**
         * 第二行文件详情
         */
        String fileInfo = mData.getDname() + "\t" + mData.getFullname() + "\t" + tempTime;
        holder.mTime.setText(mData.getTitle());
        holder.mTime1.setText(fileInfo);
        holder.mNum.setText(mData.getSharedid());
        holder.mNum1.setText(mData.getDid());

        return convertView;
    }

    static class ViewHolder {
        private TextView mTime, mType, mColor, mNum;
        private TextView mName1, mContent1, mTime1, mType1, mColor1, mNum1;
        private ImageView image;
    }

    public void updateData(List<DownloadFile> list) {
        if (null == mList) {
            mList = new ArrayList<DownloadFile>();
        }
        if (null == list) {
            return;
        }
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

}
