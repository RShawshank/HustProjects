package com.example.herve.Study.ui.login.register;

import com.example.herve.Study.base.presenter.BasePresenter;
import com.example.herve.Study.base.presenter.BasePresenterView;
import com.example.herve.Study.bean.User;

/**
 * Created           :Herve on 2016/11/2.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/11/2
 * @ projectName     :StudyApp
 * @ version
 */
public interface RegisterConstant {

    interface Presenter extends BasePresenter {

        void register(User user);

    }

    interface PresenterView extends BasePresenterView {


    }
}