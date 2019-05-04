package com.rokkhi.rokkhiguard.Model;

import java.util.Date;

public class Notifications {

    private String who_add; //userid
    private String build_id;
    private String comm_id;
    private Date n_time;
    private String n_type;
    private String n_body;
    private String n_title;
    private String n_uid; //auto



    public Notifications(){
    }

    public Notifications(String who_add, String build_id, String comm_id, Date n_time, String n_type, String n_body, String n_title, String n_uid) {
        this.who_add = who_add;
        this.build_id = build_id;
        this.comm_id = comm_id;
        this.n_time = n_time;
        this.n_type = n_type;
        this.n_body = n_body;
        this.n_title = n_title;
        this.n_uid = n_uid;
    }

    public String getWho_add() {
        return who_add;
    }

    public void setWho_add(String who_add) {
        this.who_add = who_add;
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

    public Date getN_time() {
        return n_time;
    }

    public void setN_time(Date n_time) {
        this.n_time = n_time;
    }

    public String getN_type() {
        return n_type;
    }

    public void setN_type(String n_type) {
        this.n_type = n_type;
    }

    public String getN_body() {
        return n_body;
    }

    public void setN_body(String n_body) {
        this.n_body = n_body;
    }

    public String getN_title() {
        return n_title;
    }

    public void setN_title(String n_title) {
        this.n_title = n_title;
    }

    public String getN_uid() {
        return n_uid;
    }

    public void setN_uid(String n_uid) {
        this.n_uid = n_uid;
    }
}
