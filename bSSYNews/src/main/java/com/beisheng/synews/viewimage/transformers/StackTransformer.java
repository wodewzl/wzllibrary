
package com.beisheng.synews.viewimage.transformers;

import android.view.View;

import com.nineoldandroids.view.ViewHelper;

public class StackTransformer extends BaseTransformer {

    @Override
    protected void onTransform(View view, float position) {
        ViewHelper.setTranslationX(view, position < 0 ? 0f : -view.getWidth() * position);
    }

}
