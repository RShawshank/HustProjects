package com.example.herve.Study.ui.home.Fragments.examination.presenter;


import com.example.herve.Study.base.presenter.MvpBasePresenter;


/**
 * Created           :Herve on 2016/10/10.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/10/10
 * @ projectName     :SquareDemo
 * @ version
 */
public class ExaminationPresenter extends MvpBasePresenter<ExaminationContract.PresenterView> implements ExaminationContract.Presenter {


    public ExaminationPresenter(ExaminationContract.PresenterView mPresenter) {
        super(mPresenter);
    }


    @Override
    public void loading() {
        mPresenter.isShowDialog(true);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.isShowDialog(false);
                mPresenter.success();
            }
        }, 1000);

    }
}
