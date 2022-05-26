package com.example.androidintents.fantaseav2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
//booking class
public class MyBoatAdapter extends RecyclerView.Adapter<MyBoatAdapter.MyViewHolder>  {

    Context context;
    ArrayList<UserBoat> list;
    static RecyclerViewClickListener listener;

    public MyBoatAdapter(Context context, ArrayList<UserBoat> list,RecyclerViewClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.pumpboatcardview,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserBoat user = list.get(position);

        holder.boatNameTV.setText(user.getBoat_name());
        holder.agencyNameTV.setText(user.getAgency_username());
        holder.statusTV.setText(user.getStatus());
        holder.seatingTV.setText(user.getCapacity());
        holder.dateTV.setText(user.getDate_queued());
        holder.timeTV.setText(user.getTime_queued());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView activityTV,boatNameTV,statusTV,agencyNameTV,seatingTV,dateTV,timeTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            boatNameTV = itemView.findViewById(R.id.boatNameTextView);
            agencyNameTV = itemView.findViewById(R.id.agencyUsernameTextView);
            statusTV = itemView.findViewById(R.id.pumpboatStatusTextView);
            seatingTV = itemView.findViewById(R.id.boatSeatCapTextView);
            dateTV = itemView.findViewById(R.id.date_queuedTextView);
            timeTV = itemView.findViewById(R.id.time_queuedTextView);
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
