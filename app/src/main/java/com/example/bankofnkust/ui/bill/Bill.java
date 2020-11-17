package com.example.bankofnkust.ui.bill;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.sql.Timestamp;
import java.util.Date;

@IgnoreExtraProperties
public class Bill {

    private String title;
    private String value;
    private @ServerTimestamp Date date;
    private Long balance;

    public Bill() {
        //empty constructor needed
    }

    public Bill(String title, String value, Date date, Long balance) {
        this.title = title;
        this.value = value;
        this.date = date;
        this.balance = balance;
    }

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }

    public Date getDate() {
        return date;
    }

    public Long getBalance() {
        return balance;
    }

}
