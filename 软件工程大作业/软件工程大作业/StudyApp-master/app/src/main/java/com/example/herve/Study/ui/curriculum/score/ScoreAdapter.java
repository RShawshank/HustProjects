package com.example.herve.Study.ui.curriculum.score;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.herve.Study.R;
import com.example.herve.Study.base.ui.BaseActivity;
import com.example.herve.Study.bean.AnswerBean;
import com.example.herve.Study.bean.QuestionBean;
import com.example.herve.Study.wediget.recycleview.HeadFootBaseAdapter;

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
public class ScoreAdapter extends HeadFootBaseAdapter<ScoreAdapter.ScoreViewHolder, AnswerBean> {


    public ScoreAdapter(Activity mContext) {
        super(mContext);
    }


    @Override
    protected ScoreViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_score_layout, parent, false);

        return new ScoreViewHolder(itemView);
    }

    @Override
    protected void onBindItemViewHolder(final ScoreViewHolder holder, final int position) {

        int result = data.get(position).getResult();

        if (result == -1) {
            holder.cvItemAnswer.setCardBackgroundColor(mContext.getResources().getColor(R.color.red_error));

        } else if (result == 0) {
            holder.cvItemAnswer.setCardBackgroundColor(mContext.getResources().getColor(R.color.white));

        } else if (result == 1) {
            holder.cvItemAnswer.setCardBackgroundColor(mContext.getResources().getColor(R.color.green_14e715));

        }

        holder.tvQuestionId.setText(position + 1 + "");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(v, position, data.get(position));
                }
            }
        });

    }

    class ScoreViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_question_id)
        TextView tvQuestionId;
        @BindView(R.id.cv_item_answer)
        CardView cvItemAnswer;

        public ScoreViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {

        void onItemClickListener(View view, int position, AnswerBean answerBean);
    }
}