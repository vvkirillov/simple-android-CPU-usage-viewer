package com.nimura.android.tools.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nimura on 24.05.2015.
 */
public class CpuUsageView extends View{
    private final int pointsInView;
    private final int padding;
    private final int padding2x;
    private final Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint backgroundPaint = new Paint();
    private final Paint textPaint = new Paint();
    private final List<Integer> points = new LinkedList<>();
    private final int cpuIndex;
    private final float cpuLoadingViewTextSize;

    /**
     * Constructor
     * @param context context
     * @param cpuIndex cpu index (starting from 0)
     * @param maxPointsInView maximum amount of points to be visible in view
     */
    public CpuUsageView(Context context, int cpuIndex, int maxPointsInView) {
        super(context);
        this.cpuIndex = cpuIndex;
        this.pointsInView = maxPointsInView;

        linePaint.setStrokeWidth(context.getResources().getDimension(R.dimen.line_width));
        cpuLoadingViewTextSize = context.getResources().getDimension(R.dimen.cpuLoadingViewTextSize);
        textPaint.setTextSize(cpuLoadingViewTextSize);

        padding = context.getResources().getInteger(R.integer.cpuWidgetPadding);
        padding2x = padding * 2;
    }

    /**
     * Adds one point at the end of the list.
     * If points array size exceed maximum size,
     * then the first point in array will be removed.
     * @param point point value, must be in range [0, 100]
     */
    public void addPoint(int point){
        if(point >= 0 && point <= 100){
            points.add(point);
            if(points.size() > pointsInView){
                points.remove(0);
            }
        }
        invalidate();
    }

    /**
     * Sets a new line color
     * @param color a new line color
     */
    public void setLineColor(int color){
        linePaint.setColor(color);
    }

    /**
     * Sets a new background color
     * @param color a new background color
     */
    public void setBackgroundColor(int color){
        backgroundPaint.setColor(color);
    }

    /**
     * Sets a new text color
     * @param color a new text color
     */
    public void setTextColor(int color){
        textPaint.setColor(color);
    }

    /**
     * Removes all points
     */
    public void reset(){
        points.clear();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = canvas.getWidth() - padding2x;
        int height = canvas.getHeight() - padding2x;
        canvas.drawRect(padding, padding, padding + width, padding + height, backgroundPaint);

        if(points.size() > 1){
            float x = padding;
            float step = (float)width / (float)(pointsInView - 1);
            int firstPointIndex = pointsInView - points.size();
            int pointIndex = 0;

            Float lastx = null, lasty = null;
            float[] lines = new float[points.size() * 4];
            int j = 0;

            float maxLineHeight = height - cpuLoadingViewTextSize - 5;
            for(int i=0;i< pointsInView;i++){
                if(i >= firstPointIndex){
                    float y = canvas.getHeight() - padding - (float)points.get(pointIndex)*maxLineHeight/100.0f;
                    if(lastx != null){
                        lines[j++] = lastx;
                        lines[j++] = lasty;
                        lines[j++] = x;
                        lines[j++] = y;
                    }
                    pointIndex++;
                    lastx = x;
                    lasty = y;
                }
                x += step;
            }
            canvas.drawLines(lines, 0, lines.length, linePaint);

            int currentLoad = 0;
            if(!points.isEmpty()) {
                currentLoad = points.get(points.size() - 1);
            }

            canvas.drawText(String.format(getResources().getString(R.string.cpu_load_str), cpuIndex + 1, currentLoad, "%"),
                    padding * 2, cpuLoadingViewTextSize + padding, textPaint);
        }

    }
}
