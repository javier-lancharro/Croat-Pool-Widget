package com.ardanet_systems.it.croatpool;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;

public class WidgetBroadcastReceiver extends android.content.BroadcastReceiver {
    private static final String LOG_TAG = "RECEIVER";

    public static final String ACTION_WIDGET_UPDATE = "ActionReceiverUpdate";
    public static final String ACTION_WIDGET_SETTINGS = "ActionReceiverSettings";
    public static final String ACTION_WIDGET_SETTINGS_UPDATE = "ActionReceiverPreferencesUpdated";
    public static final String ACTION_WIDGET_AUTO_UPDATE = "ActionReceiverAutoUpdate";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "onReceive");

        if (ACTION_WIDGET_UPDATE.equals(intent.getAction()) ||
                ACTION_WIDGET_SETTINGS_UPDATE.equals(intent.getAction()) ||
                ACTION_WIDGET_AUTO_UPDATE.equals(intent.getAction())) {

            int appWidgetId = intent.getIntExtra(EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID);
            if (appWidgetId != INVALID_APPWIDGET_ID) {
                Log.d(LOG_TAG, "onReceive(Update) (ID: "+ appWidgetId + ") " + intent.getAction());

                CroatpoolWidget.updateAppWidget(context, AppWidgetManager.getInstance(context), appWidgetId);
            }
        }
        else if (ACTION_WIDGET_SETTINGS.equals(intent.getAction())) {
            int appWidgetId = intent.getIntExtra(EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID);
            if (appWidgetId != INVALID_APPWIDGET_ID) {
                Log.d(LOG_TAG, "onReceive(Config) (ID: " + appWidgetId+ ")");

                intent = new Intent(context, CroatpoolPreferences.class);
                intent.putExtra(EXTRA_APPWIDGET_ID, appWidgetId);
                intent.setAction(ACTION_WIDGET_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }
}
