package com.example.bankofnkust.ui.store;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankofnkust.R;

import java.net.URLEncoder;
import java.util.ArrayList;

public class StoreFragment extends Fragment {

    private ArrayList<StoreItem> mStoreList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_store, container, false);

        mStoreList = new ArrayList<>();
        mStoreList.add(new StoreItem(R.drawable.img_store_hilife, "萊爾富便利商店 高市海洋店", "便利店", "楠梓校區", "340", "08:00–23:00"));
        mStoreList.add(new StoreItem(R.drawable.img_store_family, "全家便利商店 高雄科大店", "便利店", "第一校區", "460", "07:30–21:30"));
        mStoreList.add(new StoreItem(R.drawable.img_store_pmj, "品茗居", "冰品飲料店", "第一校區", "420", "13:00–21:00"));
        mStoreList.add(new StoreItem(R.drawable.img_store_sdk, "食大客牛排楠梓店", "餐廳", "楠梓校區", "466", "11:00–21:00"));
        mStoreList.add(new StoreItem(R.drawable.img_store_711, "7-ELEVEN 旗海門市", "便利店", "旗津校區", "46", "00:00–24:00"));
        mStoreList.add(new StoreItem(R.drawable.img_store_hwx, "海王星數位設計輸出影印店", "複印店", "建工校區", "122", "08:00–21:00"));
        mStoreList.add(new StoreItem(R.drawable.img_store_zx, "中興動物醫院-大豐總院", "動物醫院", "建工校區", "642", "09:30–02:00"));

        RecyclerView mRecyclerview= root.findViewById(R.id.storeRecyclerView);
        mRecyclerview.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        StoreAdapter mAdapter = new StoreAdapter(mStoreList);

        mRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerview.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new StoreAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String string = "geo:0,0?q=" + toURLEncoded(mStoreList.get(position).getName());
                Uri location = Uri.parse(string);
                Intent intent = new Intent(Intent.ACTION_VIEW, location);
                startActivity(intent);
            }
        });
        return root;
    }


    private static String toURLEncoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return "";
        }
        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception localException) {
        }
        return "";
    }

}