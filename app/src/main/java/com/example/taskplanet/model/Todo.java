package com.example.taskplanet.model;

public class Todo {
    private int id;
    private String title;
    private String description;
    private boolean isCompleted;
    private long createdTime;

    public Todo() {
        this.createdTime = System.currentTimeMillis();
        this.isCompleted = false;
    }

    public Todo(String title, String description) {
        this();
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }
}