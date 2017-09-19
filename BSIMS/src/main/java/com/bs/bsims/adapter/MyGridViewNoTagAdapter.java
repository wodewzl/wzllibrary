
package com.bs.bsims.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.TaskEventDetailsView_Info_RelevantVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 纯展示人员的适配器
 * 
 * @author Administrator
 */
public class MyGridViewNoTagAdapter extends BaseAdapter {

    private Context context;

    private List<TaskEventDetailsView_Info_RelevantVO> datas = new ArrayList<TaskEventDetailsView_Info_RelevantVO>();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private DisplayImageOptions options;

    public MyGridViewNoTagAdapter(Context context,
            List<TaskEventDetailsView_Info_RelevantVO> datas) {
        this.context = context;
        this.datas = datas;

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_default_portrait_s)
                // 默认图片
                .showImageForEmptyUri(R.drawable.ic_default_portrait_s)
                // 空url的图片
                .showImageOnFail(R.drawable.ic_default_portrait_s)
                .cacheInMemory().cacheOnDisc()
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        options = CommonUtils.initImageLoaderOptions();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TaskEventDetailsView_Info_RelevantVO item = datas.get(position);

        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(
                    R.layout.common_item_none_tag, null);

            // holder.img_protrait = (ImageView)
            // convertView.findViewById(R.id.common_item_protrait_none_tag);
            holder.img_protrait = (BSCircleImageView) convertView
                    .findViewById(R.id.common_item_protrait_none_tag);
            holder.txt_name = (TextView) convertView
                    .findViewById(R.id.common_item_name_none_tag);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 赋值
        holder.txt_name.setText(item.getFullname());

        if (Constant.nil.equals(item.getHeadpic())) {
            holder.img_protrait
                    .setImageResource(R.drawable.ic_default_portrait_s);
        } else {
            // imageLoader.displayImage(item.getHeadpic(), holder.img_protrait,
            // options, new ImageLoadingListener() {
            //
            // @Override
            // public void onLoadingStarted(String imageUri, View view) {
            // }
            //
            // @Override
            // public void onLoadingFailed(String imageUri, View view,
            // FailReason failReason) {
            // }
            //
            // @Override
            // public void onLoadingComplete(String imageUri,
            // View view, Bitmap loadedImage) {
            // Bitmap bitmap = CommonImageUtils
            // .toRoundBitmap(loadedImage);
            // ImageView imageView = (ImageView) view;
            // imageView.setImageBitmap(bitmap);
            // }
            //
            // @Override
            // public void onLoadingCancelled(String imageUri,
            // View view) {
            // }
            // });
            imageLoader.displayImage(item.getHeadpic(), holder.img_protrait,
                    options);
            holder.img_protrait.setUserId(item.getUserid());// 获取头像用户对应ID，为点击事件存取ID参数
            holder.img_protrait.setUserName(item.getFullname());
            holder.img_protrait.setUrl(item.getHeadpic());

            if ("1".equals(item.getStatus())) {
                holder.img_protrait.setColor(3);
                holder.img_protrait.setSemicircleNumber("阅");
            }

        }

        return convertView;
    }

    class ViewHolder {
        TextView txt_name;// 名称
        // ImageView img_protrait;// 头像
        BSCircleImageView img_protrait;
    }
}
