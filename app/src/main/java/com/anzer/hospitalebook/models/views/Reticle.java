package com.anzer.hospitalebook.models.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Reticle that is used as view finder over the {@link CameraPreview}
 */
class Reticle extends View {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Rect mTargetRect = new Rect();
    private double mReticleFraction = 1.0;
    private Paint paint = new Paint();

    public Reticle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    void setSize(double reticleFraction) {
        mReticleFraction = reticleFraction;
    }

    void setColor(int color) {
        mPaint.setColor(color);
        mPaint.setAlpha(100);
    }

    void drawTargetRect(Canvas canvas) {
        int height = (int) (canvas.getHeight() * mReticleFraction);
        int width = (int) (canvas.getWidth() * mReticleFraction);
        int smallestDim = Math.min(height, width);

        int left = (canvas.getWidth() - smallestDim) / 2;
        int top = (canvas.getHeight() - smallestDim) / 2;
        int right = left + smallestDim;
        int bottom = top + smallestDim;

        Log.e("left", String.valueOf(left) );
        Log.e("top", String.valueOf(top) );
        Log.e("right", String.valueOf(right) );
        Log.e("bottom", String.valueOf(bottom) );

        mTargetRect.set(left, top, right, bottom);
        canvas.drawRect(mTargetRect, mPaint);

        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(5.0f);

        canvas.drawLine(left + 2,top,left + 2,top + 30,paint);
        canvas.drawLine(left,top + 2 ,left + 30, top + 2, paint);

        canvas.drawLine((canvas.getWidth()-left)-30, top + 2, canvas.getWidth() - left, top + 2, paint);
        canvas.drawLine(canvas.getWidth()-(left + 2) , top, canvas.getWidth()-(left + 2), top + 30, paint);

        canvas.drawLine(left + 2, bottom , left + 2, bottom - 30 , paint);
        canvas.drawLine(left, bottom -2, left + 30, bottom-2, paint);

        canvas.drawLine((canvas.getWidth()-left) - 30, bottom -2, canvas.getWidth() - left , bottom - 2, paint);
        canvas.drawLine(canvas.getWidth()- (left + 2), bottom, canvas.getWidth()- (left + 2), bottom - 30, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTargetRect(canvas);
    }
}