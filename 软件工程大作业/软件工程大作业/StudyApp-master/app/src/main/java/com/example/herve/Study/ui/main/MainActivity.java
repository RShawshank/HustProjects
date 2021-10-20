package com.example.herve.Study.ui.main;

import android.widget.RelativeLayout;

import com.example.bannerlibrary.Banner;
import com.example.herve.Study.R;
import com.example.herve.Study.base.ui.BaseActivity;
import com.example.herve.Study.ui.main.adapter.WelComeAdapter;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends BaseActivity {


    @BindView(R.id.banner_main)
    Banner bannerMain;
    @BindView(R.id.activity_main)
    RelativeLayout activityMain;


    @Override
    protected int initLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

        final ArrayList<String> data = new ArrayList<>();
        data.add("01");
        data.add("02");
        WelComeAdapter adapter = new WelComeAdapter(mContext, data, false);

        bannerMain.setDot(R.drawable.btn_radio_on_holo_dark, R.drawable.btn_radio_on_disabled_holo_dark);
        bannerMain.setDotGravity(Banner.CENTER);
        bannerMain.setLimited(false);
        bannerMain.canAuto(false);
        bannerMain.setAdapter(adapter);

    }

    @Override
    protected void initListener() {

    }


}
