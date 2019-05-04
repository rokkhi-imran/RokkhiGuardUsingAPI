package com.rokkhi.rokkhiguard.Model;

import java.util.List;

public class Buildings {

    private String b_name;
    private String b_contacts;
    private String b_flatformat;
    private String b_houseno;
    private String b_roadno;
    private String b_district;
    private String b_area;
    private int b_tfloor;
    private int b_tflat;
    private String b_tparking;
    private String b_tgate;
    private String build_id;
    private String comm_id;
    private String b_servicecharge;
    private List<String> b_array;

    public Buildings(){

    }

    public Buildings(String b_name, String b_contacts, String b_flatformat, String b_houseno, String b_roadno, String b_district, String b_area, int b_tfloor, int b_tflat, String b_tparking, String b_tgate, String build_id, String comm_id, String b_servicecharge, List<String> b_array) {
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


    public String getB_name() {
        return b_name;
    }

    public void setB_name(String b_name) {
        this.b_name = b_name;
    }

    public String getB_contacts() {
        return b_contacts;
    }

    public void setB_contacts(String b_contacts) {
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

    public String getB_tparking() {
        return b_tparking;
    }

    public void setB_tparking(String b_tparking) {
        this.b_tparking = b_tparking;
    }

    public String getB_tgate() {
        return b_tgate;
    }

    public void setB_tgate(String b_tgate) {
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

    public String getB_servicecharge() {
        return b_servicecharge;
    }

    public void setB_servicecharge(String b_servicecharge) {
        this.b_servicecharge = b_servicecharge;
    }

    public List<String> getB_array() {
        return b_array;
    }

    public void setB_array(List<String> b_array) {
        this.b_array = b_array;
    }
}
