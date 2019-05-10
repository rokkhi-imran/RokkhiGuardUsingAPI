package com.rokkhi.rokkhiguard.Model;

public class GuardPhone {

    private String phone;  //userid
    private boolean activated;
    private String g_token;
    private String build_id;
    private String comm_id;
    private String mobilepass;

    public GuardPhone(){

    }

    public GuardPhone(String phone, boolean activated, String g_token, String build_id, String comm_id, String mobilepass) {
        this.phone = phone;
        this.activated = activated;
        this.g_token = g_token;
        this.build_id = build_id;
        this.comm_id = comm_id;
        this.mobilepass = mobilepass;
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

    public String getMobilepass() {
        return mobilepass;
    }

    public void setMobilepass(String mobilepass) {
        this.mobilepass = mobilepass;
    }
}