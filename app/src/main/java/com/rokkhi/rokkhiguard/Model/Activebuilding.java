package com.rokkhi.rokkhiguard.Model;

import java.util.ArrayList;
import java.util.List;

public class Activebuilding {

    private String b_name="none";
    private String b_flatformat="none";
    private String b_houseno="none";
    private String b_roadno="none";
    private String b_district="none";
    private String b_area="none";
    private int b_tfloor=0;
    private int b_tflat=0;
    private String build_id="none"; //doc id
    private String comm_id="none";
    private boolean b_status=false;
    private List<String> b_array= new ArrayList<>();
    private List<String> picurl= new ArrayList<>();
    private double latitude=0.0;
    private double longitude=0.0;

    public Activebuilding(){
    }

    public Activebuilding(String b_name, String b_flatformat, String b_houseno, String b_roadno, String b_district, String b_area, int b_tfloor, int b_tflat, String build_id, String comm_id, boolean b_status, List<String> b_array, List<String> picurl, double latitude, double longitude) {
        this.b_name = b_name;
        this.b_flatformat = b_flatformat;
        this.b_houseno = b_houseno;
        this.b_roadno = b_roadno;
        this.b_district = b_district;
        this.b_area = b_area;
        this.b_tfloor = b_tfloor;
        this.b_tflat = b_tflat;
        this.build_id = build_id;
        this.comm_id = comm_id;
        this.b_status = b_status;
        this.b_array = b_array;
        this.picurl = picurl;
        this.latitude = latitude;
        this.longitude = longitude;
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
}
