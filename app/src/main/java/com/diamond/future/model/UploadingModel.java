package com.diamond.future.model;

import android.net.Uri;

public class UploadingModel {
    String date, time, reciver_mail1, reciver_mail2, reciver_mail3, timeToUpload;
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

    public String getReciver_mail1() {
        return reciver_mail1;
    }

    public void setReciver_mail1(String reciver_mail1) {
        this.reciver_mail1 = reciver_mail1;
    }

    public String getReciver_mail2() {
        return reciver_mail2;
    }

    public void setReciver_mail2(String reciver_mail2) {
        this.reciver_mail2 = reciver_mail2;
    }

    public String getReciver_mail3() {
        return reciver_mail3;
    }

    public void setReciver_mail3(String reciver_mail3) {
        this.reciver_mail3 = reciver_mail3;
    }
}
