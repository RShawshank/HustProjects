package com.example.herve.Study.ui.home.Fragments.matters.presenter;


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
public class MattersPresenter extends MvpBasePresenter<MattersContract.PresenterView> implements MattersContract.Presenter {


    public MattersPresenter(MattersContract.PresenterView mPresenter) {
        super(mPresenter);
    }

    private void initData() {

        loading();

    }


    @Override
    public void loading() {
        mPresenter.isShowDialog(true);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.success();
                mPresenter.isShowDialog(false);
            }
        }, 2000);

    }
}
