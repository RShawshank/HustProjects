package com.example.herve.Study.base.presenter;

/**
 * Created by Herve on 2016/10/10.
 */

public interface BasePresenterView {

    void isShowDialog(boolean needShow);

    void success();

    void error(int errorCode);
}
