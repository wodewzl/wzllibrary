
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.CrmListVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.DateUtils;

public class CrmListAdapter extends BSBaseAdapter<CrmListVO> {
    private Context mContext;

    public CrmListAdapter(Context context) {
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

        if (convertView != null && convertView.getTag() == null)
            convertView = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.lv_crm_item, null);
            holder.mTileTv = (TextView) convertView.findViewById(R.id.title_tv);
            holder.mStateTv = (TextView) convertView.findViewById(R.id.state_tv);
            holder.mTypeTv = (TextView) convertView.findViewById(R.id.type_tv);
            holder.mTimeTv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.mIsread = (ImageView) convertView.findViewById(R.id.isread);
            holder.mItemBottomLayout = (LinearLayout) convertView.findViewById(R.id.item_bottom_layout);
            holder.mContactsCount = (TextView) convertView.findViewById(R.id.contacts_count);
            holder.mBusinessCount = (TextView) convertView.findViewById(R.id.business_count);
            holder.mContractCount = (TextView) convertView.findViewById(R.id.contract_count);
            holder.mIsSign = (TextView) convertView.findViewById(R.id.issign);
            holder.mVisitState = (TextView) convertView.findViewById(R.id.customer_vstate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CrmListVO vo = mList.get(position);
        holder.mTileTv.setText(vo.getName());
        holder.mTimeTv.setText(DateUtils.parseDateDay(vo.getAddtime()));
        holder.mStateTv.setText(vo.getLevel());
        // holder.mTypeTv.setText("负责人：" + vo.getFullname());

        if ("1".equals(vo.getIsPub())) {
            CommonUtils.setDifferentTextColor(holder.mTypeTv, "上次负责人: ", vo.getFullname(), "#00a9fe");
        } else {
            CommonUtils.setDifferentTextColor(holder.mTypeTv, "负责人: ", vo.getFullname(), "#00a9fe");

        }

        holder.mIsSign.setBackgroundDrawable(CommonUtils.setBackgroundShap(mContext, 40, "#ffffff", "#EE1100"));
        holder.mVisitState.setText(vo.getVisitdes());
        if ("A级客户".equals(vo.getLevel())) {
            holder.mStateTv.setBackgroundResource(R.drawable.frame_shixing_yellow);
        } else if ("B级客户".equals(vo.getLevel())) {
            holder.mStateTv.setBackgroundResource(R.drawable.frame_shixing_blue);
        } else if ("C级客户".equals(vo.getLevel())) {
            holder.mStateTv.setBackgroundResource(R.drawable.frame_shixing_green);
        } else {
            holder.mStateTv.setBackgroundResource(R.drawable.frame_shixing_blue_light);
        }

        if (vo.getIsread() != null) {
            if ("1".equals(vo.getIsread())) {
                holder.mIsread.setVisibility(View.GONE);
            } else {
                holder.mIsread.setVisibility(View.VISIBLE);
            }
        }

        // 是否签单
        if (vo.getIssigned() == null || vo.getIssigned().equals("0"))
            holder.mIsSign.setVisibility(View.GONE);
        else
            holder.mIsSign.setVisibility(View.VISIBLE);

        holder.mContactsCount.setText(vo.getContactsCount());
        holder.mBusinessCount.setText(vo.getBusinessCount());
        holder.mContractCount.setText(vo.getContractCount());
        return convertView;
    }

    static class ViewHolder {
        private TextView mTileTv, mStateTv, mTypeTv, mTimeTv, mIsreadTv, mIsSign, mVisitState;
        private ImageView mReadBt, mIsread;
        private LinearLayout content, mItemBottomLayout;
        private View noContentView;
        private TextView mContactsCount, mBusinessCount, mContractCount;
    }

}
