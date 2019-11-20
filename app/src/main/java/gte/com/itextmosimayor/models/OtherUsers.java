package gte.com.itextmosimayor.models;

import androidx.annotation.Keep;

@Keep
public class OtherUsers {

    public String name;
    public String mobileNumber;
    public String departmentID;
    public String img;
    public String userID;
    public String fid;

    public OtherUsers() {
    }

    public OtherUsers(String name, String mobileNumber, String departmentID, String img, String userID, String fid) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.departmentID = departmentID;
        this.img = img;
        this.userID = userID;
        this.fid = fid;
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