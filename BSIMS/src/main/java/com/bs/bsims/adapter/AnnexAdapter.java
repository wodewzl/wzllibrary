
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.AnnexVO;
import com.bs.bsims.utils.FileIconMapping;
import com.bs.bsims.utils.FileUtil;
import com.bs.bsims.view.BSCirleDwLoadProgrossView;
import com.bs.bsims.xutils.impl.DownloadInfo;
import com.bs.bsims.xutils.impl.DownloadManager;
import com.bs.bsims.xutils.impl.DownloadViewHolder;

import org.xutils.common.Callback.CancelledException;
import org.xutils.ex.DbException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AnnexAdapter extends BaseAdapter {

    private Context mContext;
    public List<AnnexVO> mList;

    public AnnexAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<AnnexVO>();
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
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.annex_layout, null);
            holder.mAnnexType = (ImageView) convertView.findViewById(R.id.annex_type);
            holder.mAnnexName = (TextView) convertView.findViewById(R.id.annex_name);
            holder.mAnnexDes = (TextView) convertView.findViewById(R.id.annex_des);
            holder.mAnnexDownLoad = (ImageView) convertView.findViewById(R.id.annex_downlaod);
            holder.mAnnexView = (ImageView) convertView.findViewById(R.id.annex_view);
            holder.mAnnexProgress = (BSCirleDwLoadProgrossView) convertView
                    .findViewById(R.id.annex_progress);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final AnnexVO vo = mList.get(position);
        holder.mAnnexName.setText(vo.getTitle());
        holder.mAnnexDes.setText(vo.getFilesize());

        Integer iconId = FileIconMapping.getIcon(vo.getTypename());
        holder.mAnnexType.setBackgroundResource(iconId);
        String fileName = vo.getFilepath().substring(vo.getFilepath().lastIndexOf("/") + 1,
                vo.getFilepath().length());
        File file = new File(FileUtil.getSaveFilePath(mContext) + fileName);
        if (file.exists()) {
            holder.mAnnexView.setVisibility(View.VISIBLE);
            holder.mAnnexProgress.setVisibility(View.GONE);
            holder.mAnnexDownLoad.setVisibility(View.GONE);
        }
        holder.mAnnexProgress.setProgress(1);
        holder.mAnnexDownLoad.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String fileName = vo.getFilepath().substring(vo.getFilepath().lastIndexOf("/") + 1,
                        vo.getFilepath().length());
                if (FileUtil.hasSDCard()) {
                    File dir = new File(FileUtil.getSaveFilePath(mContext));

                    if (!dir.exists())
                        dir.mkdir();
                    File file = new File(dir, fileName);
                    holder.mAnnexDownLoad.setVisibility(View.GONE);
                    holder.mAnnexProgress.setVisibility(View.VISIBLE);
                    holder.mAnnexView.setVisibility(View.GONE);

                    try {
                        DownloadManager.getInstance().startDownload(mList.get(position).getFilepath(),
                                fileName, FileUtil.getSaveFilePath(mContext) + fileName, true, true,
                                new DownloadViewHolder(new DownloadInfo()) {

                                    @Override
                                    public void onWaiting() {

                                    }

                                    @Override
                                    public void onSuccess(File result) {
                                        holder.mAnnexProgress.setVisibility(View.GONE);
                                        holder.mAnnexView.setVisibility(View.VISIBLE);
                                        holder.mAnnexDownLoad.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onStarted() {

                                    }

                                    @Override
                                    public void onLoading(long total, long current) {
                                        int progress = (int) ((current * 100 / total));
                                        holder.mAnnexProgress.setProgress(progress);
                                        holder.mAnnexProgress.setVisibility(View.VISIBLE);
                                        holder.mAnnexView.setVisibility(View.GONE);
                                        holder.mAnnexDownLoad.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onError(Throwable ex, boolean isOnCallback) {
                                        holder.mAnnexDownLoad.setVisibility(View.VISIBLE);
                                        holder.mAnnexProgress.setVisibility(View.GONE);
                                        holder.mAnnexView.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onCancelled(CancelledException cex) {

                                    }
                                });
                    } catch (DbException e) {
                        e.printStackTrace();
                    }

                }

            }

        });
        holder.mAnnexView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String fileName = vo.getFilepath().substring(vo.getFilepath().lastIndexOf("/") + 1,
                        vo.getFilepath().length());
                File file = new File(FileUtil.getSaveFilePath(mContext) + fileName);
                FileUtil.openFile(mContext, file);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        private ImageView mAnnexType, mAnnexDownLoad, mAnnexView;
        private TextView mAnnexName, mAnnexDes;
        private BSCirleDwLoadProgrossView mAnnexProgress;
    }

    public void updateData(List<AnnexVO> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataFrist(List<AnnexVO> list) {
        list.addAll(mList);
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataLast(List<AnnexVO> list) {
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

}
