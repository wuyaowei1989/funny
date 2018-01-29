package com.android.funny.ui.sinavideo.contract;

import com.android.funny.bean.MoveListBean;
import com.android.funny.ui.base.BaseContract;

/**
 * Created by Administrator on 2018/1/27.
 */

public interface SinaVideoContract {
    interface View extends BaseContract.BaseView {
        void loadMoveList(MoveListBean listBean);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        /**
         * 获取上映电影
         */
        void getMoveList(String city, int start, int counter);
    }
}
