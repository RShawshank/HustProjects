package com.example.herve.Study.ui.home.Fragments.matters.presenter;


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
public interface MattersContract {


    interface Presenter extends BasePresenter {

        void loading();
    }

    interface PresenterView extends BasePresenterView {


    }
}
