package com.example.bankofnkust.ui.login;


import android.content.SharedPreferences;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bankofnkust.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginViewModel extends ViewModel {

    private FirebaseAuth mAuth;

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginStatus> loginStatus = new MutableLiveData<>();
    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }
    LiveData<LoginStatus> getLoginStatus() { return loginStatus; }

    //  处理用户登录
    public void login(String email, String password) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loginStatus.setValue(new LoginStatus(true));
                        } else {
                            loginStatus.setValue(new LoginStatus(false));
                        }
                    }
                });
    }

    //  处理表单数据变化的
    public void loginDataChanged(String email, String password) {
        if (!isEmailValid(email)) {// 验证用户名的有效性
            loginFormState.setValue(new LoginFormState(R.string.invalid_email, null));
        } else if (!isPasswordValid(password)) {// 验证密码的有效性
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {// 邮箱和密码都有效
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder email validation check
    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        if (email.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } else {
            return !email.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

}
