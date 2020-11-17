package com.example.bankofnkust.ui.bill;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankofnkust.R;
import com.example.bankofnkust.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class BillActivity extends AppCompatActivity {

    private BillAdapter adapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
//        setUpRecyclerView();
        checkUser();
    }

    private void setUpRecyclerView() {
        Query query = db.collection("user").document("user.getUid()").collection("bill");
        FirestoreRecyclerOptions<Bill> options = new FirestoreRecyclerOptions.Builder<Bill>()
                .setQuery(query, Bill.class)
                .build();
        adapter = new BillAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.billRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void checkUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Query query = db.collection("user").document(user.getUid())
                    .collection("bill")
                    .orderBy("date", Query.Direction.DESCENDING);
            FirestoreRecyclerOptions<Bill> options = new FirestoreRecyclerOptions.Builder<Bill>()
                    .setQuery(query, Bill.class)
                    .setLifecycleOwner(this)
                    .build();

            adapter = new BillAdapter(options);

            RecyclerView recyclerView = findViewById(R.id.billRecyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, getString(R.string.login_lost), Toast.LENGTH_SHORT).show();
            exit();
        };
    }

    private void exit(){
        finishAffinity();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}
