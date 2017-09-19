
package com.beisheng.synews.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beisheng.base.adapter.BSBaseAdapter;
import com.beisheng.base.utils.DateUtils;
import com.beisheng.base.utils.FileUtil;
import com.beisheng.base.utils.Options;
import com.beisheng.base.utils.WebViewPictureUtil;
import com.beisheng.base.utils.WebviewUtil;
import com.beisheng.base.view.BSCircleImageView;
import com.beisheng.synews.mode.CommunityVO;
import com.beisheng.synews.view.NineGridlayout;
import com.im.zhsy.R;

import java.util.Arrays;

public class CommunityDetailAdapter extends BSBaseAdapter<CommunityVO> {
    private int currentPosition = -1;

    public CommunityDetailAdapter(Context context) {
        super(context);
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (mIsEmpty) {
            return super.getView(position, convertView, parent);
        }

        if (convertView != null && convertView.getTag() == null)
            convertView = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.community_detail_adapter, null);
            holder.headIcon = (BSCircleImageView) convertView.findViewById(R.id.head_icon);
            holder.titleTv = (TextView) convertView.findViewById(R.id.title_tv);
            holder.floorTv = (TextView) convertView.findViewById(R.id.floor_tv);
            holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.detailTv = (TextView) convertView.findViewById(R.id.detail_tv);
            holder.htmlTv = (TextView) convertView.findViewById(R.id.html_textview);
            holder.photoLayout = (NineGridlayout) convertView.findViewById(R.id.photo_layout);
            holder.countTv = (TextView) convertView.findViewById(R.id.count_tv);
            holder.webview = (WebView) convertView.findViewById(R.id.webview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final CommunityVO vo = (CommunityVO) mList.get(position);

        holder.titleTv.setText(vo.getAuthur());
        holder.floorTv.setText(vo.getFloor());
        holder.timeTv.setText(DateUtils.parseMDHM(vo.getCreatime()));

        String content = vo.getContent();

        if (position == 0) {
            holder.webview.loadDataWithBaseURL(FileUtil.getSaveFilePath(mContext), content, "text/html", "utf-8", null);
            WebviewUtil.SetWebview(holder.webview);
            holder.webview.setWebViewClient(new WebViewPictureUtil(mContext, holder.webview, "img", "this.src"));
            holder.webview.setVisibility(View.VISIBLE);
            // holder.webview.setBackgroundColor(Color.parseColor("#00000000"));
            WebviewUtil.setWebViewFontSize(mContext, holder.webview);
            holder.htmlTv.setVisibility(View.GONE);
            holder.photoLayout.setVisibility(View.GONE);
        } else {
            TextView tv = new TextView(mContext);
            tv.setText(Html.fromHtml(content));
            String str = tv.getText().toString().trim();
            holder.htmlTv.setText(str.replaceAll("￼", "").replaceAll("\n", ""));
            holder.webview.setVisibility(View.GONE);
            holder.htmlTv.setVisibility(View.VISIBLE);
            if (vo.getAttachments() != null) {
                holder.photoLayout.setImagesData(Arrays.asList(vo.getAttachments()));
                holder.photoLayout.setVisibility(View.VISIBLE);
            } else {
                holder.photoLayout.setVisibility(View.GONE);
            }

        }

        if (position == 0 && mList.size() > 0) {
            holder.countTv.setVisibility(View.VISIBLE);
            holder.countTv.setText("评论（" + (mList.size() - 1) + "）");
        } else {
            holder.countTv.setVisibility(View.GONE);
        }
        mImageLoader.displayImage(vo.getMicon(), holder.headIcon, Options.getOptionsHead(R.drawable.user_icon));

        return convertView;
    }

    static class ViewHolder {
        private BSCircleImageView headIcon;
        private TextView titleTv, timeTv, detailTv, floorTv, countTv;
        private WebView detailWb;
        private LinearLayout webviewLayout;
        private TextView htmlTv;
        private NineGridlayout photoLayout;
        // private CustomImageView oneImg;
        private WebView webview;
    }

}
