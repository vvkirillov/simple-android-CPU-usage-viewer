package com.nimura.android.tools.app;

import android.app.Activity;
import android.graphics.Point;
import android.os.Handler;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.nimura.cpuviewer.R;
import com.nimura.android.tools.model.CpuUtils;

import java.util.LinkedList;
import java.util.List;


public class CpuActivity extends Activity {
    private final Handler timerHandler = new Handler();
    private final CpuUpdateRunnable cur = new CpuUpdateRunnable();
    private LinearLayout parentLayout;
    private List<CpuInfoView> cpuInfoViews = new LinkedList<>();
    private List<long[]> prev, now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parentLayout = new LinearLayout(this);
        parentLayout.setLayoutParams(
                new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));
        parentLayout.setOrientation(LinearLayout.VERTICAL);

        Point size = null;
        for(int i=0;i<CpuUtils.getCpuCount();i++) {
            CpuInfoView cp = new CpuInfoView(this, i);
            cp.setLayoutParams(
                    new LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            0,
                            1f));
            cpuInfoViews.add(cp);
            parentLayout.addView(cp);
        }

        setContentView(parentLayout);

        timerHandler.postDelayed(cur, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cpu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            timerHandler.postDelayed(this, 1000);
        }
    }
}
