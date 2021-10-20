package com.example.herve.Study.ui.curriculum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.bannerlibrary.Banner;
import com.example.herve.Study.R;
import com.example.herve.Study.base.ui.MvpBaseActivity;
import com.example.herve.Study.bean.AnswerBean;
import com.example.herve.Study.bean.AnswerSheetBean;
import com.example.herve.Study.bean.ExaminationPaperBean;
import com.example.herve.Study.bean.QuestionBean;
import com.example.herve.Study.common.AppConstant;
import com.example.herve.Study.ui.curriculum.adapter.CurriculumBannerAdapter;
import com.example.herve.Study.ui.curriculum.presenter.CurriculumConstant;
import com.example.herve.Study.ui.curriculum.presenter.CurriculumPresenter;
import com.example.herve.Study.ui.curriculum.score.ScoreActivity;
import com.example.herve.Study.utils.fastjson.FastJsonParser;
import com.example.herve.Study.utils.string.StringUtils;

import java.util.List;

import butterknife.BindView;

/**
 * Created           :Herve on 2016/10/25.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/10/25
 * @ projectName     :StudyApp
 * @ version
 */
public class CurriculumActivity extends MvpBaseActivity<CurriculumPresenter> implements CurriculumConstant.PresenterView {


    @BindView(R.id.rv_curriculum)
    RecyclerView rvCurriculum;
    @BindView(R.id.activity_curriculum)
    RelativeLayout activityCurriculum;
    @BindView(R.id.banner_curriculum)
    Banner bannerCurriculum;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_bg)
    AppCompatImageView ivBg;

    private CurriculumBannerAdapter curriculumBannerAdapter;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_curriculum_tool_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.action_time_picker) {


            Intent intent = new Intent(mContext, ScoreActivity.class);

            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected CurriculumPresenter initPresenter() {
        return new CurriculumPresenter(this);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_curriculum;
    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void initView() {
        Glide.with(mContext).load(R.drawable.welcome).into(ivBg);
//        toolbar.setNavigationIcon(android.support.design.R.drawable.abc_ic_ab_back_material);
//        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar(); //显示返回箭头默认是不显示的
        actionbar.setDisplayHomeAsUpEnabled(true); //显示左侧的返回箭头，并且返回箭头和title一直设置返回箭头才能显示
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setDisplayUseLogoEnabled(true); //显示标题
        actionbar.setDisplayShowTitleEnabled(true);


    }

    @Override
    protected void initData() {
        mPresenter.loading();

    }

    @Override
    protected void initListener() {

    }

    @Override
    public void isShowDialog(boolean needShow) {
        showSuperDialog(needShow);
    }

    @Override
    public void success() {


        String jsonDta = StringUtils.getJson(mContext, "考卷.json");


        ExaminationPaperBean data = FastJsonParser.getInstance().fromJson(jsonDta, ExaminationPaperBean.class);

        if (AppConstant.answerSheetBean == null) {
            AppConstant.answerSheetBean = new AnswerSheetBean();
            for (int i = 0; i < data.getQuestionBeans().size(); i++) {
                AppConstant.answerSheetBean.getAnswerBeans().add(new AnswerBean());
            }
        }
        AppConstant.examinationPaperBean = data;

        curriculumBannerAdapter = new CurriculumBannerAdapter(mContext, data.getQuestionBeans(), false);

        bannerCurriculum.setDot(R.drawable.btn_radio_on_holo_dark, R.drawable.btn_radio_on_disabled_holo_dark);
        bannerCurriculum.setLimited(false);
        bannerCurriculum.canAuto(false);
        bannerCurriculum.showDot(false);
        bannerCurriculum.setAdapter(curriculumBannerAdapter);

    }

    @Override
    public void error(int errorCode) {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
    }
}