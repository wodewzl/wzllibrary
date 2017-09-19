
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.CrmBusinessHomeIndexOneInfo;
import com.bs.bsims.activity.CrmContactDetailActivity;
import com.bs.bsims.activity.CrmTradeContantDeatilsHomeTop3Activity;
import com.bs.bsims.activity.CrmTradeContantDetailsIndexActivity;
import com.bs.bsims.activity.CrmVisitRecordDetailActivity;
import com.bs.bsims.activity.ImagePreviewActivity;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.CrmClientHomeDetailVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 客户管理(总览)详情界面 记录、联系人、商机、合同List的Adapter
 */
public class CrmClientHomeAdapter extends BaseAdapter {
    private Activity mContext;
    public List<CrmClientHomeDetailVO> mList;
    public String mSortid;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private int width;
    private int mType = 1;
    private CrmClientHomeDetailVO detaiVo;
    private String busIsShow = "";// 是否在详情里显示关联商机

    private int colorIndex = 0;
    private String mNoHeadColors[] = {
            "#7A929E", "#6194FF", "#65BEE6", "#F75E8C", "#39C3B4", "#FD953C", "#9B89B9",
    };

    public CrmClientHomeAdapter(Activity context) {
        this.mContext = context;
        this.mList = new ArrayList<CrmClientHomeDetailVO>();
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

    /*
     * (non-Javadoc)
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @SuppressLint("NewApi")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final CrmClientHomeDetailVO vo = mList.get(position);
        if (mType == 1) { // 记录
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.crm_visitor_deatils_list, null);
                holder.circleImageView = (BSCircleImageView) convertView
                        .findViewById(R.id.head_iconbasic);
                holder.person_title01 = (TextView) convertView.findViewById(R.id.full_name);
                holder.person_title02 = (TextView) convertView.findViewById(R.id.full_dpatername);
                holder.person_title03 = (TextView) convertView
                        .findViewById(R.id.work_info_why_state);
                holder.visitor_content = (TextView) convertView.findViewById(R.id.visitor_content);
                holder.visitor_modeName = (TextView) convertView
                        .findViewById(R.id.visitor_modeName);
                holder.visitor_econmouser_info = (TextView) convertView
                        .findViewById(R.id.visitor_econmouser_info);
                holder.visitor_ondretion = (TextView) convertView
                        .findViewById(R.id.visitor_ondretion);
                holder.visitor_objectiveName = (TextView) convertView
                        .findViewById(R.id.visitor_objectivenname);
                holder.bussines_visitor_comment = (TextView) convertView
                        .findViewById(R.id.bussines_visitor_comment);
                holder.bussines_visitor_zan = (TextView) convertView.findViewById(R.id.praise_tv);
                holder.bussines_visitor_cai = (TextView) convertView.findViewById(R.id.decline_tv);
                holder.img_agree = (ImageView) convertView.findViewById(R.id.img_agree);
                holder.img_oppose = (ImageView) convertView.findViewById(R.id.img_oppose);
                holder.aggree_layout = (LinearLayout) convertView.findViewById(R.id.aggree_layout);
                holder.oppose_layout = (LinearLayout) convertView.findViewById(R.id.oppose_layout);
                holder.mDetailImg01 = (ImageView) convertView.findViewById(R.id.detial_img_01);
                holder.mDetailImg02 = (ImageView) convertView.findViewById(R.id.detial_img_02);
                holder.mDetailImg03 = (ImageView) convertView.findViewById(R.id.detial_img_03);
                holder.mPictureLayout = (LinearLayout) convertView
                        .findViewById(R.id.picture_layout);
                holder.visitor_father = (LinearLayout) convertView

                        .findViewById(R.id.visitor_father);
                holder.link_cname_tv = (TextView) convertView.findViewById(R.id.link_cname_tv);
                holder.visitor_ondretion_layout = (LinearLayout) convertView
                        .findViewById(R.id.visitor_ondretion_layout);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (mList.get(position).getImgs() != null) {
                holder.mDetailImg01.setVisibility(View.INVISIBLE);
                holder.mDetailImg02.setVisibility(View.INVISIBLE);
                holder.mDetailImg03.setVisibility(View.INVISIBLE);
                holder.mPictureLayout.setVisibility(View.VISIBLE);
                List<String> list = mList.get(position).getImgs();
                for (int i = 0; i < list.size(); i++) {
                    switch (i) {
                        case 0:
                            mImageLoader.displayImage(list.get(i), holder.mDetailImg01,
                                    CommonUtils.initImageLoaderOptions1());
                            holder.mDetailImg01.setVisibility(View.VISIBLE);
                            break;

                        case 1:
                            mImageLoader.displayImage(list.get(i), holder.mDetailImg02,
                                    CommonUtils.initImageLoaderOptions1());
                            holder.mDetailImg02.setVisibility(View.VISIBLE);
                            break;

                        case 2:
                            mImageLoader.displayImage(list.get(i), holder.mDetailImg03,
                                    CommonUtils.initImageLoaderOptions1());
                            holder.mDetailImg03.setVisibility(View.VISIBLE);
                            break;

                        default:
                            break;
                    }
                }
            } else {
                holder.mPictureLayout.setVisibility(View.GONE);
            }

            // 更改之后的拜访记录
            mImageLoader.displayImage(mList.get(position).getHeadpic(), holder.circleImageView,
                    CommonUtils.initImageLoaderOptions());
            holder.circleImageView.setUserId(vo.getUserid());// HL:获取(记录)头像对应的用户ID，以便实现跳转
            holder.circleImageView.setUrl(vo.getHeadpic());
            holder.circleImageView.setUserName(vo.getFullName());
            holder.person_title01.setText(mList.get(position).getFullName());
            holder.person_title02.setText(mList.get(position).getDepartmentName() + "/"
                    + mList.get(position).getPositionsName());
            holder.person_title03.setText(DateUtils.parseDateDayAndHour(mList.get(position)
                    .getTime()));
            holder.visitor_content.setText(mList.get(position).getInfo());
            holder.visitor_modeName.setText("目的:" + mList.get(position).getObjectiveName());

            if (null != mList.get(position).getBid() && !"0".equals(mList.get(position).getBid())
                    && null != mList.get(position).getBname()) {
                holder.visitor_econmouser_info.setText("关联商机:" + mList.get(position).getBname());
                holder.visitor_econmouser_info.setVisibility(View.VISIBLE);
            } else {
                holder.visitor_econmouser_info.setVisibility(View.GONE);
            }

            if (null != mList.get(position).getHid() && !"0".equals(mList.get(position).getHid())
                    && null != mList.get(position).getHname()) {
                holder.link_cname_tv.setText("关联合同：" + mList.get(position).getHname());
                holder.link_cname_tv.setVisibility(View.VISIBLE);
            } else {
                holder.link_cname_tv.setVisibility(View.GONE);
            }

            if (CommonUtils.isNormalString(mList.get(position).getAddress())) {
                holder.visitor_ondretion.setVisibility(View.VISIBLE);
                holder.visitor_ondretion_layout.setVisibility(View.VISIBLE);
                holder.visitor_ondretion.setText(mList.get(position).getAddress());
            } else {
                holder.visitor_ondretion_layout.setVisibility(View.GONE);
            }

            holder.bussines_visitor_comment.setText("回复" + "(" + mList.get(position).getComment()
                    + ")");
            holder.bussines_visitor_zan.setText("赞(" + mList.get(position).getPraise() + ")");
            holder.bussines_visitor_cai.setText("踩(" + mList.get(position).getDecline() + ")");
            holder.visitor_objectiveName.setText(mList.get(position).getModeName());
            if ("1".equals(mList.get(position).getMode())) {
                holder.visitor_objectiveName.setCompoundDrawablesWithIntrinsicBounds(mContext
                        .getResources().getDrawable(R.drawable.crm_house), null, null, null);
            } else if ("2".equals(mList.get(position).getMode())) {
                holder.visitor_objectiveName.setCompoundDrawablesWithIntrinsicBounds(mContext
                        .getResources().getDrawable(R.drawable.crm_phone), null, null, null);
            } else if ("3".equals(mList.get(position).getMode())) {
                holder.visitor_objectiveName.setCompoundDrawablesWithIntrinsicBounds(mContext
                        .getResources().getDrawable(R.drawable.crm_message), null, null, null);
            } else {
                holder.visitor_objectiveName.setCompoundDrawablesWithIntrinsicBounds(mContext
                        .getResources().getDrawable(R.drawable.crm_other), null, null, null);
            }

            holder.aggree_layout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Animation scaleAnimation = new ScaleAnimation(1.0f, 2.0f, 1.0f, 2.0f);
                    if ("1".equals(mList.get(position).getIsdeclined())
                            || "1".equals(mList.get(position).getIspraised())) {
                        CustomToast.showLongToast(mContext, "已点过赞或衰");
                        return;
                    }
                    if (mList.get(position).isAgree() || mList.get(position).isOppose()) {
                        return;
                    } else {
                        holder.img_agree.setImageResource(R.drawable.agree_select);
                        // 设置动画时间
                        scaleAnimation.setDuration(500);
                        holder.img_agree.setAnimation(scaleAnimation);
                        holder.img_agree.startAnimation(scaleAnimation);
                        int praise = Integer.parseInt(mList.get(position).getPraise()) + 1;
                        mList.get(position).setPraise(praise + "");
                        mList.get(position).setAgree(true);
                        commit(Constant.CRM_CLIENT_DISSCUSS_AGREE, mList.get(position), 1);
                        notifyDataSetChanged();
                    }
                }
            });

            holder.oppose_layout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Animation scaleAnimation = new ScaleAnimation(1.0f, 2.0f, 1.0f, 2.0f);
                    if ("1".equals(mList.get(position).getIsdeclined())
                            || "1".equals(mList.get(position).getIspraised())) {
                        CustomToast.showLongToast(mContext, "已点过赞或衰");
                        return;
                    }
                    if (mList.get(position).isAgree() || mList.get(position).isOppose()) {
                        return;
                    } else {
                        holder.img_oppose.setImageResource(R.drawable.oppose_select);
                        // 设置动画时间
                        scaleAnimation.setDuration(500);
                        holder.img_oppose.setAnimation(scaleAnimation);
                        holder.img_oppose.startAnimation(scaleAnimation);

                        int oppose = Integer.parseInt(mList.get(position).getDecline()) + 1;
                        mList.get(position).setDecline(oppose + "");
                        mList.get(position).setOppose(true);
                        commit(Constant.CRM_CLIENT_DISSCUSS_OPPOSE, mList.get(position), 0);
                        notifyDataSetChanged();
                    }
                }
            });
            holder.mDetailImg01.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent();
                    intent.putExtra("imgIndex", 0);
                    List<String> list = new ArrayList<String>();
                    if (mList.get(position).getImgs().size() > 3) {
                        list.add(mList.get(position).getImgs().get(0));
                        list.add(mList.get(position).getImgs().get(1));
                        list.add(mList.get(position).getImgs().get(2));
                        intent.putStringArrayListExtra("piclist", (ArrayList<String>) list);
                    }
                    else {
                        intent.putStringArrayListExtra("piclist",
                                (ArrayList<String>) mList.get(position).getImgs());
                    }
                    intent.setClass(mContext, ImagePreviewActivity.class);
                    mContext.startActivity(intent);
                }
            });
            holder.mDetailImg02.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent();
                    intent.putExtra("imgIndex", 1);
                    List<String> list = new ArrayList<String>();
                    if (mList.get(position).getImgs().size() > 3) {
                        list.add(mList.get(position).getImgs().get(0));
                        list.add(mList.get(position).getImgs().get(1));
                        list.add(mList.get(position).getImgs().get(2));
                        intent.putStringArrayListExtra("piclist", (ArrayList<String>) list);
                    }
                    else {
                        intent.putStringArrayListExtra("piclist",
                                (ArrayList<String>) mList.get(position).getImgs());
                    }
                    intent.setClass(mContext, ImagePreviewActivity.class);
                    mContext.startActivity(intent);
                }

            });
            holder.mDetailImg03.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent();
                    intent.putExtra("imgIndex", 2);
                    List<String> list = new ArrayList<String>();
                    if (mList.get(position).getImgs().size() > 3) {
                        list.add(mList.get(position).getImgs().get(0));
                        list.add(mList.get(position).getImgs().get(1));
                        list.add(mList.get(position).getImgs().get(2));
                        intent.putStringArrayListExtra("piclist", (ArrayList<String>) list);
                    }
                    else {
                        intent.putStringArrayListExtra("piclist",
                                (ArrayList<String>) mList.get(position).getImgs());
                    }
                    intent.setClass(mContext, ImagePreviewActivity.class);
                    mContext.startActivity(intent);
                }

            });

            holder.visitor_father.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, CrmVisitRecordDetailActivity.class);
                    intent.putExtra("vid", mList.get(position).getVid());
                    // 因为商机和客户共用这个适配器,这里只判断了商机点击过来是否是0
                    if (busIsShow.equals("0")) {
                        intent.putExtra("bid_ishow", "0");
                    }
                    if (null != CrmVisitRecordDetailActivity.aCrmVisitRecordDetailActivity) {
                        CrmVisitRecordDetailActivity.aCrmVisitRecordDetailActivity.finish();
                    }

                    mContext.startActivityForResult(intent, 1);
                    // mContext.startActivity(intent);
                }
            });

        } else if (mType == 2) { // 联系人
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.lv_crm_client_home_contacts_item,
                        null);
                holder.mContastTv = (TextView) convertView.findViewById(R.id.contacts_tv);
                holder.mPostionTv = (TextView) convertView.findViewById(R.id.postion_tv);
                holder.mPhoneTv = (TextView) convertView.findViewById(R.id.phone_tv);
                holder.mImgMessage = (ImageView) convertView.findViewById(R.id.img_message);
                holder.mImgPhone = (ImageView) convertView.findViewById(R.id.img_phone);
                holder.mItemLayout = (LinearLayout) convertView.findViewById(R.id.item_layout);
                holder.mTimeTv = (TextView) convertView.findViewById(R.id.time_tv);
                holder.mText_item = (TextView) convertView.findViewById(R.id.text_item);
                holder.mHeadIcon = (BSCircleImageView) convertView.findViewById(R.id.head_icon);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.mContastTv.setText(vo.getLname());
            if (vo.getPost().equals("暂无")) {
                holder.mPostionTv.setVisibility(View.GONE);
            } else {
                holder.mPostionTv.setVisibility(View.VISIBLE);
                holder.mPostionTv.setText(vo.getPost());
            }
            holder.mPhoneTv.setText("创建人：" + vo.getFullname());
            holder.mTimeTv.setText(DateUtils.parseDateDay(vo.getAddtime()));

            if (vo.getLheadpic() != null && !vo.getLheadpic().equals("") && !vo.getLheadpic().equals("暂无")) {
                holder.mText_item.setVisibility(View.GONE);
                holder.mHeadIcon.setVisibility(View.VISIBLE);
                mImageLoader.displayImage(vo.getLheadpic(), holder.mHeadIcon, mOptions);
            }
            else {
                holder.mText_item.setVisibility(View.VISIBLE);
                holder.mHeadIcon.setVisibility(View.GONE);
                holder.mText_item.setText(vo.getNickname());
                holder.mText_item.setBackgroundDrawable(CommonUtils.setBackgroundShap(mContext, 40, "#ffffff", mNoHeadColors[colorIndex]));
                colorIndex++;
                colorIndex = colorIndex < 6 ? colorIndex : 0;
            }

            holder.mImgMessage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    CommonUtils.sendMsg(mContext, vo.getPhone()[0]);
                }

            });
            holder.mImgPhone.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    CommonUtils.call(mContext, vo.getPhone()[0]);
                }
            });

            holder.mItemLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, CrmContactDetailActivity.class);
                    // crmEdit判断联系人详情是否可以编辑
                    intent.putExtra("crmEdit", mList.get(position).getCrmEdit());
                    intent.putExtra("lid", mList.get(position).getLid());
                    mContext.startActivity(intent);
                }
            });

        } else if (mType == 3) { // 商机
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.lv_crm_client_home_business_item,
                        null);
                holder.mTileTv = (TextView) convertView.findViewById(R.id.title_tv);
                holder.mMoneyTv = (TextView) convertView.findViewById(R.id.money_tv);
                holder.mTypeTv = (TextView) convertView.findViewById(R.id.type_tv);
                holder.mTimeTv = (TextView) convertView.findViewById(R.id.time_tv);
                holder.mIsread = (ImageView) convertView.findViewById(R.id.isread);
                holder.content = (LinearLayout) convertView.findViewById(R.id.content);

                holder.mProgressTv01 = (TextView) convertView.findViewById(R.id.progress_tv_01);
                holder.mProgressTv02 = (TextView) convertView.findViewById(R.id.progress_tv_02);
                holder.mProgressTv03 = (TextView) convertView.findViewById(R.id.progress_tv_03);
                holder.mProgressTv04 = (TextView) convertView.findViewById(R.id.progress_tv_04);
                holder.mProgressTv05 = (TextView) convertView.findViewById(R.id.progress_tv_05);
                holder.mProgressTv06 = (TextView) convertView.findViewById(R.id.progress_tv_06);

                holder.mStatusTv01 = (TextView) convertView.findViewById(R.id.status_tv_01);
                holder.mStatusTv02 = (TextView) convertView.findViewById(R.id.status_tv_02);
                holder.mStatusTv03 = (TextView) convertView.findViewById(R.id.status_tv_03);
                holder.mStatusTv04 = (TextView) convertView.findViewById(R.id.status_tv_04);
                holder.mStatusTv05 = (TextView) convertView.findViewById(R.id.status_tv_05);
                holder.mStatusTv06 = (TextView) convertView.findViewById(R.id.status_tv_06);

                holder.mStatusImg01 = (TextView) convertView.findViewById(R.id.status_img_01);
                holder.mStatusImg06 = (TextView) convertView.findViewById(R.id.status_img_06);
                holder.mStatusImg02 = (ImageView) convertView.findViewById(R.id.status_img_02);
                holder.mStatusImg03 = (ImageView) convertView.findViewById(R.id.status_img_03);
                holder.mStatusImg04 = (ImageView) convertView.findViewById(R.id.status_img_04);
                holder.mStatusImg05 = (ImageView) convertView.findViewById(R.id.status_img_05);

                holder.mHeadIcon = (BSCircleImageView) convertView.findViewById(R.id.head_icon);
                holder.mDepartTv = (TextView) convertView.findViewById(R.id.depart);
                holder.mNameTv = (TextView) convertView.findViewById(R.id.name);
                holder.mTimeTv = (TextView) convertView.findViewById(R.id.time);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.content.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent mIntent = new Intent(mContext, CrmBusinessHomeIndexOneInfo.class);
                    mIntent.putExtra("bid", vo.getBid());
                    mIntent.putExtra("stateUtilthread", "2");
                    mIntent.putExtra("vo", (Serializable) vo);
                    mContext.startActivity(mIntent);
                }
            });
            holder.mTileTv.setText(vo.getBname());
            holder.mMoneyTv.setText(CommonUtils.formatDetailMoney(vo.getMoney()));

            holder.mProgressTv01.setTextColor(mContext.getResources().getColor(R.color.C6));
            holder.mProgressTv02.setTextColor(mContext.getResources().getColor(R.color.C6));
            holder.mProgressTv03.setTextColor(mContext.getResources().getColor(R.color.C6));
            holder.mProgressTv04.setTextColor(mContext.getResources().getColor(R.color.C6));
            holder.mProgressTv05.setTextColor(mContext.getResources().getColor(R.color.C6));
            holder.mProgressTv06.setTextColor(mContext.getResources().getColor(R.color.C6));

            Drawable drawableNormal = mContext.getResources().getDrawable(R.drawable.bg_circle_c3);
            drawableNormal.setBounds(0, 0, drawableNormal.getMinimumWidth(),
                    drawableNormal.getMinimumHeight()); // 设置边界
            holder.mStatusImg01.setCompoundDrawables(null, null, drawableNormal, null);
            holder.mStatusImg02.setImageDrawable(drawableNormal);
            holder.mStatusImg03.setImageDrawable(drawableNormal);
            holder.mStatusImg04.setImageDrawable(drawableNormal);
            holder.mStatusImg05.setImageDrawable(drawableNormal);
            holder.mStatusImg06.setCompoundDrawables(drawableNormal, null, null, null);

            holder.mStatusTv01.setTextColor(mContext.getResources().getColor(R.color.C6));
            holder.mStatusTv02.setTextColor(mContext.getResources().getColor(R.color.C6));
            holder.mStatusTv03.setTextColor(mContext.getResources().getColor(R.color.C6));
            holder.mStatusTv04.setTextColor(mContext.getResources().getColor(R.color.C6));
            holder.mStatusTv05.setTextColor(mContext.getResources().getColor(R.color.C6));
            holder.mStatusTv06.setTextColor(mContext.getResources().getColor(R.color.C6));

            int puttype = Integer.parseInt(CommonUtils.isNormalData(vo.getStatus()));
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.bg_circle_c7);
            switch (puttype) {
                case 1:
                    holder.mProgressTv01.setTextColor(mContext.getResources().getColor(R.color.C7));
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                            drawable.getMinimumHeight()); // 设置边界
                    holder.mStatusImg01.setCompoundDrawables(null, null, drawable, null);
                    holder.mStatusTv01.setTextColor(mContext.getResources().getColor(R.color.C7));
                    break;
                case 2:
                    holder.mProgressTv02.setTextColor(mContext.getResources().getColor(R.color.C7));
                    holder.mStatusImg02.setImageDrawable(drawable);
                    holder.mStatusTv02.setTextColor(mContext.getResources().getColor(R.color.C7));
                    break;
                case 3:
                    holder.mProgressTv03.setTextColor(mContext.getResources().getColor(R.color.C7));
                    holder.mStatusImg03.setImageDrawable(drawable);
                    holder.mStatusTv03.setTextColor(mContext.getResources().getColor(R.color.C7));
                    break;
                case 4:
                    holder.mProgressTv04.setTextColor(mContext.getResources().getColor(R.color.C7));
                    holder.mStatusImg04.setImageDrawable(drawable);
                    holder.mStatusTv04.setTextColor(mContext.getResources().getColor(R.color.C7));
                    break;
                case 5:
                    holder.mProgressTv05.setTextColor(mContext.getResources().getColor(R.color.C7));
                    holder.mStatusImg05.setImageDrawable(drawable);
                    holder.mStatusTv05.setTextColor(mContext.getResources().getColor(R.color.C7));
                    break;
                case 6:
                    holder.mProgressTv06.setTextColor(mContext.getResources().getColor(R.color.C7));
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                            drawable.getMinimumHeight()); // 设置边界
                    holder.mStatusImg06.setCompoundDrawables(drawable, null, null, null);
                    holder.mStatusTv06.setTextColor(mContext.getResources().getColor(R.color.C7));
                    break;
            }

            mImageLoader.displayImage(vo.getHeadpic(), holder.mHeadIcon, mOptions);
            holder.mHeadIcon.setUserId(vo.getUserid());// HL:获取(商机)头像对应的用户ID，以便实现跳转
            holder.mNameTv.setText(vo.getFullname());
            holder.mDepartTv.setText(vo.getDname() + "/" + vo.getPname());
            holder.mTimeTv.setText(DateUtils.parseDateDay(vo.getAddtime()));

        } else {
            // 更改之后的合同的布局
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext,
                        R.layout.crmtradecontract_index_adpter_copylist, null);
                holder.item_taskeventlistadapter_seekbar = (ProgressBar) convertView
                        .findViewById(R.id.item_taskeventlistadapter_seekbar);
                holder.work_info_why = (TextView) convertView.findViewById(R.id.work_info_why);
                holder.work_info_why_state = (TextView) convertView
                        .findViewById(R.id.work_info_why_state);
                holder.work_info_companyname = (TextView) convertView
                        .findViewById(R.id.work_info_companyname);
                holder.work_get_money = (TextView) convertView.findViewById(R.id.work_get_money);
                holder.work_get_money1 = (TextView) convertView.findViewById(R.id.work_get_money1);
                holder.item_taskeventlistadapter_content_tv = (TextView) convertView
                        .findViewById(R.id.item_taskeventlistadapter_content_tv);
                holder.work_pay_money1 = (TextView) convertView.findViewById(R.id.work_pay_money1);
                holder.work_pay_money = (TextView) convertView.findViewById(R.id.work_pay_money);
                holder.client_layout = (LinearLayout) convertView.findViewById(R.id.client_layout);
                holder.responsible_name = (TextView) convertView
                        .findViewById(R.id.responsible_name);
                holder.add_time = (TextView) convertView.findViewById(R.id.add_time);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.client_layout.setVisibility(View.GONE);

            final CrmClientHomeDetailVO tradeDetailVO = mList.get(position);
            if (mList.get(position).getPercent().equals("暂无")) {
                CommonUtils.setDifferentTextColor(holder.item_taskeventlistadapter_content_tv,
                        "收款率", "0%", "#ff0000");
            }
            else {
                CommonUtils.setDifferentTextColor(holder.item_taskeventlistadapter_content_tv,
                        "收款率：", mList.get(position).getPercent() + "%", "#ff0000");
            }

            // CommonUtils.setDifferentTextColor(holder.work_pay_money, "实收款：",
            // "￥" + CommonUtils.countNumberSecond(mList.get(position).getPayment()),
            // "#169200");
            CommonUtils.setDifferentTextColor(holder.work_pay_money, "实收款：",
                    CommonUtils.formatDetailMoney(mList.get(position).getPayment()),
                    "#169200");

            holder.work_info_why.setText(mList.get(position).getTitle());
            holder.work_info_companyname.setText(mList.get(position).getCname());
            // CommonUtils.setDifferentTextColor(holder.work_get_money, "应收款：",
            // "￥" + CommonUtils.countNumberSecond(mList.get(position).getMoney()), "#ff0000");

            CommonUtils.setDifferentTextColor(holder.work_get_money, "应收款：",
                    CommonUtils.formatDetailMoney(mList.get(position).getMoney()), "#ff0000");
            /*
             * array("1"=>"初步接洽","2"=>"需求确定","3"=>"方案报价","4"=>"谈判审核","5"=>"赢单","6"=>"放弃","7"=>"结束关闭")
             * ;
             */
            int puttype = Integer.parseInt(mList.get(position).getStatus());
            holder.work_info_why_state.setText(mList.get(position).getStatusName() + " ");

            switch (puttype) {
                case 1:
                    holder.work_info_why_state.setBackgroundDrawable(mContext.getResources()
                            .getDrawable(R.drawable.frame_shixing_blue));
                    break;
                case 2:
                    holder.work_info_why_state.setBackgroundDrawable(mContext.getResources()
                            .getDrawable(R.drawable.frame_shixing_yellow));
                    break;
                case 3:
                    holder.work_info_why_state.setBackgroundDrawable(mContext.getResources()
                            .getDrawable(R.drawable.frame_shixing_red));
                    break;
                case 4:
                    holder.work_info_why_state.setBackgroundDrawable(mContext.getResources()
                            .getDrawable(R.drawable.frame_shixing_gray));
                    break;
                default:
                    holder.work_info_why_state.setBackgroundDrawable(mContext.getResources()
                            .getDrawable(R.drawable.frame_shixing_gray));
                    break;
            }

            holder.responsible_name.setText(tradeDetailVO.getFullname());
            holder.add_time.setText(DateUtils.parseDateDay(tradeDetailVO.getEndtime()));

            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent();
                    if ("0".equals(tradeDetailVO.getStatus())
                            || "5".equals(tradeDetailVO.getStatus())) {
                        intent.setClass(mContext, CrmTradeContantDetailsIndexActivity.class);
                        intent.putExtra("hid", tradeDetailVO.getHid());
                    } else {
                        intent.setClass(mContext, CrmTradeContantDeatilsHomeTop3Activity.class);
                        intent.putExtra("hid", tradeDetailVO.getHid());
                        intent.putExtra("title", tradeDetailVO.getTitle());
                        intent.putExtra("money", tradeDetailVO.getMoney());
                        intent.putExtra("payment", tradeDetailVO.getPayment());
                        intent.putExtra("cname", tradeDetailVO.getCname());
                        intent.putExtra("statusName", tradeDetailVO.getStatusName());
                        intent.putExtra("receiptMoney", tradeDetailVO.getReceipt_money());
                        intent.putExtra("status", tradeDetailVO.getStatus());
                        intent.putExtra("changeStatus", tradeDetailVO.getChangeStatus());
                        intent.putExtra("changeStatusName", tradeDetailVO.getChangeStatusName());
                    }
                    ((FragmentActivity) mContext).startActivityForResult(intent, 1);
                }
            });

        }
        return convertView;
    }

    static class ViewHolder {
        private TextView mTileTv, mStateTv, mTypeTv, mTimeTv, mIsreadTv, mVisitType, mModelType;
        private ImageView mReadBt, mIsread;

        // 记录布局
        private TextView mPersonTitle01, mPersonTitle02, mPersonTitle03, mPersonTitle04,
                mContentTv, link_cname_tv;
        private TextView mLinkBNametTv, mLinkCNametTv, mLinkAddressTv, mReplayTv, mPraiseTv,
                mDeclineTv;
        private BSCircleImageView mHeadIcon;
        private ImageView mImgAgree, mImgOppose;
        private LinearLayout mAgreeLayout, mOpposeLayout, mReplayLayout, content, clientfather;

        // 联系人布局
        private TextView mContastTv, mPostionTv, mPhoneTv, mText_item;
        private ImageView mImgMessage, mImgPhone;
        private LinearLayout mItemLayout, mPictureLayout;

        private ImageView mDetailImg01, mDetailImg02, mDetailImg03;// 列表页存放三张图片

        // 改更之后的布局控件 拜访记录
        private BSCircleImageView circleImageView;
        private TextView person_title01, person_title02,
                person_title03, visitor_content, visitor_modeName,
                visitor_econmouser_info, visitor_ondretion, visitor_objectiveName,
                bussines_visitor_comment, bussines_visitor_zan, bussines_visitor_cai;
        private ImageView img_agree, img_oppose;
        private LinearLayout aggree_layout, oppose_layout, visitor_father,
                visitor_ondretion_layout;

        // 商机布局
        private TextView mMoneyTv;
        private TextView mProgressTv01, mProgressTv02, mProgressTv03, mProgressTv04, mProgressTv05,
                mProgressTv06;
        private TextView mStatusTv01, mStatusTv02, mStatusTv03, mStatusTv04, mStatusTv05,
                mStatusTv06;
        private TextView mStatusImg01, mStatusImg06;
        private ImageView mStatusImg02, mStatusImg03, mStatusImg04, mStatusImg05;
        private TextView mDepartTv, mNameTv;

        // 改更之后的布局控件 合同列表
        private ProgressBar item_taskeventlistadapter_seekbar;
        private TextView work_info_why, work_info_why_state,
                work_info_companyname, work_get_money1, work_get_money,
                item_taskeventlistadapter_content_tv, work_pay_money1,
                work_pay_money, responsible_name, add_time;
        private LinearLayout client_layout;
    }

    public void updateData(List<CrmClientHomeDetailVO> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataFrist(List<CrmClientHomeDetailVO> list) {
        list.addAll(mList);
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataLast(List<CrmClientHomeDetailVO> list) {
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public void commit(String url, CrmClientHomeDetailVO discussVO, int status) {
        RequestParams params = new RequestParams();
        try {
            params.put("ftoken", BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("rid", discussVO.getVid());
            params.put("type", "1");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();

        client.post(BSApplication.getInstance().getHttpTitle() + url, params,
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

                    }

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(new String(arg2));
                            String code = (String) jsonObject.get("code");
                            if (Constant.RESULT_CODE.equals(code)) {
                            } else {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public CrmClientHomeDetailVO getDetaiVo() {
        return detaiVo;
    }

    public void setDetaiVo(CrmClientHomeDetailVO detaiVo) {
        this.detaiVo = detaiVo;
    }

    public String getBusIsShow() {
        return busIsShow;
    }

    public void setBusIsShow(String busIsShow) {
        this.busIsShow = busIsShow;
    }

}
