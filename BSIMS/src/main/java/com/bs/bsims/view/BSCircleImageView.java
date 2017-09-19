
package com.bs.bsims.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;


import com.bs.bsims.R;
import com.bs.bsims.activity.ContactPersonActivity;
import com.bs.bsims.image.selector.ImageActivityUtils;

import com.nostra13.universalimageloader.core.ImageLoader;

public class BSCircleImageView extends ImageView implements OnClickListener {

    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 1;

    private static final int DEFAULT_BORDER_WIDTH = 0;
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;

    private final RectF mDrawableRect = new RectF();
    private final RectF mBorderRect = new RectF();

    private final Matrix mShaderMatrix = new Matrix();
    private final Paint mBitmapPaint = new Paint();
    private final Paint mBorderPaint = new Paint();

    private int mBorderColor = DEFAULT_BORDER_COLOR;
    private int mBorderWidth = DEFAULT_BORDER_WIDTH;
    private int mBorderColorOut = DEFAULT_BORDER_COLOR;
    private int mBorderWidthOut = DEFAULT_BORDER_WIDTH;

    private Bitmap mBitmap;
    private BitmapShader mBitmapShader;
    private int mBitmapWidth;
    private int mBitmapHeight;

    private float mDrawableRadius;
    private float mBorderRadius;

    private boolean mReady;
    private boolean mSetupPending;

    private int measureWidht;
    private int measureHeight;
    private int radius;
    private int badgeNumber;
    private String semicircleNumber;
    private int color;
    private String isread = "";
    private boolean delete = false;
    private Context mContext;

    /* 头像点击需要的东西 */
    private String userId;// 圆形头像对应的用户ID，以便响应点击后进入该ID用户的联系界面
    private ImageLoader mImageLoader;// 图片加载
    private String userName;//用户姓名

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ImageLoader getmImageLoader() {
        return mImageLoader;
    }

    public void setmImageLoader(ImageLoader mImageLoader) {
        this.mImageLoader = mImageLoader;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;// 图片地址

 
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
    }

    public BSCircleImageView(Context context) {
        super(context);
        init();
        this.mContext = context;
    }

