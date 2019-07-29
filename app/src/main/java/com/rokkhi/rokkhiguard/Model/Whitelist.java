package com.rokkhi.rokkhiguard.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;


@Entity
public class Whitelist implements Parcelable {

    @PrimaryKey
    @NotNull  private String w_uid;
    private String w_mail;
    private String flat_id;
    private String f_no;
    private String comm_id;
    private String build_id;
    private String user_id;
    private  String w_name;
    private  String w_phone;
    private  String w_pic;
    private  String w_thumb;
    private  String w_relationship;
    @Ignore private Date w_mtime;

    @Ignore private List<String> w_array;


    public Whitelist(){
    }

    public Whitelist(String w_mail, String flat_id, String f_no, String comm_id, String build_id, String user_id, String w_name, String w_phone, String w_pic, String w_thumb, String w_relationship, Date w_mtime, String w_uid, List<String> w_array) {
        this.w_mail = w_mail;
        this.flat_id = flat_id;
        this.f_no = f_no;
        this.comm_id = comm_id;
        this.build_id = build_id;
        this.user_id = user_id;
        this.w_name = w_name;
        this.w_phone = w_phone;
        this.w_pic = w_pic;
        this.w_thumb = w_thumb;
        this.w_relationship = w_relationship;
        this.w_mtime = w_mtime;
        this.w_uid = w_uid;
        this.w_array = w_array;
    }


    protected Whitelist(Parcel in) {
        w_mail = in.readString();
        flat_id = in.readString();
        f_no = in.readString();
        comm_id = in.readString();
        build_id = in.readString();
        user_id = in.readString();
        w_name = in.readString();
        w_phone = in.readString();
        w_pic = in.readString();
        w_thumb = in.readString();
        w_relationship = in.readString();
        w_uid = in.readString();
        w_array = in.createStringArrayList();
    }

    public static final Creator<Whitelist> CREATOR = new Creator<Whitelist>() {
        @Override
        public Whitelist createFromParcel(Parcel in) {
            return new Whitelist(in);
        }

        @Override
        public Whitelist[] newArray(int size) {
            return new Whitelist[size];
        }
    };

    public String getW_mail() {
        return w_mail;
    }

    public void setW_mail(String w_mail) {
        this.w_mail = w_mail;
    }

    public String getFlat_id() {
        return flat_id;
    }

    public void setFlat_id(String flat_id) {
        this.flat_id = flat_id;
    }

    public String getF_no() {
        return f_no;
    }

    public void setF_no(String f_no) {
        this.f_no = f_no;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getW_name() {
        return w_name;
    }

    public void setW_name(String w_name) {
        this.w_name = w_name;
    }

    public String getW_phone() {
        return w_phone;
    }

    public void setW_phone(String w_phone) {
        this.w_phone = w_phone;
    }

    public String getW_pic() {
        return w_pic;
    }

    public void setW_pic(String w_pic) {
        this.w_pic = w_pic;
    }

    public String getW_thumb() {
        return w_thumb;
    }

    public void setW_thumb(String w_thumb) {
        this.w_thumb = w_thumb;
    }

    public String getW_relationship() {
        return w_relationship;
    }

    public void setW_relationship(String w_relationship) {
        this.w_relationship = w_relationship;
    }

    public Date getW_mtime() {
        return w_mtime;
    }

    public void setW_mtime(Date w_mtime) {
        this.w_mtime = w_mtime;
    }

    public String getW_uid() {
        return w_uid;
    }

    public void setW_uid(String w_uid) {
        this.w_uid = w_uid;
    }

    public List<String> getW_array() {
        return w_array;
    }

    public void setW_array(List<String> w_array) {
        this.w_array = w_array;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(w_mail);
        dest.writeString(flat_id);
        dest.writeString(f_no);
        dest.writeString(comm_id);
        dest.writeString(build_id);
        dest.writeString(user_id);
        dest.writeString(w_name);
        dest.writeString(w_phone);
        dest.writeString(w_pic);
        dest.writeString(w_thumb);
        dest.writeString(w_relationship);
        dest.writeString(w_uid);
        dest.writeStringList(w_array);
    }
}
