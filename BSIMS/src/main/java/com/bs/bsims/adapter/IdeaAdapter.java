
package com.bs.bsims.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.CreativeIdeaDetailActivity;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.IdeaVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class IdeaAdapter extends BaseAdapter {

    private Context mContext;
    public List<IdeaVO> mList;
    public ImageLoader mImageLoader;
    public DisplayImageOptions mOptions;
    private String mIsboss;

    public IdeaAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<IdeaVO>();
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();
    }

    public IdeaAdapter(Context context, String isboss) {
        this.mContext = context;
        this.mList = new ArrayList<IdeaVO>();
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();

        this.mIsboss = isboss;
        registBroadcast();
    }

    @Override
    public int getCount() {
        if (mList.size() == 0) {
            return 1;
        } else {
            return mList.size();
        }
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
            convertView = View.inflate(mContext, R.layout.lv_idea, null);
            holder.mName = (TextView) convertView.findViewById(R.id.name_tv);
            holder.mType = (TextView) convertView.findViewById(R.id.type_tv);
            holder.mTime = (TextView) convertView.findViewById(R.id.time_tv);
            holder.mContent = (TextView) convertView.findViewById(R.id.content_tv);
            holder.mState = (ImageView) convertView.findViewById(R.id.state_img);
            holder.mHead = (BSCircleImageView) convertView.findViewById(R.id.head_icon);
            holder.mItem = convertView.findViewById(R.id.item_layout);

            holder.noContent = (LinearLayout) convertView.findViewById(R.id.no_content_layout);
            holder.content = (LinearLayout) convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.mHead.setIsread("");
        }

        holder.noContent.setVisibility(View.GONE);
        holder.content.setVisibility(View.VISIBLE);
        if (mList.size() == 0) {
            holder.noContent.setVisibility(View.VISIBLE);
            holder.content.setVisibility(View.GONE);
            return convertView;
        }

        IdeaVO ideaVO = mList.get(position);
        
        if ("1".equals(ideaVO.getIsnoread())) {
            holder.mHead.setIsread("0");
        }
        // holder.mHead.setBadgeNumber(Integer.parseInt(ideaVO.getReplynum()));

        if ("1".equals(ideaVO.getIsAnonymous())) {
            holder.mName.setText("匿名");
            mImageLoader.displayImage(null, holder.mHead, mOptions);
            holder.mHead.setUserId(null);//匿名用户ID为空
        } else {
            holder.mName.setText(ideaVO.getFullname());
            mImageLoader.displayImage(ideaVO.getHeadpic(), holder.mHead, mOptions);
            holder.mHead.setUserId(ideaVO.getUserid());//获取头像对应的用户ID
            holder.mHead.setUserName(ideaVO.getFullname());
            holder.mHead.setUrl(ideaVO.getHeadpic());
        }
        // long time = Long.parseLong(ideaVO.getTime()) * 1000;
        holder.mTime.setText(DateUtils.parseDateDay(ideaVO.getTime()));
        holder.mContent.setText(ideaVO.getTitle());
        String checks = ideaVO.getChecks();
        if ("0".equals(checks)) {
            holder.mState.setImageResource(R.drawable.creative_01);
        } else if ("1".equals(checks)) {
            holder.mState.setImageResource(R.drawable.creative_02);
        }
        else if ("3".equals(checks)) {
            holder.mState.setImageResource(R.drawable.creative_03);
        }
        else if ("4".equals(checks)) {
            holder.mState.setImageResource(R.drawable.creative_04);
        }
        else if ("2".equals(checks)) {
            holder.mState.setImageResource(R.drawable.creative_05);
        }

        holder.mItem.setOnClickListener(new IdeaListeners(mContext, ideaVO));
        return convertView;
    }

    static class ViewHolder {
        private TextView mName, mContent, mTime, mType;
        private ImageView mState;
        private BSCircleImageView mHead;
        private View mItem;
        private LinearLayout noContent, content;
    }

    public void updateData(List<IdeaVO> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataFrist(List<IdeaVO> list) {
        list.addAll(mList);
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataLast(List<IdeaVO> list) {
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    private class IdeaListeners implements OnClickListener {
        private Context mContext;
        private IdeaVO mIdeaVO;

        public IdeaListeners(Context context, IdeaVO ideaVO) {
            this.mContext = context;
            this.mIdeaVO = ideaVO;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(mContext, CreativeIdeaDetailActivity.class);
            intent.putExtra("type", mIdeaVO.getType());
            intent.putExtra("id", mIdeaVO.getArticleid());
            intent.putExtra("isboss", mIsboss);
            intent.putExtra("lead", mIdeaVO.getLeadnum());
            mContext.startActivity(intent);
        }
    }

    public void registBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.HOME_MSG);
        mContext.registerReceiver(msgBroadcast, filter);
    }

    private void unRegistExitReceiver() {
        mContext.unregisterReceiver(msgBroadcast);
    }

    private BroadcastReceiver msgBroadcast = new BroadcastReceiver() {
        private long mChangeTime = 0;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constant.HOME_MSG.equals(intent.getAction())) {
                String isnoread = intent.getStringExtra("isnoread");
                String id = intent.getStringExtra("id");
                String check = intent.getStringExtra("check");
                String accept = intent.getStringExtra("accept");
                if (isnoread != null) {
                    for (int i = 0; i < mList.size(); i++) {
                        if (id.equals(mList.get(i).getArticleid())) {
                            mList.get(i).setIsnoread("0");
                            notifyDataSetChanged();
                        }
                    }
                }

                if (check != null) {
                    for (int i = 0; i < mList.size(); i++) {
                        if (id.equals(mList.get(i).getArticleid())) {
                            if ("1".equals(check)) {
                                mList.get(i).setChecks("1");
                            } else {
                                mList.get(i).setChecks("2");
                            }
                            notifyDataSetChanged();
                        }
                    }
                }

                if (accept != null) {
                    for (int i = 0; i < mList.size(); i++) {
                        if (id.equals(mList.get(i).getArticleid())) {
                            if ("1".equals(accept)) {
                                mList.get(i).setChecks("4");
                            } else {
                                mList.get(i).setChecks("3");
                            }
                            notifyDataSetChanged();
                        }
                    }
                }

            }

        }
    };

}
