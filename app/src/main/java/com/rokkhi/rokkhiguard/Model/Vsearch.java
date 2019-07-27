package com.rokkhi.rokkhiguard.Model;

import java.util.Date;

public class Vsearch {

    private String v_phone;
    private String v_name;
    private String v_thumb;
    private String v_purpose;
    private String v_mail;
    private String v_where;
    private String flat_id;
    private String f_no;
    private Date lastDate;
    private int totalVisit;

    public Vsearch() {
    }

    public Vsearch(String v_phone, String v_name, String v_thumb, String v_purpose, String v_mail, String v_where, String flat_id, String f_no, Date lastDate) {
        this.v_phone = v_phone;
        this.v_name = v_name;
        this.v_thumb = v_thumb;
        this.v_purpose = v_purpose;
        this.v_mail = v_mail;
        this.v_where = v_where;
        this.flat_id = flat_id;
        this.f_no = f_no;
        this.lastDate = lastDate;
    }

    public Vsearch(String v_phone, String v_name, String v_thumb, String v_purpose, String v_mail, String v_where, String flat_id, String f_no, Date lastDate, int totalVisit) {
        this.v_phone = v_phone;
        this.v_name = v_name;
        this.v_thumb = v_thumb;
        this.v_purpose = v_purpose;
        this.v_mail = v_mail;
        this.v_where = v_where;
        this.flat_id = flat_id;
        this.f_no = f_no;
        this.lastDate = lastDate;
        this.totalVisit = totalVisit;
    }


    public String getV_phone() {
        return v_phone;
    }

    public void setV_phone(String v_phone) {
        this.v_phone = v_phone;
    }

    public String getV_name() {
        return v_name;
    }

    public void setV_name(String v_name) {
        this.v_name = v_name;
    }

    public String getV_thumb() {
        return v_thumb;
    }

    public void setV_thumb(String v_thumb) {
        this.v_thumb = v_thumb;
    }

    public String getV_purpose() {
        return v_purpose;
    }

    public void setV_purpose(String v_purpose) {
        this.v_purpose = v_purpose;
    }

    public String getV_mail() {
        return v_mail;
    }

    public void setV_mail(String v_mail) {
        this.v_mail = v_mail;
    }

    public String getV_where() {
        return v_where;
    }

    public void setV_where(String v_where) {
        this.v_where = v_where;
    }

    public String getFlat_id() {
        return flat_id;
    }

    public void setFlat_id(String flat_id) {
        this.flat_id = flat_id;
    }

    public String getF_no() {
        return f_no;
    }

    public void setF_no(String f_no) {
        this.f_no = f_no;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    public int getTotalVisit() {
        return totalVisit;
    }

    public void setTotalVisit(int totalVisit) {
        this.totalVisit = totalVisit;
    }
}
