package com.chengw.autocallrecorder;

/**
 * Created by Cheng on 4/30/2015.
 */
public class RecordingItem {
    private long id;
    private String filename;
    private int in_cloud;
    private String comment;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getIncloud() {
        return in_cloud;
    }

    public void setIncloud(int in_cloud) {
        this.in_cloud = in_cloud;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
