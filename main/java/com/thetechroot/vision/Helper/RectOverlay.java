package com.thetechroot.vision.Helper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class RectOverlay extends GraphicOverlay.Graphic {

    private static final int REC_COLOR = Color.YELLOW;
    private static final float STROKE_WIDTH = 8.0f;

    private final Paint rectPaint;

    private final Rect bounds;

    public RectOverlay(GraphicOverlay overlay, Rect bounds) {
        super(overlay);

        this.bounds = bounds;
        rectPaint = new Paint();
        rectPaint.setColor(REC_COLOR);
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setStrokeWidth(STROKE_WIDTH);

        postInvalidate();
    }

    @Override
    public void draw(Canvas canvas) {

        if (bounds == null){

            throw new IllegalStateException("Attempting To Draw A Null Text");

        }

        RectF rect = new RectF(bounds);

        canvas.drawRect(rect,rectPaint);



    }

    @Override
    public boolean contains(float x, float y) {
        return false;
    }
}
