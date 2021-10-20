package com.example.herve.Study.ui.home.Fragments.examination;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.herve.Study.R;
import com.example.herve.Study.base.ui.MvpBaseFragment;
import com.example.herve.Study.bean.GradeBean;
import com.example.herve.Study.ui.home.Fragments.examination.adapter.ExaminationAdapter;
import com.example.herve.Study.ui.home.Fragments.examination.presenter.ExaminationContract;
import com.example.herve.Study.ui.home.Fragments.examination.presenter.ExaminationPresenter;
import com.example.herve.Study.utils.fastjson.FastJsonParser;
import com.example.herve.Study.utils.string.StringUtils;

import java.util.ArrayList;

import butterknife.BindView;


/**
 * Created           :Herve on 2016/10/10.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/10/10
 * @ projectName     :SquareDemo
 * @ version
 */
public class ExaminationFragment extends MvpBaseFragment<ExaminationContract.Presenter> implements ExaminationContract.PresenterView {

    @BindView(R.id.recycle_view_life)
    RecyclerView recycleViewLife;


    public static ExaminationFragment newInstance() {
        ExaminationFragment fragment = new ExaminationFragment();
        return fragment;
    }

    public static ExaminationFragment newInstance(Bundle args) {
        ExaminationFragment fragment = new ExaminationFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void isShowDialog(boolean needShow) {

        mContext.showSuperDialog(needShow);

    }

    @Override
    public void success() {
        ExaminationAdapter lifeAdapter = new ExaminationAdapter(mContext);


        String jsonDta = StringUtils.getJson(mContext, "课程分类.json");


        ArrayList<GradeBean> data = (ArrayList<GradeBean>) FastJsonParser.getInstance().listFromJson(jsonDta, GradeBean.class);

        lifeAdapter.setData(data);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, 1);
        recycleViewLife.setLayoutManager(layoutManager);
        recycleViewLife.setAdapter(lifeAdapter);

    }


    @Override
    public void error(int errorCode) {
        showToast("加载失败！");
    }

    @Override
    protected ExaminationContract.Presenter initPresenter() {
        return new ExaminationPresenter(this);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_life_layout;
    }

    @Override
    protected void findViewById() {
        //if you using butterKnife you can doNothing
    }

    @Override
    protected void initView() {
        //you can set VIEW.setLayoutParams() at here;

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
