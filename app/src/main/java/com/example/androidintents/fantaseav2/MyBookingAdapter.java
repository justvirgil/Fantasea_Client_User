package com.example.androidintents.fantaseav2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
//transaction class
public class MyBookingAdapter extends RecyclerView.Adapter<MyBookingAdapter.MyViewHolder> {

    Context context;
    ArrayList<UserBooking> list;
    static RecyclerViewClickListener listener;

    public MyBookingAdapter(Context context, ArrayList<UserBooking> list, RecyclerViewClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.transactioncardview,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserBooking user = list.get(position);

        holder.agencyName.setText(user.getAgency_name());
        holder.basePrice.setText(String.valueOf(user.getPrice())); //integer
        holder.dateBooked.setText(user.getDate_booked());
        holder.dateScheduled.setText(user.getDate_scheduled());
        holder.desActivities.setText(user.getActivities());
        holder.desName.setText(user.getDestination_name());
        holder.desProvince.setText(user.getDestination_province());
        holder.pumpBoatName.setText(user.getPump_boat_name());
        holder.seatingCapacity.setText(user.getSeating_capacity());
        holder.payID.setText(user.getPayID());
        holder.ticketStatus.setText(user.getTicketStatus());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView agencyName, basePrice, dateBooked, dateScheduled, desActivities, desName, desProvince, pumpBoatName, seatingCapacity, payID,ticketStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            agencyName = itemView.findViewById(R.id.agencyNameTextView);
            basePrice = itemView.findViewById(R.id.basePriceTextView);
            dateBooked = itemView.findViewById(R.id.dateBookedTextView);
            dateScheduled = itemView.findViewById(R.id.dateScheduledTextView);
            desActivities = itemView.findViewById(R.id.destinationActivitesTextView);
            desName = itemView.findViewById(R.id.destinationNameTextView);
            desProvince = itemView.findViewById(R.id.destinationProvinceTextView);
            pumpBoatName = itemView.findViewById(R.id.pumpboatNameTextView);
            seatingCapacity = itemView.findViewById(R.id.seatingCapacityTextView);
            payID = itemView.findViewById(R.id.payIDTextView);
            ticketStatus = itemView.findViewById(R.id.ticketStatusTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }
    public interface RecyclerViewClickListener{
        void onClick(View v , int Position);
    }
}
