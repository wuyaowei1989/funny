package com.android.funny.module;

import com.android.funny.BuildConfig;
import com.android.funny.MyApp;
import com.android.funny.net.ApiConstants;
import com.android.funny.net.JanDanApi;
import com.android.funny.net.JanDanApiService;
import com.android.funny.net.NewsApi;
import com.android.funny.net.NewsApiService;
import com.android.funny.net.RetrofitConfig;
import com.android.funny.net.SinaApi;
import com.android.funny.net.SinaApiService;

import java.io.File;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * desc:
 * author: Will .
 * date: 2017/9/2 .
 */
@Module
public class HttpModule {

    @Provides
    OkHttpClient.Builder provideOkHttpClient() {
        // 指定缓存路径,缓存大小100Mb
        Cache cache = new Cache(new File(MyApp.getContext().getCacheDir(), "HttpCache"),
                1024 * 1024 * 100);
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            return new OkHttpClient().newBuilder().cache(cache)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(RetrofitConfig.sLoggingInterceptor)
                    .addInterceptor(RetrofitConfig.sRewriteCacheControlInterceptor)
                    .addInterceptor(loggingInterceptor)
                    .addNetworkInterceptor(RetrofitConfig.sRewriteCacheControlInterceptor)
                    .connectTimeout(10, TimeUnit.SECONDS);
        } else {
            return new OkHttpClient().newBuilder().cache(cache)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(RetrofitConfig.sLoggingInterceptor)
                    .addInterceptor(RetrofitConfig.sRewriteCacheControlInterceptor)
                    .addNetworkInterceptor(RetrofitConfig.sRewriteCacheControlInterceptor)
                    .connectTimeout(10, TimeUnit.SECONDS);
        }
    }

//    @Provides
//    Retrofit.Builder provideBuilder(OkHttpClient okHttpClient) {
//        return new Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .client(okHttpClient);
//    }

    @Provides
    NewsApi provideNetEaseApis(OkHttpClient.Builder builder) {
        builder.addInterceptor(RetrofitConfig.sQueryParameterInterceptor);

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build());

        return NewsApi.getInstance(retrofitBuilder
                .baseUrl(ApiConstants.sIFengApi)
                .build().create(NewsApiService.class));
    }

    @Provides
    JanDanApi provideJanDanApis(OkHttpClient.Builder builder) {

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build());

        return JanDanApi.getInstance(retrofitBuilder
                .baseUrl(ApiConstants.sJanDanApi)
                .build().create(JanDanApiService.class));
    }

    @Provides
    SinaApi provideSinaApis(OkHttpClient.Builder builder) {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build());

        return SinaApi.getInstance(retrofitBuilder
                .baseUrl(ApiConstants.sSinaApi)
                .build().create(SinaApiService.class));
    }

}
