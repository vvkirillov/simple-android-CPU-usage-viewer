package com.nr.cpu.usage.viewer.app;

import android.os.Handler;

import com.nr.cpu.usage.models.CpuUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Controller for application logic
 */
public class CpuUsageController {
    private static final int FIRST_LAUNCH_INTERVAL = 500;
    private final List<CpuUsageView> cpuUsageViews = new LinkedList<>();
    private final Handler timerHandler = new Handler();
    private final CpuUpdateRunnable cur = new CpuUpdateRunnable();
    private int updateInterval = 1000;
    private List<long[]> oldCpuUsageValues, freshCpuUsageValues;
    private boolean firstLaunch = true;
    private static CpuUsageController me;

    private CpuUsageController(){}

    public static CpuUsageController getInstance(){
        if(me == null){
            me = new CpuUsageController();
        }
        return me;
    }

    /**
     * Sets a list of CpuInfoViews objects
     * @param cpuUsageViews list of CpuInfoView objects
     */
    public void setCpuUsageViews(List<CpuUsageView> cpuUsageViews){
        this.cpuUsageViews.clear();
        this.cpuUsageViews.addAll(cpuUsageViews);
    }

    /**
     * Starts the view updates
     */
    public void start(){
        timerHandler.removeCallbacks(cur);
        firstLaunch = true;
        timerHandler.postDelayed(cur, 0);
    }

    /**
     * Stops the view updates
     */
    public void stop(){
        timerHandler.removeCallbacks(cur);
        firstLaunch = false;
    }

    /**
     * Sets a new line color.
     * Will be applied on next iteration
     * @param color new line color
     */
    public void setLineColor(int color){
        for (CpuUsageView cuv : cpuUsageViews) {
            cuv.setLineColor(color);
        }
    }

    /**
     * Sets a new background color.
     * Will be applied on next iteration
     * @param color new background color
     */
    public void setBackgroundColor(int color){
        for (CpuUsageView cuv : cpuUsageViews) {
            cuv.setBackgroundColor(color);
        }
    }

    /**
     * Sets a new mesh color
     * @param color new mesh color
     */
    public void setMeshColor(int color){
        for (CpuUsageView cuv : cpuUsageViews) {
            cuv.setMeshColor(color);
        }
    }

    /**
     * Sets a new text color.
     * Will be applied on next iteration
     * @param color new text color
     */
    public void setTextColor(int color){
        for (CpuUsageView cuv : cpuUsageViews) {
            cuv.setTextColor(color);
        }
    }

    /**
     * Sets an update interval (in milliseconds)
     * @param updateInterval an update interval (in milliseconds)
     */
    public void setUpdateInterval(int updateInterval){
        this.updateInterval = updateInterval;
    }

    /**
     * Sets if the mesh must be drawn
     * @param drawMesh if true, mesh will be drawn
     */
    public void setDrawMesh(boolean drawMesh){
        for (CpuUsageView cuv : cpuUsageViews) {
            cuv.setDrawMesh(drawMesh);
        }
    }

    private final class CpuUpdateRunnable implements Runnable{

        @Override
        public void run() {
            try {
                freshCpuUsageValues = CpuUtils.getCpuUsageRaw();
                if (oldCpuUsageValues != null) {
                    int[] loads = CpuUtils.getCpuUsage(oldCpuUsageValues, freshCpuUsageValues);
                    for (int i = 0; i < loads.length; i++) {
                        CpuUsageView cuv = cpuUsageViews.get(i);
                        cuv.addPoint(loads[i]);
                    }
                }
                oldCpuUsageValues = freshCpuUsageValues;
            }catch (Throwable e){
                e.printStackTrace();
            }

            if(firstLaunch){
                firstLaunch = false;
                timerHandler.postDelayed(this, FIRST_LAUNCH_INTERVAL);
            }else{
                timerHandler.postDelayed(this, updateInterval);
            }
        }
    }
}
