package com.rokkhi.rokkhiguard.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class BlackList {

    @PrimaryKey
    @NotNull String userID = "none";
    String email = "none";
    String flatID = "none";
    String flatNo = "none";
    String commID = "none";
    String buildID = "none";
    String name = "none";
    String phone = "none";
    String b_pic = "none";
    String b_thumb_pic = "none";
    @Ignore
    Date b_mTime=new Date();
    String docID = "none";
   @Ignore List<String> b_array = new ArrayList<>();

    public BlackList() {
    }

    public BlackList(String email, String flatID, String flatNo, String commID, String buildID, String userID, String name, String phone, String b_pic, String b_thumb_pic, Date b_mTime, String docID, List<String> b_array) {
        this.email = email;
        this.flatID = flatID;
        this.flatNo = flatNo;
        this.commID = commID;
        this.buildID = buildID;
        this.userID = userID;
        this.name = name;
        this.phone = phone;
        this.b_pic = b_pic;
        this.b_thumb_pic = b_thumb_pic;
        this.b_mTime = b_mTime;
        this.docID = docID;
        this.b_array = b_array;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFlatID() {
        return flatID;
    }

    public void setFlatID(String flatID) {
        this.flatID = flatID;
    }

    public String getFlatNo() {
        return flatNo;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    public String getCommID() {
        return commID;
    }

    public void setCommID(String commID) {
        this.commID = commID;
    }

    public String getBuildID() {
        return buildID;
    }

    public void setBuildID(String buildID) {
        this.buildID = buildID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getB_pic() {
        return b_pic;
    }

    public void setB_pic(String b_pic) {
        this.b_pic = b_pic;
    }

    public String getB_thumb_pic() {
        return b_thumb_pic;
    }

    public void setB_thumb_pic(String b_thumb_pic) {
        this.b_thumb_pic = b_thumb_pic;
    }

    public Date getB_mTime() {
        return b_mTime;
    }

    public void setB_mTime(Date b_mTime) {
        this.b_mTime = b_mTime;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public List<String> getB_array() {
        return b_array;
    }

    public void setB_array(List<String> b_array) {
        this.b_array = b_array;
    }

}
