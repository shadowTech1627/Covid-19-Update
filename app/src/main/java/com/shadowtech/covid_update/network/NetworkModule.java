package com.shadowtech.covid_update.network;

import com.shadowtech.covid_update.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkModule {
    private static NetworkModule sInstance;
    private Retrofit mRetrofit;

    public NetworkModule() {

        OkHttpClient httpClient;

        httpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();


        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL) //manager.getConfigEnvironment()
                .client(httpClient).callFactory(new Call.Factory() {
                    @Override
                    public Call newCall(Request request) {
                        request = request.newBuilder().tag(new Object[]{null}).build();


                        Call call = httpClient.newCall(request);

                        // We set the element to the call, to (at least) keep some consistency
                        // If you want to only have Strings, create a String array and put the default value to null;
                        ((Object[]) request.tag())[0] = call;

                        return call;
                    }
                })
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static NetworkModule getInstance() {
        if (sInstance == null)
            sInstance = new NetworkModule();
        return sInstance;
    }

    public APIService getAPIService() {
        return mRetrofit.create(APIService.class);
    }
}
