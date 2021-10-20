package com.example.herve.Study.ui.home.presenter;

import com.example.herve.Study.base.presenter.MvpBasePresenter;

/**
 * Created           :Herve on 2016/10/23.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/10/23
 * @ projectName     :BaseApp
 * @ version
 */
public class MainPresenter extends MvpBasePresenter<MainConstant.PresenterView> implements MainConstant.Presenter {

    public MainPresenter(MainConstant.PresenterView mPresenter) {
        super(mPresenter);
    }

    @Override
    public void loadData() {
        mPresenter.isShowDialog(true);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.success();
                mPresenter.isShowDialog(false);
            }
        }, 200);


    }

    @Override
    public void refreshData() {

    }
}
