
package com.wuzhanglong.library.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wuzhanglong.library.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 底部导航
 * 
 * @author dewyze
 */
public class TabWidget extends LinearLayout {
    private static final String TAG = "MyTabWidget";
//    private int[] mDrawableIds = new int[] {
//            R.drawable.home_tb01, R.drawable.home_tb02, R.drawable.home_tb03, R.drawable.home_tb04
//    };
private int[] mDrawableIds;
    private int mHight;
    // 存放底部菜单的各个文字CheckedTextView
    private List<CheckedTextView> mCheckedList = new ArrayList<CheckedTextView>();
    // 存放底部菜单每项View
    private List<View> mViewList = new ArrayList<View>();
    // 存放指示点
    private List<ImageView> mIndicateImgs = new ArrayList<ImageView>();
    // 底部菜单的文字数组
    private CharSequence[] mLabels;

    @SuppressLint("NewApi")
    public TabWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public TabWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
//        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabWidget);
//        int ss=a.getResourceId( R.styleable.TabWidget_tab_img,0);
//        TypedArray     mTypedArray=context.getResources().obtainTypedArray(ss);
//        //参数含义，第一个参数为 ：所取图片在数组中的索引，第二个参数为：未找到时，返回的默认值。
//        mDrawableIds=new int[mTypedArray.length()];
//        for (int i = 0; i < mTypedArray.length() ; i++) {
//            mDrawableIds[i]=mTypedArray  .getResourceId(i, 1);
//        }
//        init(context);
    }

    public TabWidget(Context context) {
        super(context);
        init(context);
    }

    /**
     * 初始化控件
     */
    private void init(final Context context) {
        if(mDrawableIds==null)
            return;
        this.setOrientation(LinearLayout.HORIZONTAL);
        LayoutInflater inflater = LayoutInflater.from(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.weight = 1.0f;
        params.gravity = Gravity.CENTER;
        int size = mDrawableIds.length;
        for (int i = 0; i < size; i++) {
            final int index = i;
            // 每个tab对应的layout
            final View view = inflater.inflate(R.layout.home_tab_item, null);
            final CheckedTextView itemName = (CheckedTextView) view.findViewById(R.id.item_name);
            itemName.setCompoundDrawablesWithIntrinsicBounds(0, mDrawableIds[i], 0, 0);
            // 指示点ImageView，如有版本更新需要显示
            final ImageView indicateImg = (ImageView) view.findViewById(R.id.indicate_img);
            this.addView(view, params);
            itemName.setTag(index);
            // 将CheckedTextView添加到list中，便于操作
            mCheckedList.add(itemName);
            // 将指示图片加到list，便于控制显示隐藏
            mIndicateImgs.add(indicateImg);
            // 将各个tab的View添加到list
            mViewList.add(view);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 设置底部图片和文字的显示
                    setTabsDisplay(context, index);
                    if (null != mTabListener) {
                        // tab项被选中的回调事件
                        mTabListener.onTabSelected(index);
                    }
                }
            });

            // 初始化 底部菜单选中状态,默认第一个选中
            if (i == 0) {
                itemName.setChecked(true);
                itemName.setTextColor(context.getResources().getColor(R.color.sy_title_color));
            } else {
                itemName.setChecked(false);
                itemName.setTextColor(context.getResources().getColor(R.color.C5));
            }
            view.setBackgroundColor(Color.parseColor("#00000000"));
        }
    }

    /**
     * 设置指示点的显示
     * 
     * @param context
     * @param position 显示位置
     * @param visible 是否显示，如果false，则都不显示
     */
    public void setIndicateDisplay(Context context, int position,
            boolean visible) {
        int size = mIndicateImgs.size();
        if (size <= position) {
            return;
        }
        ImageView indicateImg = mIndicateImgs.get(position);
        indicateImg.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置底部导航中图片显示状态和字体颜色
     */
    public void setTabsDisplay(Context context, int index) {
        int size = mCheckedList.size();
        for (int i = 0; i < size; i++) {
            CheckedTextView checkedTextView = mCheckedList.get(i);
            if ((Integer) (checkedTextView.getTag()) == index) {
                checkedTextView.setChecked(true);
            } else {
                checkedTextView.setChecked(false);
            }
            mViewList.get(i).setBackgroundColor(Color.parseColor("#00000000"));
        }
    }

    // 回调接口，用于获取tab的选中状态
    private OnTabSelectedListener mTabListener;

    public interface OnTabSelectedListener {
        void onTabSelected(int index);
    }

    public void setOnTabSelectedListener(OnTabSelectedListener listener) {
        this.mTabListener = listener;
    }


    public void update(Context context ,int[] drawableIds){
        this.mDrawableIds = drawableIds;
        init(context);
    }
}
