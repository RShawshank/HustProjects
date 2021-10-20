package com.example.herve.Study.wediget.tablayout;

import android.view.View;

/**
 * Created           :Herve on 2016/10/27.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/10/27
 * @ projectName     :StudyApp
 * @ version
 */
public interface HerveTabLayoutAdapter {

    /*自定义的View*/
    View setItemTabView(int position);


    /*未选择的样式*/
    void setDraftStyle(View customView);


    /*选择状态的样式*/
    void setSelectStyle(View customView);

}
