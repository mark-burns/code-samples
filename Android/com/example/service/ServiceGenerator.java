package com.example.service;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by mburns on 8/25/2015.
 */
public class ServiceGenerator {

    private ServiceGenerator() {
        /* intentionally blank - no instance needed. */
    }

    public static <S> S createService(Class<S> serviceClass, String baseUrl) {

        // NOTE: We are using the OkHttpClient (rather than the default android) primarily so we can
        // set a timeout.
        OkHttpClient c = new OkHttpClient();
        c.setConnectTimeout(15, TimeUnit.SECONDS);
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(baseUrl)
                .setClient(new OkClient(c))
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Accept", "application/json");
                        if (AppGlobal.getInstance().isLoggedIn()) {
                            request.addHeader("Auth-Token", AppGlobal.getInstance().getToken());
                        }
                    }
                });

        RestAdapter adapter = builder.build();

        return adapter.create(serviceClass);
    }
}
