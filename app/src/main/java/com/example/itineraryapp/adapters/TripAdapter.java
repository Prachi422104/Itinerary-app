package com.example.itineraryapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.itineraryapp.R;
import com.example.itineraryapp.TripDetailsActivity;
import com.example.itineraryapp.models.Trip;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    private Context context;
    private List<Trip> tripList;

    public TripAdapter(Context context, List<Trip> tripList) {
        this.context = context;
        this.tripList = tripList;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_trip, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip trip = tripList.get(position);
        holder.tvDestination.setText(trip.getDestination());
        holder.tvDates.setText(trip.getStartDate() + " - " + trip.getEndDate());

        if (trip.getImageUrl() != null && !trip.getImageUrl().isEmpty()) {
            Glide.with(context).load(trip.getImageUrl()).into(holder.ivTripImage);
        } else {
            holder.ivTripImage.setImageResource(R.drawable.bg_main_gradient);
        }

        if (trip.isCompleted()) {
            holder.tvCompletedBadge.setVisibility(View.VISIBLE);
        } else {
            holder.tvCompletedBadge.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TripDetailsActivity.class);
            intent.putExtra("tripId", trip.getId());
            intent.putExtra("destination", trip.getDestination());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    static class TripViewHolder extends RecyclerView.ViewHolder {
        TextView tvDestination;
        TextView tvDates;
        ImageView ivTripImage;
        TextView tvCompletedBadge;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDestination = itemView.findViewById(R.id.tv_destination);
            tvDates = itemView.findViewById(R.id.tv_dates);
            ivTripImage = itemView.findViewById(R.id.iv_trip_image);
            tvCompletedBadge = itemView.findViewById(R.id.tv_completed_badge);
        }
    }
}
