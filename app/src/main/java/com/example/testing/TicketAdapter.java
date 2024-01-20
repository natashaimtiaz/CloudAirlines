package com.example.testing;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    private final TicketDB[] ticketArray; // Using an array instead of a List


    //private final TicketDB[] ticketArray;
    private TicketItemListener listener;

    public TicketAdapter(TicketDB[] ticketArray, TicketItemListener listener) {
        this.ticketArray = ticketArray;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        TicketDB ticket = ticketArray[position];
        if (ticket != null) {
            // Set text for TextViews
            holder.textViewFrom.setText(ticket.getFrom());
            holder.textViewTo.setText(ticket.getTo());
            holder.textViewStatus.setText(ticket.getStatus());
            holder.textViewFlightID.setText(ticket.getFlightID());
            holder.textViewTime.setText(ticket.getTime());

            // Show the cancel button
            holder.imageCancelButton.setVisibility(View.VISIBLE);
            holder.imageCancelButton.setOnClickListener(v -> listener.onCancelTicket(ticket, position));
        } else {
            // Hide the cancel button if there is no ticket data
            holder.imageCancelButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return ticketArray.length; // Size of the array
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewFrom, textViewTo, textViewStatus, textViewTime, textViewFlightID;
        public ImageButton imageCancelButton; // Reference to the cancel button

        public TicketViewHolder(View view) {
            super(view);
            textViewFrom = view.findViewById(R.id.textViewFrom);
            textViewTo = view.findViewById(R.id.textViewTo);
            textViewStatus = view.findViewById(R.id.textViewStatus);
            textViewTime = view.findViewById(R.id.textViewTime);
            textViewFlightID = view.findViewById(R.id.textViewFlightID);
            imageCancelButton = view.findViewById(R.id.imageCancelButton); // Initialize the cancel button
        }
    }

    public interface TicketItemListener {
        void onCancelTicket(TicketDB ticket, int position);
    }

}
