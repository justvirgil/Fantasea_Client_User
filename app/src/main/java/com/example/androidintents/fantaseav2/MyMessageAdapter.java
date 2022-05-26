package com.example.androidintents.fantaseav2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyMessageAdapter extends RecyclerView.Adapter<MyMessageAdapter.MyViewHolder> {

    Context context;
    ArrayList<UserMessage> list;
    static RecyclerViewClickListener listener;

    public MyMessageAdapter(Context context, ArrayList<UserMessage> list, RecyclerViewClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.messagescardview,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserMessage user = list.get(position);
        holder.messageAllTxt.setText(user.getMessageTxt());
        holder.messageSubject.setText(user.getSubjectTxt());
        holder.messageToTxt.setText(user.getMessageToTxt());
        holder.usernameToTxt.setText(user.getUsername());
        holder.dateSentTxt.setText(user.getDate_sent());
        holder.timeSentTxt.setText(user.getTime_sent());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView messageAllTxt,messageSubject,messageToTxt,usernameToTxt,dateSentTxt,timeSentTxt;
        //ImageView messagePicture;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            usernameToTxt = itemView.findViewById(R.id.messageFromTextView);
            messageSubject= itemView.findViewById(R.id.messageSubjectTextView);
            messageAllTxt = itemView.findViewById(R.id.messageTextView);
            messageToTxt = itemView.findViewById(R.id.messageToTextView);
            dateSentTxt = itemView.findViewById(R.id.dateSentTextView);
            timeSentTxt = itemView.findViewById(R.id.timeSentTextView);
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
