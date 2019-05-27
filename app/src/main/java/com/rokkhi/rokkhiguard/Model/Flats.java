package com.rokkhi.rokkhiguard.Model;

import java.util.List;

public class Flats {

    private String flat_id;
    private String build_id;
    private String comm_id;
    private String f_no;
    private int f_troom;
    private int f_twashroom;
    private int f_tbelcony;
    private int f_tdd;
    private int f_size;
    private int f_rent;
    private String f_internet;
    private String f_dish;
    private String f_parking;
    private boolean isVaratia;
    private List<String> f_array;

    public Flats(){

    }

    public Flats(String flat_id, String build_id, String comm_id, String f_no, int f_troom, int f_twashroom, int f_tbelcony, int f_tdd, int f_size, int f_rent, String f_internet, String f_dish, String f_parking, boolean isVaratia, List<String> f_array) {
        this.flat_id = flat_id;
        this.build_id = build_id;
        this.comm_id = comm_id;
        this.f_no = f_no;
        this.f_troom = f_troom;
        this.f_twashroom = f_twashroom;
        this.f_tbelcony = f_tbelcony;
        this.f_tdd = f_tdd;
        this.f_size = f_size;
        this.f_rent = f_rent;
        this.f_internet = f_internet;
        this.f_dish = f_dish;
        this.f_parking = f_parking;
        this.isVaratia = isVaratia;
        this.f_array = f_array;
    }

    public String getFlat_id() {
        return flat_id;
    }

    public void setFlat_id(String flat_id) {
        this.flat_id = flat_id;
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

    public String getF_no() {
        return f_no;
    }

    public void setF_no(String f_no) {
        this.f_no = f_no;
    }

    public int getF_troom() {
        return f_troom;
    }

    public void setF_troom(int f_troom) {
        this.f_troom = f_troom;
    }

    public int getF_twashroom() {
        return f_twashroom;
    }

    public void setF_twashroom(int f_twashroom) {
        this.f_twashroom = f_twashroom;
    }

    public int getF_tbelcony() {
        return f_tbelcony;
    }

    public void setF_tbelcony(int f_tbelcony) {
        this.f_tbelcony = f_tbelcony;
    }

    public int getF_tdd() {
        return f_tdd;
    }

    public void setF_tdd(int f_tdd) {
        this.f_tdd = f_tdd;
    }

    public int getF_size() {
        return f_size;
    }

    public void setF_size(int f_size) {
        this.f_size = f_size;
    }

    public int getF_rent() {
        return f_rent;
    }

    public void setF_rent(int f_rent) {
        this.f_rent = f_rent;
    }

    public String getF_internet() {
        return f_internet;
    }

    public void setF_internet(String f_internet) {
        this.f_internet = f_internet;
    }

    public String getF_dish() {
        return f_dish;
    }

    public void setF_dish(String f_dish) {
        this.f_dish = f_dish;
    }

    public String getF_parking() {
        return f_parking;
    }

    public void setF_parking(String f_parking) {
        this.f_parking = f_parking;
    }

    public boolean isVaratia() {
        return isVaratia;
    }

    public void setVaratia(boolean varatia) {
        isVaratia = varatia;
    }

    public List<String> getF_array() {
        return f_array;
    }

    public void setF_array(List<String> f_array) {
        this.f_array = f_array;
    }
}
