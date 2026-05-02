package com.example.itineraryapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.itineraryapp.R;
import com.example.itineraryapp.db.DatabaseHelper;
import com.example.itineraryapp.models.ActivityModel;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ACTIVITY = 1;

    private Context context;
    private List<Object> items;
    private DatabaseHelper dbHelper;

    public ActivityAdapter(Context context, List<Object> items) {
        this.context = context;
        this.items = items;
        this.dbHelper = new DatabaseHelper(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof String) {
            return TYPE_HEADER;
        } else {
            return TYPE_ACTIVITY;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_day_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_activity, parent, false);
            return new ActivityViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            String headerTitle = (String) items.get(position);
            headerHolder.tvDayTitle.setText(headerTitle);
        } else {
            ActivityViewHolder activityHolder = (ActivityViewHolder) holder;
            ActivityModel activity = (ActivityModel) items.get(position);
            
            activityHolder.tvTitle.setText(activity.getTitle());
            activityHolder.tvTime.setText(activity.getTime());
            activityHolder.tvNotes.setText(activity.getNotes());

            if (activity.getImageUrl() != null && !activity.getImageUrl().isEmpty()) {
                Glide.with(context).load(activity.getImageUrl()).into(activityHolder.ivActivityImage);
            } else {
                activityHolder.ivActivityImage.setImageResource(R.drawable.bg_glass_card);
            }

            activityHolder.itemView.setOnClickListener(v -> showActivityDetails(activity));
        }
    }

    private void showActivityDetails(ActivityModel activity) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_activity_details, null);
        
        ImageView ivImage = dialogView.findViewById(R.id.iv_detail_image);
        TextView tvTitle = dialogView.findViewById(R.id.tv_detail_title);
        TextView tvTime = dialogView.findViewById(R.id.tv_detail_time);
        TextView tvNotes = dialogView.findViewById(R.id.tv_detail_notes);
        EditText etPersonalNotes = dialogView.findViewById(R.id.et_personal_notes);

        tvTitle.setText(activity.getTitle());
        tvTime.setText(activity.getTime());
        
        String currentFullNotes = activity.getNotes();
        String displayNotes = currentFullNotes;
        String personalNotes = "";
        
        if (currentFullNotes.contains("\n---\nPersonal Notes: ")) {
            String[] parts = currentFullNotes.split("\n---\nPersonal Notes: ");
            displayNotes = parts[0];
            personalNotes = parts.length > 1 ? parts[1] : "";
        }
        
        tvNotes.setText(displayNotes);
        etPersonalNotes.setText(personalNotes);

        if (activity.getImageUrl() != null && !activity.getImageUrl().isEmpty()) {
            Glide.with(context).load(activity.getImageUrl()).into(ivImage);
        } else {
            ivImage.setImageResource(R.drawable.bg_glass_card);
        }

        new AlertDialog.Builder(context)
                .setView(dialogView)
                .setPositiveButton("Save Notes", (dialog, which) -> {
                    String pNotes = etPersonalNotes.getText().toString().trim();
                    String baseNotes = activity.getNotes().split("\n---\nPersonal Notes: ")[0];
                    String finalNotes = baseNotes + "\n---\nPersonal Notes: " + pNotes;
                    activity.setNotes(finalNotes);
                    dbHelper.updateActivityNotes(activity.getId(), finalNotes);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Notes Saved!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvDayTitle;
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDayTitle = itemView.findViewById(R.id.tv_day_title);
        }
    }

    static class ActivityViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvTime;
        TextView tvNotes;
        ImageView ivActivityImage;

        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_activity_title);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvNotes = itemView.findViewById(R.id.tv_notes);
            ivActivityImage = itemView.findViewById(R.id.iv_activity_image);
        }
    }
}
