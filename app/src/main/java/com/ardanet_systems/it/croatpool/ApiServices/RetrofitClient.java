package com.ardanet_systems.it.croatpool.ApiServices;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.util.Log;

import com.ardanet_systems.it.croatpool.CroatpoolWidget;
import com.ardanet_systems.it.croatpool.Models.ApiModels.ApiLiveStatsModels.LiveStats;
import com.ardanet_systems.it.croatpool.Models.ApiModels.ApiLiveStatsModels.Network;
import com.ardanet_systems.it.croatpool.Models.ApiModels.ApiStatsAddressModels.PoolStats;
import com.ardanet_systems.it.croatpool.Models.ApiModels.ApiStatsAddressModels.StatsAddress;
import com.ardanet_systems.it.croatpool.Models.ApiModels.ApiStatsAddressModels.Workers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String LOG_TAG = "RETROFIT";
    private static RetrofitApi api;

    public static void getLiveStats(Context context, int appWidgetId, String pool){

        api = getAPI(pool);

        api.getLiveStats()
            .enqueue(new Callback<LiveStats>() {
                @Override
                public void onResponse (Call<LiveStats> call, Response<LiveStats> response){

                    try {
                        Network network = response.body().getNetwork();

                        CroatpoolWidget.updateAppWidgetRetrofitNetwork(context, AppWidgetManager.getInstance(context), appWidgetId, network);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure (Call<LiveStats> call, Throwable t){
                    t.printStackTrace();
                }
            });
    }

    public static void getStatsAddress(Context context, int appWidgetId, String pool, String address){

        api = getAPI(pool);

        api.getStatsAddress(address)
                .enqueue(new Callback<PoolStats>() {
                    @Override
                    public void onResponse (Call<PoolStats> call, Response<PoolStats> response){

                        try {
                            StatsAddress statsAddress = response.body().getStatsAddress();

                            CroatpoolWidget.updateAppWidgetRetrofitAddress(context, AppWidgetManager.getInstance(context), appWidgetId, statsAddress);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure (Call<PoolStats> call, Throwable t){
                        t.printStackTrace();
                    }
                });
    }

    public static void getStatsWorkers(Context context, int appWidgetId, String pool, String address, String workerName){

        api = getAPI(pool);

        api.getStatsWorker(address)
                .enqueue(new Callback<PoolStats>() {
                    @Override
                    public void onResponse (Call<PoolStats> call, Response<PoolStats> response){

                        try {
                            StatsAddress statsAddress = response.body().getStatsAddress();
                            List<Workers> workers = response.body().getWorkers();

                            for ( Workers worker : workers ) {
                                if (workerName.equals(worker.getName())) {

                                    CroatpoolWidget.updateAppWidgetRetrofitWorker(context, AppWidgetManager.getInstance(context), appWidgetId, statsAddress, worker);

                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure (Call<PoolStats> call, Throwable t){
                        t.printStackTrace();
                    }
                });
    }

    private static RetrofitApi getAPI(String url){
        final Retrofit retrofit = createRetrofit(url);
        return retrofit.create(RetrofitApi.class);
    }

    private static OkHttpClient createOkHttpClient(){
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(new RetrofitInterceptor());

        return httpClient.build();
    }

    private static Retrofit createRetrofit(String url){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(createOkHttpClient())
                .build();
    }
}
