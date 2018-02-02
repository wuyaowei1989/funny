package com.android.funny.net;

import com.android.funny.bean.BaiduAccessTokenBean;
import com.android.funny.bean.DishDetectBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    @FormUrlEncoded
    @POST("rest/2.0/image-classify/v2/dish")
    Observable<DishDetectBean> dishesRecognition(@Query("access_token") String access_token,
                                                 @Field("image") String image,
                                                 @Field("top_num") int top_num,
                                                 @Field("filter_threshold") float filter_threshold);

    @FormUrlEncoded
    @POST("rest/2.0/image-classify/v1/car")
    Observable<Object> carDetect(@Query("access_token") String access_token,
                                 @Field("image") String image,
                                 @Field("top_num") int top_num);

    @FormUrlEncoded
    @POST("rest/2.0/image-classify/v1/plant")
    Observable<Object> plantDetect(@Query("access_token") String access_token,
                                   @Field("image") String image);

    @GET("search/index")
    Observable<Object> getImageList(@Query("url") String url,
                                    @Query("tn") String tn,
                                    @Query("ie") String ie,
                                    @Query("word") String word,
                                    @Query("pn") int pn,
                                    @Query("rn") int rn);
}
