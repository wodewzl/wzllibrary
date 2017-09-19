
package com.bs.bsims.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.EXTSharedfilesdHomeMyUpdateAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant4Sharedfiles;
import com.bs.bsims.fragment.SharedfilesdHomeAllFragment;
import com.bs.bsims.fragment.SharedfilesdHomeMyCollectionFragment;
import com.bs.bsims.fragment.SharedfilesdHomeMyUploadFragment;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.model.SharedfilesdHomeMyUploadVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomLog;
import com.bs.bsims.view.BSIndexEditText;
import com.bs.bsims.view.BSIndicator3Sharedfiles;
import com.bs.bsims.view.BSIndicator3Sharedfiles.Indicator3SharedfilesListener;
import com.bs.bsims.xutils.impl.DownloadManager;
import com.bs.bsims.xutils.impl.HttpUtilsByPC;
import com.bs.bsims.xutils.impl.RequestCallBackPC;
import com.google.gson.Gson;

import org.xutils.ex.DbException;
import org.xutils.ex.HttpException;

import java.util.HashMap;

/**
 * @author peck
 * @Description: 一、文档管理接口 1. 文档获取部门列表接口
 * @date 2015-7-3 下午5:59:27
 * @email 971371860@qq.com
 * @version V1.0
 */
@SuppressLint("NewApi")
public class EXTSharedfilesdGroupHomeActivity extends BaseActivity implements
        Indicator3SharedfilesListener, UpdateCallback, OnClickListener {

    private int pagecount;
    private static final String TAG = "EXTSharedfilesdGroupHomeActivity";
    private BSIndicator3Sharedfiles mBSIndicator3Sharedfiles;
    private Context context;
    private ViewPager mViewPager;// 设置滑动页
    // private TabPagerAdapter mPagerAdapter;
    private String uid;
    SharedfilesdHomeAllFragment mAllFragment = null;
    SharedfilesdHomeMyUploadFragment myUploadFragment = null;
    SharedfilesdHomeMyCollectionFragment myCollectionFragment = null;
    private FrameLayout id_content;
    /* 添加隐藏listview 搜索之后显示 */
    // private BSRefreshListView fragment_sharedfilesd_home_all_refreshlistview;
    private ListView fragment_sharedfilesd_home_all_refreshlistview;
    private BSIndexEditText mClearEditText;
    private EXTSharedfilesdHomeMyUpdateAdapter mEXTSharedfilesdHomeMyUpdateAdapter;
    private SharedfilesdHomeMyUploadVO mSharedfilesdHomeMyUploadVO;
    private TextView mLoading;
    private Intent intent1 = new Intent();

    private PopupWindows popupWindows;

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onIndicatorSelected(int index) {
        // TODO Auto-generated method stub
        mViewPager.setCurrentItem(index);
    }

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        View layout = View.inflate(this,
                R.layout.ac_ext_sharedfilesd_grouphome, mContentLayout);
        // mContentLayout.addView(layout);
        intent1 = getIntent();
        context = this;

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
        popupWindows = new PopupWindows(context, fragment_sharedfilesd_home_all_refreshlistview);
        mLoading = (TextView) this.findViewById(R.id.loadingfile1);
        mClearEditText = (BSIndexEditText) this.findViewById(R.id.edit_single_search);
        // TODO Auto-generated method stub
        mTitleTv.setText(getResources().getString(
                R.string.ac_ext_sharedfilesd_grouphome_title));
        // setDrableRightById mOkTv
        CommonUtils.setDrableRightById(mOkTv, "", context, R.drawable.file_list_top_upload);
        mOkTv.setVisibility(View.GONE);
        fragment_sharedfilesd_home_all_refreshlistview = (ListView) this
                .findViewById(R.id.fragment_sharedfilesd_home_all_refreshlistview);
        mBSIndicator3Sharedfiles = (BSIndicator3Sharedfiles) this
                .findViewById(R.id.ac_ext_sharedfilesd_grouphome_top_indicator);
        mBSIndicator3Sharedfiles.setIndicator3SharedfilesListener(this);
        // mViewPager = (ViewPager) findViewById(R.id.ac_ext_sharedfilesd_grouphome_view_pager);
        // mPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
        // mViewPager.setAdapter(mPagerAdapter);
        // mPagerAdapter.addFragment();
        // mViewPager.invalidate();
        mLoading.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.GONE);
        id_content = (FrameLayout) this.findViewById(R.id.id_content);
        // mViewPager.setOffscreenPageLimit(0);
        setSelect(0);
        mSharedfilesdHomeMyUploadVO = new SharedfilesdHomeMyUploadVO();
        // 根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBSIndicator3Sharedfiles.setVisibility(View.GONE);
                id_content.setVisibility(View.GONE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                CustomLog.e("msginfo", start + "");
                CustomLog.e("msginfo", count + "");
                CustomLog.e("msginfo", after + "");

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterData(s.toString());
            }
        });

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        CustomLog.e("AA", "FDFDSF");
        intent1 = getIntent();
        if (intent1 != null) {
            if (intent1.getStringExtra("key").equals("1"))
                ;
            // setSelect(pagecount);
            else {
                setSelect(1);
                intent1.putExtra("key", "1");
            }
        }

        super.onResume();
    }

    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub
        mOkTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                // Intent intent = new Intent();
                // intent.setClass(context, UploadFileListActivity.class);
                // intent.putExtra("type", 1);
                // startActivity(intent);

                popupWindows.showAtLocation(fragment_sharedfilesd_home_all_refreshlistview,
                        Gravity.BOTTOM, 0, 0);
                backgroundAlpha(0.5f);
            }
        });

        mBSIndicator3Sharedfiles
                .setIndicator3SharedfilesListener(new Indicator3SharedfilesListener() {

                    @Override
                    public void onIndicatorSelected(int index) {
                        // TODO Auto-generated method stub
                        setSelect(index);
                    }
                });

        popupWindows.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                backgroundAlpha(1f);
            }
        });
    }

    public void setSelect(int i)
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        // 把图片设置为亮的
        // 设置内容区域
        switch (i)
        {
            case 0:
                pagecount = 0;
                if (mAllFragment == null)
                {
                    mAllFragment = new SharedfilesdHomeAllFragment();
                    transaction.add(R.id.id_content, mAllFragment);
                } else
                {
                    transaction.show(mAllFragment);
                }
                break;
            case 1:
                pagecount = 1;
                if (myUploadFragment == null)
                {
                    myUploadFragment = new SharedfilesdHomeMyUploadFragment();
                    transaction.add(R.id.id_content, myUploadFragment);
                } else
                {
                    transaction.show(myUploadFragment);

                }
                break;
            case 2:
                pagecount = 2;
                if (myCollectionFragment == null)
                {
                    myCollectionFragment = new SharedfilesdHomeMyCollectionFragment();
                    transaction.add(R.id.id_content, myCollectionFragment);
                } else
                {
                    transaction.show(myCollectionFragment);
                }
                break;

            default:
                break;

        }
        mBSIndicator3Sharedfiles.setTabsDisplay(getApplicationContext(), i);
        transaction.commitAllowingStateLoss();
    }

    private void hideFragment(FragmentTransaction transaction)
    {
        if (mAllFragment != null)
        {
            transaction.hide(mAllFragment);
        }
        if (myUploadFragment != null)
        {
            transaction.hide(myUploadFragment);
        }
        if (myCollectionFragment != null)
        {
            transaction.hide(myCollectionFragment);
        }

    }

    // private class TabPagerAdapter extends FragmentStatePagerAdapter implements
    // ViewPager.OnPageChangeListener {
    // private List<Fragment> mFragmentList;
    //
    // public TabPagerAdapter(FragmentManager fm) {
    // super(fm);
    // // mViewPager.setOnPageChangeListener(this);
    // mFragmentList = new ArrayList<Fragment>();
    // }
    //
    // @Override
    // public Fragment getItem(int position) {
    // return mFragmentList.get(position);
    // }
    //
    // @Override
    // public int getCount() {
    // return mFragmentList.size();
    // }
    //
    // @Override
    // public void onPageScrollStateChanged(int arg0) {
    //
    // }
    //
    // @Override
    // public void onPageScrolled(int position, float positionOffset,
    // int positionOffsetPixels) {
    //
    // }
    //
    // @Override
    // public void onPageSelected(int position) {
    // CustomLog.e("destory", position + "个");
    // mBSIndicator3Sharedfiles.setTabsDisplay(getApplicationContext(), position);
    // if (position != 2) {
    // SharedfilesdHomeMyCollectionFragment myCollectionFragment = new
    // SharedfilesdHomeMyCollectionFragment();
    // mFragmentList.add(myCollectionFragment);
    // TabPagerAdapter.this.notifyDataSetChanged();
    // }
    //
    // }
    //
    // public void addFragment() {
    // uid = getIntent().getStringExtra("uid");
    // SharedfilesdHomeAllFragment mAllFragment = new SharedfilesdHomeAllFragment();
    // SharedfilesdHomeMyUploadFragment myUploadFragment = new SharedfilesdHomeMyUploadFragment();
    // // SharedfilesdHomeMyCollectionFragment myCollectionFragment = new
    // // SharedfilesdHomeMyCollectionFragment();
    // mFragmentList.add(mAllFragment);
    // mFragmentList.add(myUploadFragment);
    // // mFragmentList.add(myCollectionFragment);
    // notifyDataSetChanged();
    //
    // }
    //
    // @Override
    // public void destroyItem(ViewGroup container, int position, Object object) {
    // CustomLog.e("destory1", position + "个");
    // // TODO Auto-generated method stub
    // if (position != 0) {
    // super.destroyItem(container, 0, object);
    // }
    //
    // }
    //
    // }
    // 过滤搜索
    private void filterData(String filterStr) {

        if (TextUtils.isEmpty(filterStr)) {
            mLoading.setVisibility(View.GONE);
            mBSIndicator3Sharedfiles.setVisibility(View.VISIBLE);
            id_content.setVisibility(View.VISIBLE);
            fragment_sharedfilesd_home_all_refreshlistview.setVisibility(View.GONE);
            return;
        }

        // List<DangBasicUserInfo> filterDateList = new ArrayList<DangBasicUserInfo>();
        //
        // if (TextUtils.isEmpty(filterStr)) {
        // filterDateList = mList;
        // } else {
        // filterDateList.clear();
        // for (DangBasicUserInfo personnelVO : mList) {
        // String name = personnelVO.getFullname();
        // if (name.indexOf(filterStr.toString()) != -1
        // || mCharacterParser.getSelling(name).startsWith(filterStr.toString())) {
        // filterDateList.add(personnelVO);
        // }
        // }
        // }
        //
        // // 根据a-z进行排序
        // treeViewAdapter.updateListView(filterDateList);

        // TODO Auto-generated method stub
        // 右侧变成查看按钮
        // 如果下载成功 提交下载成功的记录
        CustomDialog.showProgressDialog(context);
        final Gson gson = new Gson();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("ftoken", BSApplication.getInstance().getmCompany());
        map.put("userid", BSApplication.getInstance().getUserId());
        map.put("type", "0");
        map.put("keyword", filterStr);
        final String url = BSApplication.getInstance().getHttpTitle()
                + Constant4Sharedfiles.TYPE_LIST_PATH;
        new HttpUtilsByPC().sendPostBYPC(url, map,
                new RequestCallBackPC() {
                    @Override
                    public void onFailurePC(HttpException arg0, String arg1) {
                        mLoading.setVisibility(View.VISIBLE);
                        mBSIndicator3Sharedfiles.setVisibility(View.GONE);
                        id_content.setVisibility(View.GONE);
                        fragment_sharedfilesd_home_all_refreshlistview.setVisibility(View.GONE);
                        CustomDialog.closeProgressDialog();
                    }

                    @Override
                    public void onSuccessPC(ResponseInfo rstr) {
                        // TODO Auto-generated method stub
                        // CustomLog.e("filenpathtrue",
                        // file.result.getAbsolutePath() +
                        // "");

                        mBSIndicator3Sharedfiles.setVisibility(View.GONE);
                        id_content.setVisibility(View.GONE);
                        fragment_sharedfilesd_home_all_refreshlistview.setVisibility(View.VISIBLE);
                        mLoading.setVisibility(View.GONE);
                        mSharedfilesdHomeMyUploadVO = gson.fromJson(rstr.result.toString(),
                                SharedfilesdHomeMyUploadVO.class);
                        if (mSharedfilesdHomeMyUploadVO.getCode().equals("200")) {
                            mEXTSharedfilesdHomeMyUpdateAdapter = new EXTSharedfilesdHomeMyUpdateAdapter(
                                    EXTSharedfilesdGroupHomeActivity.this, "-1",
                                    fragment_sharedfilesd_home_all_refreshlistview);
                            mEXTSharedfilesdHomeMyUpdateAdapter.mList = mSharedfilesdHomeMyUploadVO
                                    .getArray();
                            fragment_sharedfilesd_home_all_refreshlistview
                                    .setAdapter(mEXTSharedfilesdHomeMyUpdateAdapter);

                        }
                        else {
                            mLoading.setVisibility(View.VISIBLE);
                            mBSIndicator3Sharedfiles.setVisibility(View.GONE);
                            id_content.setVisibility(View.GONE);
                            fragment_sharedfilesd_home_all_refreshlistview.setVisibility(View.GONE);
                        }
                        CustomDialog.closeProgressDialog();

                    }

                });

    }

    // @Override
    // protected void onDestroy() {
    // // TODO Auto-generated method stub
    // super.onDestroy();
    // CustomLog.e("aaa", "走了");
    // if (mEXTSharedfilesdHomeMyUpdateAdapter.listhttphandler != null &&
    // mEXTSharedfilesdHomeMyUpdateAdapter.listhttphandler.size() > 0) {
    // View nodown = View.inflate(context, R.layout.extshardfilesdhomemyupdateapternodonw, null);
    // BSDialog bsd = new BSDialog(context, "系统提醒", nodown, new OnClickListener() {
    // @Override
    // public void onClick(View arg0) {
    // // TODO Auto-generated method stub
    // // if (handler != null) {
    // // handler.cancel();
    // // mContext.finish();
    // // }
    // for (int i = 0; i < mEXTSharedfilesdHomeMyUpdateAdapter.listhttphandler.size(); i++) {
    // mEXTSharedfilesdHomeMyUpdateAdapter.listhttphandler.get(i).cancel();
    // }
    // EXTSharedfilesdGroupHomeActivity.this.finish();
    // }
    // });
    // bsd.show();
    // }
    // else {
    // EXTSharedfilesdGroupHomeActivity.this.finish();
    // }
    // }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if (mEXTSharedfilesdHomeMyUpdateAdapter == null) {
            CustomLog.e("fd", "不走");
        }
        else {
            if (mEXTSharedfilesdHomeMyUpdateAdapter.downcount != 0&&
                    mEXTSharedfilesdHomeMyUpdateAdapter.downcount > 0) {
                for (int i = 0; i < mEXTSharedfilesdHomeMyUpdateAdapter.downcount; i++) {
//                    mEXTSharedfilesdHomeMyUpdateAdapter.listhttphandler.get(i).cancel();
                    try {
                        DownloadManager.getInstance().removeDownload(i);
                    } catch (DbException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        super.onDestroy();
    }

    public class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent) {

            super(mContext);

            View view = View
                    .inflate(mContext, R.layout.items_popupwindows, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.push_bottom_in_2));

            setWidth(LayoutParams.FILL_PARENT);
            setHeight(LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);

            setContentView(view);
            // showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            Button bt1 = (Button) view
                    .findViewById(R.id.item_popupwindows_camera);
            Button bt2 = (Button) view
                    .findViewById(R.id.item_popupwindows_Photo);
            Button bt3 = (Button) view
                    .findViewById(R.id.item_popupwindows_cancel);
            bt1.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, UploadFileListActivity.class);
                    intent.putExtra("type", 1);
                    startActivity(intent);
                    dismiss();
                    // backgroundAlpha(1f);
                }
            });
            bt2.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
//                    Intent intent = new Intent(EXTSharedfilesdGroupHomeActivity.this,
////                            TestPicActivity.class);
                    dismiss();
                    // backgroundAlpha(1f);
                    EXTSharedfilesdGroupHomeActivity.this.finish();
//                    startActivity(intent);
                }
            });
            bt3.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                    // backgroundAlpha(1f);
                }
            });

        }

    }

    /**
     * 设置添加屏幕的背景透明度
     * 
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

}
