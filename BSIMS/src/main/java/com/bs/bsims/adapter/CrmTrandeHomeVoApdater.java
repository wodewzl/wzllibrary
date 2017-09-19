
package com.bs.bsims.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.CrmVisitRecordDetailActivity;
import com.bs.bsims.activity.ImagePreviewActivity;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.CrmTrandeHomeVo;
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

import java.util.ArrayList;
import java.util.List;

public class CrmTrandeHomeVoApdater extends BaseAdapter {
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private Context mContext;
    public List<CrmTrandeHomeVo> mList;
    private String Top3 = "";

    public CrmTrandeHomeVoApdater(Context context,
            List<CrmTrandeHomeVo> list1) {
        this.mContext = context;
        this.mList = list1;
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();
    }

    public CrmTrandeHomeVoApdater(Context context, String top3) {
        this.mContext = context;
        this.mList = new ArrayList<CrmTrandeHomeVo>();
        this.Top3 = top3;
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return mList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {

        /**
         * 抛异常处理
         */

        /**
         * 合同下的拜访记录
         */
        final ViewHolder holder;
        if (Top3.equals("1")) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.crm_visitor_deatils_list, null);
                holder.circleImageView = (BSCircleImageView) convertView.findViewById(R.id.head_iconbasic);
                holder.person_title01 = (TextView) convertView.findViewById(R.id.full_name);
                holder.person_title02 = (TextView) convertView.findViewById(R.id.full_dpatername);
                holder.person_title03 = (TextView) convertView.findViewById(R.id.work_info_why_state);
                holder.visitor_content = (TextView) convertView.findViewById(R.id.visitor_content);
                holder.visitor_modeName = (TextView) convertView.findViewById(R.id.visitor_modeName);
                holder.visitor_econmouser_info = (TextView) convertView.findViewById(R.id.visitor_econmouser_info);
                holder.visitor_ondretion = (TextView) convertView.findViewById(R.id.visitor_ondretion);
                holder.visitor_objectiveName = (TextView) convertView.findViewById(R.id.visitor_objectivenname);
                holder.bussines_visitor_comment = (TextView) convertView.findViewById(R.id.bussines_visitor_comment);
                holder.bussines_visitor_zan = (TextView) convertView.findViewById(R.id.praise_tv);
                holder.bussines_visitor_cai = (TextView) convertView.findViewById(R.id.decline_tv);
                holder.img_agree = (ImageView) convertView.findViewById(R.id.img_agree);
                holder.img_oppose = (ImageView) convertView.findViewById(R.id.img_oppose);
                holder.aggree_layout = (LinearLayout) convertView.findViewById(R.id.aggree_layout);
                holder.oppose_layout = (LinearLayout) convertView.findViewById(R.id.oppose_layout);
                holder.mDetailImg01 = (ImageView) convertView.findViewById(R.id.detial_img_01);
                holder.mDetailImg02 = (ImageView) convertView.findViewById(R.id.detial_img_02);
                holder.mDetailImg03 = (ImageView) convertView.findViewById(R.id.detial_img_03);
                holder.mPictureLayout = (LinearLayout) convertView.findViewById(R.id.picture_layout);
                holder.visitor_father = (LinearLayout) convertView.findViewById(R.id.visitor_father);
                holder.link_cname_tv = (TextView) convertView.findViewById(R.id.link_cname_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            mImageLoader.displayImage(mList.get(position).getHeadpic(), holder.circleImageView, CommonUtils.initImageLoaderOptions());
            holder.circleImageView.setUserId(mList.get(position).getUid());//HL:获取头像对应的用户ID，以便响应跳转
            holder.person_title01.setText(mList.get(position).getFullName());
            holder.person_title02.setText(mList.get(position).getDepartmentName() + "/" + mList.get(position).getPositionsName());
            holder.person_title03.setText(DateUtils.parseDateDayAndHour(mList.get(position).getTime()));
            holder.visitor_content.setText(mList.get(position).getInfo());
            holder.visitor_modeName.setText("目的:"+mList.get(position).getObjectiveName());
            if (null != mList.get(position).getBid() &&!"0".equals(mList.get(position).getBid())&& null != mList.get(position).getBname()) {
                holder.visitor_econmouser_info.setText("关联商机:" + mList.get(position).getBname());
                holder.visitor_econmouser_info.setVisibility(View.VISIBLE);
            } else {
                holder.visitor_econmouser_info.setVisibility(View.GONE);
            }
//            if (!"0".equals(mList.get(position).getCid())) {
//                holder.link_cname_tv.setText("关联客户:" + mList.get(position).getCname());
//                holder.link_cname_tv.setVisibility(View.VISIBLE);
//            } else {
//                holder.link_cname_tv.setVisibility(View.GONE);
//            }
            if (null != mList.get(position).getHid() && !"0".equals(mList.get(position).getHid()) && null != mList.get(position).getHname()) {
                holder.link_cname_tv.setText("关联合同：" + mList.get(position).getHname());
                holder.link_cname_tv.setVisibility(View.VISIBLE);
            } else {
                holder.link_cname_tv.setVisibility(View.GONE);
            }

            if (null != mList.get(position).getAddress() && !mList.get(position).getAddress().equals(R.string.error_php_interface)) {
                holder.visitor_ondretion.setVisibility(View.VISIBLE);
                holder.visitor_ondretion.setText(mList.get(position).getAddress());
            }
            else{
                holder.visitor_ondretion.setVisibility(View.GONE);
            }

            holder.bussines_visitor_comment.setText("回复" + "(" + mList.get(position).getComment() + ")");
            holder.bussines_visitor_zan.setText("赞(" + mList.get(position).getPraise() + ")");
            holder.bussines_visitor_cai.setText("踩(" + mList.get(position).getDecline() + ")");

            holder.visitor_objectiveName.setText(mList.get(position).getModeName());
            if ("1".equals(mList.get(position).getMode())) {
                holder.visitor_objectiveName.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.crm_house), null, null, null);
            } else if ("2".equals(mList.get(position).getMode())) {
                holder.visitor_objectiveName.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.crm_phone), null, null, null);
            } else if ("3".equals(mList.get(position).getMode())) {
                holder.visitor_objectiveName.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.crm_message), null, null, null);
            } else {
                holder.visitor_objectiveName.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.crm_other), null, null, null);
            }
            
            
            
            
            
            
            if (mList.get(position).getImgs() != null) {
                holder.mDetailImg01.setVisibility(View.INVISIBLE);
                holder.mDetailImg02.setVisibility(View.INVISIBLE);
                holder.mDetailImg03.setVisibility(View.INVISIBLE);
                holder.mPictureLayout.setVisibility(View.VISIBLE);
                List<String> list = mList.get(position).getImgs();
                for (int i = 0; i < list.size(); i++) {
                    if (i > 2) {
                        break;
                    }
                    switch (i) {
                        case 0:
                            mImageLoader.displayImage(list.get(i), holder.mDetailImg01, CommonUtils.initImageLoaderOptions1());
                            holder.mDetailImg01.setVisibility(View.VISIBLE);
                            break;

                        case 1:
                            mImageLoader.displayImage(list.get(i), holder.mDetailImg02, CommonUtils.initImageLoaderOptions1());
                            holder.mDetailImg02.setVisibility(View.VISIBLE);
                            break;

                        case 2:
                            mImageLoader.displayImage(list.get(i), holder.mDetailImg03, CommonUtils.initImageLoaderOptions1());
                            holder.mDetailImg03.setVisibility(View.VISIBLE);
                            break;

                        default:
                            break;
                    }
                }
            } else {
                holder.mPictureLayout.setVisibility(View.GONE);
            }


            holder.bussines_visitor_comment.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, CrmVisitRecordDetailActivity.class);
                    intent.putExtra("vid", mList.get(position).getVid());
                    mContext.startActivity(intent);
                }
            });

            holder.aggree_layout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Animation scaleAnimation = new ScaleAnimation(1.0f, 2.0f, 1.0f, 2.0f);
                    if ("1".equals(mList.get(position).getIsdeclined()) || "1".equals(mList.get(position).getIspraised())) {
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
                    if ("1".equals(mList.get(position).getIsdeclined()) || "1".equals(mList.get(position).getIspraised())) {
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
                    // intent.putStringArrayListExtra("piclist", (ArrayList<String>) strlist);
                    List<String> list = new ArrayList<String>();
                    if (mList.get(position).getImgs().size() > 3) {
                        // intent.putStringArrayListExtra("piclist", (ArrayList<String>)
                        // mList.get(position).getImgs());
                        list.add(mList.get(position).getImgs().get(0));
                        list.add(mList.get(position).getImgs().get(1));
                        list.add(mList.get(position).getImgs().get(2));
                        intent.putStringArrayListExtra("piclist", (ArrayList<String>) list);
                    }
                    else {
                        intent.putStringArrayListExtra("piclist", (ArrayList<String>) mList.get(position).getImgs());
                    }
                    intent.setClass(mContext, ImagePreviewActivity.class);
                    mContext.startActivity(intent);
                }
            });
            holder.mDetailImg02.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent();
                    intent.putExtra("imgIndex", 1);
                    // intent.putStringArrayListExtra("piclist", (ArrayList<String>) strlist);
                    List<String> list = new ArrayList<String>();
                    if (mList.get(position).getImgs().size() > 3) {
                        // intent.putStringArrayListExtra("piclist", (ArrayList<String>)
                        // mList.get(position).getImgs());
                        list.add(mList.get(position).getImgs().get(0));
                        list.add(mList.get(position).getImgs().get(1));
                        list.add(mList.get(position).getImgs().get(2));
                        intent.putStringArrayListExtra("piclist", (ArrayList<String>) list);
                    }
                    else {
                        intent.putStringArrayListExtra("piclist", (ArrayList<String>) mList.get(position).getImgs());
                    }
                    intent.setClass(mContext, ImagePreviewActivity.class);
                    mContext.startActivity(intent);
                }

            });
            holder.mDetailImg03.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent();
                    intent.putExtra("imgIndex", 2);
                    // intent.putStringArrayListExtra("piclist", (ArrayList<String>) strlist);
                    List<String> list = new ArrayList<String>();
                    if (mList.get(position).getImgs().size() > 3) {
                        // intent.putStringArrayListExtra("piclist", (ArrayList<String>)
                        // mList.get(position).getImgs());
                        list.add(mList.get(position).getImgs().get(0));
                        list.add(mList.get(position).getImgs().get(1));
                        list.add(mList.get(position).getImgs().get(2));
                        intent.putStringArrayListExtra("piclist", (ArrayList<String>) list);
                    }
                    else {
                        intent.putStringArrayListExtra("piclist", (ArrayList<String>) mList.get(position).getImgs());
                    }
                    intent.setClass(mContext, ImagePreviewActivity.class);
                    mContext.startActivity(intent);
                }

            });
            
            
            
            
            
            
            holder.visitor_father.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent();
                    intent.setClass(mContext, CrmVisitRecordDetailActivity.class);
                    intent.putExtra("vid", mList.get(position).getVid());
                    mContext.startActivity(intent);
                }
            });
            
            
            
            
            
            
            
            
            
            

        }

        /**
         * 合同下的回款记录
         */
        else if (Top3.equals("2")) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.crmtradecontant_home_adpter_index2, null);
                holder.recorde_name = (TextView) convertView.findViewById(R.id.recorde_name);
                holder.record_time = (TextView) convertView.findViewById(R.id.record_time);
                holder.recorde_moneystate = (TextView) convertView.findViewById(R.id.recorde_moneystate);
                holder.record_states = (TextView) convertView.findViewById(R.id.record_states);
                holder.recorde_money = (TextView) convertView.findViewById(R.id.recorde_money);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.recorde_name.setText(mList.get(position).getFullname());
            holder.record_time.setText(DateUtils.parseDateDay(mList.get(position).getPlanned_date()) + "(" + mList.get(position).getPeriods() + ")");
            holder.recorde_money.setText("￥" + mList.get(position).getMoney());
            holder.recorde_moneystate.setText(mList.get(position).getPayment_mode());
            try {
                int key = Integer.parseInt(mList.get(position).getReceipt());
                switch (key) {
                    case 0:
                        holder.record_states.setBackgroundDrawable(mContext.getResources()
                                .getDrawable(R.drawable.corners_gray));
                        holder.record_states.setText("未开票");
                        break;

                    case 1:
                        holder.record_states.setBackgroundDrawable(mContext.getResources()
                                .getDrawable(R.drawable.corners_green));
                        holder.record_states.setText("已开票");
                        break;

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        /**
         * 合同下的产品记录
         */
        else if (Top3.equals("3")) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.crmproduct_management_listadapter, null);
                holder.crm_product_name = (TextView) convertView.findViewById(R.id.crm_product_name);
                holder.crm_product_money = (TextView) convertView.findViewById(R.id.crm_product_money);
                holder.crm_product_num = (TextView) convertView.findViewById(R.id.crm_product_num);
                holder.crm_product_allmoney = (TextView) convertView.findViewById(R.id.crm_product_allmoney);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.crm_product_name.setText(Html.fromHtml("<font size=\"2\" color=\"gray\">产品: </font><font size=\"4\" color=\"#000000\">" + mList.get(position).getName() + "</font>"));
            holder.crm_product_money.setText("单价: ￥" + mList.get(position).getMoney());
            holder.crm_product_num.setText("数量: " + mList.get(position).getCount());
            holder.crm_product_allmoney.setText(Html.fromHtml("<font size=\"2\" color=\"gray\">总价:  </font><font size=\"4\" color=\"#00AAFE\"><big>" + "￥" + mList.get(position).getTotalPrice() + "</big></font>"));
        }

        return convertView;
    }

    static class ViewHolder {

        private BSCircleImageView circleImageView;
        private TextView person_title01, person_title02,link_cname_tv,
                person_title03, visitor_content, visitor_modeName,
                visitor_econmouser_info, visitor_ondretion, visitor_objectiveName, bussines_visitor_comment, bussines_visitor_zan, bussines_visitor_cai;
        private ImageView img_agree, img_oppose;
        private LinearLayout aggree_layout, oppose_layout,mPictureLayout,visitor_father;
        private ImageView mDetailImg01, mDetailImg02, mDetailImg03;// 列表页存放三张图片

        /* 产品 */
        private TextView crm_product_name, crm_product_money, crm_product_num, crm_product_allmoney;

        /* 回款记录 */
        private TextView recorde_name, record_time, recorde_moneystate, record_states, recorde_money;
        

    }

    public void updateData(List<CrmTrandeHomeVo> list) {
        if (null == mList) {
            mList = new ArrayList<CrmTrandeHomeVo>();
        }
        if (null == list) {
            return;
        }
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void updateDataFrist(List<CrmTrandeHomeVo> list) {
        list.addAll(mList);
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void updateDataLast(List<CrmTrandeHomeVo> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void commit(String url, CrmTrandeHomeVo discussVO, int status) {
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

        client.post(BSApplication.getInstance().getHttpTitle() + url, params, new AsyncHttpResponseHandler() {

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

}
