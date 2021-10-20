package com.example.herve.Study.ui.curriculum.score.presenter;

import com.example.herve.Study.base.presenter.MvpBasePresenter;
import com.example.herve.Study.bean.AnswerBean;
import com.example.herve.Study.bean.QuestionBean;
import com.example.herve.Study.common.AppConstant;

import java.util.List;

/**
 * Created           :Herve on 2016/11/20.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/11/20
 * @ projectName     :StudyApp
 * @ version
 */
public class ScorePresenter extends MvpBasePresenter<ScoreConstant.PresenterView> implements ScoreConstant.Presenter {

    public ScorePresenter(ScoreConstant.PresenterView mPresenter) {
        super(mPresenter);
    }

    @Override
    public void loading() {

        int score = 0;

        int questionSize = AppConstant.examinationPaperBean.getQuestionBeans().size();

        List<QuestionBean> questionBeans = AppConstant.examinationPaperBean.getQuestionBeans();
        List<AnswerBean> answerBeans = AppConstant.answerSheetBean.getAnswerBeans();

        for (int i = 0; i < questionSize; i++) {

            if (answerBeans.get(i).getSelectBeans().size() == 0) {
                answerBeans.get(i).setResult(0);
            } else if (!questionBeans.get(i).getAnswerKey().equals(answerBeans.get(i).getSelectBeans().get(0).getSelect())) {
                answerBeans.get(i).setResult(-1);
            } else if (questionBeans.get(i).getAnswerKey().equals(answerBeans.get(i).getSelectBeans().get(0).getSelect())) {
                answerBeans.get(i).setResult(1);
                score += questionBeans.get(i).getScore();
            }
        }

        AppConstant.answerSheetBean.setTotalPoints(score);


        mPresenter.success();

    }
}