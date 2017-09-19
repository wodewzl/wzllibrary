
package com.bs.bsims.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bs.bsims.R;

@SuppressLint("NewApi")
public class BSMoveLayout extends RelativeLayout implements OnClickListener {

    private Context context;
    private int downX;
    private LinearLayout tv_top;
    private TextView delete;
    private TextView more, text01, text02, text03, text04;

    private boolean flag;
    private View view;
    private View currentView;
    private float ddx;
    private int dx;
    public DeleteListeren listeren;

    public interface DeleteListeren {
        public void deleteItem(int position);
    }

    public BSMoveLayout(Context context, boolean flag, View view, DeleteListeren listeren) {
        super(context);
        this.context = context;
        this.flag = flag;
        this.view = view;
        init();
        this.listeren = listeren;
    }

    private void init() {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.move_layout, this, true);
        delete = (TextView) findViewById(R.id.delete);
        tv_top = (LinearLayout) findViewById(R.id.tv_top);
        tv_top.addView(this.view);
        if (flag) {
            tv_top.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return handlerTouch(v, event);
                }
            });
            tv_top.setOnClickListener(this);
        }
        delete.setOnClickListener(this);
    }

    /* ---------------------处理 Touch-------------------------- */
    boolean result = false;
    boolean isOpen = false;

    protected boolean handlerTouch(View v, MotionEvent event) {

        currentView = v;
        int bottomWidth = delete.getWidth();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                // if (isAniming)
                // break;
                dx = (int) (event.getRawX() - downX);
                if (isOpen) {
                    // 打开状态， 向右滑动
                    if (dx > 0 && dx < bottomWidth) {
                        v.setTranslationX(dx - bottomWidth);
                        // 允许移动，阻止点击
                        result = true;
                    }
                } else {
                    // 闭合状态，向左移动
                    if (dx < 0 && Math.abs(dx) < bottomWidth) {
                        v.setTranslationX(dx);
                        // 允许移动，阻止点击
                        result = true;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                // 获取已经移动的
                ddx = v.getTranslationX();
                // 判断打开还是关闭
                if (ddx <= 0 && ddx > -(bottomWidth / 2)) {
                    // 关闭
                    ObjectAnimator oa1 = ObjectAnimator.ofFloat(v, "translationX", ddx, 0).setDuration(100);
                    oa1.start();
                    oa1.addListener(new AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            isOpen = false;
                            result = false;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            isOpen = false;
                            result = false;
                        }
                    });

                }
                if (ddx <= -(bottomWidth / 2) && ddx > -bottomWidth) {
                    // 打开
                    ObjectAnimator oa1 = ObjectAnimator.ofFloat(v, "translationX", ddx, -bottomWidth)
                            .setDuration(100);
                    oa1.start();
                    result = true;
                    isOpen = true;
                }
                break;
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top:
                // Toast.makeText(context, "item", 0).show();
                break;
            case R.id.more:
                break;
            case R.id.delete:
                ObjectAnimator oa1 = ObjectAnimator.ofFloat(currentView, "translationX", ddx, 0).setDuration(100);
                oa1.start();
                isOpen = false;
                result = false;
                listeren.deleteItem(Integer.parseInt(v.getTag().toString()));
                break;
        }
    }
}
