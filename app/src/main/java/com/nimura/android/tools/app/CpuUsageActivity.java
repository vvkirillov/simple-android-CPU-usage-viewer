package com.nimura.android.tools.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
    private final CpuUsageController cpuUsageController = CpuUsageController.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parentLayout = new LinearLayout(this);
        parentLayout.setLayoutParams(
                new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));
        parentLayout.setOrientation(LinearLayout.VERTICAL);

        createViews();

        setContentView(parentLayout);

        initController();
    }

    private void createViews() {
        for(int i=0;i< CpuUtils.getCpuCount();i++) {
            CpuUsageView cp = new CpuUsageView(this, i, getResources().getInteger(R.integer.pointsInView));
            cp.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f));
            cpuUsageViews.add(cp);
            parentLayout.addView(cp);
        }
    }

    private void initController() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        cpuUsageController.setCpuUsageViews(cpuUsageViews);
        cpuUsageController.setLineColor(prefs.getInt(AppConst.LINE_COLOR_PREFERENCE_ID, getResources().getColor(R.color.lineColor)));
        cpuUsageController.setBackgroundColor(prefs.getInt(AppConst.BACKGROUND_COLOR_PREFERENCE_ID, getResources().getColor(R.color.backgroundColor)));
        cpuUsageController.setTextColor(prefs.getInt(AppConst.TEXT_COLOR_PREFERENCE_ID, getResources().getColor(R.color.textColor)));
        cpuUsageController.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cpu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (R.id.action_settings == id) {
            openSettingsActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openSettingsActivity() {
        startActivity(new Intent(this, SettingsActivity.class));
    }
}
