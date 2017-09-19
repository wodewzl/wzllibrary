/**
 * 
 */

package com.bs.bsims.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.BossStatisticsDragAdapter;
import com.bs.bsims.adapter.BossStatisticsOtherAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.ChannelItem;
import com.bs.bsims.model.ChannelManage;
import com.bs.bsims.model.CrmProductVo;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.view.BSDragGridView;
import com.bs.bsims.view.BSGridView;
import com.bs.bsims.xutils.impl.HttpUtilsByPC;
import com.bs.bsims.xutils.impl.RequestCallBackPC;

import org.json.JSONObject;
import org.xutils.ex.HttpException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-1-16
 * @version 1.22 BOSS统计的导航菜单添加页
 */
public class BossStatisticsLogoUtilsActivity extends BaseActivity implements OnItemClickListener {

    /*
     * (non-Javadoc)
     * @see BaseActivity#baseSetContentView()
     */

    private Context mContext;

    /** 用户栏目的GRIDVIEW */
    private BSDragGridView userGridView;
    /** 其它栏目的GRIDVIEW */
    private BSGridView otherGridView;
    /** 用户栏目对应的适配器，可以拖动 */
    BossStatisticsDragAdapter userAdapter;
    /** 其它栏目对应的适配器 */
    BossStatisticsOtherAdapter otherAdapter;
    /** 其它栏目列表 */
    ArrayList<ChannelItem> otherChannelList = new ArrayList<ChannelItem>();
    /** 用户栏目列表 */
    ArrayList<ChannelItem> userChannelList = new ArrayList<ChannelItem>();
    /** 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。 */
    boolean isMove = false;
    private ChannelManage channelManage;
    /** 每次从首页传递过来的序列数组 **/
    private String getLogoMenu[];
    private int lenth1 = 0;// 传递过来添加的长度
    private int lenth2 = 0;// 传递过来未添加的长度

