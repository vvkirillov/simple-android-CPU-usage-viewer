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
    private final Paint backgroundPaint = new Paint();
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint meshPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final List<Integer> points = new LinkedList<>();
    private final int pointsInView;
    private final int padding;
    private final int padding2x;
    private final int cpuIndex;
    private final float cpuLoadingViewTextSize;
    private boolean drawMesh = true;
    private final int NUMBER_OF_POINTS = 100;
    private final float[] lines = new float[NUMBER_OF_POINTS * 4];

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
        if(point >= 0 && point <= NUMBER_OF_POINTS){
            points.add(point);
            if(points.size() > pointsInView){
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

        //TODO refactoring needed
        //Area dimension
        final int areaWidth = canvas.getWidth() - padding2x;
        final int areaHeight = canvas.getHeight() - padding2x;

        canvas.drawRect(padding, padding, padding + areaWidth, padding + areaHeight, backgroundPaint);

        if(points.size() > 1){
            float x = padding;
            float step = (float)areaWidth / (float)(pointsInView - 1);
            int firstPointIndex = pointsInView - points.size();
            int pointIndex = 0;

            Float lastx = null, lasty = null;

            int j = 0;

            float maxLineHeight = areaHeight - cpuLoadingViewTextSize - padding2x;

            if(drawMesh) {
                //
                float wireStep = step * 8.0f;
                //horizontal
                float wirey0 = cpuLoadingViewTextSize + padding2x;
                float wirey = wirey0;
                while (wirey < wirey0 + maxLineHeight) {
                    canvas.drawLine(x, wirey, x + areaWidth, wirey, meshPaint);
                    wirey += wireStep;
                }
                //vertical
                float wirex0 = padding;
                float wirex = wirex0;
                while (wirex < wirex0 + areaWidth) {
                    canvas.drawLine(wirex + 1, cpuLoadingViewTextSize + padding2x, wirex, areaHeight + padding, meshPaint);
                    wirex += wireStep;
                }
            }

            //Draw plot
            int pointsArraySize = 0;
            for(int i=0;i< pointsInView;i++){
                if(i >= firstPointIndex){
                    float y = canvas.getHeight() - padding - (float)points.get(pointIndex)*maxLineHeight/100.0f;
                    if(lastx != null){
                        lines[j++] = lastx;
                        lines[j++] = lasty;
                        lines[j++] = x;
                        lines[j++] = y;
                        pointsArraySize += 4;
                    }
                    pointIndex++;
                    lastx = x;
                    lasty = y;
                }
                x += step;
            }
            canvas.drawLines(lines, 0, pointsArraySize, linePaint);

            int currentLoad = 0;
            if(!points.isEmpty()) {
                currentLoad = points.get(points.size() - 1);
            }

            canvas.drawText(String.format(getResources().getString(R.string.cpu_load_str), cpuIndex + 1, currentLoad, "%"),
                    padding * 2, cpuLoadingViewTextSize + padding, textPaint);
        }

    }
}
