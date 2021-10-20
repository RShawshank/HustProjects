package com.example.herve.Study.base.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.herve.Study.base.presenter.MvpBasePresenter;

/**
 * Created by Herve on 2016/10/10.
 */

public abstract class MvpBaseActivity<P extends MvpBasePresenter> extends BaseActivity {

    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mPresenter = initPresenter();

        super.onCreate(savedInstanceState);
    }

    protected abstract P initPresenter();

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }
}
