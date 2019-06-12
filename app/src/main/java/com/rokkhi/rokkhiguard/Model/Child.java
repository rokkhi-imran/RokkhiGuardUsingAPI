package com.rokkhi.rokkhiguard.Model;

import java.util.Date;
import java.util.List;

public class Child {

    private String mem_id; //from members
    private String who_add;
    private String calling_number;
    private String flat_id;
    private String build_id;
    private String f_no;
    private boolean isactivated;
    private Date endtime;
    private String m_name;
    private String m_pic;
    private String m_thumb;
    private Date m_bday;
    private String m_gender;
    private List<String> m_array;

    public Child(){
    }


    public Child(String mem_id, String who_add, String calling_number, String flat_id, String build_id, String f_no, boolean isactivated, Date endtime, String m_name, String m_pic, String m_thumb, Date m_bday, String m_gender, List<String> m_array) {
        this.mem_id = mem_id;
        this.who_add = who_add;
        this.calling_number = calling_number;
        this.flat_id = flat_id;
        this.build_id = build_id;
        this.f_no = f_no;
        this.isactivated = isactivated;
        this.endtime = endtime;
        this.m_name = m_name;
        this.m_pic = m_pic;
        this.m_thumb = m_thumb;
        this.m_bday = m_bday;
        this.m_gender = m_gender;
        this.m_array = m_array;
    }

    public String getMem_id() {
        return mem_id;
    }

    public void setMem_id(String mem_id) {
        this.mem_id = mem_id;
    }

    public String getWho_add() {
        return who_add;
    }

    public void setWho_add(String who_add) {
        this.who_add = who_add;
    }

    public String getCalling_number() {
        return calling_number;
    }

    public void setCalling_number(String calling_number) {
        this.calling_number = calling_number;
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

    public String getF_no() {
        return f_no;
    }

    public void setF_no(String f_no) {
        this.f_no = f_no;
    }

    public boolean isIsactivated() {
        return isactivated;
    }

    public void setIsactivated(boolean isactivated) {
        this.isactivated = isactivated;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public String getM_pic() {
        return m_pic;
    }

    public void setM_pic(String m_pic) {
        this.m_pic = m_pic;
    }

    public String getM_thumb() {
        return m_thumb;
    }

    public void setM_thumb(String m_thumb) {
        this.m_thumb = m_thumb;
    }

    public Date getM_bday() {
        return m_bday;
    }

    public void setM_bday(Date m_bday) {
        this.m_bday = m_bday;
    }

    public String getM_gender() {
        return m_gender;
    }

    public void setM_gender(String m_gender) {
        this.m_gender = m_gender;
    }

    public List<String> getM_array() {
        return m_array;
    }

    public void setM_array(List<String> m_array) {
        this.m_array = m_array;
    }
}