package com.nimura.android.tools.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
public class CpuUsageActivity extends AppCompatActivity {
    private final List<CpuUsageView> cpuUsageViews = new LinkedList<>();
    private LinearLayout parentLayout;
    private CpuUsageController controller;

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
            CpuUsageView cp = new CpuUsageView(this, i, getResources().getInteger(R.integer.pointsInView));
            cp.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f));
            cpuUsageViews.add(cp);
            parentLayout.addView(cp);
        }

        setContentView(parentLayout);

        controller = new CpuUsageController(getApplicationContext(), cpuUsageViews);
        controller.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cpu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
