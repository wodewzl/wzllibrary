/**
 * 
 */

package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.CrmVisitorVo;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.bs.bsims.view.PinnedSectionListView.PinnedSectionListAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-5-16
 * @version 2.0
 */
public class CrmVisitRecordNewAdapter extends BSBaseAdapter<CrmVisitorVo> implements PinnedSectionListAdapter {
    public static String PhoneVistorColor = "#ffae00";
    public static String DoorVistorColor = "#00aeff";
    public static String OnlieVistorColor = "#2eb3bf";
    public static String OtherVistorColor = "#9733ec";
    public static final String PHONE = "电话拜访";
    public static final String DOOR = "上门拜访";
    public static final String ONLIE = "在线沟通";
    public static final String OTHER = "其他方式";
    public List<Integer> mPinnedList = new ArrayList<Integer>();
    public GradientDrawable bgdraw = null;
    public ViewHolder holder;;
    public boolean mBolcommit =false;

    public CrmVisitRecordNewAdapter(Context context) {
        super(context);
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        GradientDrawable bgdraw = null;
        if (mIsEmpty) {
            View view = super.getView(position, convertView, parent);
            return view;
        }

        if (convertView != null && convertView.getTag() == null)
            convertView = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.crmvisitrecord_index_listadpter, null);
            holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.nameTv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.departTv = (TextView) convertView.findViewById(R.id.depart_tv);
            holder.headIcon = (BSCircleImageView) convertView.findViewById(R.id.head_icon);
            holder.typeTv = (TextView) convertView.findViewById(R.id.type_tv);
            holder.publishTimeTv = (TextView) convertView.findViewById(R.id.publish_time_tv);
            holder.replayTv = (TextView) convertView.findViewById(R.id.replay_tv);
            holder.contentLayout = (LinearLayout) convertView.findViewById(R.id.content_layout);
            holder.imgAgree = (ImageView) convertView.findViewById(R.id.img_agree);
            holder.aggreeLayout = (LinearLayout) convertView.findViewById(R.id.aggree_layout);
            holder.agreeTv = (TextView) convertView.findViewById(R.id.praise_tv);
            holder.favorImg = (ImageView) convertView.findViewById(R.id.favor_img);
            holder.title01Tv = (TextView) convertView.findViewById(R.id.title01);
            holder.frameLayout = (FrameLayout) convertView.findViewById(R.id.frame_layout);
            holder.vUserName = (TextView) convertView.findViewById(R.id.vistor_name);
            holder.vAddress = (TextView) convertView.findViewById(R.id.link_address_tv);
            holder.vType = (TextView) convertView.findViewById(R.id.type_visit);
            holder.mCnmae = (TextView) convertView.findViewById(R.id.vistor_cname);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (this.getItemViewType(position) == -10) {
            holder.timeTv.setVisibility(View.VISIBLE);
            holder.timeTv.setText(mList.get(mPinnedList.get(position + 1)).getDate());
            holder.frameLayout.setVisibility(View.GONE);
        } else {
            holder.timeTv.setVisibility(View.GONE);
            holder.frameLayout.setVisibility(View.VISIBLE);
            final CrmVisitorVo vo = mList.get(mPinnedList.get(position));
            holder.nameTv.setText(vo.getFullName());// 姓名
            holder.departTv.setText(vo.getDepartmentName() + "/" + vo.getPositionsName());// 岗位
            if (vo.getMode().equals(PHONE)) {
                bgdraw = CommonUtils.setBackgroundShap(mContext, 5, PhoneVistorColor, PhoneVistorColor);
            }
            else if (vo.getMode().equals(DOOR)) {
                bgdraw = CommonUtils.setBackgroundShap(mContext, 5, DoorVistorColor, DoorVistorColor);
            }
            else if (vo.getMode().equals(ONLIE)) {
                bgdraw = CommonUtils.setBackgroundShap(mContext, 5, OnlieVistorColor, OnlieVistorColor);
            }
            else {
                bgdraw = CommonUtils.setBackgroundShap(mContext, 5, OtherVistorColor, OtherVistorColor);
            }
            holder.vType.setBackground(bgdraw);
            holder.vType.setText(vo.getMode());
            holder.vUserName.setText(vo.getName());
            mImageLoader.displayImage(vo.getHeadpic(), holder.headIcon,
                    CommonUtils.initImageLoaderOptions());
            holder.headIcon.setUserId(vo.getUserid());// HL:获取头像对应的用户ID，以便响应跳转
            holder.headIcon.setUrl(vo.getHeadpic());
            holder.headIcon.setUserName(vo.getFullName());
            if(null!=vo.getCname()){
                holder.mCnmae.setText(vo.getCname());
            }
            else{
                holder.mCnmae.setText("未知客户");
            }
           
            if ("1".equals(vo.getIsread())) {
                holder.headIcon.setIsread("1");
            } else {
                holder.headIcon.setIsread("0");
            }
            if ("1".equals(vo.getIspraise())) {
                holder.imgAgree.setImageResource(R.drawable.agree_select);
            } else {
                holder.imgAgree.setImageResource(R.drawable.agree);
            }
            if (vo.getAddress().equals("")) {
                holder.vAddress.setVisibility(View.GONE);
            }
            else {
                holder.vAddress.setText(vo.getAddress());
                holder.vAddress.setVisibility(View.VISIBLE);
            }
            holder.publishTimeTv.setText(DateUtils.parseHour(vo.getTime()));
            holder.replayTv.setText(vo.getComment());
            holder.agreeTv.setText(vo.getPraise());
            holder.title01Tv.setText(vo.getInfo());
        }

