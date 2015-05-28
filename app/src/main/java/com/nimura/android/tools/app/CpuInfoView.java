package com.nimura.android.tools.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Limi on 24.05.2015.
 */
public class CpuInfoView extends View{
    private final int pointsInView;
    private final Paint linePt = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint backgroundPaint = new Paint();
    private final Paint textPaint = new Paint();
    private final List<Integer> points = new LinkedList<>();
    private final int cpuIndex;
    private final float cpuLoadingViewTextSize;

    public CpuInfoView(Context context, int cpuIndex) {
        super(context);
        this.cpuIndex = cpuIndex;

        backgroundPaint.setColor(context.getResources().getColor(R.color.backgroundColor));

        linePt.setColor(context.getResources().getColor(R.color.lineColor));
        linePt.setStrokeWidth(2);

        textPaint.setColor(context.getResources().getColor(R.color.textColor));
        textPaint.setTextSize(context.getResources().getDimension(R.dimen.cpuLoadingViewTextSize));

        cpuLoadingViewTextSize = context.getResources().getDimension(R.dimen.cpuLoadingViewTextSize);

        pointsInView = context.getResources().getInteger(R.integer.pointsInView);
    }

    public void addPoint(int point){
        if(point >= 0 && point <= 100){
            points.add(point);
            if(points.size() > pointsInView){
                points.remove(0);
            }
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(points.size() > 1){
            int padding = 10, padding2x = padding * 2;
            int width = canvas.getWidth() - padding2x;
            int height = canvas.getHeight() - padding2x;

            canvas.drawRect(padding, padding, padding + width, padding + height, backgroundPaint);

            float x = padding;
            float step = (float)width / (float)(pointsInView - 1);
            int firstPointIndex = pointsInView - points.size();
            int pointIndex = 0;

            float lastx = -1.0f, lasty = -1.0f;
            float[] lines = new float[points.size()<<4];
            int j = 0;

            float maxLineHeight = height - cpuLoadingViewTextSize - 5;
            lastx = -1.0f;
            lasty = -1.0f;
            for(int i=0;i< pointsInView;i++){
                if(i >= firstPointIndex){
                    float y = canvas.getHeight() - padding - (float)points.get(pointIndex)*maxLineHeight/100.0f;
                    if(lastx >= 0.0f){
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
            canvas.drawLines(lines, 0, lines.length, linePt);

            int currentLoad = 0;
            if(!points.isEmpty()) {
                currentLoad = points.get(points.size() - 1);
            }

            canvas.drawText(String.format(getResources().getString(R.string.cpu_load_str), cpuIndex + 1, currentLoad, "%"),
                    padding, cpuLoadingViewTextSize + padding, textPaint);
        }

    }
}
