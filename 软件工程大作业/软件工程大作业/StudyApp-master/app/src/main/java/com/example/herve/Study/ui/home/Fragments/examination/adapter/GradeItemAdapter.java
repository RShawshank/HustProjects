package com.example.herve.Study.ui.home.Fragments.examination.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.herve.Study.R;
import com.example.herve.Study.bean.CurriculumBean;
import com.example.herve.Study.ui.curriculum.CurriculumActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created           :Herve on 2016/10/23.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/10/23
 * @ projectName     :BJXAPP
 * @ version
 */
abstract class GradeItemAdapter {


    private List<CurriculumBean> data;

    private Context mContext;
    private LinearLayout viewGroup;
    private String TAG = "测试点击";
    private String TAG2 = getClass().getSimpleName();


    public GradeItemAdapter(List<CurriculumBean> data, Context mContext) {
        this.data = data;
        this.mContext = mContext;

        viewGroup = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.ver_linearlayout, null);

        setView();
    }

    private void setView() {

        GradeItemGroupViewHolder gradeItemViewHolder = null;

        int last = data.size() % 3;


        for (int position = 0; position < data.size() - last; position++) {
            CurriculumBean curriculumBean = data.get(position);
            if (position % 3 == 0) {
                gradeItemViewHolder = new GradeItemGroupViewHolder();
                gradeItemViewHolder.ivCurriculumIcon01.setImageResource(R.drawable.ic_account_balance_wallet_black_24dp);
                gradeItemViewHolder.tvCurriculumName01.setText(curriculumBean.getCurriculumName());
                setClickListener(gradeItemViewHolder.llItemCurriculum01, position);
            }
            if (position % 3 == 1) {
                gradeItemViewHolder.ivCurriculumIcon02.setImageResource(R.drawable.ic_account_balance_wallet_black_24dp);
                gradeItemViewHolder.tvCurriculumName02.setText(curriculumBean.getCurriculumName());
                setClickListener(gradeItemViewHolder.llItemCurriculum02, position);


            }
            if (position % 3 == 2) {
                gradeItemViewHolder.ivCurriculumIcon03.setImageResource(R.drawable.ic_account_balance_wallet_black_24dp);
                gradeItemViewHolder.tvCurriculumName03.setText(curriculumBean.getCurriculumName());
                setClickListener(gradeItemViewHolder.llItemCurriculum03, position);

                viewGroup.addView(gradeItemViewHolder.itemView);
            }

        }

        if (last > 0) {
            gradeItemViewHolder = new GradeItemGroupViewHolder();

            for (int position = 0; position < last; position++) {
                int selectPosition = data.size() - last + position;
                CurriculumBean curriculumBean = data.get(selectPosition);
                if (position == 0) {
                    gradeItemViewHolder.ivCurriculumIcon01.setImageResource(R.drawable.ic_account_balance_wallet_black_24dp);
                    gradeItemViewHolder.tvCurriculumName01.setText(curriculumBean.getCurriculumName());
                    setClickListener(gradeItemViewHolder.llItemCurriculum01, selectPosition);

                }
                if (position == 1) {
                    gradeItemViewHolder.itemView.setTag(position);
                    gradeItemViewHolder.ivCurriculumIcon02.setImageResource(R.drawable.ic_account_balance_wallet_black_24dp);
                    gradeItemViewHolder.tvCurriculumName02.setText(curriculumBean.getCurriculumName());
                    setClickListener(gradeItemViewHolder.llItemCurriculum02, selectPosition);

                }

            }
            viewGroup.addView(gradeItemViewHolder.itemView);

        }

        getViews(viewGroup);
    }

    protected void setClickListener(View itemView, final int position) {

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.e(TAG, "onClick: position=" + position);
//                ((BaseActivity) mContext).showSnackToast(data.get(position).getCurriculumName());

                Intent intent = new Intent(mContext, CurriculumActivity.class);


                mContext.startActivity(intent);
            }
        });

    }


    protected abstract void getViews(LinearLayout viewGroup);


    public void setData(ArrayList<CurriculumBean> data) {
        this.data = data;

    }

    public class GradeItemGroupViewHolder {
        View itemView;
        @BindView(R.id.iv_curriculum_icon_01)
        ImageView ivCurriculumIcon01;
        @BindView(R.id.tv_curriculum_name_01)
        AppCompatTextView tvCurriculumName01;
        @BindView(R.id.iv_curriculum_icon_02)
        ImageView ivCurriculumIcon02;
        @BindView(R.id.tv_curriculum_name_02)
        AppCompatTextView tvCurriculumName02;
        @BindView(R.id.iv_curriculum_icon_03)
        ImageView ivCurriculumIcon03;
        @BindView(R.id.tv_curriculum_name_03)
        AppCompatTextView tvCurriculumName03;
        @BindView(R.id.ll_item_curriculum01)
        LinearLayout llItemCurriculum01;
        @BindView(R.id.ll_item_curriculum02)
        LinearLayout llItemCurriculum02;
        @BindView(R.id.ll_item_curriculum03)
        LinearLayout llItemCurriculum03;

        public GradeItemGroupViewHolder() {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_curriculum_layout, null, false);
            ButterKnife.bind(this, itemView);

        }
    }

}
