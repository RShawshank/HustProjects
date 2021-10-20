package com.example.herve.Study.base.ui;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    protected BaseActivity mContext;
    protected View mRootView;
    protected Unbinder mUnbinder;
    protected String tittle;


    protected boolean mIsViewInitiated;
    protected boolean mIsVisibleToUser;
    protected boolean mIsDataInitiated;

    protected abstract int initLayoutId();

    protected abstract void findViewById();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();

    protected abstract void byUserReturnFragment();


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisibleToUser = isVisibleToUser;
        initFetchData();
    }

    protected void initFetchData() {
        if (mIsVisibleToUser && mIsViewInitiated) {
            if (!mIsDataInitiated) {
                initData();
                initListener();
                mIsDataInitiated = true;
            } else {
                byUserReturnFragment();
            }

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (BaseActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mIsViewInitiated = true;
        initFetchData();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(initLayoutId(), null, false);
        mUnbinder = ButterKnife.bind(this, mRootView);
        findViewById();
        initView();
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUserVisibleHint(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        setUserVisibleHint(false);
    }

    @Override
    public void onDestroyView() {
        mUnbinder.unbind();
        super.onDestroyView();
    }

    //获取状态栏的高度
    protected int getstatusBarHeight() {

        return statusBarHeight();
    }


    //获取屏幕的高度
    protected int getScreenHeight() {

        return screenHeight();
    }

    //获取屏幕的宽度
    protected int getScreenWidth() {

        return screenWidth();
    }

    protected void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    private int statusBarHeight() {
        Rect frame = new Rect();
        mContext.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    private int screenHeight() {

        return mContext.getWindow().getDecorView().getHeight();
    }

    private int screenWidth() {

        return mContext.getWindow().getDecorView().getWidth();
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    private View getContentView() {

        return mContext.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
    }
}