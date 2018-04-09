package com.ardanet_systems.it.croatpool;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ardanet_systems.it.croatpool.Models.Pool;
import com.ardanet_systems.it.croatpool.Models.Preferences;

import java.util.ArrayList;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;

public class CroatpoolPreferences extends AppCompatActivity {
    private static final String LOG_TAG = "PREFERENCES INTENT";

    private static SharedPreferences sharedPref;

    int mAppWidgetId = INVALID_APPWIDGET_ID;

    private EditText etConfigWallet;
    private Switch swConfigWorkerList;
    private Spinner spConfigPool;
    private Switch swConfigUpdatetime;
    private TextView tvConfigUpdateTime;
    private SeekBar sbConfigUpdatetime;
    private Switch swConfigTheme;
    private Switch swConfigBackground;
    private TextView tvConfigOpacity;
    private SeekBar sbConfigOpacity;
    private Switch swConfigPush;
    private Switch swConfigEmail;
    private EditText etConfigEmailAddress;

    private Button btConfigSave;

    private AppWidgetManager widgetManager;
    private RemoteViews views;

    private Preferences config = new Preferences();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        widgetManager = AppWidgetManager.getInstance(this);
        views = new RemoteViews(this.getPackageName(), R.layout.croatpool_widget);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            mAppWidgetId = extras.getInt(EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID);
        }
        if (mAppWidgetId == INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        setResult(RESULT_CANCELED);
        setContentView(R.layout.activity_croatpool_preferences);

        etConfigWallet = findViewById(R.id.etConfigWallet);
        swConfigWorkerList = findViewById(R.id.swWorkerList);
        spConfigPool = findViewById(R.id.spConfigPool);
        swConfigUpdatetime = findViewById(R.id.swConfigUpdatetime);
        tvConfigUpdateTime = findViewById(R.id.tvConfigUpdateTime);
        sbConfigUpdatetime = findViewById(R.id.sbConfigUpdatetime);
        swConfigTheme = findViewById(R.id.swConfigTheme);
        swConfigBackground = findViewById(R.id.swConfigBackground);
        tvConfigOpacity = findViewById(R.id.tvConfigOpacity);
        sbConfigOpacity = findViewById(R.id.sbConfigureOpacity);
        swConfigPush = findViewById(R.id.swConfigPush);
        swConfigEmail = findViewById(R.id.swConfigEmail);
        etConfigEmailAddress = findViewById(R.id.etConfigEmailAddress);

        final ArrayList<Pool> pools = new ArrayList<>();
        pools.add(new Pool("croatpool.com", "https://croatpool.com:8119/"));
        pools.add(new Pool("bcncroat.fastpool.eu", "https://bcncroat.fastpool.eu:8119/"));
        pools.add(new Pool("croat.pool.cat", "https://croat.pool.cat:8119/"));
        pools.add(new Pool("croat.netips.net", "https://croat.netips.net:8118/"));
        pools.add(new Pool("pool2.croatpirineus.cat (BETA)", "https://pool2.croatpirineus.cat:8119/"));
        Log.d(LOG_TAG, "pools() NAME: "+ pools.get(0).Name());

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, pools);
        spConfigPool.setAdapter(adapter);

        sharedPref = this.getSharedPreferences("config_" + String.valueOf(mAppWidgetId), Context.MODE_PRIVATE);
        config = config.LoadPreferences(sharedPref);

        sbConfigUpdatetime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String progressText = getString(R.string.tvConfigUpdateTime) + " " + Preferences.updateInterval.returnUpdateInterval(getBaseContext(), seekBar.getProgress()).label();
                tvConfigUpdateTime.setText(progressText);
                Toast.makeText(getBaseContext(), progressText, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String progressText = getString(R.string.tvConfigUpdateTime) + " " + Preferences.updateInterval.returnUpdateInterval(getBaseContext(), seekBar.getProgress()).label();
                tvConfigUpdateTime.setText(progressText);
            }
        });

        sbConfigOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String progressText = String.valueOf(seekBar.getProgress()) + "% " + getString(R.string.tvConfigOpacity);
                tvConfigOpacity.setText(progressText);
                Toast.makeText(getBaseContext(), progressText, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String progressText = String.valueOf(seekBar.getProgress()) + "% " + getString(R.string.tvConfigOpacity);
                tvConfigOpacity.setText(progressText);
            }
        });

        swConfigEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    etConfigEmailAddress.setEnabled(true);
                }
                else{
                    etConfigEmailAddress.setEnabled(false);
                }
            }
        });

        String tvConfigUpdateTimeSetText = getString(R.string.tvConfigUpdateTime) + " " + Preferences.updateInterval.returnUpdateInterval(getBaseContext(), sbConfigUpdatetime.getProgress()).label();
        String tvConfigOpacitySetText = String.valueOf(sbConfigOpacity.getProgress()) + "% " + getString(R.string.tvConfigOpacity);

        etConfigWallet.setText(config.Wallet());
        swConfigWorkerList.setChecked(config.WorkerList());
        spConfigPool.setSelection(Pool.selectPoolData(config.PoolName(), pools));
        swConfigUpdatetime.setChecked(config.UpdateTime());
        tvConfigUpdateTime.setText(tvConfigUpdateTimeSetText);
        sbConfigUpdatetime.setProgress(config.UpdateTimeInterval());
        swConfigTheme.setChecked(config.Theme());
        swConfigBackground.setChecked(config.Background());
        tvConfigOpacity.setText(tvConfigOpacitySetText);
        sbConfigOpacity.setProgress(config.Opacity());
        swConfigPush.setChecked(config.Push());
        swConfigEmail.setChecked(config.Email());
        etConfigEmailAddress.setText(config.EmailAddress());

        btConfigSave = findViewById(R.id.btConfigSave);
        btConfigSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                handleOkButton();

            }
        });
    }

    private void handleOkButton() {

        Pool item = (Pool)spConfigPool.getSelectedItem();

        Preferences config = new Preferences(
                mAppWidgetId,
                etConfigWallet.getText().toString(),
                swConfigWorkerList.isChecked(),
                item.Name(),
                item.Api(),
                swConfigUpdatetime.isChecked(),
                sbConfigUpdatetime.getProgress(),
                swConfigTheme.isChecked(),
                swConfigBackground.isChecked(),
                sbConfigOpacity.getProgress(),
                swConfigPush.isChecked(),
                swConfigEmail.isChecked(),
                etConfigEmailAddress.getText().toString());

        config.SavePreferences(sharedPref, config);

        showAppWidget();
    }

    private void showAppWidget() {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        ComponentName thisAppWidget = new ComponentName(this.getPackageName(), CroatpoolWidget.class.getName());

        Intent updateIntent = new Intent(this, WidgetBroadcastReceiver.class);

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

        updateIntent.setAction(WidgetBroadcastReceiver.ACTION_WIDGET_SETTINGS_UPDATE);
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);

        this.sendBroadcast(updateIntent);
        setResult(RESULT_OK, updateIntent);

        finish();

    }
}
