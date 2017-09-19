
package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.bs.bsims.R;
import com.bs.bsims.model.CrmContactVo;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomLog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.view.BSIndexEditText;
import com.bs.bsims.view.BSRefreshListView;

import java.util.ArrayList;
import java.util.List;

/***
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-2-26
 * @version 1.22 由于在那个选择位置那里 數據源和適配器都是一样的，在当前页面对原始数据和其他跳转都会发生影响，故创建此页面，到这个页面赖搜索并跳转
 */
public class GaoDeMapSearchActity extends BaseActivity implements
        OnCheckedChangeListener, OnClickListener,
        OnPoiSearchListener {
    private Context mContext;
    private PoiSearch.Query mQuery;
    private PoiSearch poiSearch;// POI搜索
    private String personCity;
    private int currentPage = 0;
    private BSRefreshListView map_listid;

    private List<CrmContactVo> mList;
    GaodeContentList gaodelistadpter;

    private String aDress = "";

    private String Lng = "";
    private String Lat = "";

    // 声明AMapLocationClient类对象
    public static AMapLocationClient mLocationClient = null;

    // 声明mLocationOption对象
    public static AMapLocationClientOption mLocationOption = null;
    private BSIndexEditText mSerachEt;

    private View mFootLayout;
    private TextView mMoreTextView, loadingfile1;
    private ProgressBar mProgressBar;
    private Boolean canClickFlag = true;// 解决连续点“更多”可能会出现的异常
    private String mState = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mContext = this;

    }

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        View layout = View.inflate(this, R.layout.crm_visitor_map, null);
        mContentLayout.addView(layout);
    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void updateUi() {
        // TODO Auto-generated method stub
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        mLoadingLayout.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        mTitleTv.setText("搜索结果");
        // mTitleTv.setCompoundDrawablesWithIntrinsicBounds(null, null,
        // getResources().getDrawable(R.drawable.task_statistics_month_statusid01),
        // null);gaodeloaction_refresh
        map_listid = (BSRefreshListView) findViewById(R.id.map_listid);
        gaodelistadpter = new GaodeContentList();
        map_listid.setAdapter(gaodelistadpter);
        mSerachEt = (BSIndexEditText) findViewById(R.id.edit_single_search);
//        mSerachEt.setVisibility(View.GONE);
        if (getIntent().getStringExtra("keyword") != null) {
            map_listid.changeHeaderViewByState(BSRefreshListView.REFRESHING);
            mSerachEt.setText(getIntent().getStringExtra("keyword"));
            doSearchQuery(getIntent().getStringExtra("keyword"), currentPage);// 进来先查第一页
            initFoot();
        }

    }

    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub
        mFootLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (canClickFlag) {
                    canClickFlag = false;
                    mState = "1";
                    mMoreTextView.setText("正在加载...");
                    mProgressBar.setVisibility(View.VISIBLE);
                    doSearchQuery(getIntent().getStringExtra("keyword"), currentPage);

                }
            }
        });

    }

    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCheckedChanged(RadioGroup arg0, int arg1) {
        // TODO Auto-generated method stub

    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery(String keyWord, int currentPage) {
        // showProgressDialog();// 显示进度框
        mQuery = new PoiSearch.Query(keyWord, "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        mQuery.setPageSize(20);// 设置每页最多返回多少条poiitem
        mQuery.setPageNum(currentPage);// 设置查第一页
        poiSearch = new PoiSearch(this, mQuery);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    // 异步搜索pio数据之后回掉函数
    @Override
    public void onPoiSearched(PoiResult poiResult, int rCode) {
        CustomDialog.closeProgressDialog();
        // TODO Auto-generated method stub
        if (rCode == 0) {
            // 搜索POI的结果
            if (poiResult != null && poiResult.getQuery() != null) {
                // 是否是同一条
                if (poiResult.getQuery().equals(mQuery)) {
                    poiResult = poiResult;
                    // 取得搜索到的poiitems有多少页
                    int resultPages = poiResult.getPageCount();
                    // 取得第一页的poiitem数据，页数从数字0开始
                    List<PoiItem> poiItems = poiResult.getPois();
                    // 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    List suggestionCities = poiResult.getSearchSuggestionCitys();

                    if (poiItems != null && poiItems.size() > 0) {
                        mList = new ArrayList<CrmContactVo>();
                        for (int i = 0; i < poiItems.size(); i++) {
                            CustomLog.e("aaa", poiItems.get(i).getSnippet());
                            CustomLog.e("ccc", poiItems.get(i).getTitle());
                            // mList.get(i).setFullname(poiItems.get(i).getSnippet());
                            CrmContactVo v = new CrmContactVo();
                            v.setFullname(poiItems.get(i).getSnippet());
                            v.setHeadpic(poiItems.get(i).getTitle());
                            v.setLid(poiItems.get(i).getLatLonPoint().getLongitude() + "");// 经度
                            v.setLname(poiItems.get(i).getLatLonPoint().getLatitude() + "");// 纬度
                            v.setId(poiItems.get(i).getCityName());// 城市
                            v.setFalgecontant("0");
                            mList.add(v);
                        }
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        mList = new ArrayList<CrmContactVo>();
                    } else {
                        // Toast.makeText(this, "没有搜索到相关数据", Toast.LENGTH_SHORT).show();
                        mList = new ArrayList<CrmContactVo>();
                    }
                    updateView1(mList);
                    return;
                }

            } else {
                // dissmissProgressDialog();// 隐藏对话框
                // Toast.makeText(this, "没有搜索到相关数据", Toast.LENGTH_SHORT).show();
                mList = new ArrayList<CrmContactVo>();
                updateView1(mList);
                return;
            }

        } else {
            // dissmissProgressDialog();// 隐藏对话框
            // Toast.makeText(this, "搜索失败,请检查网络连接！", Toast.LENGTH_SHORT).show();
            CustomToast.showShortToast(mContext, "网络延迟!");
        }

    }

    public class GaodeContentList extends BaseAdapter {
        private List<CrmContactVo> mLists;

        public GaodeContentList() {
            mLists = new ArrayList<CrmContactVo>();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mLists.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return mLists.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup arg2) {
            // TODO Auto-generated method stub

            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.crm_visitor_map_apdater, null);
                holder.contants_name = (TextView) convertView.findViewById(R.id.text_name);
                holder.checkBox_up_check_selectall = (ImageView) convertView
                        .findViewById(R.id.checkBox_up_check_selectall);
                holder.allliney = (LinearLayout) convertView.findViewById(R.id.allliney);
                holder.text_phone = (TextView) convertView.findViewById(R.id.text_phone);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (mLists.get(position).getFalgecontant().equals("1")) {
                holder.checkBox_up_check_selectall.setImageResource(R.drawable.common_ic_selected);
            }
            else {
                holder.checkBox_up_check_selectall.setImageResource(R.drawable.common_ic_unselect);
            }

            holder.allliney.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    for (int i = 0; i < mList.size(); i++) {
                        mLists.get(i).setFalgecontant("0");
                    }
                    aDress = mLists.get(position).getFullname() + mLists.get(position).getHeadpic();
                    mLists.get(position).setFalgecontant("1");
                    notifyDataSetChanged();
                    Intent data = new Intent();
                    data.putExtra("pioValue", mLists.get(position));
                    setResult(2016, data);
                    GaoDeMapSearchActity.this.finish();
                }
            });

            holder.contants_name.setText(mLists.get(position).getFullname());
            holder.text_phone.setText(mLists.get(position).getHeadpic());
            return convertView;
        }

        public void updateData(List<CrmContactVo> list) {
            if (list == null)
                list = new ArrayList<CrmContactVo>();
            mList.clear();
            mList.addAll(list);
            this.notifyDataSetChanged();
        }

        public void updateDataFrist(List<CrmContactVo> list) {
            if (list == null)
                list = new ArrayList<CrmContactVo>();

            list.addAll(mList);
            mList.clear();
            mList.addAll(list);
            this.notifyDataSetChanged();
        }

        public void updateDataLast(List<CrmContactVo> list) {
            if (list == null)
                list = new ArrayList<CrmContactVo>();

            mList.addAll(list);
            this.notifyDataSetChanged();
        }

    }

    public static class ViewHolder
    {
        private ImageView checkBox_up_check_selectall;
        private TextView contants_name, text_phone;
        private LinearLayout allliney;
    }

    public void updateView1(List<CrmContactVo> mList1) {
        map_listid.onRefreshComplete();
        if (null != mList1 && mList1.size() > 0) {
            map_listid.setVisibility(View.VISIBLE);
            mFootLayout.setVisibility(View.VISIBLE);
            if (mState.equals("0")) {
                gaodelistadpter.mLists.clear();
                gaodelistadpter.mLists.addAll(mList1);
            }
            else {
                gaodelistadpter.mLists.addAll(mList1);
            }
            gaodelistadpter.notifyDataSetChanged();
        }
        else {
            if (mState.equals("0")) {
                mFootLayout.setVisibility(View.GONE);
                map_listid.setVisibility(View.GONE);
                gaodelistadpter.mLists.clear();
                gaodelistadpter.notifyDataSetChanged();
                CustomToast.showLongToast(mContext, "没有搜索到相关数据!");
            }
            else {
                mFootLayout.setVisibility(View.GONE);
                CustomToast.showLongToast(mContext, "没有更多数据了!");
                return;
            }
        }
        canClickFlag = true;
        currentPage++;
        mState = "0";
        footViewIsVisibility(currentPage);

    }

    // 加载更多数据
    public void initFoot() {
        mFootLayout = LayoutInflater.from(GaoDeMapSearchActity.this).inflate(
                R.layout.listview_bottom_more, null);
        mMoreTextView = (TextView) mFootLayout.findViewById(R.id.txt_loading);
        mMoreTextView.setText("更多");
        mProgressBar = (ProgressBar) mFootLayout.findViewById(R.id.progressBar);
        mFootLayout.setVisibility(View.GONE);
        map_listid.addFooterView(mFootLayout);
    }

    protected void footViewIsVisibility(int count) {
        if (mList == null) {
            return;
        }
        if (count > 2) {
            mFootLayout.setVisibility(View.GONE);
            // listView.removeFooterView(mFootLayout);
        } else {
            mFootLayout.setVisibility(View.VISIBLE);
            mMoreTextView.setText("更多");
            mProgressBar.setVisibility(View.GONE);
        }
    }

}
