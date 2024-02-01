package com.diamond.future.model;

import android.net.Uri;

public class UploadingModel {
    String date, time, reciver_email1, reciver_name2, reciver_number3, timeToUpload;
    Uri videouri;
    boolean uploadState;

    public boolean isUploadState() {
        return uploadState;
    }

    public void setUploadState(boolean uploadState) {
        this.uploadState = uploadState;
    }

    public String getTimeToUpload() {
        return timeToUpload;
    }

    public void setTimeToUpload(String timeToUpload) {
        this.timeToUpload = timeToUpload;
    }

    public Uri getVideouri() {
        return videouri;
    }

    public void setVideouri(Uri videouri) {
        this.videouri = videouri;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReciver_email1() {
        return reciver_email1;
    }

    public void setReciver_email1(String reciver_email1) {
        this.reciver_email1 = reciver_email1;
    }

    public String getReciver_name2() {
        return reciver_name2;
    }

    public void setReciver_name2(String reciver_name2) {
        this.reciver_name2 = reciver_name2;
    }

    public String getReciver_number3() {
        return reciver_number3;
    }

    public void setReciver_number3(String reciver_number3) {
        this.reciver_number3 = reciver_number3;
    }
}
