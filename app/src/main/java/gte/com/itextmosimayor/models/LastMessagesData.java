package gte.com.itextmosimayor.models;

import java.util.Date;

import androidx.annotation.Keep;

@Keep
public class LastMessagesData {
    public String text;
    public String senderId;
    public String senderName;
    public String type;
    public Date time;
    public String name;
    public String mobileNumber;
    public String departmentID;
    public String img;
    public String userID;
    public String fid;

    public LastMessagesData() {
    }

    public LastMessagesData(String text, String senderId, String senderName, String type, Date time, String name, String mobileNumber, String departmentID, String img, String userID, String fid) {
        this.text = text;
        this.senderId = senderId;
        this.senderName = senderName;
        this.type = type;
        this.time = time;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.departmentID = departmentID;
        this.img = img;
        this.userID = userID;
        this.fid = fid;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getText() {
        return text;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getType() {
        return type;
    }

    public Date getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getDepartmentID() {
        return departmentID;
    }

    public String getImg() {
        return img;
    }

    public String getUserID() {
        return userID;
    }

    public String getFid() {
        return fid;
    }
}