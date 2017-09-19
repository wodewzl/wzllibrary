
package com.beisheng.synews.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beisheng.base.adapter.BSBaseAdapter;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.synews.mode.LiveVO;
import com.im.zhsy.R;

public class LiveFragmentAdapter extends BSBaseAdapter<LiveVO> {

    public LiveFragmentAdapter(Context context) {
        super(context);
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (mIsEmpty) {
            return super.getView(position, convertView, parent);
        }

        if (convertView != null && convertView.getTag() == null)
            convertView = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.live_fragment_adapter, null);
            holder.titleTv = (TextView) convertView.findViewById(R.id.title_tv);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.readTv = (TextView) convertView.findViewById(R.id.read_tv);
            holder.commentTv = (TextView) convertView.findViewById(R.id.comment_tv);
            holder.statusTv = (TextView) convertView.findViewById(R.id.status_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        LiveVO vo = (LiveVO) mList.get(position);
        holder.titleTv.setText(vo.getTitle());

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < vo.getName().length; i++) {
            sb.append(vo.getName()[i]);
            if (vo.getName().length - 1 != i)
                sb.append(",");
        }
        holder.readTv.setText(vo.getRead());
        holder.commentTv.setText(vo.getComments());
        holder.statusTv.setText(vo.getState());
        holder.statusTv.setBackground(BaseCommonUtils.setBackgroundShap(mContext, 20, "#FF780A", "#FF780A"));
        mImageLoader.displayImage(vo.getImg(), holder.img, mOptions);
        return convertView;
    }

    static class ViewHolder {
        private ImageView img;
        private TextView readTv, commentTv, statusTv, titleTv;
    }
}
