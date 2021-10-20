package com.example.herve.Study.base.presenter;

import android.os.Handler;
import android.os.Looper;

/**
 * Created           :Herve on 2016/10/10.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/10/10
 * @ projectName     :SquareDemo
 * @ version
 */
public abstract class MvpBasePresenter<V extends BasePresenterView> {

    protected V mPresenter;

    protected Handler mHandler;


    public MvpBasePresenter(V mPresenter) {
        this.mPresenter = mPresenter;

        mHandler = new Handler(Looper.getMainLooper());

    }

    public void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
    }

}
