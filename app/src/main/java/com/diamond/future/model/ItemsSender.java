package com.diamond.future.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemsSender implements Serializable {
    ArrayList<Items> items;
    int status;
    String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getData() {
        return msg;
    }

    public void setData(String data) {
        this.msg = data;
    }

    public ArrayList<Items> getItems() {
        return items;
    }

    public void setItems(ArrayList<Items> items) {
        this.items = items;
    }

    public static class Items implements Serializable {
        int uploadId;
        String receiverName,add_datetime, sendDate,sendTime, price,imagePath,
                upload_image,upload_video,upload_size,reciverEmailId1,reciverEmailId2,reciverEmailId3,reciveTime,reciveDate,senderName,modified,latitude,longitude,userImage;

        public String getAdd_datetime() {
            return add_datetime;
        }

        public void setAdd_datetime(String add_datetime) {
            this.add_datetime = add_datetime;
        }

        public String getModified() {
            return modified;
        }

        public void setModified(String modified) {
            this.modified = modified;
        }

        public int getUploadId() {
            return uploadId;
        }

        public void setUploadId(int uploadId) {
            this.uploadId = uploadId;
        }

        public String getUpload_image() {
            return upload_image;
        }

        public void setUpload_image(String upload_image) {
            this.upload_image = upload_image;
        }

        public String getUpload_video() {
            return upload_video;
        }

        public void setUpload_video(String upload_video) {
            this.upload_video = upload_video;
        }

        public String getUpload_size() {
            return upload_size;
        }

        public void setUpload_size(String upload_size) {
            this.upload_size = upload_size;
        }

        public String getReciverEmailId1() {
            return reciverEmailId1;
        }

        public void setReciverEmailId1(String reciverEmailId1) {
            this.reciverEmailId1 = reciverEmailId1;
        }

        public String getReciverEmailId2() {
            return reciverEmailId2;
        }

        public void setReciverEmailId2(String reciverEmailId2) {
            this.reciverEmailId2 = reciverEmailId2;
        }

        public String getReciverEmailId3() {
            return reciverEmailId3;
        }

        public void setReciverEmailId3(String reciverEmailId3) {
            this.reciverEmailId3 = reciverEmailId3;
        }

        public String getReciveTime() {
            return reciveTime;
        }

        public void setReciveTime(String reciveTime) {
            this.reciveTime = reciveTime;
        }

        public String getReciveDate() {
            return reciveDate;
        }

        public void setReciveDate(String reciveDate) {
            this.reciveDate = reciveDate;
        }

        public String getSenderName() {
            return senderName;
        }

        public void setSenderName(String senderName) {
            this.senderName = senderName;
        }

        public int getId() {
            return uploadId;
        }

        public String getImagePath() {
            return imagePath;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }

        public void setId(int id) {
            this.uploadId = id;
        }

        public String getReceiverName() {
            return receiverName;
        }

        public void setReceiverName(String receiverName) {
            this.receiverName = receiverName;
        }

        public String getSendDate() {
            return sendDate;
        }

        public void setSendDate(String sendDate) {
            this.sendDate = sendDate;
        }

        public String getSendTime() {
            return sendTime;
        }

        public void setSendTime(String sendTime) {
            this.sendTime = sendTime;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getUserImage() {
            return userImage;
        }

        public void setUserImage(String userImage) {
            this.userImage = userImage;
        }
    }
}
