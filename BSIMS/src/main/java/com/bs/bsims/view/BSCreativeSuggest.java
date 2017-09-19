
package com.bs.bsims.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import com.bs.bsims.R;
import com.bs.bsims.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 顶部indicator
 * 
 * @author dewyze
 */
@SuppressLint("NewApi")
public class BSCreativeSuggest extends LinearLayout {

    private static final String TAG = "TopIndicator";
    private int[] mDrawableIds = new int[] {
            R.drawable.bg_create_02,
            R.drawable.bg_create_03
    };
    private List<CheckedTextView> mCheckedList = new ArrayList<CheckedTextView>();
    private List<View> mViewList = new ArrayList<View>();
    // 顶部菜单的文字数组
    private CharSequence[] mLabels = new CharSequence[] {
            "全部意见分享", "我的意见分享"
    };
    private int mScreenWidth;
    private int mUnderLineWidth;
    private View mUnderLine;
    // 底部线条移动初始位置
    private int mUnderLineFromX = 0;

    public BSCreativeSuggest(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public BSCreativeSuggest(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BSCreativeSuggest(Context context) {
        super(context);
        init(context);
    }

    private void init(final Context context) {

        setOrientation(LinearLayout.VERTICAL);
        this.setBackgroundColor(Color.rgb(250, 250, 250));
        mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
        mUnderLineWidth = mScreenWidth / mLabels.length;

        mUnderLine = new View(context);
        mUnderLine.setBackgroundColor(Color.rgb(0, 169, 254));
        LinearLayout.LayoutParams underLineParams = new LinearLayout.LayoutParams(
                mUnderLineWidth, 4);

        // 标题layout
        LinearLayout topLayout = new LinearLayout(context);
        LinearLayout.LayoutParams topLayoutParams = new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        topLayout.setOrientation(LinearLayout.HORIZONTAL);

        LayoutInflater inflater = LayoutInflater.from(context);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;
        params.gravity = Gravity.CENTER;

        int size = mLabels.length;
        for (int i = 0; i < size; i++) {

            final int index = i;

            final View view = inflater.inflate(R.layout.top_indicator_item,
                    null);

            final CheckedTextView itemName = (CheckedTextView) view
                    .findViewById(R.id.item_name);
            itemName.setCompoundDrawablesWithIntrinsicBounds(context
                    .getResources().getDrawable(mDrawableIds[i]), null, null,
                    null);
            itemName.setCompoundDrawablePadding(10);
            itemName.setText(mLabels[i]);

            topLayout.addView(view, params);

            itemName.setTag(index);

            mCheckedList.add(itemName);
            mViewList.add(view);

            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (null != mTabListener) {
                        mTabListener.onIndicatorSelected(index);
                    }
                }
            });

            // 初始化 底部菜单选中状态,默认第一个选中
            if (i == 0) {
                itemName.setChecked(true);
                // itemName.setTextColor(Color.rgb(247, 88, 123));
                itemName.setTextColor(Color.rgb(0, 169, 254));
            } else {
                itemName.setChecked(false);
                // itemName.setTextColor(Color.rgb(19, 12, 14));
                itemName.setTextColor(Color.parseColor("#666666"));
            }

        }
        this.addView(topLayout, topLayoutParams);
        this.addView(mUnderLine, underLineParams);
    }

    /**
     * 设置底部导航中图片显示状态和字体颜色
     */
    public void setTabsDisplay(Context context, int index) {
        int size = mCheckedList.size();
        for (int i = 0; i < size; i++) {
            CheckedTextView checkedTextView = mCheckedList.get(i);
            if ((Integer) (checkedTextView.getTag()) == index) {
                LogUtils.i(TAG, mLabels[index] + " is selected...");
                checkedTextView.setChecked(true);
                checkedTextView.setTextColor(Color.rgb(0, 169, 254));
            } else {
                checkedTextView.setChecked(false);
                // checkedTextView.setTextColor(Color.rgb(19, 12, 14));
                checkedTextView.setTextColor(Color.parseColor("#666666"));
            }
        }
        // 下划线动画
        doUnderLineAnimation(index);
    }

    private void doUnderLineAnimation(int index) {
        TranslateAnimation animation = new TranslateAnimation(mUnderLineFromX,
                index * mUnderLineWidth, 0, 0);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setFillAfter(true);
        animation.setDuration(150);
        mUnderLine.startAnimation(animation);
        mUnderLineFromX = index * mUnderLineWidth;
    }

    // 回调接口
    private OnTopIndicatorListener mTabListener;

    public interface OnTopIndicatorListener {
        void onIndicatorSelected(int index);
    }

    public void setOnTopIndicatorListener(OnTopIndicatorListener listener) {
        this.mTabListener = listener;
    }

    public int[] getmDrawableIds() {
        return mDrawableIds;
    }

    public void setmDrawableIds(int[] mDrawableIds) {
        this.mDrawableIds = mDrawableIds;
    }

    public CharSequence[] getmLabels() {
        return mLabels;
    }

    public void setmLabels(CharSequence[] mLabels) {
        this.mLabels = mLabels;
    }

}
