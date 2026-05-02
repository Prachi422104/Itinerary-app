package com.example.itineraryapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.itineraryapp.models.ActivityModel;
import com.example.itineraryapp.models.Trip;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ItineraryDB";
    private static final int DATABASE_VERSION = 7;

    // Table Names
    private static final String TABLE_TRIPS = "trips";
    private static final String TABLE_ACTIVITIES = "activities";

    // Trips Table Columns
    private static final String KEY_TRIP_ID = "id";
    private static final String KEY_DESTINATION = "destination";
    private static final String KEY_START_DATE = "start_date";
    private static final String KEY_END_DATE = "end_date";
    private static final String KEY_TRIP_IMAGE = "image_url";
    private static final String KEY_COMPLETED = "is_completed";

    // Activities Table Columns
    private static final String KEY_ACTIVITY_ID = "id";
    private static final String KEY_ACTIVITY_TRIP_ID = "trip_id";
    private static final String KEY_ACTIVITY_TITLE = "title";
    private static final String KEY_ACTIVITY_TIME = "time";
    private static final String KEY_ACTIVITY_NOTES = "notes";
    private static final String KEY_ACTIVITY_IMAGE = "image_url";
    private static final String KEY_ACTIVITY_DAY_NUM = "day_num";

    // Table Create Statements
    private static final String CREATE_TABLE_TRIPS = "CREATE TABLE " + TABLE_TRIPS + "("
            + KEY_TRIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_DESTINATION + " TEXT,"
            + KEY_START_DATE + " TEXT,"
            + KEY_END_DATE + " TEXT,"
            + KEY_TRIP_IMAGE + " TEXT,"
            + KEY_COMPLETED + " INTEGER DEFAULT 0" + ")";

    private static final String CREATE_TABLE_ACTIVITIES = "CREATE TABLE " + TABLE_ACTIVITIES + "("
            + KEY_ACTIVITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ACTIVITY_TRIP_ID + " INTEGER,"
            + KEY_ACTIVITY_TITLE + " TEXT,"
            + KEY_ACTIVITY_TIME + " TEXT,"
            + KEY_ACTIVITY_NOTES + " TEXT,"
            + KEY_ACTIVITY_IMAGE + " TEXT,"
            + KEY_ACTIVITY_DAY_NUM + " INTEGER DEFAULT 1" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TRIPS);
        db.execSQL(CREATE_TABLE_ACTIVITIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITIES);
        onCreate(db);
    }

    // --- Trip CRUD ---
    public long insertTrip(Trip trip) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DESTINATION, trip.getDestination());
        values.put(KEY_START_DATE, trip.getStartDate());
        values.put(KEY_END_DATE, trip.getEndDate());
        values.put(KEY_TRIP_IMAGE, trip.getImageUrl());
        values.put(KEY_COMPLETED, trip.isCompleted() ? 1 : 0);
        return db.insert(TABLE_TRIPS, null, values);
    }

    public List<Trip> getAllTrips() {
        List<Trip> trips = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TRIPS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Trip t = new Trip();
                t.setId(c.getInt(c.getColumnIndexOrThrow(KEY_TRIP_ID)));
                t.setDestination(c.getString(c.getColumnIndexOrThrow(KEY_DESTINATION)));
                t.setStartDate(c.getString(c.getColumnIndexOrThrow(KEY_START_DATE)));
                t.setEndDate(c.getString(c.getColumnIndexOrThrow(KEY_END_DATE)));
                t.setImageUrl(c.getString(c.getColumnIndexOrThrow(KEY_TRIP_IMAGE)));
                t.setCompleted(c.getInt(c.getColumnIndexOrThrow(KEY_COMPLETED)) == 1);
                trips.add(t);
            } while (c.moveToNext());
        }
        c.close();
        return trips;
    }

    public void deleteTrip(int tripId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRIPS, KEY_TRIP_ID + " = ?", new String[]{String.valueOf(tripId)});
        db.delete(TABLE_ACTIVITIES, KEY_ACTIVITY_TRIP_ID + " = ?", new String[]{String.valueOf(tripId)});
    }

    public void deleteAllTrips() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRIPS, null, null);
        db.delete(TABLE_ACTIVITIES, null, null);
    }

    public void updateTripCompletedStatus(int tripId, boolean isCompleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_COMPLETED, isCompleted ? 1 : 0);
        db.update(TABLE_TRIPS, values, KEY_TRIP_ID + " = ?", new String[]{String.valueOf(tripId)});
    }

    // --- Activity CRUD ---
    public long insertActivity(ActivityModel activity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ACTIVITY_TRIP_ID, activity.getTripId());
        values.put(KEY_ACTIVITY_TITLE, activity.getTitle());
        values.put(KEY_ACTIVITY_TIME, activity.getTime());
        values.put(KEY_ACTIVITY_NOTES, activity.getNotes());
        values.put(KEY_ACTIVITY_IMAGE, activity.getImageUrl());
        values.put(KEY_ACTIVITY_DAY_NUM, activity.getDayNum());
        return db.insert(TABLE_ACTIVITIES, null, values);
    }

    public List<ActivityModel> getActivitiesForTrip(int tripId) {
        List<ActivityModel> activities = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ACTIVITIES + " WHERE " + KEY_ACTIVITY_TRIP_ID + " = " + tripId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                ActivityModel a = new ActivityModel();
                a.setId(c.getInt(c.getColumnIndexOrThrow(KEY_ACTIVITY_ID)));
                a.setTripId(c.getInt(c.getColumnIndexOrThrow(KEY_ACTIVITY_TRIP_ID)));
                a.setTitle(c.getString(c.getColumnIndexOrThrow(KEY_ACTIVITY_TITLE)));
                a.setTime(c.getString(c.getColumnIndexOrThrow(KEY_ACTIVITY_TIME)));
                a.setNotes(c.getString(c.getColumnIndexOrThrow(KEY_ACTIVITY_NOTES)));
                a.setImageUrl(c.getString(c.getColumnIndexOrThrow(KEY_ACTIVITY_IMAGE)));
                a.setDayNum(c.getInt(c.getColumnIndexOrThrow(KEY_ACTIVITY_DAY_NUM)));
                activities.add(a);
            } while (c.moveToNext());
        }
        c.close();
        return activities;
    }

    public void deleteActivity(int activityId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ACTIVITIES, KEY_ACTIVITY_ID + " = ?", new String[]{String.valueOf(activityId)});
    }

    public void updateActivityNotes(int id, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ACTIVITY_NOTES, notes);
        db.update(TABLE_ACTIVITIES, values, KEY_ACTIVITY_ID + " = ?", new String[]{String.valueOf(id)});
    }
}
