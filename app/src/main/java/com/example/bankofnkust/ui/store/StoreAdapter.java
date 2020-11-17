package com.example.bankofnkust.ui.store;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bankofnkust.R;

import java.util.ArrayList;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {

    private ArrayList<StoreItem> mStoreList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class StoreViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mNameView;
        public TextView mTypeView;
        public TextView mAreaView;
        public TextView mFavorAmountView;
        public TextView mOpenTimeView;

        public StoreViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image);
            mNameView = itemView.findViewById(R.id.name);
            mAreaView = itemView.findViewById(R.id.area);
            mTypeView = itemView.findViewById(R.id.type);
            mFavorAmountView = itemView.findViewById(R.id.favorAmount);
            mOpenTimeView = itemView.findViewById(R.id.openTime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public StoreAdapter(ArrayList<StoreItem> storeList) {
        mStoreList = storeList;
    }

    @Override
    public StoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_store_item, parent, false);
        StoreViewHolder ivh = new StoreViewHolder(v, mListener);
        return ivh;
    }

    @Override
    public void onBindViewHolder(StoreViewHolder holder, int position) {
        StoreItem currentItem = mStoreList.get(position);

        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mNameView.setText(currentItem.getName());
        holder.mTypeView.setText(currentItem.getType());
        holder.mAreaView.setText(currentItem.getArea());
        holder.mFavorAmountView.setText(currentItem.getFavorAmount());
        holder.mOpenTimeView.setText(currentItem.getOpenTime());
    }

    @Override
    public int getItemCount() {
        return mStoreList.size();
    }


}
