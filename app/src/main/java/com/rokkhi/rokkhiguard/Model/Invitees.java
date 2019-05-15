package com.rokkhi.rokkhiguard.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

public class Invitees implements Parcelable {

    private String i_mail;
    private String flat_id;
    private String comm_id;
    private String build_id;
    private  String i_name;
    private  String i_phone;
    private  String i_purpose;
    private  String i_pic;
    private  String i_thumb;
    private Date i_mtime;
    private int i_tmem;
    private String i_uid;
    private String i_token;
    private boolean hasdone;
    private List<String> i_array;


    public Invitees(){
    }

    public Invitees(String i_mail, String flat_id, String comm_id, String build_id,  String i_name, String i_phone, String i_purpose, String i_pic, String i_thumb, Date i_mtime, int i_tmem, String i_uid, String i_token, boolean hasdone, List<String> i_array) {
        this.i_mail = i_mail;
        this.flat_id = flat_id;
        this.comm_id = comm_id;
        this.build_id = build_id;
        this.i_name = i_name;
        this.i_phone = i_phone;
        this.i_purpose = i_purpose;
        this.i_pic = i_pic;
        this.i_thumb = i_thumb;
        this.i_mtime = i_mtime;
        this.i_tmem = i_tmem;
        this.i_uid = i_uid;
        this.i_token = i_token;
        this.hasdone = hasdone;
        this.i_array = i_array;
    }

    protected Invitees(Parcel in) {
        i_mail = in.readString();
        flat_id = in.readString();
        comm_id = in.readString();
        build_id = in.readString();
        i_name = in.readString();
        i_phone = in.readString();
        i_purpose = in.readString();
        i_pic = in.readString();
        i_thumb = in.readString();
        i_tmem = in.readInt();
        i_uid = in.readString();
        i_token = in.readString();
        hasdone = in.readByte() != 0;
        i_array = in.createStringArrayList();
    }

    public static final Creator<Invitees> CREATOR = new Creator<Invitees>() {
        @Override
        public Invitees createFromParcel(Parcel in) {
            return new Invitees(in);
        }

        @Override
        public Invitees[] newArray(int size) {
            return new Invitees[size];
        }
    };

    public String getI_mail() {
        return i_mail;
    }

    public void setI_mail(String i_mail) {
        this.i_mail = i_mail;
    }

    public String getFlat_id() {
        return flat_id;
    }

    public void setFlat_id(String flat_id) {
        this.flat_id = flat_id;
    }

    public String getComm_id() {
        return comm_id;
    }

    public void setComm_id(String comm_id) {
        this.comm_id = comm_id;
    }

    public String getBuild_id() {
        return build_id;
    }

    public void setBuild_id(String build_id) {
        this.build_id = build_id;
    }



    public String getI_name() {
        return i_name;
    }

    public void setI_name(String i_name) {
        this.i_name = i_name;
    }

    public String getI_phone() {
        return i_phone;
    }

    public void setI_phone(String i_phone) {
        this.i_phone = i_phone;
    }

    public String getI_purpose() {
        return i_purpose;
    }

    public void setI_purpose(String i_purpose) {
        this.i_purpose = i_purpose;
    }

    public String getI_pic() {
        return i_pic;
    }

    public void setI_pic(String i_pic) {
        this.i_pic = i_pic;
    }

    public String getI_thumb() {
        return i_thumb;
    }

    public void setI_thumb(String i_thumb) {
        this.i_thumb = i_thumb;
    }

    public Date getI_mtime() {
        return i_mtime;
    }

    public void setI_mtime(Date i_mtime) {
        this.i_mtime = i_mtime;
    }

    public int getI_tmem() {
        return i_tmem;
    }

    public void setI_tmem(int i_tmem) {
        this.i_tmem = i_tmem;
    }

    public String getI_uid() {
        return i_uid;
    }

    public void setI_uid(String i_uid) {
        this.i_uid = i_uid;
    }

    public String getI_token() {
        return i_token;
    }

    public void setI_token(String i_token) {
        this.i_token = i_token;
    }

    public boolean isHasdone() {
        return hasdone;
    }

    public void setHasdone(boolean hasdone) {
        this.hasdone = hasdone;
    }

    public List<String> getI_array() {
        return i_array;
    }

    public void setI_array(List<String> i_array) {
        this.i_array = i_array;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(i_mail);
        dest.writeString(flat_id);
        dest.writeString(comm_id);
        dest.writeString(build_id);
        dest.writeString(i_name);
        dest.writeString(i_phone);
        dest.writeString(i_purpose);
        dest.writeString(i_pic);
        dest.writeString(i_thumb);
        dest.writeInt(i_tmem);
        dest.writeString(i_uid);
        dest.writeString(i_token);
        dest.writeByte((byte) (hasdone ? 1 : 0));
        dest.writeStringList(i_array);
    }


}
