package com.android.funny.net;

import com.android.funny.bean.MoveListBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by Administrator on 2018/1/27.
 */

public interface SinaApiService {

    @GET("cgi-bin/pitu_open_access_for_youtu.fcg")
    Observable<MoveListBean> faceMerge(@Header("Authorization") String appSign,
                                       @Body RequestBody body);
}
