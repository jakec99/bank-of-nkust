package com.example.bankofnkust.ui.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.bankofnkust.R;
import com.example.bankofnkust.ui.login.LoginActivity;
import com.example.bankofnkust.ui.deposit.DepositActivity;
import com.example.bankofnkust.ui.withdraw.WithdrawActivity;

public class WalletFragment extends Fragment {

    private WalletViewModel walletViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        walletViewModel =
                ViewModelProviders.of(this).get(WalletViewModel.class);
        View root = inflater.inflate(R.layout.fragment_wallet, container, false);

        final TextView balanceText = root.findViewById(R.id.balanceText);
        final ImageView refreshButton = root.findViewById(R.id.refreshButton);
        final Button depositButton = root.findViewById(R.id.depositButton);
        final Button withdrawButton = root.findViewById(R.id.withdrawButton);
        final Button transferButton = root.findViewById(R.id.transferButton);

        //  监听余额变化
        walletViewModel.getBalance().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                balanceText.setText(s);
            }
        });

        //  监听是否登出
        walletViewModel.getIsLoggedOut().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean b) {
                Toast.makeText(getActivity(), getString(R.string.login_lost), Toast.LENGTH_SHORT).show();
                exit();
            }
        });

        //  刷新余额
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walletViewModel.checkCurrentBalance();
                Toast.makeText(getActivity(), getString(R.string.wallet_refresh), Toast.LENGTH_SHORT).show();
            }
        });

        //  储值
        depositButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DepositActivity.class);
                startActivity(intent);
            }
        });

        //  提领
        withdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WithdrawActivity.class);
                startActivity(intent);
            }
        });

        //  转账
        transferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), getString(R.string.under_construction), Toast.LENGTH_SHORT).show();
            }
        });

        return root;

    }

    //  退出登录
    private void exit() {
        getActivity().finishAffinity();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

}