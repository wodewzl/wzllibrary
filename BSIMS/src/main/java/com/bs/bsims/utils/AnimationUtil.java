
package com.bs.bsims.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

import com.bs.bsims.R;

public class AnimationUtil {

    private static Animation animation;

    public static void showView(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation showAction = new
                TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        showAction.setDuration(500);
        view.startAnimation(showAction);

    }

    public static void hideView(View view) {
        TranslateAnimation hiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f);
        hiddenAction.setDuration(500);
        view.startAnimation(hiddenAction);
        view.setVisibility(View.GONE);
    }

    public static void setHideAnimation(final View view) {

        AlphaAnimation hideAnimation = new AlphaAnimation(1.0f, 0.0f);

        hideAnimation.setDuration(500);

        hideAnimation.setFillAfter(true);
        view.startAnimation(hideAnimation);
        hideAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                // TODO Auto-generated method stub
                view.setVisibility(View.GONE);
            }
        });

    }

    public static void setShowAnimation(final View view) {

        AlphaAnimation showAnimation = new AlphaAnimation(0.0f, 1.0f);

        showAnimation.setDuration(500);

        showAnimation.setFillAfter(true);

        view.startAnimation(showAnimation);
        showAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                // TODO Auto-generated method stub
                view.setVisibility(View.VISIBLE);
            }
        });

    }

    /**
     * 开始旋转动画
     */
    public static void setStartRotateAnimation(Context context, View view) {
        animation = AnimationUtils.loadAnimation(context,
                R.anim.bsview_rotate);
        if (null != animation) {
            LinearInterpolator lir = new LinearInterpolator();
            animation.setInterpolator(lir);
            view.startAnimation(animation);
        }

    }

    /**
     * 停止旋转动画
     */
    public static void setStopRotateAnimation(View view) {
        if (null != animation) {
            view.clearAnimation();
        }
    }

    /**
     * 开始移动进入动画
     */
    public static void setStartTranlteInAnimation(Context context, final View view) {
        animation = AnimationUtils.loadAnimation(context,
                R.anim.kcalendar_push_right_in);
        if (null != animation) {
            view.startAnimation(animation);
            animation.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationRepeat(Animation arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animation arg0) {
                    // TODO Auto-generated method stub
                    view.setVisibility(View.VISIBLE);
                }
            });
        }

    }

    /**
     * 开始移出动画
     */
    public static void setStartTranlteOutAnimation(Context context, final View view) {
        animation = AnimationUtils.loadAnimation(context,
                R.anim.kcalendar_push_right_out);
        if (null != animation) {
            animation.setInterpolator(new DecelerateInterpolator());
            view.startAnimation(animation);
            animation.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationRepeat(Animation arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animation arg0) {
                    // TODO Auto-generated method stub
                    view.setVisibility(View.GONE);
                }
            });
        }

    }


    /**
     * 开始移出动画带另一个view进入的动画
     */
    public static void setStartTranlteOutAndInAnimation(final Context context, final View view,final View viewIn) {
        animation = AnimationUtils.loadAnimation(context,
                R.anim.kcalendar_push_right_out);
        if (null != animation) {
            view.startAnimation(animation);
            animation.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationRepeat(Animation arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animation arg0) {
                    // TODO Auto-generated method stub
                    view.setVisibility(View.GONE);
                    viewIn.startAnimation(AnimationUtils.loadAnimation(context,
                            R.anim.kcalendar_push_right_in));
                    viewIn.setVisibility(View.VISIBLE);
                }
            });
        }

    }
}
