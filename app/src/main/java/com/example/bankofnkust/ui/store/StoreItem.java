package com.example.bankofnkust.ui.store;

public class StoreItem {

    private int mImageResource;
    private String mName;
    private String mType;
    private String mArea;
    private String mFavorAmount;
    private String mOpenTime;

    public StoreItem(int imageresource, String name, String type, String area, String favorAmount, String openTime) {
        mImageResource = imageresource;
        mName = name;
        mType = type;
        mArea = area;
        mFavorAmount = favorAmount;
        mOpenTime = openTime;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public String getName() {
        return mName;
    }

    public String getType() {
        return mType;
    }

    public String getArea() {
        return mArea;
    }

    public String getFavorAmount() {
        return mFavorAmount;
    }

    public String getOpenTime() {
        return mOpenTime;
    }
}