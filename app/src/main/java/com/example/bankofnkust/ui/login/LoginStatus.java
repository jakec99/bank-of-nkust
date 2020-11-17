package com.example.bankofnkust.ui.login;

import androidx.lifecycle.LiveData;

public class LoginStatus {

    private boolean isLoggedIn;

    public LoginStatus(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

}
