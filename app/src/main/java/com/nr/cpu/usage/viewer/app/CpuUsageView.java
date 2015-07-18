package com.nr.cpu.usage.viewer.app;

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
    private final float[] plotLinePoints;
    private final float[] meshLinePoints;

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
        plotLinePoints = new float[maxNumberOfPoints * 4];
        meshLinePoints = new float[maxNumberOfPoints * 8];

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
        float divider = 10.0f;
        float meshStep = maxLineHeight / divider;
        while(meshStep < 10.0f && divider > 0){
            divider -= 2.0f;
            meshStep = maxLineHeight / divider;
        }

        //horizontal lines
        float meshx0 = padding;
        float meshy0 = cpuLoadingViewTextSize + 3 * padding;
        float meshx = meshx0 + areaWidth;
        float meshy = meshy0;
        float max_meshy = meshy0 + maxLineHeight;
        int meshLinePointIndex = 0;
        while (meshy < max_meshy) {
            meshLinePoints[meshLinePointIndex++] = meshx0;
            meshLinePoints[meshLinePointIndex++] = meshy;
            meshLinePoints[meshLinePointIndex++] = meshx;
            meshLinePoints[meshLinePointIndex++] = meshy;
            meshy += meshStep;
        }

        //vertical lines
        meshx = meshx0 + 1;
        meshy = areaHeight + padding;
        float max_meshx = meshx0 + areaWidth;
        while (meshx < max_meshx) {
            meshLinePoints[meshLinePointIndex++] = meshx;
            meshLinePoints[meshLinePointIndex++] = meshy0;
            meshLinePoints[meshLinePointIndex++] = meshx;
            meshLinePoints[meshLinePointIndex++] = meshy;
            meshx += meshStep;
        }

        canvas.drawLines(meshLinePoints, 0, meshLinePointIndex, meshPaint);
    }

    private void drawPlot(Canvas canvas) {
        int firstPointIndex = maxNumberOfPoints - points.size();
        int cpuUsagePointIndex = 0;
        int linePointIndex = 0;
        Float lastx = null, lasty = null;
        float stepx = (float)areaWidth / (float)(maxNumberOfPoints - 1);
        float x = padding;

        float heightMinusPadding = canvas.getHeight() - padding;
        float maxLineHeightBy100 = maxLineHeight/100.0f;

        for(int i=0;i < maxNumberOfPoints;i++){
            if(i >= firstPointIndex){
                float y = heightMinusPadding - (float)points.get(cpuUsagePointIndex) * maxLineHeightBy100;
                if(lastx != null){
                    plotLinePoints[linePointIndex++] = lastx;
                    plotLinePoints[linePointIndex++] = lasty;
                    plotLinePoints[linePointIndex++] = x;
                    plotLinePoints[linePointIndex++] = y;
                }
                cpuUsagePointIndex++;
                lastx = x;
                lasty = y;
            }
            x += stepx;
        }
        canvas.drawLines(plotLinePoints, 0, linePointIndex, linePaint);
    }

    private void drawCpuUsageLabel(Canvas canvas, int currentLoad) {
        canvas.drawText(String.format(getResources().getString(R.string.cpu_load_str), cpuIndex + 1, currentLoad),
                padding2x, cpuLoadingViewTextSize + padding, textPaint);
    }
}
