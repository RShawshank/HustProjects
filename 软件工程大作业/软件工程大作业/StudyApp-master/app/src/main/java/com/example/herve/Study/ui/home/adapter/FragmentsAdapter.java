package com.example.herve.Study.ui.home.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.herve.Study.R;
import com.example.herve.Study.base.ui.BaseFragment;
import com.example.herve.Study.wediget.tablayout.HerveTabLayoutAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created           :Herve on 2016/10/10.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/10/10
 * @ projectName     :SquareDemo
 * @ version
 */
public class FragmentsAdapter extends FragmentPagerAdapter implements HerveTabLayoutAdapter {


    List<BaseFragment> data = new ArrayList<>();


    private final String TAG = getClass().getSimpleName();

    Context mContext;

    public FragmentsAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;

    }

    public void setData(List<BaseFragment> data) {
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {

        return data.get(position);
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return data.get(position).getTittle();
    }


    @Override
    public View setItemTabView(int position) {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.item_tab_layout, null);
        AppCompatTextView textView = (AppCompatTextView) viewGroup.findViewById(R.id.tv_tab_title);
        textView.setText(data.get(position).getTittle());

        return viewGroup;
    }


    @Override
    public void setDraftStyle(View customView) {
        TextView textView = (TextView) customView.findViewById(R.id.tv_tab_title);
        textView.setTextColor(mContext.getResources().getColor(R.color.grey_f5));

    }

    @Override
    public void setSelectStyle(View customView) {
        TextView textView = (TextView) customView.findViewById(R.id.tv_tab_title);
        textView.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
    }
}

