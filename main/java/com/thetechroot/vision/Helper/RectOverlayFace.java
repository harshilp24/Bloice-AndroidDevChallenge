package com.thetechroot.vision.Helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

public class RectOverlayFace extends GraphicOverlay.Graphic{

    private  int RECT_COLOR = Color.BLUE;
    private  float STROKE_WIDTH = 4.0f;

    private Paint rectPaint;

    private GraphicOverlay graphicOverlay;

    private Rect rect;

    //private final Rect bounds;


    public RectOverlayFace(GraphicOverlay graphicOverlay, Rect rect) {

        super(graphicOverlay);

        rectPaint = new Paint();
        rectPaint.setColor(RECT_COLOR);
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setStrokeWidth(STROKE_WIDTH);

        this.graphicOverlay = graphicOverlay;
        this.rect = rect;



        postInvalidate();
    }

    @Override
    public void draw(Canvas canvas) {

        RectF rectF = new RectF(rect);
        rectF.left = translateX(rectF.left);
        rectF.top = translateX(rectF.top);
        rectF.right = translateX(rectF.right);
        rectF.bottom = translateX(rectF.bottom);

        canvas.drawRect(rectF,rectPaint);



    }

    @Override
    public boolean contains(float x, float y) {
        return false;
    }
}
