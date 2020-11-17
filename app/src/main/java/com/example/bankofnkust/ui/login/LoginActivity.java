package com.example.bankofnkust.ui.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bankofnkust.MainActivity;
import com.example.bankofnkust.R;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    private EditText emailEditText;
    private EditText passwordEditText;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginViewModel =
                ViewModelProviders.of(this).get(LoginViewModel.class);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        final ImageView emailClearImageView = findViewById(R.id.emailClearImageView);
        final ImageView passwordClearImageView = findViewById(R.id.passwordClearImageView);
        final Button loginButton = findViewById(R.id.loginButton);
        final TextView registerText = findViewById(R.id.registerText);
        final ProgressBar loadingProgressBar = findViewById(R.id.loadingProgressBar);
        checkBox = findViewById(R.id.checkBox);

        // 观察表单数据变化
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());// 设置按钮状态
                if (loginFormState.getEmailError() != null) {// 邮箱错误
                    emailEditText.setError(getString(loginFormState.getEmailError()));
                }
                if (loginFormState.getPasswordError() != null) {// 密码错误
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        // 观察登录结果变化
        loginViewModel.getLoginStatus().observe(this, new Observer<LoginStatus>() {
            @Override
            public void onChanged(@Nullable LoginStatus loginStatus) {
                if (loginStatus == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);// 关闭loading动画
                if (!loginStatus.isLoggedIn()) {// 登录失败
                    showLoginFailed();// 显示登录失败消息
                }
                if (loginStatus.isLoggedIn()) {// 登录成功
                    if (checkBox.isChecked()) {
                        saveData(emailEditText.getText().toString(), passwordEditText.getText().toString(), true);
                    } else {
                        saveData("","",false);
                    }
                    updateUiWithUser();
                    finish();
                }
            }
        });

        //  观察输入框变化
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }
            @Override
            public void afterTextChanged(Editable s) {
                // 设置表单数据的变化，通知观察者
                loginViewModel.loginDataChanged(emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };

        emailEditText.addTextChangedListener(afterTextChangedListener);// 注册文本变化监听器
        passwordEditText.addTextChangedListener(afterTextChangedListener);// 注册文本变化监听器

        // 清除按钮
        emailClearImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailEditText.setText("");
            }
        });
        passwordClearImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordEditText.setText("");
            }
        });


        //  登录按钮事件
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });

        //  注册按钮事件
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), getString(R.string.under_construction), Toast.LENGTH_SHORT).show();
            }
        });

        loadData();

    }

    //  儲存sharedpreferences
    private void saveData(String email, String password, boolean isChecked) {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putBoolean("checkbox", isChecked);
        editor.apply();
    }

    //  讀取sharedpreferences
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        String password = sharedPreferences.getString("password", "");
        boolean isChecked = sharedPreferences.getBoolean("checkbox", false);
        if (isChecked) {
            emailEditText.setText(email);
            passwordEditText.setText(password);
            checkBox.setChecked(true);
        }
    }

    //  处理用户登录成功
    private void updateUiWithUser() {
        Toast.makeText(getApplicationContext(), getString(R.string.login_success) , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //  处理用户登录失败
    private void showLoginFailed() {
        Toast.makeText(getApplicationContext(), getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
    }

}
