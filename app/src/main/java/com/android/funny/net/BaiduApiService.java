package com.android.funny.net;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2018/2/2.
 */

public interface BaiduApiService {

    @GET("search/index")
    Observable<Object> getImageList(@Query("tn") String tn,
                                                  @Query("ie") String ie,
                                                  @Query("word") String word,
                                                  @Query("pn") int pn,
                                                  @Query("rn") int rn
    );
}
