package com.srd14.agend_in;

public class CalendarEvent {
    private String title;
    private long startTimeMillis;

    public CalendarEvent(String title, long startTimeMillis) {
        this.title = title;
        this.startTimeMillis = startTimeMillis;
    }

    public String getTitle() {
        return title;
    }

    public long getStartTimeMillis() {
        return startTimeMillis;
    }
}
