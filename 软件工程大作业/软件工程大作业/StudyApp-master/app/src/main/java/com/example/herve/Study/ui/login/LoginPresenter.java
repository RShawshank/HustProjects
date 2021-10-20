package com.example.herve.Study.ui.login;

import com.example.herve.Study.app.App;
import com.example.herve.Study.base.presenter.MvpBasePresenter;
import com.example.herve.Study.bean.User;
import com.example.herve.Study.greendao.dao.UserDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created           :Herve on 2016/11/2.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/11/2
 * @ projectName     :StudyApp
 * @ version
 */
public class LoginPresenter extends MvpBasePresenter<LoginConstant.PresenterView> implements LoginConstant.Presenter {


    public LoginPresenter(LoginConstant.PresenterView mPresenter) {
        super(mPresenter);
    }

    @Override
    public void login(final String userName, final String passWord) {


        mPresenter.isShowDialog(true);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                UserDao userDao = App.getApp().getDaoSession().getUserDao();

                QueryBuilder<User> queryBuilder = userDao.queryBuilder();

                List<User> users = queryBuilder.where(UserDao.Properties.UserName.eq(userName))
                        .where(UserDao.Properties.PassWord.eq(passWord))
                        .list();

                if (users.size() > 0) {
                    mPresenter.success();
                } else {
                    mPresenter.error(0);
                }
                mPresenter.isShowDialog(false);

            }
        }, 2000);

    }
}