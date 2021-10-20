package com.example.herve.Study.ui.home.Fragments.matters;


import android.os.Bundle;

import com.example.herve.Study.R;
import com.example.herve.Study.base.ui.MvpBaseFragment;
import com.example.herve.Study.ui.home.Fragments.matters.presenter.MattersContract;
import com.example.herve.Study.ui.home.Fragments.matters.presenter.MattersPresenter;

/**
 * Created           :Herve on 2016/10/10.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/10/10
 * @ projectName     :SquareDemo
 * @ version
 */
public class MattersFragment extends MvpBaseFragment<MattersContract.Presenter> implements MattersContract.PresenterView {


    public static MattersFragment newInstance() {
        MattersFragment fragment = new MattersFragment();
        return fragment;
    }

    public static MattersFragment newInstance(Bundle args) {
        MattersFragment fragment = new MattersFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void isShowDialog(boolean needShow) {
        mContext.showSuperDialog(needShow);

    }

    @Override
    public void success() {

    }

    @Override
    public void error(int errorCode) {
        showToast("加载失败");

    }


    @Override
    protected MattersContract.Presenter initPresenter() {
        return new MattersPresenter(this);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_work_layout;
    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mPresenter.loading();

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void byUserReturnFragment() {

    }

}
