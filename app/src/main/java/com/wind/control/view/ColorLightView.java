package com.wind.control.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class ColorLightView extends View {

    private Paint paint;

    public ColorLightView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置画笔
        paint.setColor(Color.GREEN);//设置画笔颜色
        paint.setStrokeWidth(3);//设置画笔粗细

        //获取整个屏幕的高度和宽度
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
//        wid = displayMetrics.widthPixels;
//        he = displayMetrics.heightPixels;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(50,50,50,paint);
    }

    public void setColor(int color){
        paint.setColor(color);
        postInvalidate();
    }
}
