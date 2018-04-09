package com.ardanet_systems.it.croatpool.Models;

import android.content.Context;
import android.content.SharedPreferences;

import com.ardanet_systems.it.croatpool.R;

import java.util.ArrayList;
import java.util.List;

public class Preferences {
    private static final String PREFS_WIDGETID = "WidgetId";
    private static final String PREFS_WALLET = "Wallet";
    private static final String PREFS_WORKERLIST = "WorkerList";
    private static final String PREFS_POOL_NAME = "PoolName";
    private static final String PREFS_POOL_API = "PoolApi";
    private static final String PREFS_UPDATETIME = "UpdateTime";
    private static final String PREFS_UPDATETIME_INTERVAL = "UpdateTimeInterval";
    private static final String PREFS_THEME = "Theme";
    private static final String PREFS_BACKGROUND = "Background";
    private static final String PREFS_OPACITY = "Opacity";
    private static final String PREFS_PUSH = "Push";
    private static final String PREFS_EMAIL = "Email";
    private static final String PREFS_EMAIL_ADDRESS = "EmailAddress";

    private int WidgetId = 0;
    private String Wallet = "";
    private Boolean WorkerList = false;
    private String PoolName = "";
    private String PoolApi = "";
    private Boolean UpdateTime = true;
    private int UpdateTimeInterval = 30;
    private Boolean Theme = true;
    private Boolean Background = false;
    private int Opacity = 50;
    private Boolean Push = false;
    private Boolean Email = false;
    private String EmailAddress = "";

    public Preferences() {

    }

    public Preferences(int WidgetId, String Wallet, Boolean WorkerList, String PoolName, String PoolApi, Boolean UpdateTime, int UpdateTimeInterval,
                                   Boolean Theme, Boolean Background, int Opacity, Boolean Push, Boolean Email, String EmailAddress) {

        this.WidgetId = WidgetId;
        this.Wallet = Wallet;
        this.WorkerList = WorkerList;
        this.PoolName = PoolName;
        this.PoolApi = PoolApi;
        this.UpdateTime = UpdateTime;
        this.UpdateTimeInterval = UpdateTimeInterval;
        this.Theme = Theme;
        this.Background = Background;
        this.Opacity = Opacity;
        this.Push = Push;
        this.Email = Email;
        this.EmailAddress = EmailAddress;
    }

    public int WidgetId() {return WidgetId; }
    public String Wallet() { return Wallet; }
    public Boolean WorkerList() { return WorkerList; }
    public String PoolName() { return PoolName; }
    public String PoolApi() { return PoolApi; }
    public Boolean UpdateTime() { return UpdateTime; }
    public int UpdateTimeInterval() {return UpdateTimeInterval; }
    public Boolean Theme() {return Theme; }
    public Boolean Background() { return Background; }
    public int Opacity() {return Opacity; }
    public Boolean Push() { return Push; }
    public Boolean Email() { return Email; }
    public String EmailAddress() { return EmailAddress; }

    public boolean SavePreferences(SharedPreferences sharedPreferences, Preferences preferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(PREFS_WIDGETID, preferences.WidgetId());
        editor.putString(PREFS_WALLET, preferences.Wallet());
        editor.putBoolean(PREFS_WORKERLIST, preferences.WorkerList());
        editor.putString(PREFS_POOL_NAME, preferences.PoolName());
        editor.putString(PREFS_POOL_API, preferences.PoolApi());
        editor.putBoolean(PREFS_UPDATETIME, preferences.UpdateTime());
        editor.putInt(PREFS_UPDATETIME_INTERVAL, preferences.UpdateTimeInterval());
        editor.putBoolean(PREFS_THEME, preferences.Theme());
        editor.putBoolean(PREFS_BACKGROUND, preferences.Background());
        editor.putInt(PREFS_OPACITY, preferences.Opacity());
        editor.putBoolean(PREFS_PUSH, preferences.Push());
        editor.putBoolean(PREFS_EMAIL, preferences.Email());
        editor.putString(PREFS_EMAIL_ADDRESS, preferences.EmailAddress());

        editor.commit();

        return true;
    }

