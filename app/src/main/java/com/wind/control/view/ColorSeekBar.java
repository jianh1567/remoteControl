package com.wind.control.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * create  by lei
 */

public class ColorSeekBar extends View {
    private final String TAG = "ColorSeekBar";
    private final Paint paint = new Paint();
    private final Path sPath = new Path();
    private float sLeft, sTop, sRight, sBottom;
    private float sWidth, sHeight;
    private LinearGradient linearGradient;
    private float x, y;
    private float mRadius;
    private int progress;
    private OnStateChangeListener onStateChangeListener;
    private int startColor = Color.BLACK;
    private int endColor = Color.WHITE;
    private int thumbColor = Color.BLACK;
    private int thumbBorderColor = Color.WHITE;
    private int colorArray[] = {startColor, endColor};


    public ColorSeekBar(Context context) {
        this(context, null);
    }

    public void setColor(int startColor, int endColor, int thumbColor, int thumbBorderColor) {
        this.startColor = startColor;
        this.endColor = endColor;
        this.thumbColor = thumbColor;
        this.thumbBorderColor = thumbBorderColor;
        colorArray[0] = startColor;
        colorArray[1] = endColor;
    }

    public ColorSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec * 2);
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = h;
        x = h;//圆的横坐标
        sLeft = 0; // 背景左的坐标
        sTop = h * 0.25f;//top位置
        sRight = w; // 背景的宽的全部
        sBottom = h * 0.75f; // 背景底部
        sWidth = sRight - sLeft; // 背景的宽度
        sHeight = sBottom - sTop; // 背景的高度
        RectF sRectF = new RectF(sLeft, sTop, sBottom, sBottom);
        sPath.arcTo(sRectF, 90, 180);
        sRectF.left = sRight - sBottom;
        sRectF.right = sRight;
        sPath.arcTo(sRectF, 270, 180);
        sPath.close();    // path准备背景的路径
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        drawCircle(canvas);
        paint.reset();
    }

    public boolean onTouchEvent(MotionEvent event) {
        this.x = event.getX();
        progress = (int) ((x / (sWidth)) * 255);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://ACTION_DOWN
                break;
            case MotionEvent.ACTION_UP:
                if (progress < 0) {
                    progress = 0;
                } else if (progress > 255) {
                    progress = 255;
                }
                if (onStateChangeListener != null) {
                    onStateChangeListener.onStopTrackingTouch(progress);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (progress < 0) {
                    progress = 0;
                } else if (progress > 255) {
                    progress = 255;
                }
                if (onStateChangeListener != null) {
                    onStateChangeListener.OnStateChangeListener(progress);
                }
                this.invalidate();
                break;
        }
        return true;
    }

    private void drawCircle(Canvas canvas) {
        Paint thumbPaint = new Paint();
        x = x < (mRadius / 2) ? (mRadius / 2) : x;//判断thumb边界
        x = x > sWidth - mRadius / 2 ? sWidth - mRadius / 2 : x;
        thumbPaint.setStyle(Style.FILL);
        thumbPaint.setColor(thumbColor);
        canvas.drawCircle(x, mRadius / 2, mRadius / 2, thumbPaint);
        thumbPaint.setStyle(Style.STROKE);
        thumbPaint.setColor(thumbBorderColor);
        thumbPaint.setStrokeWidth(2);
        canvas.drawCircle(x, mRadius / 2, mRadius / 2, thumbPaint);
    }

    private void drawBackground(Canvas canvas) {
        linearGradient = new LinearGradient(sLeft, sTop, sWidth, sHeight, colorArray, null, Shader.TileMode.REPEAT);
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL);
        //设置渲染器
        paint.setShader(linearGradient);
        canvas.drawPath(sPath, paint);
    }

    public interface OnStateChangeListener {
        void OnStateChangeListener(int progress);

        void onStopTrackingTouch(int progress);
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

}