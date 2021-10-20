package com.example.herve.Study.ui.home;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.example.herve.Study.R;
import com.example.herve.Study.base.ui.BaseFragment;
import com.example.herve.Study.base.ui.MvpBaseActivity;
import com.example.herve.Study.ui.home.Fragments.examination.ExaminationFragment;
import com.example.herve.Study.ui.home.Fragments.matters.MattersFragment;
import com.example.herve.Study.ui.home.adapter.FragmentsAdapter;
import com.example.herve.Study.ui.home.presenter.MainConstant;
import com.example.herve.Study.ui.home.presenter.MainPresenter;
import com.example.herve.Study.wediget.tablayout.HerveTabLayout;

import java.util.ArrayList;

import butterknife.BindView;

public class HomeActivity extends MvpBaseActivity<MainPresenter> implements MainConstant.PresenterView, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.tab_layout)
    HerveTabLayout tabLayout;
    @BindView(R.id.vp_home)
    ViewPager vpHome;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_home)
    RelativeLayout activityHome;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    String cacheName = "http://bj.bcebos.com/v1/tomato-dev/e8f42651-701e-11e6-9172-94de8065b233/e8f42651-701e-11e6-9172-94de8065b233.jpg";

    ImageView iv_navigation;
    private FragmentsAdapter fragmentsAdapter;
    private String TAG = getClass().getSimpleName();


    @Override
    protected int initLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void findViewById() {
        //if you using butterKnife you can doNothing
        View headerView = navView.getHeaderView(0);
        iv_navigation = (ImageView) headerView.findViewById(R.id.iv_navigation);

        iv_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Glide.with(mContext).load(cacheName)
                        .signature(new StringSignature(System.currentTimeMillis() + "50"))
                        .into(iv_navigation);

            }
        });


    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.drawer_home) {
            // Handle the camera action
        } else if (id == R.id.drawer_tables) {

        } else if (id == R.id.drawer_tables) {

        } else if (id == R.id.drawer_collection) {

        } else if (id == R.id.drawer_error) {

        } else if (id == R.id.drawer_nearly) {

        } else if (id == R.id.drawer_theme) {

        } else if (id == R.id.drawer_setting) {

        } else if (id == R.id.drawer_about) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void initView() {
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_setting, R.string.navigation_about);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    protected void initData() {
//        examinationPaperBeanDao= App.getApp().getDaoSession().e
        Glide.with(mContext).load(cacheName)
                .signature(new StringSignature(System.currentTimeMillis() + "20"))
                .into(iv_navigation);

        mPresenter.loadData();
    }

    @Override
    protected void initListener() {

        navView.setNavigationItemSelectedListener(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Log.i(TAG, "onClick: Snackbar的点击事件：" + "点击了");

                            }
                        }).show();
            }
        });


    }

    @Override
    protected MainPresenter initPresenter() {
        return new MainPresenter(this);
    }


    @Override
    public void isShowDialog(boolean needShow) {

        showSuperDialog(needShow);
    }

    @Override
    public void success() {
        fragmentsAdapter = new FragmentsAdapter(getSupportFragmentManager(), mContext);

        ArrayList<BaseFragment> data = new ArrayList<>();


        for (int i = 0; i < 2; i++) {
            BaseFragment baseFragment;
            if (i == 0) {
                baseFragment = new ExaminationFragment();
                baseFragment.setTittle(getResources().getString(R.string.home_fragment_examination));

            } else {
                baseFragment = new MattersFragment();
                baseFragment.setTittle(getResources().getString(R.string.home_fragment_matters));
            }
            data.add(baseFragment);
        }

        fragmentsAdapter.setData(data);

        vpHome.setOffscreenPageLimit(data.size());

        vpHome.setAdapter(fragmentsAdapter);

        tabLayout.setupWithViewPager(vpHome);


    }

    @Override
    public void error(int errorCode) {

    }


}
