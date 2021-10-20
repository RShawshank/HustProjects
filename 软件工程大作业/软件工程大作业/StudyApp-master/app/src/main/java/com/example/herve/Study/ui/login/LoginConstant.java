package com.example.herve.Study.ui.login;

import com.example.herve.Study.base.presenter.BasePresenter;
import com.example.herve.Study.base.presenter.BasePresenterView;

/**
 * Created           :Herve on 2016/11/2.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/11/2
 * @ projectName     :StudyApp
 * @ version
 */
public interface LoginConstant {

    interface Presenter extends BasePresenter {

        void login(String userName, String passWord);

    }

    interface PresenterView extends BasePresenterView {


    }
}