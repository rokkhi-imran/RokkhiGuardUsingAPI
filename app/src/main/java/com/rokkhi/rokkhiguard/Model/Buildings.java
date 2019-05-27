package com.rokkhi.rokkhiguard.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Buildings implements Parcelable {

    private String b_name;
    private List<String>  b_contacts;
    private String b_flatformat;
    private String b_houseno;
    private String b_roadno;
    private String b_district;
    private String b_area;
    private int b_tfloor;
    private int b_tflat;
    private int b_tparking;
    private int b_tgate;
    private String build_id;
    private String comm_id;
    private int b_servicecharge;
    private List<String> b_array;

    public Buildings(){
    }

    public Buildings(String b_name, List<String> b_contacts, String b_flatformat, String b_houseno, String b_roadno, String b_district, String b_area, int b_tfloor, int b_tflat, int b_tparking, int b_tgate, String build_id, String comm_id, int b_servicecharge, List<String> b_array) {
        this.b_name = b_name;
        this.b_contacts = b_contacts;
        this.b_flatformat = b_flatformat;
        this.b_houseno = b_houseno;
        this.b_roadno = b_roadno;
        this.b_district = b_district;
        this.b_area = b_area;
        this.b_tfloor = b_tfloor;
        this.b_tflat = b_tflat;
        this.b_tparking = b_tparking;
        this.b_tgate = b_tgate;
        this.build_id = build_id;
        this.comm_id = comm_id;
        this.b_servicecharge = b_servicecharge;
        this.b_array = b_array;
    }

    protected Buildings(Parcel in) {
        b_name = in.readString();
        b_contacts = in.createStringArrayList();
        b_flatformat = in.readString();
        b_houseno = in.readString();
        b_roadno = in.readString();
        b_district = in.readString();
        b_area = in.readString();
        b_tfloor = in.readInt();
        b_tflat = in.readInt();
        b_tparking = in.readInt();
        b_tgate = in.readInt();
        build_id = in.readString();
        comm_id = in.readString();
        b_servicecharge = in.readInt();
        b_array = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(b_name);
        dest.writeStringList(b_contacts);
        dest.writeString(b_flatformat);
        dest.writeString(b_houseno);
        dest.writeString(b_roadno);
        dest.writeString(b_district);
        dest.writeString(b_area);
        dest.writeInt(b_tfloor);
        dest.writeInt(b_tflat);
        dest.writeInt(b_tparking);
        dest.writeInt(b_tgate);
        dest.writeString(build_id);
        dest.writeString(comm_id);
        dest.writeInt(b_servicecharge);
        dest.writeStringList(b_array);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Buildings> CREATOR = new Creator<Buildings>() {
        @Override
        public Buildings createFromParcel(Parcel in) {
            return new Buildings(in);
        }

        @Override
        public Buildings[] newArray(int size) {
            return new Buildings[size];
        }
    };

    public String getB_name() {
        return b_name;
    }

    public void setB_name(String b_name) {
        this.b_name = b_name;
    }

    public List<String> getB_contacts() {
        return b_contacts;
    }

    public void setB_contacts(List<String> b_contacts) {
        this.b_contacts = b_contacts;
    }

    public String getB_flatformat() {
        return b_flatformat;
    }

    public void setB_flatformat(String b_flatformat) {
        this.b_flatformat = b_flatformat;
    }

    public String getB_houseno() {
        return b_houseno;
    }

    public void setB_houseno(String b_houseno) {
        this.b_houseno = b_houseno;
    }

    public String getB_roadno() {
        return b_roadno;
    }

    public void setB_roadno(String b_roadno) {
        this.b_roadno = b_roadno;
    }

    public String getB_district() {
        return b_district;
    }

    public void setB_district(String b_district) {
        this.b_district = b_district;
    }

    public String getB_area() {
        return b_area;
    }

    public void setB_area(String b_area) {
        this.b_area = b_area;
    }

    public int getB_tfloor() {
        return b_tfloor;
    }

    public void setB_tfloor(int b_tfloor) {
        this.b_tfloor = b_tfloor;
    }

    public int getB_tflat() {
        return b_tflat;
    }

    public void setB_tflat(int b_tflat) {
        this.b_tflat = b_tflat;
    }

    public int getB_tparking() {
        return b_tparking;
    }

    public void setB_tparking(int b_tparking) {
        this.b_tparking = b_tparking;
    }

    public int getB_tgate() {
        return b_tgate;
    }

    public void setB_tgate(int b_tgate) {
        this.b_tgate = b_tgate;
    }

    public String getBuild_id() {
        return build_id;
    }

    public void setBuild_id(String build_id) {
        this.build_id = build_id;
    }

    public String getComm_id() {
        return comm_id;
    }

    public void setComm_id(String comm_id) {
        this.comm_id = comm_id;
    }

    public int getB_servicecharge() {
        return b_servicecharge;
    }

    public void setB_servicecharge(int b_servicecharge) {
        this.b_servicecharge = b_servicecharge;
    }

    public List<String> getB_array() {
        return b_array;
    }

    public void setB_array(List<String> b_array) {
        this.b_array = b_array;
    }
}
