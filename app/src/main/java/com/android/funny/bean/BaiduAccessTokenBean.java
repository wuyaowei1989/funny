package com.android.funny.bean;

/**
 * Created by Administrator on 2018/1/31.
 */

public class BaiduAccessTokenBean {

    /**
     * access_token : 24.3f7c89927712d4c18e21f4a24918e485.2592000.1519988729.282335-10763813
     * session_key : 9mzdCKJA3k/xGxR/EHLDvQ83PGErUxHk/wDguS6MbWFxpmnHhtyjBWLvWzhu6cFSj5JSK627A3U5/ij4rBxZxT3UxNIoRg==
     * scope : public vis-classify_dishes vis-classify_car brain_all_scope vis-classify_animal vis-classify_plant brain_object_detect brain_realtime_logo brain_dish_detect brain_car_detect brain_animal_classify brain_plant_classify wise_adapt lebo_resource_base lightservice_public hetu_basic lightcms_map_poi kaidian_kaidian ApsMisTest_Test权限 vis-classify_flower bnstest_fasf lpq_开放 cop_helloScope ApsMis_fangdi_permission
     * refresh_token : 25.bc3631b72e10cb11378460a15454060e.315360000.1832756729.282335-10763813
     * session_secret : 0c73b39ae3e1d6c765f93d1e82b73aa6
     * expires_in : 2592000
     */

    public String access_token;
    public String session_key;
    public String scope;
    public String refresh_token;
    public String session_secret;
    public int expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getSession_key() {
        return session_key;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getSession_secret() {
        return session_secret;
    }

    public void setSession_secret(String session_secret) {
        this.session_secret = session_secret;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }
}
