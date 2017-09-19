
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.ApprovalViewActivity;
import com.bs.bsims.activity.CreativeActivity;
import com.bs.bsims.activity.CrmApprovalPaymentActivity;
import com.bs.bsims.activity.CrmTradeContantHomeListActivity;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.NeedDoVO;
import com.bs.bsims.onekey.remove.WaterDrop;
import com.bs.bsims.utils.CommonUtils;

public class BossNeedDoAdapter extends BSBaseAdapter<NeedDoVO> {

    public BossNeedDoAdapter(Context context) {
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
        NeedDoVO vo = mList.get(position);
        if (convertView != null && convertView.getTag() == null)
            convertView = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.boss_need_do_adapter, null);
            holder.mItemImg = (ImageView) convertView.findViewById(R.id.item_icon);
            holder.mItemTitleTv = (TextView) convertView.findViewById(R.id.item_title);
            holder.mItemDepictTv = (TextView) convertView.findViewById(R.id.item_depict);
            holder.mItemWaterDrop = (WaterDrop) convertView.findViewById(R.id.item_water_drop);
            holder.mItemLayout = (LinearLayout) convertView.findViewById(R.id.item_layout);
            holder.mTimeTv = (TextView) convertView.findViewById(R.id.item_tv);
            holder.mCountTv = (TextView) convertView.findViewById(R.id.count_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String type = vo.getType();
        holder.mItemWaterDrop.setTag(type);
        holder.mItemTitleTv.setText(vo.getTypename());
        holder.mTimeTv.setText(vo.getDate());
        holder.mItemDepictTv.setText(vo.getDescript());

        // holder.mItemImg.setBadgeNumber(Integer.parseInt(CommonUtils.isNormalCount(vo.getCount())));
        holder.mCountTv.setText(vo.getCount());
        holder.mCountTv.setBackground(CommonUtils.setBackgroundShap(mContext, 100, R.color.C9, R.color.C9));

        final Intent intent = new Intent();
        intent.putExtra("todo", "1");
        intent.putExtra("msg", "1");
        switch (Integer.parseInt(type)) {

            case 10:
                // 意见分享
                holder.mItemImg.setImageResource(R.drawable.affair003);
                intent.putExtra("type", "1");
                intent.putExtra("isboss", CommonUtils.getLimitsSpecial(Constant.LIMITS_SPECIAL003));
                intent.putExtra("isall", "1");
                intent.putExtra("isadd", "0");
                intent.putExtra("todo", "1");
                intent.setClass(mContext, CreativeActivity.class);

                break;

            case 7:
                // 审批事务
                holder.mItemImg.setImageResource(R.drawable.affair001);
                intent.putExtra("isall", "1");
                intent.putExtra("modeid", "0");
                intent.putExtra("isboss", BSApplication.getInstance().getUserFromServerVO().getIsboss());
                intent.setClass(mContext, ApprovalViewActivity.class);
                break;

            case 14:
                // 合同
                holder.mItemImg.setImageResource(R.drawable.home_crm_traded);
                intent.setClass(mContext, CrmTradeContantHomeListActivity.class);
                break;

            case 17:
                // 回款审批
                holder.mItemImg.setImageResource(R.drawable.home_crm_repayment);
                intent.setClass(mContext, CrmApprovalPaymentActivity.class);
                break;

            default:
                break;
        }

        holder.mItemLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (intent.getComponent() != null)
                    mContext.startActivity(intent);
            }
        });

        return convertView;

    }

    static class ViewHolder {
        private ImageView mItemImg;
        private WaterDrop mItemWaterDrop;
        private TextView mItemTitleTv, mItemDepictTv, mTimeTv, mCountTv;
        private LinearLayout mItemLayout;
    }

}
