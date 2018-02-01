package com.android.funny.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/2/1.
 */

public class DishDetectBean {

    /**
     * log_id : 5161664606284176287
     * result_num : 1
     * result : [{"calorie":"0","has_calorie":true,"name":"非菜","probability":"0.999977"}]
     */

    public long log_id;
    public int result_num;
    public List<ResultBean> result;

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public int getResult_num() {
        return result_num;
    }

    public void setResult_num(int result_num) {
        this.result_num = result_num;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * calorie : 0
         * has_calorie : true
         * name : 非菜
         * probability : 0.999977
         */

        public String calorie;
        public boolean has_calorie;
        public String name;
        public String probability;

        public String getCalorie() {
            return calorie;
        }

        public void setCalorie(String calorie) {
            this.calorie = calorie;
        }

        public boolean isHas_calorie() {
            return has_calorie;
        }

        public void setHas_calorie(boolean has_calorie) {
            this.has_calorie = has_calorie;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProbability() {
            return probability;
        }

        public void setProbability(String probability) {
            this.probability = probability;
        }
    }
}
