package com.example.herve.Study.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.herve.Study.R;
import com.example.herve.Study.base.ui.MvpBaseActivity;
import com.example.herve.Study.ui.home.HomeActivity;
import com.example.herve.Study.ui.login.register.RegisterActivity;

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
public class LoginActivity extends MvpBaseActivity<LoginPresenter> implements LoginConstant.PresenterView {


    @BindView(R.id.login_progress)
    ProgressBar loginProgress;
    @BindView(R.id.tv_email)
    AutoCompleteTextView tvEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_sign_in)
    Button btnEmailSignInButton;
    @BindView(R.id.ll_email_login_form)
    LinearLayout llEmailLoginForm;
    @BindView(R.id.sc_login_form)
    ScrollView scLoginForm;
    @BindView(R.id.btn_register)
    Button btnRegister;

    @Override
    public void isShowDialog(boolean needShow) {

        showSuperDialog(needShow);
    }

    @Override
    public void success() {

        Intent intent = new Intent(mContext, HomeActivity.class);

        startActivity(intent);


        Toast.makeText(mContext, "loginSuccess", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void error(int errorCode) {

        Toast.makeText(mContext, "loginError", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected LoginPresenter initPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_login;
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
        btnEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = tvEmail.getText().toString();
                String passWord = etPassword.getText().toString();

                if (TextUtils.isEmpty(userName)) {
                    tvEmail.setError(getString(R.string.error_invalid_email));
                    tvEmail.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(passWord) || passWord.length() < 4) {
                    etPassword.setError(getString(R.string.error_invalid_password));
                    etPassword.requestFocus();
                    return;
                }

                mPresenter.login(userName, passWord);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goActivity(RegisterActivity.class);


            }
        });

    }


    public void goActivity(Class<?> cls) {

        Intent intent = new Intent(mContext, cls);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
    }
}