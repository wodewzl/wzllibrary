
package com.beisheng.synews.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.adapter.BSBaseAdapter;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.DateUtils;
import com.beisheng.base.utils.ThreadUtil;
import com.beisheng.synews.activity.DisscussMyselfActivity;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.mode.DisscussMyselfVO;
import com.im.zhsy.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

public class DiscussMyselfAdapter extends BSBaseAdapter<DisscussMyselfVO> {
    private int currentPosition = -1;
    private BaseActivity mActivity;

    public DiscussMyselfAdapter(Context context) {
        super(context);
        mActivity = (BaseActivity) context;
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
            convertView = View.inflate(mContext, R.layout.disscuss_myself_adapter, null);
            holder.titleTv = (TextView) convertView.findViewById(R.id.title_tv);
            holder.myCommentLayout = (LinearLayout) convertView.findViewById(R.id.my_commnet_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final DisscussMyselfVO vo = (DisscussMyselfVO) mList.get(position);

        holder.titleTv.setText("  【" + vo.getTypename() + "】" + vo.getTitle());

        holder.myCommentLayout.removeAllViews();
        for (int i = 0; i < vo.getReplyList().size(); i++) {
            final LinearLayout layout = new LinearLayout(mContext);
            layout.setOrientation(LinearLayout.VERTICAL);
            final DisscussMyselfVO childVo = vo.getReplyList().get(i);
            TextView contentTv = new TextView(mContext);
            contentTv.setTextColor(mContext.getResources().getColor(R.color.C5));
            String str1 = "";

            if ("".equals(childVo.getReplyname())) {
                str1 = "我评论的：";
            } else {
                str1 = "我 回复 #" + childVo.getReplyname() + "#：";
            }

            String str2 = childVo.getContent();
            BaseActivity activity = (BaseActivity) mContext;
            BaseCommonUtils.setTextTwoBefore(activity, contentTv, str1, str2, R.color.sy_title_color, 1.0f);

            TextView timeTv = new TextView(mContext);
            LinearLayout.LayoutParams timeTvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            timeTv.setGravity(Gravity.LEFT);
            timeTv.setTextColor(mContext.getResources().getColor(R.color.C6));
            timeTv.setText(DateUtils.parseMDHM(childVo.getCreatime()));
            timeTv.setLayoutParams(timeTvParams);
            View dividerView = new View(mContext);
            dividerView.setBackgroundColor(mContext.getResources().getColor(R.color.devider_bg));
            LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, BaseCommonUtils.dip2px(mContext, (float) 0.8));
            dividerParams.topMargin = BaseCommonUtils.dip2px(mContext, 10);
            dividerParams.bottomMargin = BaseCommonUtils.dip2px(mContext, 10);
            dividerView.setLayoutParams(dividerParams);
            layout.addView(contentTv);
            layout.addView(timeTv);
            holder.myCommentLayout.addView(layout);
            if (i < vo.getReplyList().size() - 1)
                holder.myCommentLayout.addView(dividerView);

            layout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    PopupWindow pop = deletePop(mContext, layout, childVo);
                    pop.showAsDropDown(layout);
                }
            });

        }

        return convertView;
    }

    public PopupWindow deletePop(Context activity, final View view, final DisscussMyselfVO vo) {
        final PopupWindow popView;
        LinearLayout rootLayout = new LinearLayout(activity);
        rootLayout.setBackgroundResource(R.drawable.delete);
        TextView textView = new TextView(activity);
        textView.setText("删除");
        textView.setPadding(BaseCommonUtils.dip2px(mContext, 10), BaseCommonUtils.dip2px(mContext, 8), BaseCommonUtils.dip2px(mContext, 10), BaseCommonUtils.dip2px(mContext, 15));
        rootLayout.setGravity(Gravity.CENTER);
        textView.setTextColor(activity.getResources().getColor(R.color.C1));
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.addView(textView);
        popView = new PopupWindow(rootLayout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popView.setFocusable(true);
        popView.setOutsideTouchable(true);
        popView.setBackgroundDrawable(new BitmapDrawable());

        int[] location = new int[2];
        view.getLocationOnScreen(location);
        popView.showAtLocation(view, Gravity.NO_GRAVITY, location[0] + BaseCommonUtils.getViewWidth(view) / 2, location[1] -
                BaseCommonUtils.getViewHigh(rootLayout));
        rootLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                commit(vo);
                popView.dismiss();
            }
        });
        return popView;
    }

    static class ViewHolder {
        private TextView titleTv;
        private LinearLayout myCommentLayout;
    }

    public void commit(DisscussMyselfVO vo) {
        mActivity.showProgressDialog();
        RequestParams params = new RequestParams();
        try {
            params.put("rid", vo.getPid());
            params.put("uid", AppApplication.getInstance().getUid());
            params.put("sessionid", AppApplication.getInstance().getSessionid());

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String url = Constant.DOMAIN_NAME + Constant.MY_DISSCUSS_DELETE;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                mActivity.dismissProgressDialog();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                String result = new String(arg2);
                mActivity.dismissProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(new String(arg2));
                    String str = (String) jsonObject.get("retinfo");
                    String code = (String) jsonObject.get("code");
                    if (Constant.RESULT_SUCCESS_CODE.equals(code)) {
                        mActivity.showCustomToast(str);
                        DisscussMyselfActivity activity = (DisscussMyselfActivity) mContext;
                        new ThreadUtil(mContext, activity).start();
                    } else {
                        mActivity.showCustomToast(str);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
