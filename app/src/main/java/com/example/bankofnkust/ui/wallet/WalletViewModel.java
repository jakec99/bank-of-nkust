package com.example.bankofnkust.ui.wallet;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class WalletViewModel extends ViewModel {

    //  登出livedata
    private MutableLiveData<Boolean> mIsLoggedOut;
    LiveData<Boolean> getIsLoggedOut() {
        return mIsLoggedOut;
    }

    //  余额livedata
    private MutableLiveData<String> mBalance;
    LiveData<String> getBalance() {
        return mBalance;
    }

    //  初始化
    public WalletViewModel() {
        mBalance = new MutableLiveData<>();
        mIsLoggedOut = new MutableLiveData<>();
        checkCurrentBalance();
    };

    //  firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //  获取余额
    public void checkCurrentBalance() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            //  有登入
            DocumentReference docRef = db.collection("user").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            mBalance.setValue(document.getLong("balance").toString());
//                            mBalance.setValue(document.getString("card"));
                        } else {
                            mBalance.setValue("No such document");
                        }
                    } else {
                        mBalance.setValue("Failed" + task.getException());
                    }
                }
            });
        } else {
            //  没有登入
            mIsLoggedOut.setValue(true);
        }
    }

    //  登出(useless)
    public void logOut() {
        FirebaseAuth.getInstance().signOut();
    }


}