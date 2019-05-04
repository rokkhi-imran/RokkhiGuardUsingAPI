package com.rokkhi.rokkhiguard.Model;

public class Settings {

    private String build_id; // document id
    private String comm_id;
    private boolean gCanSeePhone;
    private boolean h_status;
    private int h_fee;
    private String mobilepass;
    private int isgedit;

    public Settings(){

    }

    public Settings(String build_id, String comm_id, boolean gCanSeePhone, boolean h_status, int h_fee, String mobilepass, int isgedit) {
        this.build_id = build_id;
        this.comm_id = comm_id;
        this.gCanSeePhone = gCanSeePhone;
        this.h_status = h_status;
        this.h_fee = h_fee;
        this.mobilepass = mobilepass;
        this.isgedit = isgedit;
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

    public boolean isgCanSeePhone() {
        return gCanSeePhone;
    }

    public void setgCanSeePhone(boolean gCanSeePhone) {
        this.gCanSeePhone = gCanSeePhone;
    }

    public boolean isH_status() {
        return h_status;
    }

    public void setH_status(boolean h_status) {
        this.h_status = h_status;
    }

    public int getH_fee() {
        return h_fee;
    }

    public void setH_fee(int h_fee) {
        this.h_fee = h_fee;
    }

    public String getMobilepass() {
        return mobilepass;
    }

    public void setMobilepass(String mobilepass) {
        this.mobilepass = mobilepass;
    }

    public int getIsgedit() {
        return isgedit;
    }

    public void setIsgedit(int isgedit) {
        this.isgedit = isgedit;
    }
}
