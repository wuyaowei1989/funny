package com.android.funny.component;

import com.android.funny.ui.imageclassify.CarDetectFragment;
import com.android.funny.ui.imageclassify.ImageClassifyFragment;
import com.android.funny.ui.jandan.JdDetailFragment;
import com.android.funny.ui.news.ArticleReadActivity;
import com.android.funny.ui.news.ImageBrowseActivity;
import com.android.funny.ui.news.NewsFragment;
import com.android.funny.ui.video.DetailFragment;
import com.android.funny.ui.video.VideoFragment;

import dagger.Component;

/**
 * desc: .
 * author: Will .
 * date: 2017/9/2 .
 */
@Component(dependencies = ApplicationComponent.class)
public interface HttpComponent {

    void inject(VideoFragment videoFragment);

    void inject(DetailFragment detailFragment);

    void inject(JdDetailFragment jdDetailFragment);

    void inject(ImageBrowseActivity imageBrowseActivity);

    void inject( com.android.funny.ui.news.DetailFragment detailFragment);

    void inject(ArticleReadActivity articleReadActivity);

    void inject(NewsFragment newsFragment);

    void inject(ImageClassifyFragment imageClassifyFragment);

    void inject(CarDetectFragment carDetectFragment);
}
