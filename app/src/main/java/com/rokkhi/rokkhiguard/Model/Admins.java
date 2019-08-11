package com.rokkhi.rokkhiguard.Model;

import java.util.Date;
import java.util.List;

public class Admins {


    private String user_id;
    private String build_id;
    private String comm_id;
    private String who_add;
    private Date when_add;
    private String type;
    private List<String> a_array;

    public Admins() {
    }

    public Admins(String user_id, String build_id, String comm_id, String who_add, Date when_add, String type, List<String> a_array) {
        this.user_id = user_id;
        this.build_id = build_id;
        this.comm_id = comm_id;
        this.who_add = who_add;
        this.when_add = when_add;
        this.type = type;
        this.a_array = a_array;
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getWho_add() {
        return who_add;
    }

    public void setWho_add(String who_add) {
        this.who_add = who_add;
    }

    public Date getWhen_add() {
        return when_add;
    }

    public void setWhen_add(Date when_add) {
        this.when_add = when_add;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getA_array() {
        return a_array;
    }

    public void setA_array(List<String> a_array) {
        this.a_array = a_array;
    }
}
