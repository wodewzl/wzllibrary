
package com.bs.bsims.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.PersonHeadVO;

import java.util.ArrayList;

public class BossHeadAdapter extends BaseAdapter {
    public ArrayList<PersonHeadVO> mList;
    private int mState;
    private Context mContext;

    public BossHeadAdapter(Context context, int state) {
        // TODO Auto-generated constructor stub
        mList = new ArrayList<PersonHeadVO>();
        mState = state;
        mContext = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            // convertView = mView;
            // convertView = View.inflate(mContext, R.layout.send_person_gv_item, null);
            convertView = View.inflate(mContext, R.layout.send_person_gv_item, null);

            holder.personHead = (ImageView) convertView.findViewById(R.id.person_head);
            holder.personName = (TextView) convertView.findViewById(R.id.person_name);
            // if (mState == 1) {
            // holder.notifyInfo = (ImageView) convertView.findViewById(R.id.delete_icon);
            // holder.notifyInfo.setVisibility(View.VISIBLE);
            // } else if (mState == 2) {
            // holder.notifyInfo = (ImageView) convertView.findViewById(R.id.read_state);
            // holder.notifyInfo.setVisibility(View.VISIBLE);
            // }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            // holder.notifyInfo.setImageResource(R.drawable.delete_icon);
        }
        // if (mState == 1) {
        // holder.notifyInfo.setImageResource(R.drawable.delete_icon);
        // } else if (mState == 2) {
        // holder.notifyInfo.setImageResource(R.drawable.read_state);
        // }
        holder.personHead.setImageResource(R.drawable.head_icon);
        holder.personName.setText(mList.get(position).getName());

        return convertView;
    }

    static class ViewHolder
    {
        private ImageView personHead;
        private TextView personName;
        private ImageView notifyInfo;
    }

}
