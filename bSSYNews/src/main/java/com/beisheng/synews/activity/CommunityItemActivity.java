
package com.beisheng.synews.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.adapter.BSBaseAdapter;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.Options;
import com.beisheng.base.view.BSGridView;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.mode.ChannelManage;
import com.beisheng.synews.mode.CommunityVO;
import com.google.gson.Gson;
import com.im.zhsy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("NewApi")
public class CommunityItemActivity extends BaseActivity implements OnClickListener {
    private LinearLayout mRootLayout;
    private CommunityVO mCommunityVO;
    private int[] mColorArray = new int[] {
            R.color.life_fragment_color1, R.color.life_fragment_color2, R.color.life_fragment_color3
    };
    private boolean mIsChange = false;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.community_item_activity, mBaseContentLayout);
    }

    @Override
    public boolean getDataResult() {
        // return getData();
        return true;
    }

    @Override
    public void initView() {
        mRootLayout = (LinearLayout) findViewById(R.id.root_layout);
        mBaseTitleTv.setText("栏目分类");
    }

    @Override
    public void bindViewsListener() {
        mBaseBackTv.setOnClickListener(this);
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();

            if (hasNetWork()) {
                String jsonStr = HttpClientUtil.getRequest(this, Constant.DOMAIN_NAME + Constant.COMMUNITY_ITEM, map);
                mCommunityVO = gson.fromJson(jsonStr, CommunityVO.class);
                saveJsonCache(Constant.COMMUNITY_ITEM, map, jsonStr);

            } else {
                String oldStr = getCacheFromDatabase(Constant.COMMUNITY_ITEM, map);
                mCommunityVO = gson.fromJson(oldStr, CommunityVO.class);
            }

            if (Constant.RESULT_SUCCESS_CODE.equals(mCommunityVO.getCode())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void executeSuccess() {
        super.executeSuccess();
        createGroup();
    }

    @SuppressLint("ResourceAsColor")
    public void createGroup() {
        if (ChannelManage.getManage().getCommunity().size() == 0)
            return;
        ArrayList<HashMap<String, Object>> item = new ArrayList<HashMap<String, Object>>();

        List<CommunityVO> listVo = ChannelManage.getManage().getCommunity();
        for (int i = 0; i < listVo.size(); i++) {
            LinearLayout titleLayout = new LinearLayout(this);
            titleLayout.setOrientation(LinearLayout.HORIZONTAL);
            titleLayout.setBackgroundColor(this.getResources().getColor(R.color.C3));
            LinearLayout.LayoutParams titleLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            titleLayout.setLayoutParams(titleLayoutParams);

            // 添加左边的
            ImageView leftImg = new ImageView(this);
            LinearLayout.LayoutParams leftImgParams = new LinearLayout.LayoutParams(BaseCommonUtils.dip2px(this, 5), LinearLayout.LayoutParams.MATCH_PARENT);
            leftImg.setLayoutParams(leftImgParams);
            leftImg.setBackgroundColor(this.getResources().getColor(mColorArray[i % 3]));
            titleLayout.addView(leftImg);

            // 添加标题
            TextView tv = new TextView(this);
            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            tvParams.weight = 1;
            tv.setLayoutParams(tvParams);
            tv.setPadding(BaseCommonUtils.dip2px(this, 15), BaseCommonUtils.dip2px(this, 10), BaseCommonUtils.dip2px(this, 15), BaseCommonUtils.dip2px(this, 10));
            tv.setTextColor(this.getResources().getColor(R.color.C5));
            tv.setBackgroundColor(this.getResources().getColor(R.color.C3));
            tv.setText(listVo.get(i).getName());
            titleLayout.addView(tv);

            final TextView deleteTv = new TextView(this);
            LinearLayout.LayoutParams deletetvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            deletetvParams.rightMargin = BaseCommonUtils.dip2px(this, 10);
            deleteTv.setLayoutParams(deletetvParams);
            deleteTv.setText("点击编辑");
            deleteTv.setTextColor(this.getResources().getColor(R.color.sy_title_color));
            deleteTv.setGravity(Gravity.CENTER_VERTICAL);
            titleLayout.addView(deleteTv);

            // 添加上边的两个
            mRootLayout.addView(titleLayout);
            BSGridView gv = new BSGridView(this);
            gv.setNumColumns(4);
            gv.setVerticalSpacing(BaseCommonUtils.dip2px(this, 30));
            gv.setPadding(0, BaseCommonUtils.dip2px(this, 15), 0, BaseCommonUtils.dip2px(this, 15));
            mRootLayout.addView(gv);

            // 填出数据
            final MenuItmeAdapter adapter = new MenuItmeAdapter(this);
            gv.setAdapter(adapter);

            if (i == 0) {
                if (ChannelManage.getManage().getCommunityDef().size() == 0) {
                    ChannelManage.getManage().getCommunityDef().addAll(listVo.get(i).getList());
                }
                adapter.updateData(ChannelManage.getManage().getCommunityDef());
                deleteTv.setVisibility(View.VISIBLE);
                deleteTv.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if ("取消编辑".equals(deleteTv.getText().toString())) {
                            deleteTv.setText("点击编辑");
                            adapter.setDelete(false);
                        } else {
                            deleteTv.setText("取消编辑");
                            adapter.setDelete(true);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            } else {
                deleteTv.setVisibility(View.GONE);
                adapter.updateData(listVo.get(i).getList());
            }

        }
    }

    class MenuItmeAdapter extends BSBaseAdapter {
        public boolean delete = false;
        private Context mContext;

        public MenuItmeAdapter(Context context) {
            super(context);
            mOptions = Options.getOptionsDefaultIcon(R.drawable.ic_launcher);
            this.mContext = context;
        }

        @Override
        public void updateData(List list) {
            mList = list;
        }

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
                convertView = View.inflate(CommunityItemActivity.this, R.layout.communityl_item, null);
                holder.itemName = (TextView) convertView.findViewById(R.id.text_item);
                holder.itemImage = (ImageView) convertView.findViewById(R.id.delete_img);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final CommunityVO vo = (CommunityVO) mList.get(position);

            holder.itemName.setText(vo.getName());
            holder.itemName.setBackground(BaseCommonUtils.setBackgroundShap(CommunityItemActivity.this, 0, R.color.devider_bg, R.color.C3));
            if (delete && position > 3 && !"我的".equals(vo.getName())) {
                holder.itemImage.setVisibility(View.VISIBLE);
            } else {
                holder.itemImage.setVisibility(View.GONE);
            }
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent();
                    if (delete && position > 3) {
                        mList.remove(vo);
                        notifyDataSetChanged();
                        mIsChange = true;
                        ChannelManage.getManage().saveCommunityDef(mList);
                    } else {
                        List<CommunityVO> list = ChannelManage.getManage().getCommunityDef();

                        for (int i = 0; i < list.size(); i++) {
                            if (vo.getName().equals(list.get(i).getName())) {
                                intent.putExtra("position", i);
                                break;
                            } else {
                                if (i == list.size() - 1) {
                                    list.add(vo);
                                    ChannelManage.getManage().saveCommunityDef(list);
                                    intent.putExtra("position", ChannelManage.getManage().getCommunityDef().size() - 1);
                                }
                            }
                        }
                        CommunityItemActivity.this.setResult(1, intent);
                        CommunityItemActivity.this.finish();
                    }
                }
            });
            return convertView;
        }

        class ViewHolder {
            private ImageView itemImage;
            private TextView itemName;
        }

        public boolean isDelete() {
            return delete;
        }

        public void setDelete(boolean delete) {
            this.delete = delete;
        }

    }

    @Override
    public void onBackPressed() {
        if (mIsChange) {
            Intent intent = new Intent();
            CommunityItemActivity.this.setResult(1, intent);
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_back_tv:

                if (mIsChange) {
                    Intent intent = new Intent();
                    CommunityItemActivity.this.setResult(1, intent);
                }

                super.onBackPressed();
                break;

            default:
                break;
        }
    }

}
