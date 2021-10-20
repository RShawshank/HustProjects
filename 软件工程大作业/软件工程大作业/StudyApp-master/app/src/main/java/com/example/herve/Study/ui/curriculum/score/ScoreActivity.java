package com.example.herve.Study.ui.curriculum.score;

import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewStub;

import com.example.herve.Study.R;
import com.example.herve.Study.base.ui.MvpBaseActivity;
import com.example.herve.Study.bean.AnswerBean;
import com.example.herve.Study.bean.QuestionBean;
import com.example.herve.Study.common.AppConstant;
import com.example.herve.Study.ui.curriculum.score.presenter.ScoreConstant;
import com.example.herve.Study.ui.curriculum.score.presenter.ScorePresenter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created           :Herve on 2016/11/20.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/11/20
 * @ projectName     :StudyApp
 * @ version
 */
public class ScoreActivity extends MvpBaseActivity<ScorePresenter> implements ScoreConstant.PresenterView {

    @BindView(R.id.tv_score_title)
    AppCompatTextView tvScoreTitle;
    @BindView(R.id.cv_score_title)
    CardView cvScoreTitle;
    @BindView(R.id.rv_score_detail)
    RecyclerView rvScoreDetail;
    @BindView(R.id.btn_sure)
    AppCompatButton btnSure;
    @BindView(R.id.tv_item_title)
    AppCompatTextView tvItemTitle;
    @BindView(R.id.tv_item_detail)
    AppCompatTextView tvItemDetail;
    @BindView(R.id.stub_import_question)
    ViewStub stubImportQuestion;

    private View questionDetailView;
    /**
     * 数据
     */
    private ScoreAdapter scoreAdapter;

    @Override
    public void isShowDialog(boolean needShow) {

        showSuperDialog(false);
    }

    @Override
    public void success() {

        scoreAdapter = new ScoreAdapter(mContext);
        scoreAdapter.setData((ArrayList<AnswerBean>) AppConstant.answerSheetBean.getAnswerBeans());

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, 5, RecyclerView.VERTICAL, false);

        rvScoreDetail.setLayoutManager(layoutManager);

        rvScoreDetail.setAdapter(scoreAdapter);

        tvItemDetail.setText(AppConstant.answerSheetBean.getTotalPoints() + "");

    }


    public void showQuestion(AnswerBean answerBean) {

        if (questionDetailView == null) {
            questionDetailView = stubImportQuestion.inflate();
            questionDetailView.bringToFront();
        }
        questionDetailView.setVisibility(View.VISIBLE);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (questionDetailView != null && questionDetailView.isShown()) {
                questionDetailView.setVisibility(View.GONE);
                return false;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void error(int errorCode) {

    }

    @Override
    protected ScorePresenter initPresenter() {
        return new ScorePresenter(this);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_score;
    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

        showSuperDialog(false);

        mPresenter.loading();

    }

    @Override
    protected void initListener() {

        scoreAdapter.setOnItemClickListener(new ScoreAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position, AnswerBean answerBean) {

                showQuestion(answerBean);

            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}