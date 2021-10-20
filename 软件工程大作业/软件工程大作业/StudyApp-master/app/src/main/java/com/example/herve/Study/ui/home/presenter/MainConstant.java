package com.example.herve.Study.ui.home.presenter;

import com.example.herve.Study.base.presenter.BasePresenter;
import com.example.herve.Study.base.presenter.BasePresenterView;

/**
 * Created           :Herve on 2016/10/23.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/10/23
 * @ projectName     :BaseApp
 * @ version
 */
public interface MainConstant {

    interface Presenter extends BasePresenter {
        void loadData();

        void refreshData();
    }

    interface PresenterView extends BasePresenterView {
    }
}
