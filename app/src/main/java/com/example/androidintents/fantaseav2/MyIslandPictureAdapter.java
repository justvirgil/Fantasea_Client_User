package com.example.androidintents.fantaseav2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyIslandPictureAdapter extends RecyclerView.Adapter<MyIslandPictureAdapter.MyViewHolder> {

    Context context;
    ArrayList<UserIslandPicture> list;
    static RecyclerViewClickListener listener;

    public MyIslandPictureAdapter(Context context, ArrayList<UserIslandPicture> list, RecyclerViewClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.travelpicturescardview, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserIslandPicture user = list.get(position);
        Picasso.get().load(user.getGallery_pic_url()).into(holder.islandPicture);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView islandPicture;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            islandPicture = itemView.findViewById(R.id.IslandImageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v,getAdapterPosition());
        }
    }
    public interface RecyclerViewClickListener{
        void onClick(View v , int Position);
    }
}

