package com.rokkhi.rokkhiguard.Model;

public class GuardPhone {

    private String phone;  //userid
    private String userid;
    private boolean activated;
    private String g_token;
    private String build_id;
    private String comm_id;

    public GuardPhone(){

    }

    public GuardPhone(String phone, String userid, boolean activated, String g_token, String build_id, String comm_id) {
        this.phone = phone;
        this.userid = userid;
        this.activated = activated;
        this.g_token = g_token;
        this.build_id = build_id;
        this.comm_id = comm_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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
}