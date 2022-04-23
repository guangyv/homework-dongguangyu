package com.bytedance.jstu.chapter3;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CircleProgressBar extends View {
    // 画圆环的画笔
    private Paint ringPaint;
    // 圆环颜色
    private int ringColor;
    // 半径
    private float radius;
    // 圆环宽度
    private float strokeWidth;
    // 总进度
    private int totalProgress = 100;
    // 当前进度
    private int currentProgress;

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initVariable();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressbar, 0 , 0);
        radius = typeArray.getDimension(R.styleable.CircleProgressbar_radius, 10);
        strokeWidth = typeArray.getDimension(R.styleable.CircleProgressbar_strokeWidth, 2);
        ringColor = typeArray.getColor(R.styleable.CircleProgressbar_ringColor, 0xFF00BCD4);
    }

    private void initVariable() {
        ringPaint = new Paint();
        ringPaint.setAntiAlias(true);
        ringPaint.setDither(true);
        ringPaint.setColor(ringColor);
        ringPaint.setStyle(Paint.Style.STROKE);
        ringPaint.setStrokeCap(Paint.Cap.ROUND);
        ringPaint.setStrokeWidth(strokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (currentProgress >= 0) {
            RectF oval = new RectF(getWidth() / 2 - radius, getHeight() / 2 - radius, getWidth() / 2 + radius, getHeight() / 2 + radius);
            canvas.drawArc(oval, -90, ((float) currentProgress / totalProgress) * 360, false, ringPaint);
        }
    }

    public void setProgress(int progress) {
        currentProgress = progress;
        postInvalidate();
    }
}