    private StringBuffer related = new StringBuffer();// 配置菜单的id

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        View layout = View.inflate(this, R.layout.bossstatictislogo_utils, null);
        mContentLayout.addView(layout);
        mContext = this;
        CrmProductVo crmProductVo = (CrmProductVo) getIntent().getSerializableExtra("logomodel");
        channelManage = new ChannelManage(crmProductVo);
        getLogoMenu = planDataMenu(crmProductVo);// 得到已经经过处理数据源
        // initView();

    }

    /** 初始化数据 */
    @SuppressLint("NewApi")
    private void initData() {
        userChannelList = ChannelManage.getDefaultUserChannels();
        otherChannelList = ChannelManage.getDefaultOtherChannels();
        userAdapter = new BossStatisticsDragAdapter(this, userChannelList);
        userGridView.setAdapter(userAdapter);
        otherAdapter = new BossStatisticsOtherAdapter(this, otherChannelList);
        otherGridView.setAdapter(this.otherAdapter);
        // 设置GRIDVIEW的ITEM的点击监听
        otherGridView.setOnItemClickListener(this);
        userGridView.setOnItemClickListener(this);

    }

    /*
     * (non-Javadoc)
     * @see BaseActivity#getDataResult()
     */
    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return true;
    }

    /*
     * (non-Javadoc)
     * @see BaseActivity#updateUi()
     */
    @Override
    public void updateUi() {
        // TODO Auto-generated method stub

    }

    /** 处理传递过来的菜单数据的方法 **/

    @SuppressWarnings("null")
    @SuppressLint("NewApi")
    public String[] planDataMenu(CrmProductVo vo) {
        String getLogoMenuSelect[] = null;
        String getLogoMenuUnSelect[] = null;
        if (null != vo.getInfo().getSelected()) {
            lenth1 = vo.getInfo().getSelected().size();
            getLogoMenuSelect = new String[lenth1];
            for (int i = 0; i < vo.getInfo().getSelected().size(); i++) {
                getLogoMenuSelect[i] = vo.getInfo().getSelected().get(i).getId();
            }

        }

        if (null != vo.getInfo().getUnselected()) {
            lenth2 = vo.getInfo().getUnselected().size();
            getLogoMenuUnSelect = new String[lenth2];
            for (int j = 0; j < vo.getInfo().getUnselected().size(); j++) {
                getLogoMenuUnSelect[j] = vo.getInfo().getUnselected().get(j).getId();
            }
        }

        if (lenth1 != 0 && lenth2 != 0) {
            getLogoMenuSelect = Arrays.copyOf(getLogoMenuSelect, lenth1 + lenth2);// 扩容
            System.arraycopy(getLogoMenuUnSelect, 0, getLogoMenuSelect, lenth1, lenth2);// 将第二个数组与第一个数组合并
            return getLogoMenuSelect;
        } else if (lenth1 != 0 && lenth2 == 0) {
            return getLogoMenuSelect;
        } else {
            return getLogoMenuUnSelect;
        }
    }

    /*
     * (non-Javadoc)
     * @see BaseActivity#initView()
     */
    @SuppressLint("NewApi")
    @Override
    public void initView() {
        // TODO Auto-generated method stub
        userGridView = (BSDragGridView) findViewById(R.id.userGridView);
        otherGridView = (BSGridView) findViewById(R.id.otherGridView);
        userGridView.setLayoutAnimation(getAnimationController());
        otherGridView.setLayoutAnimation(getAnimationController());

     

        mTitleTv.setText("更多");
        mOkTv.setText("确定");
        initData();
    }

    /**
     * 点击ITEM移动动画
     * 
     * @param moveView
     * @param startLocation
     * @param endLocation
     * @param moveChannel
     * @param clickGridView
     */
    private void MoveAnim(View moveView, int[] startLocation, int[] endLocation, final ChannelItem moveChannel, final GridView clickGridView) {
        int[] initLocation = new int[2];
        // 获取传递过来的VIEW的坐标
        moveView.getLocationInWindow(initLocation);
        // 得到要移动的VIEW,并放入对应的容器中
        final ViewGroup moveViewGroup = getMoveViewGroup();
        final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
        // 创建移动动画
        TranslateAnimation moveAnimation = new TranslateAnimation(startLocation[0], endLocation[0], startLocation[1], endLocation[1]);
        moveAnimation.setDuration(500);// 动画时间
        // 动画配置
        AnimationSet moveAnimationSet = new AnimationSet(true);
        moveAnimationSet.setFillAfter(false);// 动画效果执行完毕后，View对象不保留在终止的位置
        moveAnimationSet.addAnimation(moveAnimation);
        mMoveView.startAnimation(moveAnimationSet);
        moveAnimationSet.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                isMove = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                moveViewGroup.removeView(mMoveView);
                // instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
                if (clickGridView instanceof BSDragGridView) {
                    otherAdapter.setVisible(true);
                    otherAdapter.notifyDataSetChanged();
                    userAdapter.remove();
                } else {
                    userAdapter.setVisible(true);
                    userAdapter.notifyDataSetChanged();
                    otherAdapter.remove();
                }

                isMove = false;

            }
        });
    }

    /**
     * 获取移动的VIEW，放入对应ViewGroup布局容器
     * 
     * @param viewGroup
     * @param view
     * @param initLocation
     * @return
     */
    private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
        int x = initLocation[0];
        int y = initLocation[1];
        viewGroup.addView(view);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = x;
        mLayoutParams.topMargin = y;
        view.setLayoutParams(mLayoutParams);
        return view;
    }

    /**
     * 创建移动的ITEM对应的ViewGroup布局容器
     */
    private ViewGroup getMoveViewGroup() {
        ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
        LinearLayout moveLinearLayout = new LinearLayout(this);
        moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        moveViewGroup.addView(moveLinearLayout);
        return moveLinearLayout;
    }

    /**
     * 获取点击的Item的对应View，
     * 
     * @param view
     * @return
     */
    private ImageView getView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        ImageView iv = new ImageView(this);
        iv.setImageBitmap(cache);
        return iv;
    }

    /*
     * (non-Javadoc)
     * @see BaseActivity#bindViewsListener()
     */
    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub
        mOkTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (userAdapter.channelList.size() == 0) {
                    CustomToast.showShortToast(mContext, "至少选择一项菜单哦~");
                    return;
                }

                if (checkDataIsChange()) {
                    final String url = BSApplication.getInstance().getHttpTitle() + Constant.BOSS_STATISTICS_INDEX_MENU;
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("ftoken", BSApplication.getInstance().getmCompany());
                    map.put("userid", BSApplication.getInstance().getUserId());
                    related.setLength(0);// 参数数据
                    for (int i = 0; i < userAdapter.channelList.size(); i++) {
                        related.append(userAdapter.channelList.get(i).getId());
                        if (i != userAdapter.channelList.size() - 1) {
                            related.append(",");
                        }

                    }
                    map.put("option", related.toString());

                    // 提交接口
                    new HttpUtilsByPC().sendPostBYPC(url, map,
                            /**
                             * @author Administrator
                             */
                            new RequestCallBackPC() {
                                @Override
                                public void onFailurePC(HttpException arg0, String arg1) {
                                    // TODO Auto-generated method stub
                                    CustomToast.showLongToast(mContext, "网络似乎断开了~");
                                }

                                @Override
                                public void onSuccessPC(ResponseInfo rstr) {
                                    // TODO Auto-generated method stub

                                    JSONObject jsonObject;
                                    try {
                                        jsonObject = new JSONObject(new String(rstr.result.toString()));
                                        String str = (String) jsonObject.get("retinfo");
                                        String code = (String) jsonObject.get("code");
                                        if (Constant.RESULT_CODE.equals(code)) {
                                            Intent i = new Intent();
                                            i.putExtra("value", "boolean");
                                            setResult(2016, i);
                                            BossStatisticsLogoUtilsActivity.this.finish();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                            });

                } else {
                    CustomToast.showShortToast(mContext, "您还没有做任何操作哦~");
                }
            }
        });
    }

    /**
     * 判断用户是否操作了数据变化
     **/

    public boolean checkDataIsChange() {
        if (userAdapter.channelList.size() != lenth1 || otherAdapter.channelList.size() != lenth2) {
            return true;// 说明有变
        } else if (isChangeListSort()) {
            return true;// 说明有变化 返回的true
        } else {
            return false;// 说明没有任何变化
        }
    }

    /*
     * 判断整个数据的排序是否发生了变化
     */
    @SuppressLint("NewApi")
    public boolean isChangeListSort() {
        String getLogoMenuSelect[] = new String[userAdapter.channelList.size()];
        String getLogoMenuUnSelect[] = new String[otherAdapter.channelList.size()];
        for (int i = 0; i < userAdapter.channelList.size(); i++) {
            getLogoMenuSelect[i] = userAdapter.channelList.get(i).getId() + "";
        }
        for (int j = 0; j < otherAdapter.channelList.size(); j++) {
            getLogoMenuUnSelect[j] = otherAdapter.channelList.get(j).getId() + "";
        }

        getLogoMenuSelect = Arrays.copyOf(getLogoMenuSelect, userAdapter.channelList.size() + otherAdapter.channelList.size());// 扩容
        System.arraycopy(getLogoMenuUnSelect, 0, getLogoMenuSelect, userAdapter.channelList.size(), otherAdapter.channelList.size());// 将第二个数组与第一个数组合并

        for (int i = 0; i < getLogoMenu.length; i++) {
            if (!getLogoMenu[i].equals(getLogoMenuSelect[i])) {
                return true;// 说明有变化
            }
        }
        return false;// 说明没有变化

    }

    /*
     * (non-Javadoc)
     * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget .AdapterView,
     * android.view.View, int, long)
     */
    @Override
    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
        // TODO Auto-generated method stub
        // 如果点击的时候，之前动画还没结束，那么就让点击事件无效
        if (isMove) {
            return;
        }
        switch (parent.getId()) {
            case R.id.userGridView:
                // position为 0，1 的不可以进行任何操作
                // if (position != 0 && position != 1) {
                final ImageView moveImageView = getView(view);
                if (moveImageView != null) {
                    final TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                    final int[] startLocation = new int[2];
                    newTextView.getLocationInWindow(startLocation);
                    final ChannelItem channel = ((BossStatisticsDragAdapter) parent.getAdapter()).getItem(position);// 获取点击的频道内容
                    otherAdapter.setVisible(false);
                    // 添加到最后一个
                    otherAdapter.addItem(channel);
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                userAdapter.setDisPlayView(position, true);
                                int[] endLocation = new int[2];
                                // 获取终点的坐标
                                otherGridView.getChildAt(otherGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                MoveAnim(moveImageView, startLocation, endLocation, channel, userGridView);
                                userAdapter.setRemove(position);
                            } catch (Exception localException) {
                            }
                        }
                    });
                }
                // }
                break;
            case R.id.otherGridView:
                final ImageView moveImageViews = getView(view);
                if (moveImageViews != null) {
                    final ChannelItem channel = ((BossStatisticsOtherAdapter) parent.getAdapter()).getItem(position);
                    userAdapter.setVisible(false);
                    // 添加到最后一个
                    userAdapter.addItem(channel);
                    TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                    final int[] startLocation = new int[2];
                    newTextView.getLocationInWindow(startLocation);
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                otherAdapter.setDisPlayView(position, true);
                                int[] endLocation = new int[2];
                                // 获取终点的坐标
                                userGridView.getChildAt(userGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                MoveAnim(moveImageViews, startLocation, endLocation, channel, otherGridView);
                                otherAdapter.setRemove(position);
                            } catch (Exception localException) {
                            }
                        }
                    });
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // saveChannel();
        super.onBackPressed();
    }

    /**
     * Layout动画
     * 
     * @return
     */
    protected LayoutAnimationController getAnimationController() {
        int duration = 150;
        AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(duration);
        set.addAnimation(animation);

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(duration);
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        return controller;
    }

}
