package com.example.itineraryapp.models;

public class ActivityModel {
    private int id;
    private int tripId;
    private String title;
    private String time;
    private String notes;
    private String imageUrl;
    private int dayNum;

    public ActivityModel() {}

    public ActivityModel(int id, int tripId, String title, String time, String notes, int dayNum) {
        this.id = id;
        this.tripId = tripId;
        this.title = title;
        this.time = time;
        this.notes = notes;
        this.dayNum = dayNum;
    }

    public ActivityModel(int id, int tripId, String title, String time, String notes, String imageUrl, int dayNum) {
        this.id = id;
        this.tripId = tripId;
        this.title = title;
        this.time = time;
        this.notes = notes;
        this.imageUrl = imageUrl;
        this.dayNum = dayNum;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getTripId() { return tripId; }
    public void setTripId(int tripId) { this.tripId = tripId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getDayNum() { return dayNum; }
    public void setDayNum(int dayNum) { this.dayNum = dayNum; }
}
