package com.ardanet_systems.it.croatpool;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.PersistableBundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.ardanet_systems.it.croatpool.Models.Preferences;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility {
    private static final String LOG_TAG = "UTILITY";

    public static String getCurrentTime(){
        Format formatter = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return formatter.format(new Date());
    }

    public static String getCurrentDate(){
        Format formatter = new SimpleDateFormat("dd 'de' MMMM 'del' yyyy", Locale.getDefault());
        return formatter.format(new Date());
    }

    public static String getCurrentDateTime(){
        Format formatter = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.getDefault());
        return formatter.format(new Date());
    }

    public static void WidgetStyle(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Boolean background, int opacityBackground, Boolean themeBackground){
        int backgroundColor;
        int textColor;
        int tintColor;
        float opacity;

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.croatpool_widget);

        if(themeBackground){
            backgroundColor = 0x000000;
            textColor = Color.WHITE;
            tintColor = Color.WHITE;
        }
        else{
            backgroundColor = 0xffffff;
            textColor = Color.BLACK;
            tintColor = Color.BLACK;
        }

        views.setInt(R.id.tvDate, "setTextColor", textColor);
        views.setInt(R.id.tvTime, "setTextColor", textColor);
        views.setInt(R.id.tvPool, "setTextColor", textColor);
        views.setInt(R.id.tvWallet, "setTextColor", textColor);
        views.setInt(R.id.tvHashrate, "setTextColor", textColor);
        views.setInt(R.id.tvHashrateLabel, "setTextColor", textColor);
        views.setInt(R.id.tvDifficulty, "setTextColor", textColor);
        views.setInt(R.id.tvDifficultyLabel, "setTextColor", textColor);
        views.setInt(R.id.tvLastshare, "setTextColor", textColor);
        views.setInt(R.id.tvLastshareLabel, "setTextColor", textColor);
        views.setInt(R.id.tvTotalshares, "setTextColor", textColor);
        views.setInt(R.id.tvTotalsharesLabel, "setTextColor", textColor);
        views.setInt(R.id.tvPending, "setTextColor", textColor);
        views.setInt(R.id.tvPendingLabel, "setTextColor", textColor);
        views.setInt(R.id.tvPayed, "setTextColor", textColor);
        views.setInt(R.id.tvPayedLabel, "setTextColor", textColor);

        views.setInt(R.id.ivPreferences, "setColorFilter", tintColor);
        views.setInt(R.id.ivUpdate, "setColorFilter", tintColor);
        views.setInt(R.id.ivPool, "setColorFilter", tintColor);
        views.setInt(R.id.ivHashrate, "setColorFilter", tintColor);
        views.setInt(R.id.ivDifficulty, "setColorFilter", tintColor);
        views.setInt(R.id.ivLastshare, "setColorFilter", tintColor);
        views.setInt(R.id.ivTotalshares, "setColorFilter", tintColor);
        views.setInt(R.id.ivPending, "setColorFilter", tintColor);
        views.setInt(R.id.ivPayed, "setColorFilter", tintColor);

        if (background){
            opacity = opacityBackground/100f;
            views.setInt( R.id.background, "setBackgroundColor", (int)(opacity * 0xFF) << 24 | backgroundColor);
        }
        else {
            opacity = 0f;
            views.setInt( R.id.background, "setBackgroundColor", (int)(opacity * 0xFF) << 24 | backgroundColor);
        }
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void scheduleJob(Context context, int appWidgetId, int updateTimeInterval) {

        int intervalTime = Preferences.updateInterval.returnUpdateInterval(context, updateTimeInterval).value();

        ComponentName component = new ComponentName(context, UpdateService.class);

        JobInfo.Builder builder = new JobInfo.Builder(appWidgetId, component);

        PersistableBundle bundle = new PersistableBundle();
        bundle.putInt("updateTimeInterval", updateTimeInterval);

        builder.setExtras(bundle);
        builder.setMinimumLatency(intervalTime);
        builder.setPersisted(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NOT_ROAMING);
        else
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }

    public static void scheduleJobStop(Context context, int appWidgetId) {

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        for (JobInfo jobInfo : jobScheduler.getAllPendingJobs()) {
            if (jobInfo.getId() == appWidgetId){
                jobScheduler.cancel(jobInfo.getId());
            }
        }
    }

    public static String hashrateSuffix(String hashrate) {
        final String[] suffixes = new String[]{"H/sec", "KH/sec", "MH/sec", "GH/sec", "TH/sec"};
        hashrate = hashrate.replaceAll("[^\\d.]", "");
        Double count = Double.parseDouble(hashrate);

        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%.2f %s", count / Math.pow(1000, exp), suffixes[exp], Locale.getDefault());
    }

    public static String difficultySuffix(long count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%.2f %c", count / Math.pow(1000, exp), "KMGTPE".charAt(exp-1), Locale.getDefault());
    }

    public static String convertCoin(String coin) {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        nf.setMaximumFractionDigits(8);
        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("Croat");

        ((DecimalFormat) nf).setDecimalFormatSymbols(decimalFormatSymbols);

        try {
            float coins = Long.parseLong(coin);
            coins = coins / 100000000000L;
            return String.valueOf(nf.format(coins));
        }catch(Exception e){
            e.printStackTrace();
        }

        return " CROAT";
    }

    public static String convertLastShare(String lastShare){
        return DateUtils.getRelativeTimeSpanString(
                Long.parseLong(lastShare) * 1000,
                getDateInMillis(Utility.getCurrentDateTime()),
                30000).toString();
    }

    private static long getDateInMillis(String srcDate) {
        SimpleDateFormat desiredFormat = new SimpleDateFormat(
                "dd MMMM yyyy HH:mm:ss", Locale.getDefault());

        long dateInMillis = 0;
        try {
            Date date = desiredFormat.parse(srcDate);
            dateInMillis = date.getTime();
            return dateInMillis;
        } catch (ParseException e) {
            Log.d("TAG","Exception while parsing date. " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }
}
