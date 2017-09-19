package com.bs.bsims.adapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.Spannable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.bs.bsims.R;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.huanxin.SmileUtils;
import com.bs.bsims.huanxin.VoiceReplyClickListener;
import com.bs.bsims.model.ReplyItem;
import com.bs.bsims.utils.CommFileUtils;
import com.bs.bsims.utils.CommonImageUtils;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.bs.bsims.xutils.impl.DownloadInfo;
import com.bs.bsims.xutils.impl.DownloadManager;
import com.bs.bsims.xutils.impl.DownloadViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.xutils.common.Callback.CancelledException;
import org.xutils.ex.DbException;

/**评论适配器
 * @author Administrator
 *
 */
public class ListViewReplyAdapter extends BaseAdapter {
	
	private Context context;
	
	private List<ReplyItem> datas = new ArrayList<ReplyItem>();
	
	private ImageLoader imageLoader = ImageLoader.getInstance();

	private DisplayImageOptions options;
	
	public ListViewReplyAdapter (Context context, List<ReplyItem> datas) {
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
	}
	
	@Override
	public int getCount() {
		return datas.size();
	}
	@Override
	public int getItemViewType(int position) {
		ReplyItem item = datas.get(position);
		return item.getSort();
	}
	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ReplyItem item = datas.get(position);
		
		ViewHolder holder=null;
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");;
		if (convertView == null) { 
			holder = new ViewHolder();
			if(item.getSort() == 0) {
    			convertView = LayoutInflater.from(context).inflate(R.layout.common_item_replay, null);
    			holder.txt_content = (TextView)convertView.findViewById(R.id.common_txt_item_content);
			} else {
				convertView = LayoutInflater.from(context).inflate(R.layout.row_listview_reply_voice, null);
				//txt_content 复用为length
				holder.txt_content = (TextView)convertView.findViewById(R.id.tv_length);
				holder.img_voice= (ImageView)convertView.findViewById(R.id.iv_voice);
				holder.ll_iv_voice=(LinearLayout) convertView.findViewById(R.id.ll_iv_voice);
			}
			
			holder.img_portrait = (BSCircleImageView)convertView.findViewById(R.id.common_img_item_protrait);
			holder.txt_name = (TextView)convertView.findViewById(R.id.common_txt_item_name);
			holder.txt_time = (TextView)convertView.findViewById(R.id.common_txt_item_time);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.img_portrait.setTag(item.getHeadpic());
		holder.img_portrait.setUserId(item.getUserid());//HL:获取评论处头像对应的用户ID，便于实现点击跳转
		//赋值
		imageLoader.displayImage(item.getHeadpic(), holder.img_portrait, options, new ImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				ImageView imageView = (ImageView) view;
				String portraitUrl = (String) imageView.getTag();
				if(portraitUrl.equals(imageUri)) {
					imageView.setImageResource(R.drawable.ic_default_portrait_s);
				}
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				ImageView imageView = (ImageView) view;
				String portraitUrl = (String) imageView.getTag();
				if(portraitUrl.equals(imageUri)) {
					Bitmap roundBitmap = CommonImageUtils.toRoundBitmap(loadedImage);
					imageView.setImageBitmap(roundBitmap);
				}
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				
			}
		});
		holder.txt_name.setText(item.getFullname());
		holder.txt_time.setText(sdf.format(new Date(Long.parseLong(item.getTime())*1000)));
		
		
		if(item.getSort() == 0){
			Spannable span = SmileUtils.getSmiledText(context,item.getContent());
			holder.txt_content.setText(span, BufferType.SPANNABLE);
		} else {
			  holder.txt_content.setText(item.getSoundlength()+"''");
//				String filePath=Environment.getExternalStorageDirectory().toString()+"/"+CommFileUtils.getFileNameCloudSuffix(item.getContent());
				String filePath = Constant.FileInfo.AUDIO_PATH + CommFileUtils.getFileNameCloudSuffix(item.getContent());
				Log.d("语音下载路径",filePath);
				   try {
		                DownloadManager.getInstance().startDownload(  item.getContent(), CommonUtils.getFileNameCloudSuffix(item.getContent()), filePath, false, true, new DownloadViewHolder(new DownloadInfo()) {
		                    
		                    @Override
		                    public void onWaiting() {
		                        // TODO Auto-generated method stub
		                        
		                    }
		                    
		                    @Override
		                    public void onSuccess(File result) {
		                        // TODO Auto-generated method stub
		                        
		                    }
		                    
		                    @Override
		                    public void onStarted() {
		                        // TODO Auto-generated method stub
		                        
		                    }
		                    
		                    @Override
		                    public void onLoading(long total, long current) {
		                        // TODO Auto-generated method stub
		                        
		                    }
		                    
		                    @Override
		                    public void onError(Throwable ex, boolean isOnCallback) {
		                        // TODO Auto-generated method stub
		                        
		                    }
		                    
		                    @Override
		                    public void onCancelled(CancelledException cex) {
		                        // TODO Auto-generated method stub
		                        
		                    }
		                });
		            } catch (DbException e) {
		                // TODO Auto-generated catch block
		                e.printStackTrace();
		            }
				
 
			  
  			holder.ll_iv_voice.setOnClickListener(new VoiceReplyClickListener(filePath, holder.img_voice,  (Activity)context));

			}
		
		
		return convertView;
	}
	
	public void refresh() {		
		notifyDataSetChanged();
	}

	class ViewHolder {
		BSCircleImageView img_portrait;
		TextView txt_name;
		TextView txt_time;
		TextView txt_content;
		ImageView img_voice;
		LinearLayout ll_iv_voice;
		
	}
}
