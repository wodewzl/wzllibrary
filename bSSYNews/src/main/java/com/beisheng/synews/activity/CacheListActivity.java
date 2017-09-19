
package com.beisheng.synews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.view.BSDialog;
import com.beisheng.synews.adapter.CacheListAdapter;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.mode.CacheListVO;
import com.beisheng.synews.mode.LiveVO;
import com.beisheng.synews.utils.StartViewUitl;
import com.beisheng.synews.view.BSListViewConflict;
import com.google.gson.Gson;
import com.im.zhsy.R;

import java.util.ArrayList;
import java.util.List;

public class CacheListActivity extends BaseActivity implements OnItemClickListener, OnClickListener {
    private BSListViewConflict mListView;
    private CacheListAdapter mAdapter;
    private String mLikeKey = "";
    private BSDialog mDialog;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.cache_list_activity, mBaseContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return true;
    }

    @Override
    public void initView() {
        mBaseOkTv.setText("清空");
        mListView = (BSListViewConflict) findViewById(R.id.list_view);
        mAdapter = new CacheListAdapter(this);
        mListView.setAdapter(mAdapter);
        initData();
    }

    public void initData() {
        Intent intent = this.getIntent();
        String type = intent.getStringExtra("type");// type 1为历史，2为收藏
        List<CacheListVO> listVo = new ArrayList<CacheListVO>();
        if ("1".equals(type)) {
            mBaseTitleTv.setText("历史");
            mLikeKey = Constant.NEWS_DETAIL_URL;
        } else {
            mBaseTitleTv.setText("收藏");
            mLikeKey = "favor";
        }
        List<String> list = queryLike(mLikeKey);
        if (list.size() > 0) {
            mBaseOkTv.setVisibility(View.VISIBLE);
        } else {
            mBaseOkTv.setVisibility(View.GONE);
        }
        Gson gson = new Gson();
        for (int i = 0; i < list.size(); i++) {
            CacheListVO vo = gson.fromJson(list.get(i), CacheListVO.class);
            if (vo == null)
                continue;
            listVo.add(vo);
        }
        if (listVo.size() == 0) {
            mListView.setVisibility(View.VISIBLE);
            showCustomToast("亲，没有相关数据哦");
        } else {
            mListView.setVisibility(View.VISIBLE);
        }
        mAdapter.updateData(listVo);
    }

    @Override
    public void bindViewsListener() {
        mListView.setOnItemClickListener(this);
        mBaseOkTv.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (mAdapter.mList.size() == 0)
            return;
        CacheListVO vo = (CacheListVO) arg0.getAdapter().getItem(arg2);
        if (vo == null)
            return;
        // suburl 0 普通新闻，1论坛，2图片，3专题，4视频，5直播
        if ("8".equals(vo.getSuburl())) {
            // 数字报详情与爆料详情的
            Bundle bundle = new Bundle();
            bundle.putString("id", vo.getContentid());

            LiveVO liveVO = new LiveVO();
            liveVO.setSuburl(vo.getSuburl());
            liveVO.setLid(vo.getContentid());
            liveVO.setTitle(vo.getTitle());
            liveVO.setLink(vo.getLink());
            liveVO.setComments(vo.getComments());
            liveVO.setShare_tit(vo.getTitle());
            liveVO.setShare_url(vo.getLink());
            bundle.putString("url", vo.getLink());
            bundle.putSerializable("livevo", liveVO);
            openActivity(WebViewActivity.class, bundle, 0);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("id", vo.getContentid());
            StartViewUitl.startView(this, vo.getSuburl(), vo.getContentid(), vo.getLink(), vo.getGovermentid(), vo.getTypename());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_ok_tv:
                NotifyDialog();
                break;

            default:
                break;
        }
    }

    public void NotifyDialog() {
        View v = LayoutInflater.from(this).inflate(R.layout.pop_bottom_item, null);
        final TextView textView = (TextView) v.findViewById(R.id.textview);
        textView.setText("您确定要全部删除嘛？");
        int color = this.getResources().getColor(R.color.sy_title_color);
        mDialog = new BSDialog(this, "删除数据", v, color, new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mDialog.dismiss();
                deleteLike(mLikeKey);
                mBaseOkTv.setVisibility(View.GONE);
                mAdapter.mList.clear();
                mAdapter.notifyDataSetChanged();
            }
        });
        mDialog.show();
    }

}
