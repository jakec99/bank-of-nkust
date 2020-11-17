package com.example.bankofnkust.ui.login;

import androidx.annotation.Nullable;

public class LoginFormState {

    @Nullable
    private Integer emailError;// 用户名错误
    @Nullable
    private Integer passwordError;// 密码错误
    // 我们用这个字段来控制登录按钮的状态
    private boolean isDataValid;

    LoginFormState(@Nullable Integer emailError, @Nullable Integer passwordError) {
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.isDataValid = false;
    }

    LoginFormState(boolean isDataValid) {
        this.emailError = null;
        this.passwordError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getEmailError() {
        return emailError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    boolean isDataValid() {
        return isDataValid;
    }

}
