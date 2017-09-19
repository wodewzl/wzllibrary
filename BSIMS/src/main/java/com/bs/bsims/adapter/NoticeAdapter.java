
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.imageLoad.ImageLoaderUtil;
import com.bs.bsims.model.PublishVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.view.BSCirleDwLoadProgrossView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class NoticeAdapter extends BaseAdapter {
    private Context mContext;
    public List<PublishVO> mList;
    public String mSortid;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private int width;
    private ImageLoaderUtil mImageLoaderUtil;

    public NoticeAdapter(Context context, String sortid) {

        this.mContext = context;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        width = wm.getDefaultDisplay().getWidth();
        this.mList = new ArrayList<PublishVO>();
        this.mSortid = sortid;
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions1();
        this.mImageLoaderUtil = new ImageLoaderUtil(context);
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

    @SuppressLint("NewApi")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // 3通知11公文12制动19企业文化
        final ViewHolder holder;
        String date = "";
        if ("3".equals(mSortid) || "11".equals(mSortid) || "12".equals(mSortid)) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.notice_list, null);
                holder.mTileTv = (TextView) convertView.findViewById(R.id.title_tv);
                holder.mStateTv = (TextView) convertView.findViewById(R.id.state_tv);
                holder.mTypeTv = (TextView) convertView.findViewById(R.id.type_tv);
                holder.mTimeTv = (TextView) convertView.findViewById(R.id.time_tv);
                holder.mReadBt = (ImageView) convertView.findViewById(R.id.read_img);
                holder.mIsread = (ImageView) convertView.findViewById(R.id.isread);
                holder.mRead = (TextView) convertView.findViewById(R.id.read);
                holder.mNoRead = (TextView) convertView.findViewById(R.id.no_read);
                holder.mContent = (TextView) convertView.findViewById(R.id.corporate_content_readinfo);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.mTileTv.setText(mList.get(position).getTitle());
            if ("1".endsWith(mList.get(position).getIsread())) {
                holder.mIsread.setVisibility(View.GONE);
            } else {
                holder.mIsread.setVisibility(View.VISIBLE);
            }

            if ("11".equals(mSortid)) {
                if (mList.get(position).getClassid() != null) {
                    if ("1".equals(mList.get(position).getClassid())) {
                        holder.mTypeTv.setText("会议记录");
                        holder.mTypeTv.setBackgroundResource(R.drawable.frame_kongxin_blue);
                        holder.mTypeTv.setTextColor(Color.parseColor("#00a9fe"));
                    } else if ("2".equals(mList.get(position).getClassid())) {
                        holder.mTypeTv.setText("公   告");
                        holder.mTypeTv.setTextColor(Color.parseColor("#FFBA00"));
                        holder.mTypeTv.setBackgroundResource(R.drawable.frame_kongxin_yellow);
                    } else if ("3".equals(mList.get(position).getClassid())) {
                        holder.mTypeTv.setText("工作报告");
                        holder.mTypeTv.setBackgroundResource(R.drawable.frame_kongxin_green);
                        holder.mTypeTv.setTextColor(Color.parseColor("#019A3F"));
                    } else {
                        holder.mTypeTv.setText("系统公告");
                        holder.mTypeTv.setBackgroundResource(R.drawable.frame_kongxin_red);
                        holder.mTypeTv.setTextColor(Color.parseColor("#FF0000"));
                    }
                }
            } else if ("3".equals(mSortid)) {
                if (mList.get(position).getNoticeid() != null) {
                    if ("1".equals(mList.get(position).getNoticeid())) {
                        holder.mTypeTv.setText("人事通知");
                        holder.mTypeTv.setBackgroundResource(R.drawable.frame_kongxin_blue);
                        holder.mTypeTv.setTextColor(Color.parseColor("#00a9fe"));

                    } else {
                        holder.mTypeTv.setText("行政通知");
                        holder.mTypeTv.setBackgroundResource(R.drawable.frame_kongxin_yellow);
                        holder.mTypeTv.setTextColor(Color.parseColor("#FFBA00"));
                    }
                }
            } else if ("12".equals(mSortid)) {
                if (mList.get(position).getNoticeid() != null) {
                    if ("1".equals(mList.get(position).getNoticeid())) {
                        holder.mTypeTv.setText("人事制度");
                        holder.mTypeTv.setBackgroundResource(R.drawable.frame_kongxin_blue);
                        holder.mTypeTv.setTextColor(Color.parseColor("#00a9fe"));

                    } else {
                        holder.mTypeTv.setText("行政制度");
                        holder.mTypeTv.setBackgroundResource(R.drawable.frame_kongxin_yellow);
                        holder.mTypeTv.setTextColor(Color.parseColor("#FFBA00"));
                    }
                }
            }
            holder.mRead.setText(mList.get(position).getRead());
            holder.mNoRead.setText(mList.get(position).getNoread());
            long time = Long.valueOf(mList.get(position).getTime()) * 1000;
            holder.mTimeTv.setText(DateUtils.parseDate(time));
            holder.mContent.setText(mList.get(position).getContent());
            if ("1".equals(mList.get(position).getIsannex())) {
                holder.mStateTv.setVisibility(View.VISIBLE);
            } else {
                holder.mStateTv.setVisibility(View.GONE);
            }

            return convertView;
        }

        // 新企业文化
        else if ("19".equals(mSortid)) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.corporate_culture_details_one, null);
                holder.mTileTv = (TextView) convertView.findViewById(R.id.corporate_title_content);
                holder.mTimeTv = (TextView) convertView.findViewById(R.id.corporate_title_time);
                holder.mReadBt = (ImageView) convertView.findViewById(R.id.corporate_content_imgtost);
                holder.mTypeTv = (TextView) convertView.findViewById(R.id.corporate_content_readinfo);
                holder.mIsreadTv = (TextView) convertView.findViewById(R.id.isread_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.mTileTv.setText(mList.get(position).getTitle());
            if ("1".equals(mList.get(position).getIsread())) {
                holder.mIsreadTv.setTextColor(Color.parseColor("#A3A3A3"));
            } else {
                holder.mIsreadTv.setTextColor(Color.parseColor("#000000"));
            }
            long time = Long.valueOf(mList.get(position).getTime()) * 1000;
            holder.mTimeTv.setText(DateUtils.parseDate(time));
            String url = mList.get(position).getPictures();

            if (url.equals("暂无") || "gif".equalsIgnoreCase(url.substring(url.lastIndexOf(".") + 1, url.length()))) {
                holder.mReadBt.setVisibility(View.GONE);
            } else {
                holder.mReadBt.setVisibility(View.VISIBLE);
                mImageLoaderUtil.DisplayImage(url, holder.mReadBt, false, false);
            }

            holder.mTypeTv.setText(mList.get(position).getContent().toString());
            return convertView;
        }

        // 这段现在没有 只是到时候怕改回来，留着备用
        else {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.lv_company_culture, null);
                holder.mTileTv = (TextView) convertView.findViewById(R.id.title_tv);
                holder.mStateTv = (TextView) convertView.findViewById(R.id.state_tv);
                holder.mTimeTv = (TextView) convertView.findViewById(R.id.time_tv);
                holder.mIsread = (ImageView) convertView.findViewById(R.id.isread);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.mTileTv.setText(mList.get(position).getTitle());

            if ("1".equals(mList.get(position).getIsread())) {
                holder.mIsread.setVisibility(View.GONE);
            } else {
                holder.mIsread.setVisibility(View.VISIBLE);
            }
            long time = Long.valueOf(mList.get(position).getTime()) * 1000;
            holder.mTimeTv.setText(DateUtils.parseDate(time));

            return convertView;
        }

    }

    static class ViewHolder {
        private TextView mTileTv, mStateTv, mTypeTv, mTimeTv, mIsreadTv, mNoRead, mRead, mContent;
        private ImageView mReadBt, mIsread;
        private BSCirleDwLoadProgrossView mProgress;

    }

    public void updateData(List<PublishVO> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataFrist(List<PublishVO> list) {
        list.addAll(mList);
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataLast(List<PublishVO> list) {
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    // 缩放图片
    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
        return newbm;
    }

}
