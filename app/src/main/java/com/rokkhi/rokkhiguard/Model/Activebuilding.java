package com.rokkhi.rokkhiguard.Model;

import java.util.List;

public class Activebuilding {

    private String b_name;
    private String b_flatformat;
    private String b_houseno;
    private String b_roadno;
    private String b_district;
    private String b_area;
    private String b_code;
    private int b_tfloor;
    private int b_tflat;
    private String build_id; //doc id
    private String comm_id;
    private boolean b_status;
    private List<String> b_array;
    private List<String> picurl;
    private double latitude;
    private double longitude;
    private String b_roadname;

    public Activebuilding(){
    }

    public Activebuilding(String b_name, String b_flatformat, String b_houseno, String b_roadno, String b_district, String b_area, String b_code, int b_tfloor, int b_tflat, String build_id, String comm_id, boolean b_status, List<String> b_array, List<String> picurl, double latitude, double longitude, String b_roadname) {
        this.b_name = b_name;
        this.b_flatformat = b_flatformat;
        this.b_houseno = b_houseno;
        this.b_roadno = b_roadno;
        this.b_district = b_district;
        this.b_area = b_area;
        this.b_code = b_code;
        this.b_tfloor = b_tfloor;
        this.b_tflat = b_tflat;
        this.build_id = build_id;
        this.comm_id = comm_id;
        this.b_status = b_status;
        this.b_array = b_array;
        this.picurl = picurl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.b_roadname = b_roadname;
    }

    public String getB_name() {
        return b_name;
    }

    public void setB_name(String b_name) {
        this.b_name = b_name;
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

    public String getB_code() {
        return b_code;
    }

    public void setB_code(String b_code) {
        this.b_code = b_code;
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

    public boolean isB_status() {
        return b_status;
    }

    public void setB_status(boolean b_status) {
        this.b_status = b_status;
    }

    public List<String> getB_array() {
        return b_array;
    }

    public void setB_array(List<String> b_array) {
        this.b_array = b_array;
    }

    public List<String> getPicurl() {
        return picurl;
    }

    public void setPicurl(List<String> picurl) {
        this.picurl = picurl;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getB_roadname() {
        return b_roadname;
    }

    public void setB_roadname(String b_roadname) {
        this.b_roadname = b_roadname;
    }
}
