package com.nimura.cpuviewer.com.nimura.cpuviewer.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.nimura.cpuviewer.R;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Limi on 24.05.2015.
 */
public class CpuInfoView extends View{
    private final int POINTS_ON_VIEW = 20;
    private static final int LINE_COLOR = Color.rgb(42, 255, 0);
    private static final int BACKGROUNG_COLOR = Color.rgb(0, 40, 0);
    private static final Paint linePt = new Paint();
    private final List<Integer> points = new LinkedList<>();
    private final int cpuIndex;
    private final float textSize = getResources().getDimension(R.dimen.textsize);

    public CpuInfoView(Context context, int cpuIndex) {
        super(context);
        this.cpuIndex = cpuIndex;
        linePt.setColor(LINE_COLOR);
        linePt.setStrokeWidth(2);
    }

    public void addPoint(int point){
        if(point >= 0 && point <= 100){
            points.add(point);
            if(points.size() > POINTS_ON_VIEW){
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

            Paint pt = new Paint();
            pt.setColor(BACKGROUNG_COLOR);
            canvas.drawRect(padding, padding, padding + width, padding + height, pt);

            float x = padding;
            float step = (float)width / (float)(POINTS_ON_VIEW - 1);
            int firstPointIndex = POINTS_ON_VIEW - points.size();
            int pointIndex = 0;

            float lastx = -1.0f, lasty = -1.0f;
            float[] lines = new float[points.size()<<4];
            int j = 0;

            float maxLineHeight = height - textSize - 5;
            lastx = -1.0f;
            lasty = -1.0f;
            for(int i=0;i<POINTS_ON_VIEW;i++){
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

            pt = new Paint(Paint.ANTI_ALIAS_FLAG);
            pt.setColor(LINE_COLOR);
            pt.setTextSize(textSize);
            canvas.drawText(String.format("CPU %d load: %d %s", cpuIndex + 1, currentLoad, "%"),
                    padding, textSize + padding, pt);
        }

    }
}
