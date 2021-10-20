package com.example.herve.Study.ui.login.register;

import com.example.herve.Study.app.App;
import com.example.herve.Study.base.presenter.MvpBasePresenter;
import com.example.herve.Study.bean.User;
import com.example.herve.Study.greendao.dao.UserDao;

/**
 * Created           :Herve on 2016/11/2.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/11/2
 * @ projectName     :StudyApp
 * @ version
 */
public class RegisterPresenter extends MvpBasePresenter<RegisterConstant.PresenterView> implements RegisterConstant.Presenter {


    public RegisterPresenter(RegisterConstant.PresenterView mPresenter) {
        super(mPresenter);
    }

    @Override
    public void register(User user) {
        UserDao userDao = App.getApp().getDaoSession().getUserDao();

        userDao.insert(user);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                mPresenter.success();

            }
        }, 2000);

    }
}