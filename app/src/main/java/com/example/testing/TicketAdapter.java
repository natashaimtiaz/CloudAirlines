package com.example.testing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    private final TicketDB[] ticketArray; // Using an array instead of a List

    public TicketAdapter(TicketDB[] ticketArray) {
        this.ticketArray = ticketArray;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        TicketDB ticket = ticketArray[position]; // Access array using index
        if (ticket != null) {
            holder.textViewFrom.setText(ticket.getFrom());
            holder.textViewTo.setText(ticket.getTo());
            holder.textViewStatus.setText(ticket.getStatus());
            holder.textViewFlightID.setText(ticket.getFlightID());
            holder.textViewTicketID.setText(ticket.getTicketID());
            holder.textViewPassengerID.setText(ticket.getPassengerID());

            // Convert Timestamp to a readable String
            Timestamp timestamp = ticket.getTime();
            if (timestamp != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String formattedDate = dateFormat.format(timestamp.toDate());
                holder.textViewTime.setText(formattedDate);
            } else {
                holder.textViewTime.setText("N/A"); // or some placeholder text
            }
        }
    }

    @Override
    public int getItemCount() {
        return ticketArray.length; // Size of the array
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewFrom, textViewTo, textViewStatus, textViewTime, textViewFlightID, textViewTicketID, textViewPassengerID;

        public TicketViewHolder(View view) {
            super(view);
            textViewFrom = view.findViewById(R.id.textViewFrom);
            textViewTo = view.findViewById(R.id.textViewTo);
            textViewStatus = view.findViewById(R.id.textViewStatus);
            textViewTime = view.findViewById(R.id.textViewTime);
            textViewFlightID = view.findViewById(R.id.textViewFlightID);
            textViewTicketID = view.findViewById(R.id.textViewTicketID);
            textViewPassengerID = view.findViewById(R.id.textViewPassengerID);
        }
    }
}
