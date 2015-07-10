package com.nimura.android.tools.app;

import android.os.Handler;

import com.nimura.android.tools.models.CpuUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Controller for application logic
 *
 * Created by Nimura
 */
public class CpuUsageController {
    private final List<CpuUsageView> cpuUsageViews = new LinkedList<>();
    private final Handler timerHandler = new Handler();
    private final CpuUpdateRunnable cur = new CpuUpdateRunnable();
    private boolean shouldRepeat = true;
    private int updateInterval = 1000;
    private List<long[]> prev, now;
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
        this.cpuUsageViews.addAll(cpuUsageViews);
    }

    /**
     * Starts the view updates
     */
    public void start(){
        shouldRepeat = true;
        timerHandler.postDelayed(cur, 0);
    }

    /**
     * Stops the view updates
     */
    public void stop(){
        shouldRepeat = false;
        for(CpuUsageView civ : cpuUsageViews){
            civ.reset();
        }
    }

    /**
     * Sets a new line color.
     * Will be applied on next iteration
     * @param color a new line color
     */
    public void setLineColor(int color){
        for (CpuUsageView cuv : cpuUsageViews) {
            cuv.setLineColor(color);
        }
    }

    /**
     * Sets a new background color.
     * Will be applied on next iteration
     * @param color a new background color
     */
    public void setBackgroundColor(int color){
        for (CpuUsageView cuv : cpuUsageViews) {
            cuv.setBackgroundColor(color);
        }
    }

    /**
     * Sets a new text color.
     * Will be applied on next iteration
     * @param color a new text color
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

    private final class CpuUpdateRunnable implements Runnable{

        @Override
        public void run() {
            try {
                now = CpuUtils.getCpuLoadRaw();
                if (prev != null) {
                    int[] loads = CpuUtils.getCpuLoad(prev, now);
                    for (int i = 0; i < loads.length; i++) {
                        cpuUsageViews.get(i).addPoint(loads[i]);
                    }
                }
                prev = now;
            }catch (Throwable e){
                e.printStackTrace();
            }
            if(shouldRepeat) {
                timerHandler.postDelayed(this, updateInterval);
            }
        }
    }
}
