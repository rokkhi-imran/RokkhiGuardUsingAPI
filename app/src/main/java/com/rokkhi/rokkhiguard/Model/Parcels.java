package com.rokkhi.rokkhiguard.Model;

import java.util.Date;
import java.util.List;

public class Parcels {

    private String build_id;
    private String comm_id;
    private String flat_id;
    private String family_id;
    private String p_com;
    private String g_uid;
    private Date p_rtime;
    private String p_type;
    private String p_pic;
    private String p_thumb;
    private String p_uid; //auto
    private List<String> p_array;

    public Parcels(){
    }

    public Parcels(String build_id, String comm_id, String flat_id, String family_id, String p_com, String g_uid, Date p_rtime, String p_type, String p_pic, String p_thumb, String p_uid, List<String> p_array) {
        this.build_id = build_id;
        this.comm_id = comm_id;
        this.flat_id = flat_id;
        this.family_id = family_id;
        this.p_com = p_com;
        this.g_uid = g_uid;
        this.p_rtime = p_rtime;
        this.p_type = p_type;
        this.p_pic = p_pic;
        this.p_thumb = p_thumb;
        this.p_uid = p_uid;
        this.p_array = p_array;
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

    public String getFlat_id() {
        return flat_id;
    }

    public void setFlat_id(String flat_id) {
        this.flat_id = flat_id;
    }

    public String getFamily_id() {
        return family_id;
    }

    public void setFamily_id(String family_id) {
        this.family_id = family_id;
    }

    public String getP_com() {
        return p_com;
    }

    public void setP_com(String p_com) {
        this.p_com = p_com;
    }

    public String getG_uid() {
        return g_uid;
    }

    public void setG_uid(String g_uid) {
        this.g_uid = g_uid;
    }

    public Date getP_rtime() {
        return p_rtime;
    }

    public void setP_rtime(Date p_rtime) {
        this.p_rtime = p_rtime;
    }

    public String getP_type() {
        return p_type;
    }

    public void setP_type(String p_type) {
        this.p_type = p_type;
    }

    public String getP_pic() {
        return p_pic;
    }

    public void setP_pic(String p_pic) {
        this.p_pic = p_pic;
    }

    public String getP_thumb() {
        return p_thumb;
    }

    public void setP_thumb(String p_thumb) {
        this.p_thumb = p_thumb;
    }

    public String getP_uid() {
        return p_uid;
    }

    public void setP_uid(String p_uid) {
        this.p_uid = p_uid;
    }

    public List<String> getP_array() {
        return p_array;
    }

    public void setP_array(List<String> p_array) {
        this.p_array = p_array;
    }
}
