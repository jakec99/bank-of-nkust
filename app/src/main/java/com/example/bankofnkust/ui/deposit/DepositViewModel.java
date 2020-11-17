package com.example.bankofnkust.ui.deposit;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DepositViewModel extends ViewModel {

    //  处理成功
    private MutableLiveData<String> depositStatus;
    LiveData<String> getDepositStatus() {
        return depositStatus;
    }

    //  初始化
    public DepositViewModel() {
        depositStatus = new MutableLiveData<>();
    };

    //  firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static Long code;
    private static Long getCode;
    private static Long amount;
    private static String card;
    private static Long balance;
    private static Long cardBalance;
    private static Long afterBalance;

    public void deposit (Long inputAmount, Long inputCode) {
        amount = inputAmount;
        code = inputCode;
        checkUser();
    }

    //  获取卡及余额
    private void checkUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            db.collection("user").document(user.getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            card = document.getString("card");
                            balance = document.getLong("balance");
                            getCode = document.getLong("code");
                            checkCode();
                        } else {
                            depositStatus.setValue("fail_firebase_file");
                        }
                    } else {
                        depositStatus.setValue("fail_firebase");
                    }
                }
            });
        } else {
            depositStatus.setValue("fail_login");
        };
    }

    private void checkCode() {
        if ( code.equals(getCode) ) {
            checkCard();
        }
        else {
            depositStatus.setValue("fail_code");
        }
    }

    //  获取主卡余额
    private void checkCard() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            db.collection("user").document(user.getUid()).collection("card").document(card)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                cardBalance = document.getLong("balance");
                                update();
                            } else {
                                depositStatus.setValue("fail_firebase_file");
                            }
                        } else {
                            depositStatus.setValue("fail_firebase");
                        }
                    }
            });
        } else {
        depositStatus.setValue("fail_login");
        };
    }

    //  更新余额
    private void update() {
        Long afterCardBalance = cardBalance - amount ;
        if ( afterCardBalance < 0 ) {
            depositStatus.setValue("fail_card_balance");
            return;
        }
        afterBalance = balance + amount ;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            db.collection("user").document(user.getUid()).collection("card").document(card)
                    .update("balance", afterCardBalance);
            db.collection("user").document(user.getUid())
                    .update("balance", afterBalance);
            addBill();
        } else {
            depositStatus.setValue("fail_login");
        }
    };

    private void addBill() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Map<String, Object> data = new HashMap<>();
        data.put("title", "儲值");
        data.put("value", "+"+amount.toString());
        data.put("balance",afterBalance);
        data.put("date", new Timestamp(new Date()));
        if (user != null) {
            db.collection("user").document(user.getUid())
                    .collection("bill")
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            depositStatus.setValue("success");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            depositStatus.setValue("fail_firebase_file");
                        }
                    });
        } else {
            depositStatus.setValue("fail_login");
        }
    }
}