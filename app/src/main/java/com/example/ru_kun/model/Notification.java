package com.example.ru_kun.model;

public class Notification {
    private String title;
    private String description;
    private String timestamp;

    public Notification(String title, String description, int iconResId, String timestamp) {
        this.title = title;
        this.description = description;
        this.iconResId = iconResId;
        this.isStatusVisible = true;
        this.timestamp = timestamp;

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

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    private int iconResId;
    private boolean isStatusVisible; // Add this line

    // Add getters and setters for isStatusVisible
    public boolean isStatusVisible() {
        return isStatusVisible;
    }

    public void setStatusVisible(boolean statusVisible) {
        isStatusVisible = statusVisible;
    }

    public String getTimestamp() {return timestamp;}

    public void setTimestamp(String timestamp) {this.timestamp = timestamp;}

}
