
package com.bs.bsims.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.DanganWorkTransfer;
import com.bs.bsims.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class DanganAllWorkChilrdTransferApdater extends BaseAdapter {
    private Context mContext;
    public List<DanganWorkTransfer> mList;

    public DanganAllWorkChilrdTransferApdater(Context context) {
        mContext = context;
        mList = new ArrayList<DanganWorkTransfer>();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext,
                    R.layout.da_workallchrildtrianfer_apdater, null);
            holder.mTitleTime = (TextView) convertView.findViewById(R.id.title_time);
            holder.mTPname = (TextView) convertView.findViewById(R.id.tranfer_dname);
            holder.mTReason = (TextView) convertView.findViewById(R.id.tranfer_reason);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 给值
        holder.mTitleTime.setText(DateUtils.parseDateDay(mList.get(position)
                .getTime()));

        holder.mTPname.setText(Html.fromHtml("从" + "<font size=\"14\" color=\"#13A3F8\">"
                + mList.get(position).getDnameold() + "/" + mList.get(position).getPnameold() + "</font>" + "<br/>" + "调入"
                + "<font size=\"14\" color=\"#13A3F8\">"
                + mList.get(position).getDnamenew() + "</font>" + "担任" + "<font size=\"14\" color=\"#13A3F8\">"
                + mList.get(position).getPnamenew() + "</font>"));
        // holder.zwnamenew.setText(mList.get(position).getZwnamenew()); #148CF0\
        holder.mTReason.setText("调岗原因："+mList.get(position).getReason());
        return convertView;
    }

    static class ViewHolder {
        private TextView mTitleTime, mTPname, mTReason;

    }
}
