package com.example.herve.Study.ui.login.register;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.herve.Study.R;
import com.example.herve.Study.base.ui.MvpBaseActivity;
import com.example.herve.Study.bean.User;

import butterknife.BindView;

/**
 * Created           :Herve on 2016/11/2.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/11/2
 * @ projectName     :StudyApp
 * @ version
 */
public class RegisterActivity extends MvpBaseActivity<RegisterPresenter> implements RegisterConstant.PresenterView {


    @BindView(R.id.tv_username)
    AutoCompleteTextView tvUsername;
    @BindView(R.id.tl_username)
    TextInputLayout tlUsername;
    @BindView(R.id.tv_passWord)
    AutoCompleteTextView tvPassWord;
    @BindView(R.id.tl_passWord)
    TextInputLayout tlPassWord;
    @BindView(R.id.tv_sex)
    AutoCompleteTextView tvSex;
    @BindView(R.id.tl_sex)
    TextInputLayout tlSex;
    @BindView(R.id.btn_register)
    AppCompatButton btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
    }

    @Override
    public void isShowDialog(boolean needShow) {
        showSuperDialog(needShow);
    }

    @Override
    public void success() {

        finish();

    }

    @Override
    public void error(int errorCode) {

        Toast.makeText(mContext, "loginError", Toast.LENGTH_SHORT).show();


    }

    @Override
    protected RegisterPresenter initPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = tvUsername.getText().toString();
                String passWord = tvPassWord.getText().toString();

                if (TextUtils.isEmpty(userName)) {
                    tvUsername.setError(getString(R.string.error_invalid_email));
                    tvUsername.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(passWord) || passWord.length() < 4) {
                    tvPassWord.setError(getString(R.string.error_invalid_password));
                    tvPassWord.requestFocus();
                    return;
                }

                User user = new User(System.currentTimeMillis() + "", userName, passWord, "10", false);

                mPresenter.register(user);

            }
        });

    }

}