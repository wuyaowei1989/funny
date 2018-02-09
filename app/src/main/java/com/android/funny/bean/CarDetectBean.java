package com.android.funny.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/2/9.
 */

public class CarDetectBean {

    /**
     * log_id : 3188170555572589153
     * result : [{"score":0.40633463859558,"name":"标致BB1","year":"无年份信息"},{"score":0.14677540957928,"name":"丰田FTEV","year":"无年份信息"},{"score":0.065599098801613,"name":"雷诺RSpace","year":"无年份信息"}]
     * color_result : 蓝色
     */

    public long log_id;
    public String color_result;
    public List<ResultBean> result;

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public String getColor_result() {
        return color_result;
    }

    public void setColor_result(String color_result) {
        this.color_result = color_result;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * score : 0.40633463859558
         * name : 标致BB1
         * year : 无年份信息
         */

        public double score;
        public String name;
        public String year;
        public String color;

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }
}
