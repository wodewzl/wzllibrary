
package com.bs.bsims.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.download.domain.LocalFilePathObj;
import com.bs.bsims.utils.CustomLog;

import java.io.File;

public class UploadFileListAdapter extends BaseAdapter {

    private static final String TAG = "UploadFileListAdapter";

    private Context context;
    private LocalFilePathObj obj = new LocalFilePathObj();

    public UploadFileListAdapter(Context context, LocalFilePathObj obj) {
        super();
        this.context = context;
        this.obj = obj;
    }

    public void setObj(LocalFilePathObj obj) {
        this.obj = obj;
    }

    public LocalFilePathObj getObj() {
        return obj;
    }

    @Override
    public int getCount() {
        return obj.getLists().length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        View view = null;

        if (convertView == null) {
            view = View.inflate(context, R.layout.item_upload_file_list, null);
            holder = new ViewHolder();
            holder.icon = (ImageView) view.findViewById(R.id.img_upload_file_list_icon);
            holder.name = (TextView) view.findViewById(R.id.txt_upload_file_list_name);
            holder.line = view.findViewById(R.id.view_upload_file_list_line);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        String string = obj.getPrePath() + obj.getLists()[position];
        File file = new File(string);
        CustomLog.d(TAG, "file name >>> " + string);
        boolean isFile = file.isFile();
        if (isFile)
            holder.icon.setBackgroundResource(R.drawable.ic_upload_file);
        else
            holder.icon.setBackgroundResource(R.drawable.ic_upload_folder);

        holder.name.setText(obj.getLists()[position]);

        if (position == obj.getLists().length - 1)
            holder.line.setVisibility(View.INVISIBLE);
        else
            holder.line.setVisibility(View.VISIBLE);

        return view;
    }

    class ViewHolder {
        ImageView icon;
        TextView name;
        View line;
    }

}
