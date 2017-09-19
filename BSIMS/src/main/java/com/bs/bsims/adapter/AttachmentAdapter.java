
package com.bs.bsims.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.Attachment;
import com.bs.bsims.utils.FileIconMapping;

import java.util.ArrayList;

public class AttachmentAdapter extends BaseAdapter {
    ArrayList<Attachment> list;
    Context context;

    public AttachmentAdapter(Context context, ArrayList<Attachment> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int paramInt) {

        return list.get(paramInt);
    }

    @Override
    public long getItemId(int paramInt) {

        return paramInt;
    }

    @Override
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        ViewHolder holder;
        View view;
        if (paramView != null) {
            view = paramView;
            holder = (ViewHolder) view.getTag();
        } else {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_workapprove_attachment, null);
            holder.line_attachment_action = (RelativeLayout) view.findViewById(R.id.line_attachment_action);
            holder.line_attachment_content = (RelativeLayout) view.findViewById(R.id.line_attachment_content);
            holder.image_attachment_action = (ImageView) view.findViewById(R.id.image_attachment_action);
            holder.text_attachment_title = (TextView) view.findViewById(R.id.text_attachment_title);
            holder.img_attachment_reduce = (ImageView) view.findViewById(R.id.img_attachment_reduce);
            holder.image_attachment_file = (ImageView) view.findViewById(R.id.image_attachment_file);
            view.setTag(holder);
        }
        Attachment attachment = list.get(paramInt);
        switch (attachment.getType()) {
            case 1: {
                holder.line_attachment_action.setVisibility(View.VISIBLE);
                holder.line_attachment_content.setVisibility(View.GONE);
                holder.image_attachment_action.setImageResource(R.drawable.upload_picture);
                break;
            }

            case 2: {
                holder.line_attachment_action.setVisibility(View.VISIBLE);
                holder.line_attachment_content.setVisibility(View.GONE);

                holder.image_attachment_action.setImageResource(R.drawable.ic_workapprove_grid_cancel);
                break;
            }
            case 3: {
                holder.line_attachment_action.setVisibility(View.GONE);
                holder.line_attachment_content.setVisibility(View.VISIBLE);

                // 图标
                Integer icon = FileIconMapping.getIcon_task(attachment.getFileType());
                holder.image_attachment_file.setImageResource(icon);

                holder.text_attachment_title.setText(attachment.getTitle());
                if (attachment.isDel()) {
                    holder.img_attachment_reduce.setVisibility(View.VISIBLE);
                } else {
                    holder.img_attachment_reduce.setVisibility(View.GONE);
                }
                break;
            }
        }

        return view;
    }

    class ViewHolder {
        RelativeLayout line_attachment_action;
        RelativeLayout line_attachment_content;
        ImageView image_attachment_action;
        ImageView img_attachment_reduce, image_attachment_file;
        TextView text_attachment_title;
    }
}
