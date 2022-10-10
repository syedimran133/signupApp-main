package com.diamond.future.model;

import java.io.Serializable;

public class LoginModel implements Serializable {
    int status;
    String errors, msg;
    User User;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public LoginModel.User getUser() {
        return User;
    }

    public void setUser(LoginModel.User user) {
        User = user;
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String message) {
        this.msg = message;
    }

    public int isSuccess() {
        return status;
    }

    public void setSuccess(int success) {
        this.status = success;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public static class User implements Serializable {
        int id;
        String user_password, tokenid,user_phonenumber,user_profilepic,aboutus,user_name,devicename,location,deviceVersion,deviceid;

        public String getDevicename() {
            return devicename;
        }

        public void setDevicename(String devicename) {
            this.devicename = devicename;
        }

        public String getDeviceVersion() {
            return deviceVersion;
        }

        public void setDeviceVersion(String deviceVersion) {
            this.deviceVersion = deviceVersion;
        }

        public String getDeviceid() {
            return deviceid;
        }

        public void setDeviceid(String deviceid) {
            this.deviceid = deviceid;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getAboutus() {
            return aboutus;
        }

        public void setAboutus(String aboutus) {
            this.aboutus = aboutus;
        }

        public String getUser_profilepic() {
            return user_profilepic;
        }

        public void setUser_profilepic(String user_profilepic) {
            this.user_profilepic = user_profilepic;
        }

        public String getUser_phonenumber() {
            return user_phonenumber;
        }

        public void setUser_phonenumber(String user_phonenumber) {
            this.user_phonenumber = user_phonenumber;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUser_password() {
            return user_password;
        }

        public void setUser_password(String user_password) {
            this.user_password = user_password;
        }

        public String getTokenid() {
            return tokenid;
        }

        public void setTokenid(String tokenid) {
            this.tokenid = tokenid;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }
}
