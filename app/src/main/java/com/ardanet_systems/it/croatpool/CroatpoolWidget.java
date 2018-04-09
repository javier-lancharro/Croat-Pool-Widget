package com.ardanet_systems.it.croatpool;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.ardanet_systems.it.croatpool.ApiServices.RetrofitClient;
import com.ardanet_systems.it.croatpool.Models.ApiModels.ApiLiveStatsModels.Network;
import com.ardanet_systems.it.croatpool.Models.ApiModels.ApiStatsAddressModels.StatsAddress;
import com.ardanet_systems.it.croatpool.Models.ApiModels.ApiStatsAddressModels.Workers;
import com.ardanet_systems.it.croatpool.Models.Preferences;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;

public class CroatpoolWidget extends AppWidgetProvider {

    private static final String LOG_TAG = "WIDGET";

    private static SharedPreferences sharedPref;
    private static Preferences config;
    private PendingIntent pendingIntent;

    public static void updateAppWidgetRetrofitNetwork(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Network network) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.croatpool_widget);

        views.setTextViewText(R.id.tvDifficulty, Utility.difficultySuffix(network.getDifficulty()));

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateAppWidgetRetrofitAddress(Context context, AppWidgetManager appWidgetManager, int appWidgetId, StatsAddress statsAddress) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.croatpool_widget);

        views.setTextViewText(R.id.tvHashrate, Utility.hashrateSuffix(statsAddress.getHashrate()));
        views.setTextViewText(R.id.tvLastshare, Utility.convertLastShare(statsAddress.getLastShare()));
        views.setTextViewText(R.id.tvTotalshares, statsAddress.getHashes());
        views.setTextViewText(R.id.tvPending, Utility.convertCoin(statsAddress.getBalance()));
        views.setTextViewText(R.id.tvPayed, Utility.convertCoin(statsAddress.getPaid()));

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateAppWidgetRetrofitWorker(Context context, AppWidgetManager appWidgetManager, int appWidgetId, StatsAddress statsAddress, Workers worker){
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.croatpool_widget);

        views.setTextViewText(R.id.tvHashrate, Utility.hashrateSuffix(worker.getHashrate()));
        views.setTextViewText(R.id.tvLastshare, Utility.convertLastShare(worker.getLastShare()));
        views.setTextViewText(R.id.tvTotalshares, worker.getHashes());

        views.setTextViewText(R.id.tvPending, Utility.convertCoin(statsAddress.getBalance()));
        views.setTextViewText(R.id.tvPayed, Utility.convertCoin(statsAddress.getPaid()));

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        sharedPref = context.getSharedPreferences("config_" + String.valueOf(appWidgetId), Context.MODE_PRIVATE);
        config = Preferences.LoadPreferences(sharedPref);

        if (!config.UpdateTime()){
            Utility.scheduleJobStop(context, appWidgetId);
        }
        else {
            Utility.scheduleJob(context, appWidgetId, config.UpdateTimeInterval());
        }

        Utility.WidgetStyle(context, AppWidgetManager.getInstance(context), appWidgetId, config.Background(), config.Opacity(), config.Theme());

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.croatpool_widget);

        views.setTextViewText(R.id.tvDate, Utility.getCurrentDate());
        views.setTextViewText(R.id.tvTime, Utility.getCurrentTime());
        views.setTextViewText(R.id.tvPool, config.PoolName());
        views.setTextViewText(R.id.tvWallet, config.Wallet());

        if(config.PoolApi().length() > 0){
            try {
                RetrofitClient.getLiveStats(context, appWidgetId, config.PoolApi());
            }catch(Exception e){
                Toast.makeText(context, R.string.errorPoolApi, Toast.LENGTH_LONG).show();
            }
        }

        Boolean test = true;

        if(config.Wallet().length() == 95 && appWidgetId != 69) { // 95 character wallet
            try {
                RetrofitClient.getStatsAddress(context, appWidgetId, config.PoolApi(), config.Wallet());
            }catch(Exception e){
                Toast.makeText(context, R.string.errorWallet, Toast.LENGTH_LONG).show();
            }
        }else if(config.Wallet().length() > 95){
            String address[] = config.Wallet().split("\\+");

            String wallet = address[0];
            String worker = address[1];

            try {
                RetrofitClient.getStatsWorkers(context, appWidgetId, config.PoolApi(), wallet, worker);
            }catch(Exception e){
                Toast.makeText(context, R.string.errorWallet, Toast.LENGTH_LONG).show();
            }
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(LOG_TAG, "onUpdate");
        for (int appWidgetId : appWidgetIds) {

            Log.d(LOG_TAG, "Alarm started (ID " + appWidgetId + ")");

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.croatpool_widget);

            Intent intent = new Intent(context, WidgetBroadcastReceiver.class);
            intent.putExtra(EXTRA_APPWIDGET_ID, appWidgetId);

            intent.setAction(WidgetBroadcastReceiver.ACTION_WIDGET_UPDATE);
            pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.ivUpdate, pendingIntent);

            intent.setAction(WidgetBroadcastReceiver.ACTION_WIDGET_SETTINGS);
            pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.ivPreferences, pendingIntent);

            updateAppWidget(context, AppWidgetManager.getInstance(context), appWidgetId);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(LOG_TAG, "onEnabled");
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(LOG_TAG, "onDisabled");
        context.stopService(new Intent(context, UpdateService.class));
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(LOG_TAG, "onDeleted");
        for (int appWidgetId : appWidgetIds) {
           Utility.scheduleJobStop(context, appWidgetId);
        }
    }
}

