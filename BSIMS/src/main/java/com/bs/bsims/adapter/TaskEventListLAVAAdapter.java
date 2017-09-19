
package com.bs.bsims.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.CreativeIdeaDetailActivity;
import com.bs.bsims.model.TaskEventItem;
import com.bs.bsims.utils.CommonDateUtils;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author peck
 * @Description:
 * @date 2015-6-4 下午3:03:48
 * @email 971371860@qq.com
 * @version V1.0
 */

public class TaskEventListLAVAAdapter extends BaseAdapter {

    private Context context;
    public List<TaskEventItem> mList;
    public ImageLoader mImageLoader;
    public DisplayImageOptions mOptions;

    public TaskEventListLAVAAdapter(Context context) {
        this.context = context;
        this.mList = new ArrayList<TaskEventItem>();
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
        ViewHolderNew holderNew;
        if (convertView == null) {
            holderNew = new ViewHolderNew();
            convertView = View.inflate(context,
                    R.layout.item_taskeventlistadapter_ly, null);
            holderNew.mName = (TextView) convertView
                    .findViewById(R.id.item_taskeventlistadapter_name_tv);
            holderNew.mIsread = (ImageView) convertView.findViewById(R.id.isread);
            // holderNew.mType = (TextView)
            // convertView.findViewById(R.id.type_tv);
            holderNew.mTime = (TextView) convertView
                    .findViewById(R.id.item_taskeventlistadapter_time_tv);
            holderNew.mContent = (TextView) convertView
                    .findViewById(R.id.item_taskeventlistadapter_content_tv);
            holderNew.mState = (ImageView) convertView
                    .findViewById(R.id.item_taskeventlistadapter_state_img);
            holderNew.mFujian = (ImageView) convertView
                    .findViewById(R.id.img_item_taskeventlistadapter_havefile);
            // holderNew.mHead = (BSCircleImageView)
            // convertView.findViewById(R.id.head_icon);
            // holderNew.mItem = convertView.findViewById(R.id.item_layout);
            holderNew.mProgressBar = (ProgressBar) convertView
                    .findViewById(R.id.item_taskeventlistadapter_seekbar);
            convertView.setTag(holderNew);
        } else {
            holderNew = (ViewHolderNew) convertView.getTag();
        }
        TaskEventItem data = mList.get(position);

        holderNew.mName.setText(data.getTitle());
        // 开始与结束时间
        holderNew.mTime.setText(CommonDateUtils.parseDate(
                data.getStarttime() * 1000, "MM.dd")
                + "-"
                + CommonDateUtils.parseDate(data.getEndtime() * 1000, "MM.dd"));
        // 进度
        int percentValue = (int) (Double.parseDouble(data.getSchedule()) * 100);
        holderNew.mContent.setText(percentValue + "%");
        holderNew.mProgressBar.setProgress(percentValue);
        /**
         * 当前状态0未审1已完成2完结3定审完成4终止5过期
         */
        int statusInt = Integer.parseInt(data.getStatus());
        int drawableId = R.drawable.creative_01;
        switch (statusInt) {
            case 0:
                drawableId = R.drawable.nsm_task_state_0;
                break;
            case 1:
                drawableId = R.drawable.nsm_task_state_1;
                break;
            case 2:
                drawableId = R.drawable.nsm_task_state_2;
                break;
            case 3:
                drawableId = R.drawable.nsm_task_state_3;
                break;
            case 5:
                drawableId = R.drawable.nsm_task_state_5;
                break;
        }
        holderNew.mState.setImageResource(drawableId);

        // if (TextUtils.isEmpty(data.getAnnex_size())
        // || "0".equals(data.getAnnex_size())
        // || "暂无".equals(data.getAnnex_size())) {
        // holderNew.mFujian.setVisibility(View.GONE);
        // } else {
        // holderNew.mFujian.setVisibility(View.VISIBLE);
        // }
        if (TextUtils.isEmpty(data.getAnnex())
                || "0".equals(data.getAnnex())
                || "暂无".equals(data.getAnnex())) {
            holderNew.mFujian.setVisibility(View.GONE);
        } else {
            holderNew.mFujian.setVisibility(View.VISIBLE);
        }
        // holderNew.mFujian.setVisibility(View.GONE);

        if ("1".equals(data.getIsnoread())) {
            holderNew.mIsread.setVisibility(View.VISIBLE);
        } else {
            holderNew.mIsread.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolderNew {
        private TextView mName, mContent, mTime, mType;
        private ImageView mState, mFujian, mIsread;
        private BSCircleImageView mHead;
        private View mItem;
        private ProgressBar mProgressBar;
    }

    public void updateData(List<TaskEventItem> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataFrist(List<TaskEventItem> list) {
        list.addAll(mList);
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataLast(List<TaskEventItem> list) {
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    private class IdeaListeners implements OnClickListener {
        private Context mContext;
        private TaskEventItem mTaskEventItem;

        public IdeaListeners(Context context, TaskEventItem taskEventItem) {
            this.mContext = context;
            this.mTaskEventItem = taskEventItem;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(mContext, CreativeIdeaDetailActivity.class);
            mContext.startActivity(intent);
        }
    }
}
