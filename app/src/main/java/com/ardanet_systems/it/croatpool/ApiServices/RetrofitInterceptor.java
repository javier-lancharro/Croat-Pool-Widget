package com.ardanet_systems.it.croatpool.ApiServices;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;

public class RetrofitInterceptor implements Interceptor {
    private static String LOG_TAG = "INTERCEPTOR";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Response response = chain.proceed(request);
        Headers headers = response.headers();

        if( "deflate".equals(headers.get("Content-Encoding"))) {

            ResponseBody responseBody = response.body();
            BufferedSource source = responseBody.source();

            InputStream os = source.inputStream();
            Inflater inf = new Inflater(true);
            InputStream gzipped = new InflaterInputStream(os, inf);

            String responseConverted = convertStreamToString(gzipped);

            MediaType contentType = response.body().contentType();
            ResponseBody body = ResponseBody.create(contentType, responseConverted);

            return response.newBuilder().body(body).build();
        }

//        Log.e(LOG_TAG, response.toString());

        return response;

    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}
