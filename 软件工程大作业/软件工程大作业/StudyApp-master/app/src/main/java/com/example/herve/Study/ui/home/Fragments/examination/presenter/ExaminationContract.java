package com.example.herve.Study.ui.home.Fragments.examination.presenter;


import com.example.herve.Study.base.presenter.BasePresenter;
import com.example.herve.Study.base.presenter.BasePresenterView;

/**
 * Created           :Herve on 2016/10/10.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/10/10
 * @ projectName     :SquareDemo
 * @ version
 */
public interface ExaminationContract {


    interface Presenter extends BasePresenter {

        void loading();
    }

    interface PresenterView extends BasePresenterView {


    }
}
