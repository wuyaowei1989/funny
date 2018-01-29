package com.android.funny.net;

import com.android.funny.bean.MoveListBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2018/1/27.
 */

public interface SinaApiService {

    @GET("movie/in_theaters")
    Observable<MoveListBean> getMoveList(@Query("apikey") String key,
                                         @Query("city") String city,
                                         @Query("start") int start,
                                         @Query("count") int count,
                                         @Query("client") String client,
                                         @Query("udid") String udid);
}
