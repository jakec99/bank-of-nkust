package com.example.bankofnkust.ui.bill;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankofnkust.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;

public class BillAdapter extends FirestoreRecyclerAdapter<Bill, BillAdapter.BillHolder> {

    public BillAdapter(@NonNull FirestoreRecyclerOptions<Bill> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BillHolder holder, int position, @NonNull Bill model) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.textViewTitle.setText(model.getTitle());
        holder.textViewValue.setText(model.getValue());
        holder.textViewDate.setText(dateFormat.format(model.getDate()));
        holder.textViewBalance.setText(String.valueOf(model.getBalance()));
    }

    @NonNull
    @Override
    public BillHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_bill_item,
                parent, false);
        return new BillHolder(v);
    }

    class BillHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewValue;
        TextView textViewDate;
        TextView textViewBalance;

        public BillHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.billTitleText);
            textViewValue = itemView.findViewById(R.id.billValueText);
            textViewDate = itemView.findViewById(R.id.billDateText);
            textViewBalance = itemView.findViewById(R.id.billBalanceText);
        }
    }

}
