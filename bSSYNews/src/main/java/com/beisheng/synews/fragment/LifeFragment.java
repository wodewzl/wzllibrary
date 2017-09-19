
package com.beisheng.synews.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.adapter.BSBaseAdapter;
import com.beisheng.base.fragment.BaseFragment;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.interfaces.UpdateCallback;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.Options;
import com.beisheng.base.utils.ThreadUtil;
import com.beisheng.base.view.BSGridView;
import com.beisheng.synews.activity.BrokeActivity;
import com.beisheng.synews.activity.WebViewActivity;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.mode.LifeVO;
import com.google.gson.Gson;
import com.im.zhsy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LifeFragment extends BaseFragment implements OnClickListener, UpdateCallback {
    private String TAG = "LifeFragment";
    private BaseActivity mActivity;
    private int[] mColorArray = new int[] {
            R.color.life_fragment_color1, R.color.life_fragment_color2, R.color.life_fragment_color3
    };
    private LinearLayout mRootLayout;
    private LifeVO mLifeVO;

    public static LifeFragment newInstance() {
        LifeFragment lifeFragment = new LifeFragment();
        return lifeFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (BaseActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.life_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new ThreadUtil(mActivity, this).start();
    }

    private void initViews(View view) {
        mActivity.mBaseOkTv.setVisibility(View.GONE);
        mActivity.mBaseHeadLayout.setVisibility(View.VISIBLE);
        mRootLayout = (LinearLayout) view.findViewById(R.id.root_layout);
        mActivity.showProgressDialog();
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            if (mActivity.hasNetWork()) {
                String jsonStr = HttpClientUtil.getRequest(mActivity, Constant.DOMAIN_NAME + Constant.LIFE_FRAGMENT_URL, map);
                mLifeVO = gson.fromJson(jsonStr, LifeVO.class);
                mActivity.saveJsonCache(Constant.LIFE_FRAGMENT_URL, map, jsonStr);
            } else {
                String oldStr = mActivity.getCacheFromDatabase(Constant.LIFE_FRAGMENT_URL, map);
                mLifeVO = gson.fromJson(oldStr, LifeVO.class);
            }
            if (Constant.RESULT_SUCCESS_CODE.equals(mLifeVO.getCode())) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean execute() {
        return getData();
    }

    @Override
    public void executeSuccess() {
        mActivity.dismissProgressDialog();
        createGroup();
    }

    @Override
    public void executeFailure() {
        mActivity.dismissProgressDialog();
        if (mLifeVO != null)
            mActivity.showCustomToast(mLifeVO.getRetinfo());
        else
            mActivity.showCustomToast("亲，请检查网络哦");
    }

    @SuppressLint("ResourceAsColor")
    public void createGroup() {
        ArrayList<HashMap<String, Object>> item = new ArrayList<HashMap<String, Object>>();

        List<LifeVO> listVo = mLifeVO.getList();
        for (int i = 0; i < listVo.size(); i++) {
            LinearLayout titleLayout = new LinearLayout(mActivity);
            titleLayout.setBackgroundColor(R.color.C7);
            LinearLayout.LayoutParams titleLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            titleLayout.setLayoutParams(titleLayoutParams);

            // 添加左边的
            ImageView leftImg = new ImageView(mActivity);
            LinearLayout.LayoutParams leftImgParams = new LinearLayout.LayoutParams(BaseCommonUtils.dip2px(mActivity, 5), LinearLayout.LayoutParams.MATCH_PARENT);
            leftImg.setLayoutParams(leftImgParams);
            titleLayout.addView(leftImg);
            leftImg.setBackgroundColor(mActivity.getResources().getColor(mColorArray[i % 3]));

            // 添加标题
            TextView tv = new TextView(mActivity);
            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            tvParams.weight = 1;
            tv.setLayoutParams(tvParams);
            tv.setPadding(BaseCommonUtils.dip2px(mActivity, 15), BaseCommonUtils.dip2px(mActivity,
                    10), BaseCommonUtils.dip2px(mActivity, 15), BaseCommonUtils.dip2px(mActivity, 10));
            tv.setTextColor(mActivity.getResources().getColor(R.color.C5));
            tv.setBackgroundColor(mActivity.getResources().getColor(R.color.C3));
            tv.setText(listVo.get(i).getTitle());
            titleLayout.addView(tv);

            // 添加上边的两个
            mRootLayout.addView(titleLayout);
            BSGridView gv = new BSGridView(mActivity);
            gv.setNumColumns(5);
            gv.setVerticalSpacing(BaseCommonUtils.dip2px(mActivity, 30));
            gv.setPadding(0, BaseCommonUtils.dip2px(mActivity, 15), 0, BaseCommonUtils.dip2px(mActivity, 15));
            mRootLayout.addView(gv);

            // 填出数据
            MenuItmeAdapter adapter = new MenuItmeAdapter(mActivity);
            gv.setAdapter(adapter);

            adapter.updateData(listVo.get(i).getChildren());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
    }

    private class ImtemOnclick implements OnClickListener {
        private LifeVO mClickVO;

        public ImtemOnclick(LifeVO vo) {
            this.mClickVO = vo;
        }

        @Override
        public void onClick(View arg0) {
            Bundle bundle = new Bundle();
            bundle.putString("url", mClickVO.getLink());
            bundle.putString("name", mClickVO.getTitle());
            if ("rebellion".equals(mClickVO.getLink())) {
                ((BaseActivity) mActivity).openActivity(BrokeActivity.class);
            } else {
                ((BaseActivity) mActivity).openActivity(WebViewActivity.class, bundle, 0);
            }
        }
    }

    class MenuItmeAdapter extends BSBaseAdapter {
        private Context context;

        public MenuItmeAdapter(Context context) {
            super(context);
            mOptions = Options.getOptionsDefaultIcon(R.drawable.ic_launcher);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final ViewHolder holder;
            if (mIsEmpty) {
                return super.getView(position, convertView, parent);
            }

            if (convertView != null && convertView.getTag() == null)
                convertView = null;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mActivity, R.layout.life_menu_item, null);
                holder.itemImage = (ImageView) convertView.findViewById(R.id.item_image);
                holder.itemName = (TextView) convertView.findViewById(R.id.item_name);
                holder.itemLayout = (LinearLayout) convertView.findViewById(R.id.item_layout);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            LifeVO vo = (LifeVO) mList.get(position);

            holder.itemName.setText(vo.getTitle());
            mImageLoader.displayImage(vo.getImg(), holder.itemImage, mOptions);
            holder.itemLayout.setOnClickListener(new ImtemOnclick(vo));
            return convertView;
        }
    }

    static class ViewHolder {
        private ImageView itemImage;
        private TextView itemName;
        private LinearLayout itemLayout;
    }

    public String getFragmentName() {
        return TAG;// 不知道该方法有没有用
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mActivity.mBaseOkTv.setVisibility(View.GONE);
            mActivity.mBaseHeadLayout.setVisibility(View.VISIBLE);
        }
    }
}
