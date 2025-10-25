package org.khan.coursetodoapp;

import java.io.Serializable;
public class ToDo implements Serializable {
    private String courseId;
    private String title;
    private String text;
    private long lastSaveTime;

    public ToDo(String courseId, String title, String text){
        this.courseId = courseId;
        this.title = title;
        this.text = text;
        this.lastSaveTime = System.currentTimeMillis();

    }
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public long getLastSaveTime() { return lastSaveTime; }
    public void setLastSaveTime(long lastSaveTime) {
        this.lastSaveTime = lastSaveTime;
    }
}
