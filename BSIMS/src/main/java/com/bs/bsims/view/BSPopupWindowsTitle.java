
package com.bs.bsims.view;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.PopupListAdapter;
import com.bs.bsims.model.TreeVO;
import com.bs.bsims.time.ScreenInfo;
import com.bs.bsims.time.WheelMain;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CommonUtils.ResultCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@SuppressLint("NewApi")
public class BSPopupWindowsTitle extends PopupWindow {
    private LinearLayout conentView;
    private ListView mParentListView, mChildListView;
    private PopupListAdapter mParentAdapter, mChildAdapter;
    private Context mContext;
    private List<TreeVO> mList;
    private List<PopupListAdapter> mAdapterList;
    private List<ListView> mListViewList;
    private int mCurrentLevel;
    private int mSreenWidth;
    private ListView mListView;

    // 多久弹出框点击后回掉接口
    public interface TreeCallBack {
        public void callback(TreeVO vo);
    }

    private TreeCallBack mCallBack;

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 0);
        } else {
            this.dismiss();
        }
    }

    // 时间
    public BSPopupWindowsTitle(Activity context, int type, int height, final ResultCallback callback, boolean flage) {
        this.mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        final View timepickerview = inflater.inflate(R.layout.timepicker, null);
        ScreenInfo screenInfo = new ScreenInfo(context);
        final WheelMain timeWheel = new WheelMain(timepickerview, false, flage);// 是否显示天
        timeWheel.screenheight = screenInfo.getHeight();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        switch (type) {
            case 1:
                timeWheel.initDateTimePicker(year, month, day, hour, minute);
                break;
            case 2:
                timeWheel.initDateTimePicker(year, month, day, 0, 0);
                break;
            case 3:
                timeWheel.initDateTimePicker(year, month, 0, 0, 0);
                break;
            case 4:
                timeWheel.initDateTimePicker(0, month, day, 0, 0);
                break;
            case 5:
                timeWheel.initDateTimePicker(0, 0, 0, hour, minute);
                break;
            case 6:
                timeWheel.initDateTimePickerWithJD(year, month, 0, 0, 0);
                break;
            default:
                break;
        }

        LinearLayout linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);// 定义文本显示组件
        timepickerview.setLayoutParams(params);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(timepickerview);

        TextView textView = new TextView(context);
        int padding = CommonUtils.dip2px(context, 10);
        textView.setPadding(padding, padding, padding, padding);
        textView.setText("确定");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(context.getResources().getColor(R.color.C1));
        textView.setBackgroundColor(context.getResources().getColor(R.color.C7));
        textView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                callback.callback(timeWheel.getTime(), 0);
                dismiss();
            }
        });
        linearLayout.addView(textView);

        this.setContentView(linearLayout);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);

        this.setFocusable(true);
        this.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(Color.parseColor("#40000000"));
        this.setBackgroundDrawable(dw);

        linearLayout.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                dismiss();
                return true;
            }
        });
    }

    // 单级
    public BSPopupWindowsTitle(final Activity context, final List<TreeVO> array, TreeCallBack callback, int height) {

        this.mContext = context;
        this.mCallBack = callback;
        final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < array.size(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("option", array.get(i).getName());
            list.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(context, list, R.layout.dropdown_one_leve_item,
                new String[] {
                        "option"
                }, new int[] {
                        R.id.textview
                });
        ListView listView = new ListView(context);
        listView.setAdapter(adapter);
        listView.setDivider(new ColorDrawable(context.getResources().getColor(R.color.C3)));
        listView.setDividerHeight(1);
        listView.setVerticalScrollBarEnabled(true);
        LinearLayout linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                height);// 定义文本显示组件
        listView.setLayoutParams(params);
        linearLayout.addView(listView);

        this.setContentView(linearLayout);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);

        this.setFocusable(true);
        this.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(Color.parseColor("#40000000"));
        this.setBackgroundDrawable(dw);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TreeVO childVo = array.get((int) id);
                mCallBack.callback(childVo);
                dismiss();
            }
        });

        linearLayout.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                dismiss();
                return true;
            }
        });

    }

    // 可多级
    public BSPopupWindowsTitle(final Activity context, List<TreeVO> treevo, TreeCallBack callback) {
        this.mContext = context;
        this.mAdapterList = new ArrayList<PopupListAdapter>();
        this.mListViewList = new ArrayList<ListView>();
        this.mList = treevo;
        this.mCallBack = callback;
        mSreenWidth = CommonUtils.getScreenWidth(mContext);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout rootView = (LinearLayout) inflater.inflate(R.layout.popup_category, null);
        conentView = (LinearLayout) rootView.findViewById(R.id.content_layout);
        rootView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                dismiss();
                return false;
            }
        });

        ArrayList<TreeVO> list = (ArrayList<TreeVO>) getLeveData(null);
        CreateListView(list);

        if (list.get(0).isHaschild())
            CreateListView(getLeveData(list.get(0)));

        this.setContentView(rootView);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);

        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        // ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(new BitmapDrawable());
        // 设置SelectPicPopupWindow弹出窗体动画效果
        // this.setAnimationStyle(R.style.AnimationPreview);
    }

    // 获取当前条目下的子条目
    public ArrayList<TreeVO> getLeveData(TreeVO treevo) {
        ArrayList<TreeVO> list = new ArrayList<TreeVO>();
        for (int i = 0; i < mList.size(); i++) {
            TreeVO vo = mList.get(i);
            if (treevo == null) {
                if (0 == mList.get(i).getParentId()) {
                    list.add(vo);
                }
            } else {
                if (treevo.getId() == mList.get(i).getParentId()) {
                    list.add(vo);
                }
            }

        }
        return list;
    }

    // 动态添加listView
    public void CreateListView(final ArrayList<TreeVO> list) {

        mCurrentLevel = list.get(0).getLevel();
        final ListView listView = new ListView(mContext);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                CommonUtils.getScreenWidth(mContext) / 2,
                CommonUtils.getScreenHigh(mContext) / 3);// 定义文本显示组件

        if (mCurrentLevel == 1) {
            listView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else if (mCurrentLevel == 2) {
            listView.setBackgroundColor(Color.parseColor("#F2F2F2"));
        } else if (mCurrentLevel == 3) {
            listView.setBackgroundColor(Color.parseColor("#DCDCDC"));
        }
        listView.setLayoutParams(params);
        listView.setDivider(null);
        listView.setVerticalScrollBarEnabled(true);
        conentView.addView(listView);
        final PopupListAdapter adapter = new PopupListAdapter(mContext, null, list);
        listView.setAdapter(adapter);
        mAdapterList.add(adapter);
        mListViewList.add(listView);
        adapter.setSelectDid(list.get(0).getId());
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TreeVO parentVo = list.get((int) id);
                if (parentVo.getName() != null)
                    setSelectItem(parentVo, adapter);
            }
        });

        if (mCurrentLevel > 2) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(mListViewList.get(mCurrentLevel -
                    2), "translationX", 0, -mSreenWidth / 4).setDuration(500);
            animator.start();
            mListViewList.get(mCurrentLevel - 2).setTag(true);

            ObjectAnimator animator1 = ObjectAnimator.ofFloat(listView, "translationX", 0,
                    -mSreenWidth / 2).setDuration(500);
            animator1.start();
            listView.setTag(true);
        }

    }

    public void setSelectItem(TreeVO parentVo, PopupListAdapter adapter) {
        adapter.setSelectDid(parentVo.getId());
        adapter.notifyDataSetChanged();

        // 点击父类菜单后，如有之类菜单，则颜色重置
        if (parentVo.getLevel() + 1 < mCurrentLevel) {
            for (int i = 0; i < mListViewList.get(parentVo.getLevel() + 1).getChildCount(); i++) {
                View v = mListViewList.get(parentVo.getLevel()).getChildAt(i);
                v.findViewById(R.id.item_layout).setBackgroundColor(mContext.getResources().getColor(R.color.C1));
            }
        }

        // 点击父类菜单后，如有之类菜单，则创建子类
        // getLeveData(childVo).size() > 0
        if (parentVo.isHaschild()) {
            if (parentVo.getLevel() >= mCurrentLevel) {
                CreateListView(getLeveData(parentVo));
            } else {
                // 若之类菜单已有，直接更新数据
                mAdapterList.get(parentVo.getLevel()).updateData(getLeveData(parentVo));

                // 判断是否移动过位置
                if (parentVo.getLevel() == 1) {
                    if (mListViewList.get(parentVo.getLevel()).getTag() != null && (Boolean) mListViewList.get(parentVo.getLevel()).getTag()) {
                        ObjectAnimator animator =
                                ObjectAnimator.ofFloat(mListViewList.get(parentVo.getLevel()),
                                        "translationX", -mSreenWidth / 3, 0).setDuration(500);
                        animator.start();
                        mListViewList.get(parentVo.getLevel()).setTag(false);
                    }
                }

                // 已有菜单，点击弹出子菜单，动画效果
                if (mCurrentLevel > 2 && parentVo.getLevel() != 1) {
                    if (mListViewList.get(mCurrentLevel - 2).getTag() != null &&
                            !(Boolean) mListViewList.get(mCurrentLevel - 2).getTag()) {
                        ObjectAnimator animator = ObjectAnimator.ofFloat(mListViewList.get(mCurrentLevel -
                                2), "translationX", 0, -mSreenWidth / 4).setDuration(500);
                        animator.start();
                        mListViewList.get(mCurrentLevel - 2).setTag(true);
                    }
                    if (mListViewList.get(mCurrentLevel - 1).getTag() != null &&
                            !(Boolean) mListViewList.get(mCurrentLevel - 1).getTag()) {
                        ObjectAnimator animator1 = ObjectAnimator.ofFloat(mListViewList.get(mCurrentLevel -
                                1), "translationX", 0,
                                -mSreenWidth / 2).setDuration(500);
                        animator1.start();
                        mListViewList.get(mCurrentLevel -
                                1).setTag(true);
                    }

                }

                ArrayList<TreeVO> list = new ArrayList<TreeVO>();
                // 若之类菜单更有之类菜单，且已经被创建，则清除
                for (int i = parentVo.getLevel() + 1; i < mCurrentLevel; i++) {

                    // 判断是否移动过位置
                    if (mListViewList.get(i).getTag() != null && (Boolean) mListViewList.get(i).getTag()) {

                        ObjectAnimator animator = ObjectAnimator.ofFloat(mListViewList.get(i),
                                "translationX", -mSreenWidth / 3, 0).setDuration(500);
                        animator.start();
                        mListViewList.get(i).setTag(false);
                    }

                    mAdapterList.get(i).updateData(list);
                }
            }
        } else {
            // 若之类菜单更有之类菜单，且已经被创建，则清除
            if (parentVo.getLevel() < mCurrentLevel) {
                ArrayList<TreeVO> list = new ArrayList<TreeVO>();
                for (int i = parentVo.getLevel(); i < mCurrentLevel; i++) {

                    // 判断是否移动过位置
                    if (parentVo.getLevel() == 1) {
                        if (mListViewList.get(i).getTag() != null && (Boolean)
                                mListViewList.get(i).getTag()) {
                            ObjectAnimator animator = ObjectAnimator.ofFloat(mListViewList.get(i),
                                    "translationX", -mSreenWidth / 3, 0).setDuration(500);
                            animator.start();
                            mListViewList.get(i).setTag(false);
                        }
                    }
                    mAdapterList.get(i).updateData(list);
                }
            }

            mCallBack.callback(parentVo);
            BSPopupWindowsTitle.this.dismiss();
        }
    }
}
