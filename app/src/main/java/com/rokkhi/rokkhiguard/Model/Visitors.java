package com.rokkhi.rokkhiguard.Model;

import java.util.Date;
import java.util.List;

public class Visitors{

    private String v_phone;
    private String v_name;
    private String v_pic;
    private String v_thumb;
    private String v_purpose;
    private String v_mail;
    private String v_where;
    private String flat_id;
    private String f_no;
    private String comm_id;
    private String build_id;
    private String v_vehicleno;
    private String v_gpass;
    private Date v_checkin;
    private Date v_checkout;
    private String v_uid;
    private boolean isin;
    private int response;
    private String v_type;
    private List<String> v_array;





    public Visitors() {
    }


    public Visitors(String v_phone, String v_name, String v_pic, String v_thumb, String v_purpose, String v_mail, String v_where, String flat_id, String f_no, String comm_id, String build_id, String v_vehicleno, String v_gpass, Date v_checkin, Date v_checkout, String v_uid, boolean isin, int response, String v_type, List<String> v_array) {
        this.v_phone = v_phone;
        this.v_name = v_name;
        this.v_pic = v_pic;
        this.v_thumb = v_thumb;
        this.v_purpose = v_purpose;
        this.v_mail = v_mail;
        this.v_where = v_where;
        this.flat_id = flat_id;
        this.f_no = f_no;
        this.comm_id = comm_id;
        this.build_id = build_id;
        this.v_vehicleno = v_vehicleno;
        this.v_gpass = v_gpass;
        this.v_checkin = v_checkin;
        this.v_checkout = v_checkout;
        this.v_uid = v_uid;
        this.isin = isin;
        this.response = response;
        this.v_type = v_type;
        this.v_array = v_array;
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

    public String getV_pic() {
        return v_pic;
    }

    public void setV_pic(String v_pic) {
        this.v_pic = v_pic;
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

    public String getComm_id() {
        return comm_id;
    }

    public void setComm_id(String comm_id) {
        this.comm_id = comm_id;
    }

    public String getBuild_id() {
        return build_id;
    }

    public void setBuild_id(String build_id) {
        this.build_id = build_id;
    }

    public String getV_vehicleno() {
        return v_vehicleno;
    }

    public void setV_vehicleno(String v_vehicleno) {
        this.v_vehicleno = v_vehicleno;
    }

    public String getV_gpass() {
        return v_gpass;
    }

    public void setV_gpass(String v_gpass) {
        this.v_gpass = v_gpass;
    }

    public Date getV_checkin() {
        return v_checkin;
    }

    public void setV_checkin(Date v_checkin) {
        this.v_checkin = v_checkin;
    }

    public Date getV_checkout() {
        return v_checkout;
    }

    public void setV_checkout(Date v_checkout) {
        this.v_checkout = v_checkout;
    }

    public String getV_uid() {
        return v_uid;
    }

    public void setV_uid(String v_uid) {
        this.v_uid = v_uid;
    }

    public boolean isIsin() {
        return isin;
    }

    public void setIsin(boolean isin) {
        this.isin = isin;
    }

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public String getV_type() {
        return v_type;
    }

    public void setV_type(String v_type) {
        this.v_type = v_type;
    }

    public List<String> getV_array() {
        return v_array;
    }

    public void setV_array(List<String> v_array) {
        this.v_array = v_array;
    }
}
