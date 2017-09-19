/**  
 * @Title: GridImageAdapter.java 
 * @Package workapprove.photos.adapter 
 * @Description: TODO() 
 * @author Derek 
 * @email renchun525@gmail.com
 * @date 2013-11-12 下午3:39:27 
 * @version V1.0  
 */

package com.bs.bsims.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bs.bsims.R;
import com.bs.bsims.utils.ImageManager2;

import java.util.ArrayList;

/**
 * @ClassName: GridImageAdapter
 * @Description: TODO()
 * @author Derek
 * @date 2013-11-12 下午3:39:27
 */

public class GridImageAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> dataList;
    private DisplayMetrics dm;
    boolean isDel = false;

    public GridImageAdapter(Context c, ArrayList<String> dataList, boolean isDel) {

        mContext = c;
        this.dataList = dataList;
        this.isDel = isDel;
        dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);

    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // ImageView imageView;
        ViewHolder holder;
        View view;
        if (convertView == null) {
            /*
             * imageView = new ImageView(mContext); imageView.setLayoutParams(new
             * GridView.LayoutParams( GridView.LayoutParams.MATCH_PARENT, dipToPx(65)));
             * imageView.setAdjustViewBounds(true);
             * imageView.setScaleType(ImageView.ScaleType.FIT_XY);
             */
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(
                    R.layout.item_photos_main_grid, null);
            holder.image = (ImageView) view.findViewById(R.id.img_photos_image);
            holder.reduce = (ImageView) view
                    .findViewById(R.id.img_photos_reduce);
            view.setTag(holder);

        } else {
            // imageView = (ImageView) convertView;
            view = convertView;
            holder = (ViewHolder) view.getTag();

        }
        String path;
        if (dataList != null && position < dataList.size()) {
            path = dataList.get(position);
        } else {
            path = "camera_default";
        }

        if (path.contains("default")) {
            // imageView.setImageResource(R.drawable.common_photos_camera_default);

            holder.image.setImageResource(R.drawable.upload_picture);
        } else if (path.contains("reduce")) {

            // imageView.setImageResource(R.drawable.common_photos_camera_default);

            holder.image.setImageResource(R.drawable.upload_picture);

        } else {
            ImageManager2.from(mContext).displayImage(holder.image, path,
                    R.drawable.ic_workapprove_grid_add, 100, 100);
        }

        if (isDel) {
            if (path.equals("camera_default") || path.equals("camera_reduce")) {
                holder.reduce.setVisibility(View.GONE);
            } else {
                holder.reduce.setVisibility(View.VISIBLE);
            }

        } else {
            holder.reduce.setVisibility(View.GONE);
        }

        return view;
    }

    class ViewHolder {
        ImageView image;
        ImageView reduce;

    }

    public int dipToPx(int dip) {
        return (int) (dip * dm.density + 0.5f);
    }

}