    public BSCircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.mContext = context;
    }

    public BSCircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BSCircleImageView, defStyle, 0);

        mBorderWidth = a.getDimensionPixelSize(R.styleable.BSCircleImageView_border_width, DEFAULT_BORDER_WIDTH);
        mBorderColor = a.getColor(R.styleable.BSCircleImageView_border_color, DEFAULT_BORDER_COLOR);
        mBorderWidthOut = a.getDimensionPixelSize(R.styleable.BSCircleImageView_border_width_out, DEFAULT_BORDER_WIDTH);
        mBorderColorOut = a.getColor(R.styleable.BSCircleImageView_border_color_out, DEFAULT_BORDER_COLOR);

        a.recycle();

        init();
        // 设置该控件的点击事件(头像点击事件)
        this.setOnClickListener(this);
        // 设置该控件的点击事件(头像点击事件)
        // this.setOnClickListener(this);
    }

    private void init() {
        super.setScaleType(SCALE_TYPE);
        mReady = true;

        if (mSetupPending) {
            setup();
            mSetupPending = false;
        }
    }

    @Override
    public ScaleType getScaleType() {
        return SCALE_TYPE;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (scaleType != SCALE_TYPE) {
            throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }

        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDrawableRadius, mBitmapPaint);
        if (mBorderWidth != 0) {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderRadius, mBorderPaint);
        }
        if (mBorderWidthOut != 0) {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderRadius, mBorderPaint);
        }
        if (badgeNumber != 0) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            // 绘制右上角的圆圈
            paint.setColor(Color.RED);
            int circleX = measureWidht - radius;
            ;
            int circleY = radius;
            canvas.drawCircle(circleX, circleY, radius, paint);

            // 画文�?
            paint.setColor(Color.WHITE);
            paint.setTextSize(1.2f * radius);
            int textWidth = getTextWidth(paint, String.valueOf(badgeNumber));
            int textHeight = getTextHeight(paint, String.valueOf(badgeNumber));

            FontMetrics fontMetrics = paint.getFontMetrics();
            float bottomY = textHeight /*- 1.0f*fontMetrics.bottom*/; // 计算bottom线的位置，测试出来的值，不标�?

            canvas.drawText(String.valueOf(badgeNumber), circleX - (textWidth / 2), circleY + (bottomY / 2), paint);
            return;
        }

        if (!"".equals(isread)) {
            if ("0".equals(isread)) {
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                // 绘制右上角的圆圈
                paint.setColor(Color.RED);
                int circleX = measureWidht - radius / 2;
                int circleY = radius / 2;
                canvas.drawCircle(circleX, circleY, radius / 2, paint);
                return;
            }
        }

        if (!"".equals(semicircleNumber) && semicircleNumber != null) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            // 绘制右上角的圆圈
            if (getColor() == 1) {
                paint.setColor(Color.parseColor("#07ACFF"));
            } else if (getColor() == 2) {
                paint.setColor(Color.parseColor("#959595"));
            } else if (getColor() == 3) {
                paint.setColor(Color.parseColor("#F4A91A"));
            }

            int circleX = measureWidht - radius;
            int circleY = radius;
            canvas.drawCircle(circleX, circleY, radius, paint);

            // 画文�?
            paint.setColor(Color.WHITE);
            paint.setTextSize(1.2f * radius);
            int textWidth = getTextWidth(paint, String.valueOf(badgeNumber));
            int textHeight = getTextHeight(paint, String.valueOf(badgeNumber));

            FontMetrics fontMetrics = paint.getFontMetrics();
            float bottomY = textHeight /*- 1.0f*fontMetrics.bottom*/; // 计算bottom线的位置，测试出来的值，不标�?

            if (getColor() == 3) {
                textWidth = getTextWidth(paint, String.valueOf(semicircleNumber));
                canvas.drawText(String.valueOf(semicircleNumber), circleX - textWidth / 2, circleY + (bottomY / 2), paint);
            } else {
                canvas.drawText(String.valueOf(semicircleNumber), circleX - textWidth + textWidth / 5, circleY + (bottomY / 2), paint);
            }
            return;
        }

        if (delete) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            // 绘制右上角的圆圈
            paint.setColor(Color.RED);
            int circleX = measureWidht - radius;
            int circleY = radius;
            canvas.drawCircle(circleX, circleY, radius, paint);

            // 画文�?
            paint.setColor(Color.WHITE);
            paint.setTextSize(1.2f * radius);
            int textWidth = getTextWidth(paint, String.valueOf("——"));
            int textHeight = getTextHeight(paint, String.valueOf(badgeNumber));

            FontMetrics fontMetrics = paint.getFontMetrics();
            float bottomY = textHeight /*- 1.0f*fontMetrics.bottom*/; // 计算bottom线的位置，测试出来的值，不标�?

            canvas.drawText(String.valueOf(badgeNumber), circleX - (textWidth / 2), circleY + (bottomY / 2), paint);
            return;
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(int borderColor) {
        if (borderColor == mBorderColor) {
            return;
        }

        mBorderColor = borderColor;
        mBorderPaint.setColor(mBorderColor);
        invalidate();
    }

    public int getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        if (borderWidth == mBorderWidth) {
            return;
        }

        mBorderWidth = borderWidth;
        setup();
    }

    public int getBorderWidthOut() {
        return mBorderWidthOut;
    }

    public void setBorderWidthOut(int borderWidth) {
        if (borderWidth == mBorderWidthOut) {
            return;
        }

        mBorderWidthOut = borderWidth;
        setup();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        mBitmap = bm;
        setup();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        mBitmap = getBitmapFromDrawable(drawable);
        setup();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                // if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0)
                // return null;
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    private void setup() {
        if (!mReady) {
            mSetupPending = true;
            return;
        }

        if (mBitmap == null) {
            return;
        }

        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setShader(mBitmapShader);

        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        mBitmapHeight = mBitmap.getHeight();
        mBitmapWidth = mBitmap.getWidth();

        mBorderRect.set(0, 0, getWidth(), getHeight());
        mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2, (mBorderRect.width() - mBorderWidth) / 2);

        mDrawableRect.set(mBorderWidth, mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);
        mDrawableRadius = Math.min(mDrawableRect.height() / 2, mDrawableRect.width() / 2);

        updateShaderMatrix();
        invalidate();
    }

    private void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;

        mShaderMatrix.set(null);

        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / (float) mBitmapHeight;
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
        } else {
            scale = mDrawableRect.width() / (float) mBitmapWidth;
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
        }

        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth, (int) (dy + 0.5f) + mBorderWidth);

        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        measureWidht = measureWidht(widthMeasureSpec);
        measureHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(measureWidht, measureHeight);
        // Log.e("PointImageView", "onMeasure() -> width = " + measureWidht + " , height = " +
        // measureHeight);

        radius = measureWidht * 1 / 5;
        // Log.e("PointImageView", "radius = " + radius);
    }

    private int measureWidht(int widthMeasureSpec) {
        int result = 0;

        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY) { // 如果是精确的尺寸
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            // Log.e("PointImageView", "测量宽度时，测量出的mode是MeasureSpec.AT_MOST");
            Drawable drawable = getBackground();
            if (null != drawable) {
                result = drawable.getMinimumWidth();
            } else {
                result = getDrawable().getMinimumWidth();
            }
        }

        return result;
    }

    private int measureHeight(int heightMeasureSpec) {
        int result = 0;

        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY) { // 如果是精确的尺寸
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            // Log.e("PointImageView", "测量宽度时，测量出的mode是MeasureSpec.AT_MOST");
            Drawable drawable = getBackground();
            if (null != drawable) {
                result = drawable.getMinimumHeight();
            } else {
                result = getDrawable().getMinimumHeight();
            }
        }

        return result;
    }

    public void setBadgeNumber(int badgeNumber) {
        this.badgeNumber = badgeNumber;
        invalidate();
    }

    public String getSemicircleNumber() {
        return semicircleNumber;
    }

    public void setSemicircleNumber(String semicircleNumber) {
        this.semicircleNumber = semicircleNumber;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    /**
     * 精确计算宽度
     * 
     * @param paint
     * @param str
     * @return
     */
    private int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    /**
     * 粗略计算宽高
     * 
     * @param paint
     * @param str
     * @return
     */
    private int getTextHeight(Paint paint, String str) {
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        int w = rect.width();
        int h = rect.height();
        return h;
    }

    /**
     * 给圆形头像设置点击事件 跳转到头像对应的联系人详情界面
     */
    @Override
    public void onClick(View v) {
        Context currentActivity = v.getContext();
        String userID = this.getUserId();
        if (userID != null) {
            Intent intent = new Intent();
            intent.putExtra("uid", userID);
            intent.putExtra("username", this.getUserName());
            intent.setClass(currentActivity, ContactPersonActivity.class);
            // currentActivity.startActivity(intent);
            ImageActivityUtils.sendByteImg(this.getmImageLoader(), mContext, intent, this.getUrl(), (BSCircleImageView) v);
        } 
    }
 

}