    public static Preferences LoadPreferences(SharedPreferences sharedPreferences) {
        Preferences preferences = new Preferences();

        preferences.WidgetId = sharedPreferences.getInt(PREFS_WIDGETID, 0);
        preferences.Wallet = sharedPreferences.getString(PREFS_WALLET, "");
        preferences.WorkerList = sharedPreferences.getBoolean(PREFS_WORKERLIST, false);
        preferences.PoolName = sharedPreferences.getString(PREFS_POOL_NAME, "croatpool.com");
        preferences.PoolApi = sharedPreferences.getString(PREFS_POOL_API, "https://croatpool.com:8119/");
        preferences.UpdateTime = sharedPreferences.getBoolean(PREFS_UPDATETIME, true);
        preferences.UpdateTimeInterval = sharedPreferences.getInt(PREFS_UPDATETIME_INTERVAL, 5);
        preferences.Theme = sharedPreferences.getBoolean(PREFS_THEME, true);
        preferences.Background = sharedPreferences.getBoolean(PREFS_BACKGROUND, true);
        preferences.Opacity = sharedPreferences.getInt(PREFS_OPACITY, 50);
        preferences.Push = sharedPreferences.getBoolean(PREFS_PUSH, false);
        preferences.Email = sharedPreferences.getBoolean(PREFS_EMAIL, false);
        preferences.EmailAddress = sharedPreferences.getString(PREFS_EMAIL_ADDRESS, "");

        return preferences;
    }

    public static class updateInterval {
        private static final int minute = 1000 * 60;
        private static final int hour = minute * 60;
        private static final int day = hour * 24;

        private static final int TIME_1_MINUTE = minute;
        private static final int TIME_2_MINUTES = minute * 2;
        private static final int TIME_5_MINUTES = minute * 5;
        private static final int TIME_10_MINUTES = minute * 10;
        private static final int TIME_15_MINUTES = minute * 15;
        private static final int TIME_30_MINUTES = minute * 30;
        private static final int TIME_1_HOUR = hour;
        private static final int TIME_2_HOURS = hour * 2;
        private static final int TIME_6_HOURS = hour * 6;
        private static final int TIME_12_HOURS = hour * 12;
        private static final int TIME_1_DAY = day;

        private String label;
        private int value;

        private updateInterval(String label, int value){
            this.label = label;
            this.value = value;
        }

        public String label(){ return label; }
        public int value(){ return value; }

        public static updateInterval returnUpdateInterval(Context context, int interval){
            List<updateInterval> list = new ArrayList<>();

            list.add(new updateInterval(context.getResources().getString(R.string.time_01m), TIME_1_MINUTE));
            list.add(new updateInterval(context.getResources().getString(R.string.time_02m), TIME_2_MINUTES));
            list.add(new updateInterval(context.getResources().getString(R.string.time_05m), TIME_5_MINUTES));
            list.add(new updateInterval(context.getResources().getString(R.string.time_10m), TIME_10_MINUTES));
            list.add(new updateInterval(context.getResources().getString(R.string.time_15m), TIME_15_MINUTES));
            list.add(new updateInterval(context.getResources().getString(R.string.time_30m), TIME_30_MINUTES));
            list.add(new updateInterval(context.getResources().getString(R.string.time_01h), TIME_1_HOUR));
            list.add(new updateInterval(context.getResources().getString(R.string.time_02h), TIME_2_HOURS));
            list.add(new updateInterval(context.getResources().getString(R.string.time_06h), TIME_6_HOURS));
            list.add(new updateInterval(context.getResources().getString(R.string.time_12h), TIME_12_HOURS));
            list.add(new updateInterval(context.getResources().getString(R.string.time_01d), TIME_1_DAY));

            return list.get(interval);
        }
    }
}
