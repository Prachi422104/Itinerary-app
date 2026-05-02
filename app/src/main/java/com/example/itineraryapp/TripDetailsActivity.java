package com.example.itineraryapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itineraryapp.adapters.ActivityAdapter;
import com.example.itineraryapp.db.DatabaseHelper;
import com.example.itineraryapp.models.ActivityModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class TripDetailsActivity extends AppCompatActivity {

    private int tripId;
    private String destination;
    private RecyclerView recyclerActivities;
    private TextView tvEmpty;
    private FloatingActionButton fabAddActivity;
    private ExtendedFloatingActionButton btnCompleteTrip;
    private KonfettiView konfettiView;
    private DatabaseHelper dbHelper;
    private ActivityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        tripId = getIntent().getIntExtra("tripId", -1);
        destination = getIntent().getStringExtra("destination");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(destination != null ? destination + " Itinerary" : "Trip Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        dbHelper = new DatabaseHelper(this);
        
        recyclerActivities = findViewById(R.id.recycler_activities);
        tvEmpty = findViewById(R.id.tv_empty);
        fabAddActivity = findViewById(R.id.fab_add_activity);
        btnCompleteTrip = findViewById(R.id.btn_complete_trip);
        konfettiView = findViewById(R.id.konfettiView);

        recyclerActivities.setLayoutManager(new LinearLayoutManager(this));

        fabAddActivity.setOnClickListener(v -> {
            Intent intent = new Intent(TripDetailsActivity.this, AddActivityActivity.class);
            intent.putExtra("tripId", tripId);
            startActivity(intent);
        });

        btnCompleteTrip.setOnClickListener(v -> {
            explodeKonfetti();
            dbHelper.updateTripCompletedStatus(tripId, true);
            Toast.makeText(this, "Trip Marked as Completed! 🎉", Toast.LENGTH_LONG).show();
            btnCompleteTrip.setEnabled(false);
            btnCompleteTrip.setText("Completed");
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadActivities();
    }

    private void loadActivities() {
        if (tripId == -1) return;
        List<ActivityModel> activities = dbHelper.getActivitiesForTrip(tripId);
        
        if (activities.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerActivities.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerActivities.setVisibility(View.VISIBLE);
            
            // Group actions into dynamic Object list
            List<Object> groupedItems = new ArrayList<>();
            int currentDay = -1;
            
            for (ActivityModel a : activities) {
                if (a.getDayNum() != currentDay) {
                    currentDay = a.getDayNum();
                    groupedItems.add("Day " + currentDay);
                }
                groupedItems.add(a);
            }
            
            adapter = new ActivityAdapter(this, groupedItems);
            recyclerActivities.setAdapter(adapter);
        }
    }

    private void explodeKonfetti() {
        EmitterConfig emitterConfig = new Emitter(100L, TimeUnit.MILLISECONDS).max(100);
        Party party = new PartyFactory(emitterConfig)
                .spread(360)
                .colors(java.util.Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                .setSpeedBetween(0f, 30f)
                .position(new nl.dionsegijn.konfetti.core.Position.Relative(0.5, 0.3))
                .build();
        
        konfettiView.start(party);
    }
}
