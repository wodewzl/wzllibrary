
package com.bs.bsims.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.widget.EditText;

import com.bs.bsims.R;
import com.bs.bsims.emoji.EmojiconHandler;
import com.bs.bsims.emoji.EmojiconSpan;

public class BsPasteEditTextIM extends EditText {
    private static final String TAG = "EmojiconEditText";
    private int mEmojiconSize;

    public BsPasteEditTextIM(Context context) {
        super(context);
        mEmojiconSize = (int) getTextSize();

    }

    public BsPasteEditTextIM(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BsPasteEditTextIM(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Emojicon);
        mEmojiconSize = (int) a.getDimension(R.styleable.Emojicon_emojiconSize, getTextSize());
        a.recycle();
        setText(getText());
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        EmojiconHandler.addEmojis(getContext(), getText(), mEmojiconSize);
    }
    
    public static String convertToMsg(CharSequence cs, Context mContext) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(cs);
        EmojiconSpan[] spans = ssb.getSpans(0, cs.length(), EmojiconSpan.class);
        for (int i = 0; i < spans.length; i++) {
            EmojiconSpan span = spans[i];
            String c = EmojiconHandler.getEmojiCode(span.getmResourceId());
            int a = ssb.getSpanStart(span);
            int b = ssb.getSpanEnd(span);
            ssb.replace(a, b, c);
        }
        ssb.clearSpans();
        return ssb.toString();
    }
    /**
     * Set the size of emojicon in pixels.
     */
    public void setEmojiconSize(int pixels) {
        mEmojiconSize = pixels;
    }

}
