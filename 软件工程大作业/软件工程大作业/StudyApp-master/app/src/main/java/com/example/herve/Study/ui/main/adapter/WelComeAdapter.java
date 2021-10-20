package com.example.herve.Study.ui.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bannerlibrary.BannerPagerAdapter;
import com.example.herve.Study.R;
import com.example.herve.Study.ui.home.HomeActivity;

import java.util.List;

/**
 * Created           :Herve on 2016/10/23.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/10/23
 * @ projectName     :BJXAPP
 * @ version
 */
public class WelComeAdapter extends BannerPagerAdapter {


    private Context mContext;

    private String TAG = getClass().getSimpleName();


    public WelComeAdapter(Context mContext, List data, boolean isLimited) {
        super(data, isLimited);
        this.mContext = mContext;
    }


    @Override
    public View setView(ViewGroup container, int position) {


        View itemView = LayoutInflater.from(mContext).inflate(R.layout.welcome_item, container, false);

        ImageView vpWelcome = (ImageView) itemView.findViewById(R.id.vp_welcome);
        CardView cvEnter = (CardView) itemView.findViewById(R.id.cv_enter);
        TextView tvEnterMain = (TextView) itemView.findViewById(R.id.tv_enter_main);

        if (position == mData.size() - 1) {
            cvEnter.setVisibility(View.VISIBLE);
            cvEnter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, HomeActivity.class);
                    mContext.startActivity(intent);
                }
            });
        } else {
            cvEnter.setVisibility(View.GONE);
        }

        vpWelcome.setImageResource(R.drawable.welcome);

        itemView.setClickable(false);


        return itemView;
    }


}
