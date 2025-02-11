package com.example.foodplanner.data.pojos;
public class Event {
    private long id;
    private String title;
    private long dtStart;

    public Event(long id, String title, long dtStart) {
        this.id = id;
        this.title = title;
        this.dtStart = dtStart;
    }

    // Getters (and setters if needed)
    public long getId() { return id; }
    public String getTitle() { return title; }
    public long getDtStart() { return dtStart; }
}
