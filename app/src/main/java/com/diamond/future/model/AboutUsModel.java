package com.diamond.future.model;

import java.io.Serializable;

public class AboutUsModel implements Serializable {
    int status;
    //Data data;
    String title,description;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }



    public static class Data implements Serializable{
        String title,description;

        public String getPagetitle() {
            return title;
        }

        public void setPagetitle(String pagetitle) {
            this.title = pagetitle;
        }

        public String getContent() {
            return description;
        }

        public void setContent(String content) {
            this.description = content;
        }
    }
}
