package com.android.funny.net;

import com.android.funny.bean.BaiduAccessTokenBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2018/1/31.
 */

public interface BaiduAiApiService {
    @GET("oauth/2.0/token?")
    Observable<BaiduAccessTokenBean> getAccessToken(@Query("grant_type") String grant_type,
                                                    @Query("client_id") String ak,
                                                    @Query("client_secret") String sk
    );

    @GET("rest/2.0/image-classify/v2/dish?")
    Observable<Object> dishesRecognition(@Query("access_token") String access_token);
}
