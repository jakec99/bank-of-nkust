package com.example.bankofnkust.ui.deposit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bankofnkust.R;
import com.example.bankofnkust.ui.code.CodeDialog;
import com.example.bankofnkust.ui.login.LoginActivity;

public class DepositActivity extends AppCompatActivity implements CodeDialog.CodeDialogListener {

    private DepositViewModel depositViewModel;

    private static Long amount;
    private Button depositButton;
    private EditText amountEditText;
    private ProgressBar depositProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        depositViewModel =
                ViewModelProviders.of(this).get(DepositViewModel.class);

        initChannels(this.getApplicationContext());

        amountEditText = findViewById(R.id.amountEditText);
        depositButton = findViewById(R.id.depositButton);
        depositProgressBar = findViewById(R.id.depositProgressBar);

        //  监听储值
        depositViewModel.getDepositStatus().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if  (s.equals("success")){
                    Toast.makeText(getApplicationContext(), getString(R.string.success_deposit), Toast.LENGTH_SHORT).show();
                    showNotification();
                    finish();
                } else if (s.equals("fail_code")){
                    Toast.makeText(getApplicationContext(), getString(R.string.fail_code), Toast.LENGTH_SHORT).show();
                } else if (s.equals("fail_login")){
                    Toast.makeText(getApplicationContext(), getString(R.string.login_lost), Toast.LENGTH_SHORT).show();
                    exit();
                } else if (s.equals("fail_firebase")){
                    Toast.makeText(getApplicationContext(), getString(R.string.fail_firebase), Toast.LENGTH_SHORT).show();
                } else if (s.equals("fail_firebase_file")){
                    Toast.makeText(getApplicationContext(), getString(R.string.fail_firebase_file), Toast.LENGTH_SHORT).show();
                } else if (s.equals("fail_card_balance")){
                    Toast.makeText(getApplicationContext(), getString(R.string.fail_card_balance), Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), getString(R.string.fail_deposit), Toast.LENGTH_SHORT).show();
                }
                depositProgressBar.setVisibility(View.GONE);
                depositButton.setEnabled(true);
                amountEditText.setEnabled(true);
            }
        });

        //  储值按钮事件
        depositButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = Long.parseLong(amountEditText.getText().toString());
                pickCode();
            }
        });
    }

    private void pickCode() {
        CodeDialog codeDialog = new CodeDialog();
        codeDialog.show(getSupportFragmentManager(), "code dialog");
    }

    @Override
    public void applyTexts(Long code) {
        depositButton.setEnabled(false);
        amountEditText.setEnabled(false);
        depositProgressBar.setVisibility(View.VISIBLE);
        depositViewModel.deposit(amount, code);
    }

    private void initChannels(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel name";
            String description = "Channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("default", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showNotification() {
        String title = "儲值成功";
        String text = "閣下的零錢到賬 " + amountEditText.getText().toString() + " 元";

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this.getApplicationContext(), "default")
                        .setSmallIcon(R.drawable.ic_attach_money_black_24dp)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, builder.build());
    }

    //  退出登录
    private void exit() {
        finishAffinity();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}
