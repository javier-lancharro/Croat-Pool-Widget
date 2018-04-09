package com.ardanet_systems.it.croatpool;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.appwidget.AppWidgetManager;
import android.util.Log;

public class UpdateService extends JobService {
    private static final String LOG_TAG = "JOB SERVICE";
    private JobParameters params;

    @Override
    public boolean onStartJob(JobParameters params) {
        this.params = params;
        Log.d(LOG_TAG, "JOB SERVICE RUNNING (ID:" + params.getJobId() + "): "+ Utility.getCurrentTime());

        CroatpoolWidget.updateAppWidget(this, AppWidgetManager.getInstance(this), params.getJobId());

        return true;

    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(LOG_TAG, "JOB SERVICE STOPPED: " + Utility.getCurrentTime());
        return false;
    }
}
