package com.nimura.android.tools.app;

import android.content.Context;
import android.os.Handler;

import com.nimura.android.tools.models.CpuUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Controller for application logic
 *
 * Created by Nimura
 */
public class CpuInfoController {
    private Context context;
    private final List<CpuInfoView> cpuInfoViews = new LinkedList<>();
    private final Handler timerHandler = new Handler();
    private final CpuUpdateRunnable cur = new CpuUpdateRunnable();
    private final AtomicBoolean willRepeat = new AtomicBoolean(true);
    private List<long[]> prev, now;

    /**
     * Constructor
     * @param context context
     * @param cpuInfoViews list of CpuInfoView objects
     */
    public CpuInfoController(Context context, List<CpuInfoView> cpuInfoViews){
        this.context = context;
        this.cpuInfoViews.addAll(cpuInfoViews);
    }

    /**
     * Starts the view updates
     */
    public void start(){
        willRepeat.set(true);
        timerHandler.postDelayed(cur, 0);
    }

    /**
     * Stops the view updates
     */
    public void stop(){
        willRepeat.set(false);
    }

    private class CpuUpdateRunnable implements Runnable{

        @Override
        public void run() {
            try {
                now = CpuUtils.getCpuLoadRaw();
                if (prev != null) {
                    int[] loads = CpuUtils.getCpuLoad(prev, now);
                    for (int i = 0; i < loads.length; i++) {
                        cpuInfoViews.get(i).addPoint(loads[i]);
                    }
                }
                prev = now;
            }catch (Throwable e){
                e.printStackTrace();
            }
            if(willRepeat.get()) {
                timerHandler.postDelayed(this, context.getResources().getInteger(R.integer.cpuWidgetUpdateInterval));
            }
        }
    }
}
