
package com.bs.bsims.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Environment;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.emoji.EmojiconHandler;
import com.bs.bsims.emoji.EmojiconTextView;
import com.bs.bsims.fragment.CommentFragment;
import com.bs.bsims.huanxin.PasteEditText;
import com.bs.bsims.huanxin.SmileUtils;
import com.bs.bsims.model.DiscussVO;
import com.bs.bsims.utils.CommonDateUtils;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.bs.bsims.xutils.impl.DownloadInfo;
import com.bs.bsims.xutils.impl.DownloadManager;
import com.bs.bsims.xutils.impl.DownloadViewHolder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback.CancelledException;
import org.xutils.ex.DbException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DiscussAdapter extends BaseAdapter {
    private Context mContext;
    public ArrayList<DiscussVO> mList;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;

    private List<TextView> mListText;
    private String mId;
    private String agreeUrl;
    private String opposeUrl;
    private String attitude;

    private String content;
    public List<DiscussVO> mListVo;
    private String length;
    private String filePath;
    private CommentFragment mFragment;
    private String twoStr = "";
    private DiscussVO mCurrentDisVo;
    private DiscussVO mReCurrentVo;
    public String contentUrl;
    public int type;// 1为日志的，2为通知的
    private MediaPlayer mediaPlayer;
    private static boolean playState = false;
    private TextView mDiscussTitle;

    private List<DiscussVO> replayListVo;
    private AnimationDrawable voiceAnimation = null;

    // 评论或回复后回调
    public interface DiscussCallback {
        public void callback();
    }

    public DiscussCallback callback;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public DiscussAdapter(Context context, String id, CommentFragment fragment) {
        mContext = context;
        mList = new ArrayList<DiscussVO>();
        this.mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();
        this.mId = id;
        mListVo = new ArrayList<DiscussVO>();
        this.mFragment = fragment;

        // 注册广播
        IntentFilter filter = new IntentFilter(Constant.NoticeDetailsAction);
        mContext.registerReceiver(receiver, filter);
    }

    public DiscussAdapter(Context context, String id, CommentFragment fragment, TextView discussTitle) {
        mContext = context;
        mList = new ArrayList<DiscussVO>();
        this.mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();
        this.mId = id;
        mListVo = new ArrayList<DiscussVO>();
        this.mFragment = fragment;

        // 注册广播
        IntentFilter filter = new IntentFilter(Constant.NoticeDetailsAction);
        mContext.registerReceiver(receiver, filter);

        this.mDiscussTitle = discussTitle;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.lv_discuss_item, null);
            holder.personHead = (BSCircleImageView) convertView.findViewById(R.id.person_head);
            holder.personName = (TextView) convertView.findViewById(R.id.name);
            holder.agreeCount = (TextView) convertView.findViewById(R.id.agree);
            holder.agreeImge = (ImageView) convertView.findViewById(R.id.img_agree);
            holder.opposeCount = (TextView) convertView.findViewById(R.id.oppose);
            holder.opposeImage = (ImageView) convertView.findViewById(R.id.img_oppose);
            holder.sendMsg = (ImageView) convertView.findViewById(R.id.send_msg);
            holder.discussTime = (TextView) convertView.findViewById(R.id.discuss_tiem);
            holder.discussContent = (EmojiconTextView) convertView.findViewById(R.id.discuss_content);
            holder.recordLayout = (FrameLayout) convertView.findViewById(R.id.record_layout);
            holder.voiceImg = (ImageView) convertView.findViewById(R.id.voice_imge);
            holder.voideLength = (TextView) convertView.findViewById(R.id.voice_length);
            holder.replayContent = (LinearLayout) convertView.findViewById(R.id.replay_content);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.personHead.setImageResource(R.drawable.ic_default_portrait_s);
        }
        final DiscussVO discussVO = mList.get(position);
        if ("1".equals(discussVO.getIsanonymous())) {
            holder.personName.setText("匿名");
            discussVO.setFullname("匿名");
            mImageLoader.displayImage(null, holder.personHead, mOptions);
            holder.personHead.setUserId("");// 匿名用户ID为空
        } else {
            if ("1".equals(discussVO.getIsboss())) {
                holder.personName.setTextColor(Color.parseColor("#ff0000"));
            } else {
                holder.personName.setTextColor(Color.parseColor("#000000"));
            }
            mImageLoader.displayImage(discussVO.getHeadpic(), holder.personHead, mOptions);
            holder.personName.setText(discussVO.getFullname());
            holder.personHead.setUserId(discussVO.getUserid());// HL:获取评论者头像对应的用户ID，以便实现跳转
            holder.personHead.setUserName(discussVO.getFullname());
            holder.personHead.setmImageLoader(mImageLoader);
            holder.personHead.setUrl(discussVO.getHeadpic());
            // holder.personHead.setUserId("");//获取面头像对应的用户ID，以便响应跳转
        }

        holder.agreeCount.setText(discussVO.getPraise());
        holder.opposeCount.setText(discussVO.getDecline());
        long time = Long.parseLong(discussVO.getTime()) * 1000;
        holder.discussTime.setText(DateUtils.parseDate(time));
        holder.discussTime.setText(CommonDateUtils.parseDate(time,
                "MM-dd HH:mm"));

        if ("0".equals(discussVO.getSort())) {
            holder.discussContent.setVisibility(View.VISIBLE);
            holder.recordLayout.setVisibility(View.GONE);
            holder.discussContent.setText(discussVO.getContent());
        } else {
            holder.discussContent.setVisibility(View.GONE);
            holder.recordLayout.setVisibility(View.VISIBLE);
            holder.voideLength.setText(discussVO.getSoundlength() + "''");
            String filePath = Constant.AUDIO_PATH +
                    CommonUtils.getFileNameCloudSuffix(discussVO.getContent());
            // String filePath = FileUtil.getSaveFilePath(mContext) +
            // CommonUtils.getFileNameCloudSuffix(discussVO.getContent());
            try {
                DownloadManager.getInstance().startDownload(discussVO.getContent(), CommonUtils.getFileNameCloudSuffix(discussVO.getContent()), filePath, false, true, new DownloadViewHolder(new DownloadInfo()) {

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

            holder.recordLayout.setOnClickListener(new DiscussListners(mContext, holder.opposeImage, discussVO));
        }
        holder.agreeImge.setOnClickListener(new DiscussListners(mContext, holder.agreeImge, discussVO));
        holder.opposeImage.setOnClickListener(new DiscussListners(mContext, holder.opposeImage, discussVO));
        holder.sendMsg.setOnClickListener(new DiscussListners(mContext, holder.sendMsg, discussVO));

        if (discussVO.getReplay() != null) {

            holder.replayContent.removeAllViews();
            for (int i = 0; i < discussVO.getReplay().size(); i++) {
                final DiscussVO vo = discussVO.getReplay().get(i);
                StringBuffer sb = new StringBuffer();

                if (vo.getReplayname() != null) {
                    if ("1".equals(vo.getIsanonymous())) {
                        sb.append("匿名");
                    } else {
                        sb.append(vo.getFullname());
                    }

                    sb.append("回复");
                    if ("1".equals(discussVO.getIsanonymous())) {
                        sb.append("匿名");
                        vo.setReplayname("匿名");
                    } else {
                        sb.append(vo.getReplayname());

                    }
                    sb.append(": ")
                            .append(vo.getContent());

                } else {
                    if ("1".equals(vo.getIsanonymous())) {
                        sb.append("匿名");
                        vo.setReplayname("匿名");
                    } else {
                        sb.append(vo.getFullname());
                    }
                    sb.append(": ")
                            .append(vo.getContent());
                }

                int bstart = 0;
                int bend = 0;
                int fstart = 0;
                int fend = 0;

                String[] str;
                if (vo.getReplayname() != null) {
                    str = sb.toString().trim().split(":")[0].split("回复");
                } else {
                    str = sb.toString().trim().split(":");
                }

                String content = sb.toString();

                bstart = content.indexOf(str[0]);
                bend = bstart + str[0].length();

                if (vo.getReplayname() != null) {

                    fstart = content.indexOf("回复") + "回复".length();
                    fend = fstart + str[1].length();
                }
                EmojiconTextView tv = new EmojiconTextView(mContext);
                tv.setDiscussDear(true);
                tv.setTextColor(Color.parseColor("#666666"));
                String context = EmojiconHandler.conEmojis(content.toString());
                Spannable span = SmileUtils.getSmiledText(mContext, context);
                if (bstart >= 0 && bend >= 0) {
                    /**
                     * 这里增加判断 为了处理 CP上 的第54条任务详情中的回复内容 出现异常 情况
                     */
                    span.setSpan(
                            new ForegroundColorSpan(Color.parseColor("#00A9FE")),
                            bstart, bend, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                if (vo.getReplayname() != null) {
                    span.setSpan(new ForegroundColorSpan(Color.parseColor("#00A9FE")), fstart, fend,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                tv.setTextSize(12);
                tv.setPadding(0, 2, 0, 2);
                tv.setText(span, BufferType.SPANNABLE);
                tv.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Constant.DISCUSS_MSG);
                        intent.putExtra("hint", "回复#" + vo.getFullname() + "#：");
                        twoStr = "re";
                        mCurrentDisVo = discussVO;
                        mReCurrentVo = vo;
                        mContext.sendBroadcast(intent);
                    }
                });
                holder.replayContent.addView(tv);
            }
            // holder.replayContent.setText(sb.toString());
            holder.replayContent.setVisibility(View.VISIBLE);
        } else {
            holder.replayContent.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder
    {
        private BSCircleImageView personHead;
        private ImageView agreeImge, opposeImage, voiceImg, sendMsg;
        private TextView personName, voideLength;
        private TextView discussTime;
        private EmojiconTextView discussContent;
        private FrameLayout recordLayout;
        private TextView agreeCount;
        private TextView opposeCount;
        private LinearLayout replayContent;
    }

    private class DiscussListners implements OnClickListener {

        private ImageView mImageView;
        private Context mContext;
        private DiscussVO mDiscussVO;

        public DiscussListners(Context context, ImageView imageView, DiscussVO discussVO) {
            this.mImageView = imageView;
            this.mContext = context;
            this.mDiscussVO = discussVO;
        }

        @Override
        public void onClick(View v) {
            Animation scaleAnimation = new ScaleAnimation(1.0f, 2.0f, 1.0f, 2.0f);

            switch (v.getId()) {
                case R.id.img_agree:
                    if ("1".equals(mDiscussVO.getIsdeclined()) || "1".equals(mDiscussVO.getIspraised())) {
                        CustomToast.showLongToast(mContext, "已点过赞或衰");
                        return;
                    }
                    if (mDiscussVO.isAgree() || mDiscussVO.isOppose()) {
                        return;
                    } else {
                        mImageView.setImageResource(R.drawable.agree_select);
                        // 设置动画时间
                        scaleAnimation.setDuration(500);
                        mImageView.setAnimation(scaleAnimation);
                        mImageView.startAnimation(scaleAnimation);
                        int praise = Integer.parseInt(mDiscussVO.getPraise()) + 1;
                        mDiscussVO.setPraise(praise + "");
                        mDiscussVO.setAgree(true);
                        commit(getAgreeUrl(), mDiscussVO, 1);
                        notifyDataSetChanged();
                    }

                    break;
                case R.id.img_oppose:
                    if ("1".equals(mDiscussVO.getIsdeclined()) || "1".equals(mDiscussVO.getIspraised())) {
                        CustomToast.showLongToast(mContext, "已点过赞或衰");
                        return;
                    }
                    if (mDiscussVO.isAgree() || mDiscussVO.isOppose()) {
                        return;
                    } else {
                        mImageView.setImageResource(R.drawable.oppose_select);
                        // 设置动画时间
                        scaleAnimation.setDuration(500);
                        mImageView.setAnimation(scaleAnimation);
                        mImageView.startAnimation(scaleAnimation);

                        int oppose = Integer.parseInt(mDiscussVO.getDecline()) + 1;
                        mDiscussVO.setDecline(oppose + "");
                        mDiscussVO.setOppose(true);
                        commit(getOpposeUrl(), mDiscussVO, 0);
                        notifyDataSetChanged();
                    }

                    break;

                case R.id.record_layout:
                    final ImageView imageView = (ImageView) v.findViewById(R.id.voice_imge);
                    if (!playState) {
                        startAnimation(imageView);
                        mediaPlayer = new MediaPlayer();
                        String url = Environment.getExternalStorageDirectory() + "/my/voice.amr";
                        try
                        {
                            mediaPlayer.setDataSource(mDiscussVO.getSounpath());
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            playState = true;

                            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    mediaPlayer.release();
                                    mediaPlayer = null;
                                    stopPlayVoice(imageView); // stop animation
                                    if (playState) {
                                        playState = false;
                                    }
                                }
                            });
                        } catch (IllegalArgumentException e)
                        {
                            e.printStackTrace();
                        } catch (IllegalStateException e)
                        {
                            e.printStackTrace();
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }

                    } else {
                        if (mediaPlayer.isPlaying()) {
                            stopAnimation(imageView);
                            mediaPlayer.stop();
                            playState = false;
                        } else {
                            playState = false;
                        }
                    }

                    break;

                case R.id.send_msg:
                    Intent intent = new Intent(Constant.DISCUSS_MSG);
                    intent.putExtra("hint", "回复#" + mDiscussVO.getFullname() + "#：");
                    mContext.sendBroadcast(intent);
                    twoStr = "re";
                    /**
                     * 公文详情的评论和企业文化详情页面的评论，点击后出现闪退 点击别人的评论，点击后出现闪退
                     */
                    if (null != mFragment) {
                        mFragment.setCommentid(mDiscussVO.getCommentid());
                        mFragment.setmDiscussVO(mDiscussVO);
                    }
                    mCurrentDisVo = mDiscussVO;
                    break;
                default:
                    break;
            }
        }
    }

    public void commit(String url, DiscussVO discussVO, final int status) {
        RequestParams params = new RequestParams();
        try {
            params.put("ftoken", BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("logid", mId);
            params.put("commentid", discussVO.getCommentid());
            params.put("articleid", mId);
            params.put("replyid", discussVO.getArid());
            params.put("attitude", status);
            params.put("rid", discussVO.getCid());

            if (type == 3) {
                params.put("rid", discussVO.getCommentid());
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(new String(arg2));
                    String code = (String) jsonObject.get("code");
                    String str = (String) jsonObject.get("retinfo");
                    if (Constant.RESULT_CODE.equals(code)) {
                        if (callback != null)
                            callback.callback();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    public String getAgreeUrl() {
        return agreeUrl;
    }

    public void setAgreeUrl(String agreeUrl) {
        this.agreeUrl = agreeUrl;
    }

    public String getOpposeUrl() {
        return opposeUrl;
    }

    public void setOpposeUrl(String opposeUrl) {
        this.opposeUrl = opposeUrl;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String stringExtra = intent.getStringExtra("content");
            String length = intent.getStringExtra("length");
            String reContent = intent.getStringExtra("recontent");
            Log.d("content:" + stringExtra, "length:" + length);
            if (length.equals("-1")) {
                content = stringExtra;
                commitContent("0", content, "", "", reContent);
            } else {
                length = length;
                commitContent("1", stringExtra, stringExtra, length, reContent);
            }
        }

    };

    public void getWrite() {
        PasteEditText et = (PasteEditText) mFragment.getView().findViewById(R.id.et_sendmessage);
        et.requestFocus();
        InputMethodManager im = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    public void commitContent(String sort, String content, String filePath, String length, String reContent) {
        RequestParams params = new RequestParams();
        DiscussVO discussVO = new DiscussVO();
        try {
            params.put("ftoken", BSApplication.getInstance().getmCompany());
            int type = getType();
            String allContent = content;
            // 二级回回复
            if (!"".equals(twoStr)) {
                params.put("content", allContent);
                params.put("commentid", mCurrentDisVo.getCommentid());
                params.put("ir_rid", mCurrentDisVo.getCommentid());
                params.put("lcid", mCurrentDisVo.getCommentid());
                params.put("rid", mCurrentDisVo.getCommentid());
                twoStr = "";
                discussVO.setContent(reContent);
                discussVO.setSort(sort);
                discussVO.setCommentid(mCurrentDisVo.getCommentid());
                discussVO.setPraise("0");
                discussVO.setDecline("0");
                discussVO.setFullname(BSApplication.getInstance().getUserFromServerVO().getFullname());
                discussVO.setTime(System.currentTimeMillis() / 1000 + "");
                if (mReCurrentVo != null) {
                    discussVO.setReplayname(mReCurrentVo.getFullname());
                } else {
                    discussVO.setReplayname(mCurrentDisVo.getFullname());
                }

                List list = new ArrayList<DiscussVO>();

                if (mCurrentDisVo.getReplay() != null) {
                    mCurrentDisVo.getReplay().add(discussVO);
                } else {
                    list.add(discussVO);
                    mCurrentDisVo.setReplay(list);
                }

            } else {
                params.put("content", allContent);
                params.put("commentid", "");
                params.put("ir_rid", "");
                discussVO.setContent(content);
                discussVO.setSort(sort);
                if ("1".equals(sort)) {
                    discussVO.setSoundlength(length);
                    discussVO.setSounpath(content);
                }
                discussVO.setCommentid("");
                discussVO.setPraise("0");
                discussVO.setDecline("0");
                discussVO.setFullname(BSApplication.getInstance().getUserFromServerVO().getFullname());
                discussVO.setTime(System.currentTimeMillis() / 1000 + "");
                discussVO.setHeadpic(BSApplication.getInstance().getUserFromServerVO().getHeadpic());
                discussVO.setIsboss(BSApplication.getInstance().getUserFromServerVO().getIsboss());
                mList.add(discussVO);
            }
            notifyDataSetChanged();
            if (mDiscussTitle != null) {
                String str = mDiscussTitle.getText().toString();
                String count = str.substring(str.indexOf("(") + 1, str.indexOf(")"));
                if (mDiscussTitle.getVisibility() == View.GONE)
                    count = "0";
                if (mList.size() == 1) {
                    mDiscussTitle.setVisibility(View.VISIBLE);
                }

                // if ("评论(%1$d)".equals(mDiscussTitle.getText().toString()))
                // count = "0";

                String discussStr = String.format(mContext.getResources().getString(R.string.discuss), Integer.parseInt(count) + 1);
                mDiscussTitle.setText(discussStr);
            }

            params.put("logid", mId);
            params.put("sort", sort);
            params.put("articleid", mId);
            params.put("id", mId);
            params.put("sort", sort);
            params.put("ir_articleid", mId);
            params.put("ir_sort", sort);
            params.put("ir_content", allContent);
            params.put("ir_soundlength", length);
            params.put("vid", mId);
            if (!"".equals(filePath)) {
                params.put("ir_soundpath", new File(filePath));
            }
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("uid", BSApplication.getInstance().getUserId());

            params.put("soundlength", length);
            if (!"".equals(filePath)) {
                params.put("sounpath", new File(filePath));
                params.put("src", new File(filePath));
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String url = getContentUrl();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(arg2));
                    String str = (String) jsonObject.get("retinfo");
                    String code = (String) jsonObject.get("code");
                    if (callback != null)
                        callback.callback();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    public void updateData(List<DiscussVO> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataLast(List<DiscussVO> list) {
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void AddAndUpdateListData(List<DiscussVO> list) {
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void AddAndUpdateADiscussVO(DiscussVO vo) {
        mList.add(vo);
        this.notifyDataSetChanged();
    }

    @Override
    protected void finalize() throws Throwable {
        mContext.unregisterReceiver(receiver);
        super.finalize();
    }

    public void stopSound() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                playState = false;
            } else {
                playState = false;
            }
        }
    }

    public void startAnimation(ImageView v) {
        v.setImageResource(R.anim.voice_from_icon);
        voiceAnimation = (AnimationDrawable) v.getDrawable();
        voiceAnimation.start();
    }

    public void stopAnimation(ImageView v) {
        v.setImageResource(R.anim.voice_from_icon);
        voiceAnimation = (AnimationDrawable) v.getDrawable();
        voiceAnimation.stop();
    }

    private void stopPlayVoice(ImageView v) {
        v.setImageResource(R.anim.voice_from_icon);
        voiceAnimation = (AnimationDrawable) v.getDrawable();
        voiceAnimation.stop();
        v.setImageResource(R.drawable.start_voide_f3);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        playState = false;
    }

    public DiscussCallback getCallback() {
        return callback;
    }

    public void setCallback(DiscussCallback callback) {
        this.callback = callback;
    }

}
