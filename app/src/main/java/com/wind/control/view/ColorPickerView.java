package com.wind.control.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.wind.control.interfaces.OnColorChangedListener;
import com.wind.control.util.LogUtils;


public class ColorPickerView extends View {

    private final boolean debug = true;
    private final String TAG = "ColorPicker";

    private OnColorChangedListener mListener;
    private Paint mPaint;           //渐变色环画笔
    private Paint mCenterPaint;     //中间圆画笔
    private Paint mBPaint;

    private final int[] mCircleColors = new int[]{0xFFFF0000, 0xFFFF00FF, 0xFF0000FF,
            0xFF00FFFF, 0xFF00FF00, 0xFFFFFF00, 0xFFFF0000};
    ;      //渐变色环颜色

    private int mHeight;                    //View高
    private int mWidth;                     //View宽
    private float r;                        //色环半径(paint中部)
    private float centerRadius;             //中心圆半径

    private boolean downInCircle = true;    //按在渐变环上
    private boolean highlightCenter;        //高亮
    private boolean highlightCenterLittle;  //微亮
    private int mInitialColor;//初始颜色
    private Handler mHandler;

    public ColorPickerView(Context context, int height, int width, int color, Handler handler) {
        super(context);
        this.mInitialColor = color;
        this.mHeight = height - 20;
        this.mWidth = width;
        this.mHandler = handler;
        setMinimumHeight(height - 20);
        setMinimumWidth(width);

        //渐变色环参数

        Shader s = new SweepGradient(0, 0, mCircleColors, null);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setShader(s);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(dp2px(context,150));
        r = width / 2 - mPaint.getStrokeWidth() * 0.5f;

        //中心圆参数
        mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterPaint.setColor(mInitialColor);
        mCenterPaint.setTextSize(90);
        centerRadius = 101;
        Log.i("TOM_MING", "r: " + r + ",centerRadius: " + centerRadius);
    }

    private int dp2px(Context context,float dpValue){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //移动中心
        canvas.translate(mWidth / 2, mHeight / 2);
        //画中心圆
        canvas.drawCircle(0, 0, centerRadius, mCenterPaint);
        //画色环
        canvas.drawOval(new RectF(-r, -r, r, r), mPaint);
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX() - mWidth / 2;
        float y = event.getY() - mHeight / 2 + 50;
        boolean inCircle = inColorCircle(x, y,
                r + mPaint.getStrokeWidth() / 2, r - mPaint.getStrokeWidth() / 2);
        boolean inCenter = inCenter(x, y, centerRadius);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downInCircle = inCircle;
                highlightCenter = inCenter;
            case MotionEvent.ACTION_MOVE:
                if (downInCircle && inCircle) {//down按在渐变色环内, 且move也在渐变色环内
                    float angle = (float) Math.atan2(y, x);
                    float unit = (float) (angle / (2 * Math.PI));
                    if (unit < 0) {
                        unit += 1;
                    }
                    int color = interpCircleColor(mCircleColors, unit);
                    mCenterPaint.setColor(color);
//                    Message message = new Message();
//                    message.what = 3;
//                    message.obj = color;
//                    LogUtils.d("cokoe"+ color);
//                    mHandler.sendMessage(message);
                    if (debug) Log.v(TAG, "色环内, 坐标: " + x + "," + y);
                }

                if (debug)
                    Log.v(TAG, "[MOVE] 高亮: " + highlightCenter + "微亮: " + highlightCenterLittle + " 中心: " + inCenter);
                if ((highlightCenter && inCenter) || (highlightCenterLittle && inCenter)) {//点击中心圆, 当前移动在中心圆
                    highlightCenter = true;
                    highlightCenterLittle = false;
                } else if (highlightCenter || highlightCenterLittle) {//点击在中心圆, 当前移出中心圆
                    highlightCenter = false;
                    highlightCenterLittle = true;
                } else {
                    highlightCenter = false;
                    highlightCenterLittle = false;
                }
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (highlightCenter && inCenter) {//点击在中心圆, 且当前启动在中心圆
                    if (mListener != null) {
                        mListener.colorChanged(mCenterPaint.getColor());
                    }
                    Message message = new Message();
                    message.what = 2;
                    mHandler.sendMessage(message);
                }
                if (downInCircle) {
                    downInCircle = false;
                }
                if (highlightCenter) {
                    highlightCenter = false;
                }
                if (highlightCenterLittle) {
                    highlightCenterLittle = false;
                }
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(mWidth, mHeight);
    }

    /**
     * 坐标是否在色环上
     *
     * @param x         坐标
     * @param y         坐标
     * @param outRadius 色环外半径
     * @param inRadius  色环内半径
     * @return
     */
    private boolean inColorCircle(float x, float y, float outRadius, float inRadius) {
        double outCircle = Math.PI * outRadius * outRadius;
        double inCircle = Math.PI * inRadius * inRadius;
        double fingerCircle = Math.PI * (x * x + y * y);
        if (fingerCircle < outCircle && fingerCircle > inCircle) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 坐标是否在中心圆上
     *
     * @param x            坐标
     * @param y            坐标
     * @param centerRadius 圆半径
     * @return
     */
    private boolean inCenter(float x, float y, float centerRadius) {
        double centerCircle = Math.PI * centerRadius * centerRadius;
        double fingerCircle = Math.PI * (x * x + y * y);
        if (fingerCircle < centerCircle) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取圆环上颜色
     *
     * @param colors
     * @param unit
     * @return
     */
    private int interpCircleColor(int colors[], float unit) {
        if (unit <= 0) {
            return colors[0];
        }
        if (unit >= 1) {
            return colors[colors.length - 1];
        }

        float p = unit * (colors.length - 1);
        int i = (int) p;
        p -= i;

        // now p is just the fractional part [0...1) and i is the index
        int c0 = colors[i];
        int c1 = colors[i + 1];
        int a = ave(Color.alpha(c0), Color.alpha(c1), p);
        int r = ave(Color.red(c0), Color.red(c1), p);
        int g = ave(Color.green(c0), Color.green(c1), p);
        int b = ave(Color.blue(c0), Color.blue(c1), p);
        LogUtils.d("cokoe" + r + ",g: " + g + ",b: " + b);
        Message message = new Message();
        message.what = 1;
        message.obj = String.format("%02x", r) + "," + String.format("%02x", g) + "," + String.format("%02x", b);
        mHandler.sendMessage(message);
        return Color.argb(a, r, g, b);
    }

    private int ave(int s, int d, float p) {
        return s + Math.round(p * (d - s));
    }

}
