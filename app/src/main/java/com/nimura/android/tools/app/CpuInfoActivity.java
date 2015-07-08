package com.nimura.android.tools.app;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.nimura.android.tools.models.CpuUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Application activity
 */
public class CpuInfoActivity extends Activity {
    private LinearLayout parentLayout;
    private List<CpuInfoView> cpuInfoViews = new LinkedList<>();
    private CpuInfoController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parentLayout = new LinearLayout(this);
        parentLayout.setLayoutParams(
                new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));
        parentLayout.setOrientation(LinearLayout.VERTICAL);

        for(int i=0;i<CpuUtils.getCpuCount();i++) {
            CpuInfoView cp = new CpuInfoView(this, i, getResources().getInteger(R.integer.pointsInView));
            cp.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f));
            cpuInfoViews.add(cp);
            parentLayout.addView(cp);
        }

        setContentView(parentLayout);

        controller = new CpuInfoController(getApplicationContext(), cpuInfoViews);
        controller.start();
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
}
