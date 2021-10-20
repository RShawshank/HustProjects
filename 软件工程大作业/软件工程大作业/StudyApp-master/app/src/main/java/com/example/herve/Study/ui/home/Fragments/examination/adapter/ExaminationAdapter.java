package com.example.herve.Study.ui.home.Fragments.examination.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.herve.Study.R;
import com.example.herve.Study.bean.GradeBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created           :Herve on 2016/10/10.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/10/10
 * @ projectName     :SquareDemo
 * @ version
 */
public class ExaminationAdapter extends RecyclerView.Adapter<ExaminationAdapter.LifeViewHolder> {


    private Context mContext;

    ArrayList<GradeBean> data = new ArrayList<>();


    public ExaminationAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(ArrayList<GradeBean> data) {
        this.data = data;
    }

    @Override
    public LifeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_grade_layout, parent, false);

        return new LifeViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final LifeViewHolder holder, int position) {
        final GradeBean gradeBean = data.get(position);
        holder.tvGradeTitle.setText(gradeBean.getGradeName());

        if (holder.itemGroup.getChildCount() == 0) {
            GradeItemAdapter gradeItemAdapter = new GradeItemAdapter(gradeBean.getCurriculumBeans(), mContext) {
                @Override
                protected void getViews(LinearLayout viewGroup) {
                    holder.itemGroup.addView(viewGroup);
                }
            };
        }

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class ScrollGridLayoutManager extends GridLayoutManager {
        private boolean isScrollEnabled = false;

        public ScrollGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        public ScrollGridLayoutManager(Context context, int spanCount) {
            super(context, spanCount);
        }

        public ScrollGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
            super(context, spanCount, orientation, reverseLayout);
        }

        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollVertically() {
            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
            return isScrollEnabled && super.canScrollVertically();
        }
    }

    public class LifeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_grade_title)
        TextView tvGradeTitle;
        @BindView(R.id.view_line)
        View viewLine;
        @BindView(R.id.item_group)
        LinearLayout itemGroup;

        public LifeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
