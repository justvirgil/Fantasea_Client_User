package com.example.androidintents.fantaseav2;
//used for travel destination class
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<UserDestination> list;
    static RecyclerViewClickListener listener;

    public MyAdapter(Context context, ArrayList<UserDestination> list, RecyclerViewClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        UserDestination user = list.get(position);
        holder.desActivties.setText(user.getActivities());
        holder.desID.setText(user.getAgency_id());
        holder.desAgencyName.setText(user.getAgency_name());
        holder.desName.setText(user.getDestination_name());
        holder.desProvince.setText(user.getDestination_province());
        holder.desIslandName.setText(user.getDestination_name());
        holder.desPrice.setText(String.valueOf(user.getBase_price())); // for integer
        Picasso.get().load(user.getDestination_pic()).into(holder.desPic); // for picture recyclerview

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView desName,desID,desPrice,desProvince,desActivties,desAgencyName,desIslandName;
        ImageView desPic;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            desIslandName = itemView.findViewById(R.id.DestinationIslandTextView);
            desActivties = itemView.findViewById(R.id.DestinationActivitiesTextView2);
            desName = itemView.findViewById(R.id.DestinationNameTextView2);
            desID = itemView.findViewById(R.id.DestinationAgencyIDTextView2);
            desPrice = itemView.findViewById(R.id.DestinationPriceTextView2);
            desProvince = itemView.findViewById(R.id.DestinationProvinceTextView2);
            desPic = itemView.findViewById(R.id.DestinationImageView);
            desAgencyName = itemView.findViewById(R.id.DestinationAgencyNameTextView2);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            listener.onClick(view,getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener{
        void onClick(View v , int Position);
    }

}
