package com.example.l09_chengbincai;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DataItemAdapter extends RecyclerView.Adapter<DataItemAdapter.MyViewHolder>{
    private List<DataItem> mDataItemList;

    public DataItemAdapter(List<DataItem> mDataItemList) {
        this.mDataItemList = mDataItemList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public ImageView imageView;
        public MyViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textView);
            imageView = view.findViewById(R.id.imageView);
        }
    }

    @Override
    public DataItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.data_item_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        vh.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = vh.getAdapterPosition();
                DataItem dataItem = mDataItemList.get(position);
                if(dataItem.isFavorite() == false){
                    dataItem.setFavorite(true);
                    mDataItemList.set(position, dataItem);
                    notifyItemChanged(position);
                }else{
                    dataItem.setFavorite(false);
                    mDataItemList.set(position, dataItem);
                    notifyItemChanged(position);
                }
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DataItem dataItem = mDataItemList.get(position);
        holder.textView.setText(dataItem.getName());
        if(dataItem.isFavorite() == false){
            holder.imageView.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }else{
            holder.imageView.setImageResource(R.drawable.ic_baseline_favorite_24);
        }
    }

    @Override
    public int getItemCount() {
        return mDataItemList.size();
    }
}