        holder.aggreeLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if ("1".equals(mList.get(mPinnedList.get(position)).getIspraise())) {
                    CustomToast.showLongToast(mContext, "已点过赞");
                    return;
                }
                else{
                    if(!mBolcommit){
                        commit(Constant.CRM_CLIENT_DISSCUSS_AGREE, mList.get(mPinnedList.get(position)), 1, position);
                    }
                  
                }
                
                
            }
        });

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        if (mPinnedList.size() == 0)
            return 0;
        if (position >= mPinnedList.size())
            return mPinnedList.size() - 1;
        return mPinnedList.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return mList.size() + 1;
    }

    @Override
    public int getCount() {
        if (mList == null || mList.size() == 0) {
            mIsEmpty = true;
            return 1;
        } else {
            mIsEmpty = false;
            return mPinnedList.size();
        }

    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == -10;
    }

    @Override
    public void updateData(List<CrmVisitorVo> list) {
        super.updateData(list);
        sortTime(mList);
    }

    @Override
    public void updateDataFrist(List<CrmVisitorVo> list) {
        super.updateDataFrist(list);
        sortTime(mList);
    }

    @Override
    public void updateDataLast(List<CrmVisitorVo> list) {
        super.updateDataLast(list);
        sortTime(mList);
    }

    @Override
    public CrmVisitorVo getItem(int position) {
        return mList.get(mPinnedList.get(position));
    }

    public void sortTime(List<CrmVisitorVo> list) {
        mPinnedList.clear();
        for (int i = 0; i < list.size(); i++) {
            CrmVisitorVo vo = list.get(i);
            if (i == 0) {
                mPinnedList.add(-10);
            }
            if (i != 0 && !vo.getDate().equals((list.get(i - 1).getDate()))) {
                mPinnedList.add(-10);
                mPinnedList.add(i);
            } else {
                mPinnedList.add(i);
            }
        }
    }

    static class ViewHolder {
        public TextView timeTv, nameTv, typeTv, todayTv, finishTv, tomrrowTv, experienceTv, publishTimeTv, replayTv, agreeTv, departTv, title01Tv, title02Tv, title03Tv, title04Tv;
        public BSCircleImageView headIcon;
        private LinearLayout contentLayout, aggreeLayout;
        private TextView shengLue, vUserName, vAddress, vType,mCnmae;
        private ImageView imgAgree, favorImg;
        private FrameLayout frameLayout;
    }

    public void commit(String url, CrmVisitorVo discussVO, int status, final int position) {
        mBolcommit =true;
        RequestParams params = new RequestParams();
        try {
            params.put("ftoken", BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("rid", discussVO.getVid());
            params.put("type", "1"); // type为1表示针对纪录，不传表示针对纪录的评论
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();

        client.post(BSApplication.getInstance().getHttpTitle() + url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                mBolcommit =false;
                CustomToast.showNetErrorToast(mContext);
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                mBolcommit =false;
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(new String(arg2));
                    String code = (String) jsonObject.get("code");
                    String str = (String) jsonObject.get("retinfo");
                    if (Constant.RESULT_CODE.equals(code)) {
                        Animation scaleAnimation = new ScaleAnimation(1.0f, 2.0f, 1.0f, 2.0f);
                        holder.imgAgree.setImageResource(R.drawable.agree_select);
                        // 设置动画时间
                        scaleAnimation.setDuration(500);
                        holder.imgAgree.setAnimation(scaleAnimation);
                        holder.imgAgree.startAnimation(scaleAnimation);
                        int praise = Integer.parseInt(mList.get(mPinnedList.get(position)).getPraise()) + 1;
                        mList.get(mPinnedList.get(position)).setPraise(praise + "");
                        mList.get(mPinnedList.get(position)).setIspraise("1");
                    } else {
                        CustomToast.showShortToast(mContext, str);
                    }
                    notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }
}
