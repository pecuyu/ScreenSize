package com.yu.screensize;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by D22436 on 2017/8/17.
 */

public class DistanceTouchView extends View {
    private PointF firstPoint, secondPoint;
    double distance;
    private Paint paint;

    public DistanceTouchView(Context context) {
        this(context,null);
    }

    public DistanceTouchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DistanceTouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        firstPoint = new PointF();
        secondPoint = new PointF();
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(60);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                calcDistanceAndInvalidate(event);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                calcDistanceAndInvalidate(event);
                break;
            case MotionEvent.ACTION_MOVE:
                calcDistanceAndInvalidate(event);
                break;
            case MotionEvent.ACTION_UP:
                calcDistanceAndInvalidate(event);
                break;
        }
        return true;
    }

    /**
     * 计算距离并刷新
     * @param event
     */
    private void calcDistanceAndInvalidate(MotionEvent event) {
        int pointerCount = event.getPointerCount();
        if (pointerCount >= 2) {
            firstPoint.x =event.getX(0);
            firstPoint.y = event.getY(0);
            secondPoint.x =  event.getX(1);
            secondPoint.y = event.getY(1);
            float dx = Math.abs(firstPoint.x - secondPoint.x);
            float dy = Math.abs(firstPoint.y - secondPoint.y);
            distance = Math.sqrt(dx * dx + dy * dy);
            LogUtil.e("TAG", "distance="+distance);
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制文本
        canvas.drawText("distance=" + distance, 100, 200, paint);
        SystemClock.sleep(10);
    }
}
