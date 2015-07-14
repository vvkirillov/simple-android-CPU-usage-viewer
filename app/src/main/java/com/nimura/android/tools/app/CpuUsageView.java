package com.nimura.android.tools.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

/**
 * Control responsible for drawing CPU usage plot
 */
public class CpuUsageView extends View{
    private final Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint meshPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final List<Integer> points = new LinkedList<>();
    private final int maxNumberOfPoints;
    private final int padding;
    private final int padding2x;
    private final int cpuIndex;
    private final float cpuLoadingViewTextSize;
    private final float[] linePoints;

    private int areaWidth;
    private int areaHeight;
    private float maxLineHeight;
    private boolean drawMesh = true;

    /**
     * Constructor
     * @param context context
     * @param cpuIndex cpu index (starting from 0)
     * @param maxNumberOfPoints maximum amount of points to be visible in view
     */
    public CpuUsageView(Context context, int cpuIndex, int maxNumberOfPoints) {
        super(context);
        this.cpuIndex = cpuIndex;
        this.maxNumberOfPoints = maxNumberOfPoints;
        linePoints = new float[maxNumberOfPoints * 4];

        cpuLoadingViewTextSize = context.getResources().getDimension(R.dimen.cpuLoadingViewTextSize);
        linePaint.setStrokeWidth(context.getResources().getDimension(R.dimen.line_width));
        textPaint.setTextSize(cpuLoadingViewTextSize);
        meshPaint.setStrokeWidth(getContext().getResources().getDimension(R.dimen.mesh_width));

        padding = context.getResources().getInteger(R.integer.cpuWidgetPadding);
        padding2x = padding * 2;
        setWillNotDraw(false);
    }

    /**
     * Adds one point at the end of the list.
     * If points array size exceed maximum size,
     * then the first point in array will be removed.
     * @param point point value, must be in range [0, 100]
     */
    public void addPoint(int point){
        if(point >= 0 && point <= maxNumberOfPoints){
            points.add(point);
            if(points.size() > maxNumberOfPoints){
                points.remove(0);
            }
        }
        invalidate();
    }

    /**
     * Sets a new line color
     * @param color new line color
     */
    public void setLineColor(int color){
        linePaint.setColor(color);
    }

    /**
     * Sets a new background color
     * @param color new background color
     */
    public void setBackgroundColor(int color){
        backgroundPaint.setColor(color);
    }

    /**
     * Sets a new mesh color
     * @param color mesh color
     */
    public void setMeshColor(int color){
        meshPaint.setColor(color);
    }

    /**
     * Sets a new text color
     * @param color new text color
     */
    public void setTextColor(int color){
        textPaint.setColor(color);
    }

    /**
     * Sets if the mesh must be drawn
     * @param drawMesh if true, mesh will be drawn;
     */
    public void setDrawMesh(boolean drawMesh){
        this.drawMesh = drawMesh;
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
        super.onDraw(canvas);

        updateDimensionVariables(canvas);
        drawBackgroundRect(canvas);

        if (drawMesh) {
            drawMesh(canvas);
        }

        if(points.size() > 1){
            drawPlot(canvas);
        }

        drawCpuUsageLabel(canvas, getCurrentLoad());
    }

    private int getCurrentLoad() {
        int result = 0;
        if(!points.isEmpty()) {
            result = points.get(points.size() - 1);
        }
        return result;
    }

    private void updateDimensionVariables(Canvas canvas) {
        areaWidth = canvas.getWidth() - padding2x;
        areaHeight = canvas.getHeight() - padding2x;
        maxLineHeight = areaHeight - cpuLoadingViewTextSize - padding2x;
    }

    private void drawBackgroundRect(Canvas canvas) {
        canvas.drawRect(padding, padding, padding + areaWidth, padding + areaHeight, backgroundPaint);
    }

    private void drawMesh(Canvas canvas) {
        float step = (float)areaWidth / (float)(maxNumberOfPoints - 1);
        float x = padding;
        float wireStep = step * 8.0f;

        //horizontal lines
        float wirey0 = cpuLoadingViewTextSize + padding2x;
        float wirey = wirey0;
        while (wirey < wirey0 + maxLineHeight) {
            canvas.drawLine(x, wirey, x + areaWidth, wirey, meshPaint);
            wirey += wireStep;
        }

        //vertical lines
        float wirex0 = padding;
        float wirex = wirex0;
        while (wirex < wirex0 + areaWidth) {
            canvas.drawLine(wirex + 1, cpuLoadingViewTextSize + padding2x, wirex, areaHeight + padding, meshPaint);
            wirex += wireStep;
        }
    }

    private void drawPlot(Canvas canvas) {
        int firstPointIndex = maxNumberOfPoints - points.size();
        int cpuUsagePointIndex = 0;
        int linePointIndex = 0;
        int pointArraySize = 0;
        Float lastx = null, lasty = null;
        float stepx = (float)areaWidth / (float)(maxNumberOfPoints - 1);
        float x = padding;

        for(int i=0;i< maxNumberOfPoints;i++){
            if(i >= firstPointIndex){
                float y = canvas.getHeight() - padding - (float)points.get(cpuUsagePointIndex)*maxLineHeight/100.0f;
                if(lastx != null){
                    linePoints[linePointIndex++] = lastx;
                    linePoints[linePointIndex++] = lasty;
                    linePoints[linePointIndex++] = x;
                    linePoints[linePointIndex++] = y;
                    pointArraySize += 4;
                }
                cpuUsagePointIndex++;
                lastx = x;
                lasty = y;
            }
            x += stepx;
        }
        canvas.drawLines(linePoints, 0, pointArraySize, linePaint);
    }

    private void drawCpuUsageLabel(Canvas canvas, int currentLoad) {
        canvas.drawText(String.format(getResources().getString(R.string.cpu_load_str), cpuIndex + 1, currentLoad, "%"),
                padding * 2, cpuLoadingViewTextSize + padding, textPaint);
    }
}
