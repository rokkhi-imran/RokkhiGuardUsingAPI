package com.rokkhi.rokkhiguard.Model;

import java.util.ArrayList;

public class GuardPhone {

    private String phone;  //userid
    private boolean activated;
    private String g_token;
    private String comm_id;
    private String mobilepass;
    private ArrayList<String> build_array;

    public GuardPhone(){
    }

    public GuardPhone(String phone, boolean activated, String g_token, String comm_id, String mobilepass, ArrayList<String> build_array) {
        this.phone = phone;
        this.activated = activated;
        this.g_token = g_token;
        this.comm_id = comm_id;
        this.mobilepass = mobilepass;
        this.build_array = build_array;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getG_token() {
        return g_token;
    }

    public void setG_token(String g_token) {
        this.g_token = g_token;
    }

    public String getComm_id() {
        return comm_id;
    }

    public void setComm_id(String comm_id) {
        this.comm_id = comm_id;
    }

    public String getMobilepass() {
        return mobilepass;
    }

    public void setMobilepass(String mobilepass) {
        this.mobilepass = mobilepass;
    }

    public ArrayList<String> getBuild_array() {
        return build_array;
    }

    public void setBuild_array(ArrayList<String> build_array) {
        this.build_array = build_array;
    }
}