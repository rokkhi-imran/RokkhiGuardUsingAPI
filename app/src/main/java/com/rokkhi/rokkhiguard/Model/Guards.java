package com.rokkhi.rokkhiguard.Model;


import java.util.Date;
import java.util.List;


public class Guards {

    private String build_id;
    private String comm_id;
    private String g_name;
    private String g_pass;
    private String g_nid;
    private Date g_joining;
    private Date g_bday;
    private String g_org;
    private String g_pic;
    private String g_thumb;
    private String g_phone;
    private String g_uid; //auto id
    private List<String> g_array;




    public Guards() {
    }

    public Guards(String build_id, String comm_id, String g_name, String g_pass, String g_nid, Date g_joining, Date g_bday, String g_org, String g_pic, String g_thumb, String g_phone, String g_uid, List<String> g_array) {
        this.build_id = build_id;
        this.comm_id = comm_id;
        this.g_name = g_name;
        this.g_pass = g_pass;
        this.g_nid = g_nid;
        this.g_joining = g_joining;
        this.g_bday = g_bday;
        this.g_org = g_org;
        this.g_pic = g_pic;
        this.g_thumb = g_thumb;
        this.g_phone = g_phone;
        this.g_uid = g_uid;
        this.g_array = g_array;
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

    public String getG_name() {
        return g_name;
    }

    public void setG_name(String g_name) {
        this.g_name = g_name;
    }

    public String getG_pass() {
        return g_pass;
    }

    public void setG_pass(String g_pass) {
        this.g_pass = g_pass;
    }

    public String getG_nid() {
        return g_nid;
    }

    public void setG_nid(String g_nid) {
        this.g_nid = g_nid;
    }

    public Date getG_joining() {
        return g_joining;
    }

    public void setG_joining(Date g_joining) {
        this.g_joining = g_joining;
    }

    public Date getG_bday() {
        return g_bday;
    }

    public void setG_bday(Date g_bday) {
        this.g_bday = g_bday;
    }

    public String getG_org() {
        return g_org;
    }

    public void setG_org(String g_org) {
        this.g_org = g_org;
    }

    public String getG_pic() {
        return g_pic;
    }

    public void setG_pic(String g_pic) {
        this.g_pic = g_pic;
    }

    public String getG_thumb() {
        return g_thumb;
    }

    public void setG_thumb(String g_thumb) {
        this.g_thumb = g_thumb;
    }

    public String getG_phone() {
        return g_phone;
    }

    public void setG_phone(String g_phone) {
        this.g_phone = g_phone;
    }

    public String getG_uid() {
        return g_uid;
    }

    public void setG_uid(String g_uid) {
        this.g_uid = g_uid;
    }

    public List<String> getG_array() {
        return g_array;
    }

    public void setG_array(List<String> g_array) {
        this.g_array = g_array;
    }
}
