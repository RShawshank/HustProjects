package com.example.herve.Study.ui.curriculum.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.bannerlibrary.BannerPagerAdapter;
import com.example.herve.Study.R;
import com.example.herve.Study.bean.AnswerBean;
import com.example.herve.Study.bean.QuestionBean;
import com.example.herve.Study.bean.SelectBean;
import com.example.herve.Study.common.AppConstant;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created           :Herve on 2016/10/30.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/10/30
 * @ projectName     :StudyApp
 * @ version
 */
public class CurriculumBannerAdapter extends BannerPagerAdapter<QuestionBean> {

    private Context mContext;


    public CurriculumBannerAdapter(Context mContext, List<QuestionBean> data, boolean isLimited) {
        super(data, isLimited);
        this.mContext = mContext;
    }

    @Override
    public View setView(ViewGroup container, final int position) {

        AnswerBean answerBean = AppConstant.answerSheetBean.getAnswerBeans().get(position);

        QuestionBean questionBean = mData.get(position);

        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_banner_question_layout, null);


        AppCompatCheckedTextView tvQuestion = (AppCompatCheckedTextView) itemView.findViewById(R.id.tv_question);
        final LinearLayout llSelectA = (LinearLayout) itemView.findViewById(R.id.ll_select_a);
        AppCompatCheckedTextView tvSelectTabA = (AppCompatCheckedTextView) itemView.findViewById(R.id.tv_select_tab_a);
        AppCompatCheckedTextView tvSelectA = (AppCompatCheckedTextView) itemView.findViewById(R.id.tv_select_a);
        final LinearLayout llSelectB = (LinearLayout) itemView.findViewById(R.id.ll_select_b);
        AppCompatCheckedTextView tvSelectTabB = (AppCompatCheckedTextView) itemView.findViewById(R.id.tv_select_tab_b);
        AppCompatCheckedTextView tvSelectB = (AppCompatCheckedTextView) itemView.findViewById(R.id.tv_select_b);
        final LinearLayout llSelectC = (LinearLayout) itemView.findViewById(R.id.ll_select_c);
        AppCompatCheckedTextView tvSelectTabC = (AppCompatCheckedTextView) itemView.findViewById(R.id.tv_select_tab_c);
        AppCompatCheckedTextView tvSelectC = (AppCompatCheckedTextView) itemView.findViewById(R.id.tv_select_c);
        final LinearLayout llSelectD = (LinearLayout) itemView.findViewById(R.id.ll_select_d);
        AppCompatCheckedTextView tvSelectTabD = (AppCompatCheckedTextView) itemView.findViewById(R.id.tv_select_tab_d);
        AppCompatCheckedTextView tvSelectD = (AppCompatCheckedTextView) itemView.findViewById(R.id.tv_select_d);
        AppCompatImageView ivSolution = (AppCompatImageView) itemView.findViewById(R.id.iv_solution);
        AppCompatCheckedTextView tvSolutionExp = (AppCompatCheckedTextView) itemView.findViewById(R.id.tv_solution_exp);
        AppCompatCheckedTextView tvSolution = (AppCompatCheckedTextView) itemView.findViewById(R.id.tv_solution);


        tvQuestion.setText(questionBean.getQuestion());


        Observable.just(answerBean)
                .filter(new Func1<AnswerBean, Boolean>() {
                    @Override
                    public Boolean call(AnswerBean answerBean) {
                        return answerBean != null && answerBean.getSelectBeans() != null && answerBean.getSelectBeans().size() > 0;
                    }
                }).flatMap(new Func1<AnswerBean, Observable<SelectBean>>() {
            @Override
            public Observable<SelectBean> call(AnswerBean answerBean) {
                return Observable.from(answerBean.getSelectBeans());
            }
        }).map(new Func1<SelectBean, String>() {
            @Override
            public String call(SelectBean selectBean) {
                return selectBean.getSelect();
            }
        }).filter(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {

                return s != null;
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {

                if (s.equals("A")) {
                    llSelectA.setBackgroundColor(Color.RED);
                }
                if (s.equals("B")) {
                    llSelectB.setBackgroundColor(Color.RED);
                }
                if (s.equals("C")) {
                    llSelectC.setBackgroundColor(Color.RED);
                }
                if (s.equals("D")) {
                    llSelectD.setBackgroundColor(Color.RED);
                }
            }
        });

        for (int i = 0; i < questionBean.getSelectBeans().size(); i++) {
            SelectBean selectBean = questionBean.getSelectBeans().get(i);
            if (i == 0) {
                tvSelectA.setText(selectBean.getSelectString());
            }
            if (i == 1) {
                tvSelectB.setText(selectBean.getSelectString());
            }
            if (i == 2) {
                tvSelectC.setText(selectBean.getSelectString());
            }
            if (i == 3) {
                tvSelectD.setText(selectBean.getSelectString());
            }
        }

        setTag(llSelectA, "A");
        setTag(llSelectB, "B");
        setTag(llSelectC, "C");
        setTag(llSelectD, "D");

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                llSelectA.setBackgroundColor(Color.TRANSPARENT);
                llSelectB.setBackgroundColor(Color.TRANSPARENT);
                llSelectC.setBackgroundColor(Color.TRANSPARENT);
                llSelectD.setBackgroundColor(Color.TRANSPARENT);
                view.setBackgroundColor(Color.RED);

                SelectBean selectBean = new SelectBean();
                selectBean.setSelect((String) view.getTag());

                AppConstant.answerSheetBean.getAnswerBeans().get(position).getSelectBeans().clear();
                AppConstant.answerSheetBean.getAnswerBeans().get(position).getSelectBeans().add(selectBean);

                boolean result = goNextPager();
                if (!result) {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.complete_tips), Toast.LENGTH_SHORT).show();
                }
            }
        };

        llSelectA.setOnClickListener(onClickListener);
        llSelectB.setOnClickListener(onClickListener);
        llSelectC.setOnClickListener(onClickListener);
        llSelectD.setOnClickListener(onClickListener);

        return itemView;
    }


    private void setTag(View selectView, String tag) {
        selectView.setTag(tag);
    }

}
