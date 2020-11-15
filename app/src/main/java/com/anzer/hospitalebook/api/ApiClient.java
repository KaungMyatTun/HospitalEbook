package com.anzer.hospitalebook.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // City Hospital
    // private static String BaseUrl = "http://172.24.20.43:8083/api/HandwritingOrder/";
    private static String BaseUrl = "http://103.29.91.202:8084/api/HandwritingOrder/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        OkHttpClient client = new OkHttpClient.Builder().build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
