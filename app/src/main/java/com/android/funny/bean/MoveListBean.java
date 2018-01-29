package com.android.funny.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 14400 on 2018/1/27.
 */

public class MoveListBean {

    /**
     * count : 1
     * start : 0
     * total : 24
     * subjects : [{"rating":{"max":10,"average":5.4,"details":{"1":139,"3":682,"2":489,"5":47,"4":210},"stars":"30","min":0},"genres":["动作","科幻","冒险"],"title":"移动迷宫3：死亡解药","casts":[{"avatars":{"small":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p53688.jpg","large":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p53688.jpg","medium":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p53688.jpg"},"name_en":"Dylan O'Brien","name":"迪伦·奥布莱恩","alt":"https://movie.douban.com/celebrity/1314963/","id":"1314963"},{"avatars":{"small":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p13769.jpg","large":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p13769.jpg","medium":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p13769.jpg"},"name_en":"Kaya Scodelario","name":"卡雅·斯考达里奥","alt":"https://movie.douban.com/celebrity/1031178/","id":"1031178"},{"avatars":{"small":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1489259634.04.jpg","large":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1489259634.04.jpg","medium":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1489259634.04.jpg"},"name_en":"Ki Hong Lee","name":"李起弘","alt":"https://movie.douban.com/celebrity/1333684/","id":"1333684"}],"durations":["142分钟"],"collect_count":7180,"mainland_pubdate":"2018-01-26","has_video":false,"original_title":"The Maze Runner: The Death Cure","subtype":"movie","directors":[{"avatars":{"small":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1417887954.94.jpg","large":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1417887954.94.jpg","medium":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1417887954.94.jpg"},"name_en":"Wes Ball","name":"韦斯·鲍尔","alt":"https://movie.douban.com/celebrity/1332723/","id":"1332723"}],"pubdates":["2018-01-26(美国)","2018-01-26(中国大陆)"],"year":"2018","images":{"small":"https://img3.doubanio.com/view/photo/s_ratio_poster/public/p2508618114.jpg","large":"https://img3.doubanio.com/view/photo/s_ratio_poster/public/p2508618114.jpg","medium":"https://img3.doubanio.com/view/photo/s_ratio_poster/public/p2508618114.jpg"},"alt":"https://movie.douban.com/subject/26004132/","id":"26004132"}]
     * title : 正在上映的电影-郑州
     */

