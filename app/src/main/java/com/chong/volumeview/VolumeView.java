package com.chong.volumeview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by weilong.chong on 2016/8/24.
 * 音频播放时动态条形图
 */
public class VolumeView extends View {
    public static final String TAG = "VolumeView";
    /**
     * 矩形画笔
     */
    private Paint mRectPaint;
    /**
     * 分割线画笔
     */
    private Paint mLinePaint;
    /**
     * 矩形间距
     */
    public int mRectOffset = 10;
    /**
     * 矩形宽度
     */
    public int mRectWidth = 10;
    /**
     * 矩形高度
     */
    public int mRectHeight;
    /**
     * 矩形数量
     */
    private int mRectCount = 10;
    /**
     * 矩形颜色
     */
    private int mRectColor = 0x00000000;
    /**
     * 分割线颜色
     */
    private int mLineColor = Color.BLUE;
    /**
     * 刷新频率
     */
    private int mInterval = 300;
    /**
     * 是否需要上下对称变化
     */
    private boolean mBothSide = true;
    /**
     * 是否需要分割线
     */
    private boolean mNeedLine = false;
    /**
     * 分割线宽度
     */
    private int mLineWidth;
    /**
     * 是否开始刷新
     */
    private boolean mStarted;

    public VolumeView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public VolumeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public VolumeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.VolumeView);
            mRectCount = ta.getInteger(R.styleable.VolumeView_rect_count, mRectCount);
            mRectWidth = (int) ta.getDimension(R.styleable.VolumeView_rect_width, mRectWidth);
            mRectColor = ta.getColor(R.styleable.VolumeView_rect_color, mRectColor);
            mRectOffset = (int) ta.getDimension(R.styleable.VolumeView_rect_offset, mRectOffset);
            mInterval = ta.getInteger(R.styleable.VolumeView_rect_interval, mInterval);
            mBothSide = ta.getBoolean(R.styleable.VolumeView_both_side, mBothSide);
            mNeedLine = ta.getBoolean(R.styleable.VolumeView_need_line, mNeedLine);
            mLineColor = ta.getColor(R.styleable.VolumeView_line_color, mLineColor);
            ta.recycle();
        }
        mRectPaint = new Paint();
        mRectPaint.setAntiAlias(true);
        mRectPaint.setColor(mRectColor);
        if (mNeedLine) {
            mLinePaint = new Paint();
            mLinePaint.setAntiAlias(true);
            mLinePaint.setColor(mLineColor);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST) {
            width = (mRectWidth + mRectOffset) * mRectCount + mRectOffset + getPaddingLeft() + getPaddingRight();
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            height = 100 + getPaddingTop() + getPaddingBottom();
        }
        mRectHeight = height;
        if (mNeedLine) {
            mLineWidth = (mRectWidth + mRectOffset) * mRectCount + mRectOffset;
        }
        setMeasuredDimension(width, height);

    }

//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        // 颜色渐变，以后添加
//        LinearGradient mLinearGradient = new LinearGradient(0, 0,
//                mRectWidth, mRectHeight,
//                Color.YELLOW, Color.GREEN,
//                Shader.TileMode.CLAMP);
//        mRectPaint.setShader(mLinearGradient);
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int padding = getPaddingLeft();
//        Log.d(TAG, "padding == " + padding);
        for (int i = 0; i < mRectCount; i++) {
            float topHeight = (float) (Math.random() * mRectHeight / 2);
            if (mBothSide) {
                // 对称
                // 上半部分
                canvas.drawRect(padding + mRectWidth * i + mRectOffset * (i + 1), mRectHeight / 2 - topHeight,
                        padding + mRectWidth * (i + 1) + mRectOffset * (i + 1), mRectHeight / 2, mRectPaint);
                // 下半部分
                canvas.drawRect(padding + mRectWidth * i + mRectOffset * (i + 1), mRectHeight / 2,
                        padding + mRectWidth * (i + 1) + mRectOffset * (i + 1), mRectHeight / 2 + topHeight, mRectPaint);
            } else {
                canvas.drawRect(padding + mRectWidth * i + mRectOffset * (i + 1), topHeight,
                        padding + mRectWidth * (i + 1) + mRectOffset * (i + 1), mRectHeight, mRectPaint);
            }

        }
        if (mBothSide && mNeedLine) {
            // 分割线
            canvas.drawRect(padding, mRectHeight / 2 - 1, padding + mLineWidth, mRectHeight / 2 + 1, mLinePaint);
        }

        if (mStarted) {
            postInvalidateDelayed(mInterval);
        }

    }

    /**
     * 开始刷新
     */
    public void start() {
        if (mStarted) {
            return;
        }
        mStarted = true;
        invalidate();
    }

    /**
     * 停止刷新
     */
    public void stop() {
        if (!mStarted) {
            return;
        }
        mStarted = false;
    }

    public int getRectOffset() {
        return mRectOffset;
    }

    public void setRectOffset(int rectOffset) {
        mRectOffset = rectOffset;
        requestLayout();
    }

    public int getRectWidth() {
        return mRectWidth;
    }

    public void setRectWidth(int rectWidth) {
        mRectWidth = rectWidth;
        requestLayout();
    }

    public int getRectCount() {
        return mRectCount;
    }

    public void setRectCount(int rectCount) {
        mRectCount = rectCount;
        requestLayout();
    }

    public int getRectColor() {
        return mRectColor;
    }

    public void setRectColor(int rectColor) {
        mRectColor = rectColor;
        invalidate();
    }

    public int getLineColor() {
        return mLineColor;
    }

    public void setLineColor(int lineColor) {
        mLineColor = lineColor;
        invalidate();
    }

    public int getInterval() {
        return mInterval;
    }

    public void setInterval(int interval) {
        mInterval = interval;
    }
}
