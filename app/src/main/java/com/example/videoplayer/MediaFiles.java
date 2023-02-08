package com.example.videoplayer;

public class MediaFiles {
    private String id;
    private String title;
    private String displayName;
    private String size;
    private String duration;
    private String path;
    private String dateAdded;

    public MediaFiles(String id, String title, String displayName, String size, String duration, String path, String dateAdded) {
        this.id = id;
        this.title = title;
        this.displayName = displayName;
        this.size = size;
        this.duration = duration;
        this.path = path;
        this.dateAdded = dateAdded;
    }
}
