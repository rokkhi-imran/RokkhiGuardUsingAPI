package com.rokkhi.rokkhiguard.Model;

import java.util.Date;
import java.util.List;

public class Members {

    private boolean approve;
    private String m_id; //auto
    private String who_add; //uid
    private String m_name;
    private String m_phone;
    private String m_userid;
    private String m_pic;
    private String m_thumb;
    private Date m_bday;
    private String m_gender;
    private List<String> m_array;

    public Members(){
    }

    public Members(boolean approve, String m_id, String who_add, String m_name, String m_phone, String m_userid, String m_pic, String m_thumb, Date m_bday, String m_gender, List<String> m_array) {
        this.approve = approve;
        this.m_id = m_id;
        this.who_add = who_add;
        this.m_name = m_name;
        this.m_phone = m_phone;
        this.m_userid = m_userid;
        this.m_pic = m_pic;
        this.m_thumb = m_thumb;
        this.m_bday = m_bday;
        this.m_gender = m_gender;
        this.m_array = m_array;
    }

    public boolean isApprove() {
        return approve;
    }

    public void setApprove(boolean approve) {
        this.approve = approve;
    }

    public String getM_id() {
        return m_id;
    }

    public void setM_id(String m_id) {
        this.m_id = m_id;
    }

    public String getWho_add() {
        return who_add;
    }

    public void setWho_add(String who_add) {
        this.who_add = who_add;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public String getM_phone() {
        return m_phone;
    }

    public void setM_phone(String m_phone) {
        this.m_phone = m_phone;
    }

    public String getM_userid() {
        return m_userid;
    }

    public void setM_userid(String m_userid) {
        this.m_userid = m_userid;
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