    private int count;
    private int start;
    private int total;
    private String title;
    private List<SubjectsBean> subjects;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SubjectsBean> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectsBean> subjects) {
        this.subjects = subjects;
    }

    public static class SubjectsBean {
        /**
         * rating : {"max":10,"average":5.4,"details":{"1":139,"3":682,"2":489,"5":47,"4":210},"stars":"30","min":0}
         * genres : ["动作","科幻","冒险"]
         * title : 移动迷宫3：死亡解药
         * casts : [{"avatars":{"small":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p53688.jpg","large":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p53688.jpg","medium":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p53688.jpg"},"name_en":"Dylan O'Brien","name":"迪伦·奥布莱恩","alt":"https://movie.douban.com/celebrity/1314963/","id":"1314963"},{"avatars":{"small":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p13769.jpg","large":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p13769.jpg","medium":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p13769.jpg"},"name_en":"Kaya Scodelario","name":"卡雅·斯考达里奥","alt":"https://movie.douban.com/celebrity/1031178/","id":"1031178"},{"avatars":{"small":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1489259634.04.jpg","large":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1489259634.04.jpg","medium":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1489259634.04.jpg"},"name_en":"Ki Hong Lee","name":"李起弘","alt":"https://movie.douban.com/celebrity/1333684/","id":"1333684"}]
         * durations : ["142分钟"]
         * collect_count : 7180
         * mainland_pubdate : 2018-01-26
         * has_video : false
         * original_title : The Maze Runner: The Death Cure
         * subtype : movie
         * directors : [{"avatars":{"small":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1417887954.94.jpg","large":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1417887954.94.jpg","medium":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1417887954.94.jpg"},"name_en":"Wes Ball","name":"韦斯·鲍尔","alt":"https://movie.douban.com/celebrity/1332723/","id":"1332723"}]
         * pubdates : ["2018-01-26(美国)","2018-01-26(中国大陆)"]
         * year : 2018
         * images : {"small":"https://img3.doubanio.com/view/photo/s_ratio_poster/public/p2508618114.jpg","large":"https://img3.doubanio.com/view/photo/s_ratio_poster/public/p2508618114.jpg","medium":"https://img3.doubanio.com/view/photo/s_ratio_poster/public/p2508618114.jpg"}
         * alt : https://movie.douban.com/subject/26004132/
         * id : 26004132
         */

        private RatingBean rating;
        private String title;
        private int collect_count;
        private String mainland_pubdate;
        private boolean has_video;
        private String original_title;
        private String subtype;
        private String year;
        private ImagesBean images;
        private String alt;
        private String id;
        private List<String> genres;
        private List<CastsBean> casts;
        private List<String> durations;
        private List<DirectorsBean> directors;
        private List<String> pubdates;

        public RatingBean getRating() {
            return rating;
        }

        public void setRating(RatingBean rating) {
            this.rating = rating;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getCollect_count() {
            return collect_count;
        }

        public void setCollect_count(int collect_count) {
            this.collect_count = collect_count;
        }

        public String getMainland_pubdate() {
            return mainland_pubdate;
        }

        public void setMainland_pubdate(String mainland_pubdate) {
            this.mainland_pubdate = mainland_pubdate;
        }

        public boolean isHas_video() {
            return has_video;
        }

        public void setHas_video(boolean has_video) {
            this.has_video = has_video;
        }

        public String getOriginal_title() {
            return original_title;
        }

        public void setOriginal_title(String original_title) {
            this.original_title = original_title;
        }

        public String getSubtype() {
            return subtype;
        }

        public void setSubtype(String subtype) {
            this.subtype = subtype;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public ImagesBean getImages() {
            return images;
        }

        public void setImages(ImagesBean images) {
            this.images = images;
        }

        public String getAlt() {
            return alt;
        }

        public void setAlt(String alt) {
            this.alt = alt;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<String> getGenres() {
            return genres;
        }

        public void setGenres(List<String> genres) {
            this.genres = genres;
        }

        public List<CastsBean> getCasts() {
            return casts;
        }

        public void setCasts(List<CastsBean> casts) {
            this.casts = casts;
        }

        public List<String> getDurations() {
            return durations;
        }

        public void setDurations(List<String> durations) {
            this.durations = durations;
        }

        public List<DirectorsBean> getDirectors() {
            return directors;
        }

        public void setDirectors(List<DirectorsBean> directors) {
            this.directors = directors;
        }

        public List<String> getPubdates() {
            return pubdates;
        }

        public void setPubdates(List<String> pubdates) {
            this.pubdates = pubdates;
        }

        public static class RatingBean {
            /**
             * max : 10
             * average : 5.4
             * details : {"1":139,"3":682,"2":489,"5":47,"4":210}
             * stars : 30
             * min : 0
             */

            private int max;
            private double average;
            private DetailsBean details;
            private String stars;
            private int min;

            public int getMax() {
                return max;
            }

            public void setMax(int max) {
                this.max = max;
            }

            public double getAverage() {
                return average;
            }

            public void setAverage(double average) {
                this.average = average;
            }

            public DetailsBean getDetails() {
                return details;
            }

            public void setDetails(DetailsBean details) {
                this.details = details;
            }

            public String getStars() {
                return stars;
            }

            public void setStars(String stars) {
                this.stars = stars;
            }

            public int getMin() {
                return min;
            }

            public void setMin(int min) {
                this.min = min;
            }

            public static class DetailsBean {
                /**
                 * 1 : 139
                 * 3 : 682
                 * 2 : 489
                 * 5 : 47
                 * 4 : 210
                 */

                @SerializedName("1")
                private int _$1;
                @SerializedName("3")
                private int _$3;
                @SerializedName("2")
                private int _$2;
                @SerializedName("5")
                private int _$5;
                @SerializedName("4")
                private int _$4;

                public int get_$1() {
                    return _$1;
                }

                public void set_$1(int _$1) {
                    this._$1 = _$1;
                }

                public int get_$3() {
                    return _$3;
                }

                public void set_$3(int _$3) {
                    this._$3 = _$3;
                }

                public int get_$2() {
                    return _$2;
                }

                public void set_$2(int _$2) {
                    this._$2 = _$2;
                }

                public int get_$5() {
                    return _$5;
                }

                public void set_$5(int _$5) {
                    this._$5 = _$5;
                }

                public int get_$4() {
                    return _$4;
                }

                public void set_$4(int _$4) {
                    this._$4 = _$4;
                }
            }
        }

        public static class ImagesBean {
            /**
             * small : https://img3.doubanio.com/view/photo/s_ratio_poster/public/p2508618114.jpg
             * large : https://img3.doubanio.com/view/photo/s_ratio_poster/public/p2508618114.jpg
             * medium : https://img3.doubanio.com/view/photo/s_ratio_poster/public/p2508618114.jpg
             */

            private String small;
            private String large;
            private String medium;

            public String getSmall() {
                return small;
            }

            public void setSmall(String small) {
                this.small = small;
            }

            public String getLarge() {
                return large;
            }

            public void setLarge(String large) {
                this.large = large;
            }

            public String getMedium() {
                return medium;
            }

            public void setMedium(String medium) {
                this.medium = medium;
            }
        }

        public static class CastsBean {
            /**
             * avatars : {"small":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p53688.jpg","large":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p53688.jpg","medium":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p53688.jpg"}
             * name_en : Dylan O'Brien
             * name : 迪伦·奥布莱恩
             * alt : https://movie.douban.com/celebrity/1314963/
             * id : 1314963
             */

            private AvatarsBean avatars;
            private String name_en;
            private String name;
            private String alt;
            private String id;

            public AvatarsBean getAvatars() {
                return avatars;
            }

            public void setAvatars(AvatarsBean avatars) {
                this.avatars = avatars;
            }

            public String getName_en() {
                return name_en;
            }

            public void setName_en(String name_en) {
                this.name_en = name_en;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAlt() {
                return alt;
            }

            public void setAlt(String alt) {
                this.alt = alt;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public static class AvatarsBean {
                /**
                 * small : https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p53688.jpg
                 * large : https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p53688.jpg
                 * medium : https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p53688.jpg
                 */

                private String small;
                private String large;
                private String medium;

                public String getSmall() {
                    return small;
                }

                public void setSmall(String small) {
                    this.small = small;
                }

                public String getLarge() {
                    return large;
                }

                public void setLarge(String large) {
                    this.large = large;
                }

                public String getMedium() {
                    return medium;
                }

                public void setMedium(String medium) {
                    this.medium = medium;
                }
            }
        }

        public static class DirectorsBean {
            /**
             * avatars : {"small":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1417887954.94.jpg","large":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1417887954.94.jpg","medium":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1417887954.94.jpg"}
             * name_en : Wes Ball
             * name : 韦斯·鲍尔
             * alt : https://movie.douban.com/celebrity/1332723/
             * id : 1332723
             */

            private AvatarsBeanX avatars;
            private String name_en;
            private String name;
            private String alt;
            private String id;

            public AvatarsBeanX getAvatars() {
                return avatars;
            }

            public void setAvatars(AvatarsBeanX avatars) {
                this.avatars = avatars;
            }

            public String getName_en() {
                return name_en;
            }

            public void setName_en(String name_en) {
                this.name_en = name_en;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAlt() {
                return alt;
            }

            public void setAlt(String alt) {
                this.alt = alt;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public static class AvatarsBeanX {
                /**
                 * small : https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1417887954.94.jpg
                 * large : https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1417887954.94.jpg
                 * medium : https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1417887954.94.jpg
                 */

                private String small;
                private String large;
                private String medium;

                public String getSmall() {
                    return small;
                }

                public void setSmall(String small) {
                    this.small = small;
                }

                public String getLarge() {
                    return large;
                }

                public void setLarge(String large) {
                    this.large = large;
                }

                public String getMedium() {
                    return medium;
                }

                public void setMedium(String medium) {
                    this.medium = medium;
                }
            }
        }
    }
}
