package com.example.herve.Study.wediget.tablayout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created           :Herve on 2016/10/27.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/10/27
 * @ projectName     :HerveTabLayout
 * @ version
 */
public class HerveTabLayout extends TabLayout {
    private String TAG = getClass().getSimpleName();


    private int dmw = 0;
    private int dmh = 0;

    private Context mContext;
    private ViewPager mViewPager;
    private LinearLayout linearLayout;
    private HerveTabLayoutAdapter tabLayoutAdapter;


    public HerveTabLayout(Context context) {
        this(context, null);
    }

    public HerveTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HerveTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    @Override
    public void setupWithViewPager(@Nullable ViewPager viewPager) {
        this.setupWithViewPager(viewPager, true);

    }

    @Override
    public void setupWithViewPager(@Nullable ViewPager viewPager, boolean autoRefresh) {
        super.setupWithViewPager(viewPager, autoRefresh);
        mViewPager = viewPager;


        setViewSize();

        reSetView();
    }

    private void reSetView() {


        if (mViewPager.getAdapter() instanceof HerveTabLayoutAdapter) {

            tabLayoutAdapter = (HerveTabLayoutAdapter) mViewPager.getAdapter();
        } else {
            Log.e(TAG, "reSetView: you can let you FragmentAdapter implements TanLayoutAdapter set you customLayout");

            return;
        }


        for (int position = 0; position < getTabCount(); position++) {
            TabLayout.Tab tab = getTabAt(position);
            if (tab != null) {
                tab.setCustomView(tabLayoutAdapter.setItemTabView(position));
                if (tab.getCustomView() != null) {
                    View tabView = (View) tab.getCustomView().getParent();
                    tabView.setTag(position);
                    if (position == 0) {
                        tabLayoutAdapter.setSelectStyle(tab.getCustomView());
                    }
                    tabView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            mViewPager.setCurrentItem((Integer) view.getTag());
                        }
                    });
                }
            }
        }


        initSelectedListener();

    }

    private void initSelectedListener() {


        addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                tabLayoutAdapter.setSelectStyle(tab.getCustomView());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabLayoutAdapter.setDraftStyle(tab.getCustomView());


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {


            }
        });
    }

    private void initView(Context context) {
        this.mContext = context;

        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();

        dmh = displayMetrics.heightPixels;
        dmw = displayMetrics.widthPixels;

    }


    private void setViewSize() {

        linearLayout = (LinearLayout) getChildAt(0);

        int totalWidth = getViewWidth(this);

        if (totalWidth < dmw) {

            for (int i = 0; i < mViewPager.getAdapter().getCount(); i++) {

                View itemView = linearLayout.getChildAt(i);

                int itemWidth = getViewWidth(itemView);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) itemView.getLayoutParams();
                int wid = dmw * itemWidth / totalWidth;

                layoutParams.width = wid;

                itemView.setLayoutParams(layoutParams);

            }
        }

    }

    private int getViewWidth(View view) {

        int w = MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED);
        int h = MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED);

        view.measure(w, h);

        int height = view.getMeasuredHeight();
        int width = view.getMeasuredWidth();


        return width;

    }
}
