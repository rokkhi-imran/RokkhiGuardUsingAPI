package com.rokkhi.rokkhiguard.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Visitors implements Serializable {

    private String v_phone="none";
    private String v_name="none";
    private String v_pic="none";
    private String thumb_v_pic="none";
    private String v_purpose="none";
    private String v_mail="none";
    private String v_where="none";
    private String flat_id="none";
    private String f_no="none";
    private String comm_id="none";
    private String build_id="none";
    private String v_vehicleno="none";
    private String v_gpass="none";
    private Date time= new Date();
    private String another_uid="none";
    private String v_uid="none";
    private String statusOfEntry="none";
    private boolean in=false; //aste aste obsolete hye jabe
    private boolean completed=false;
    private String response="none";
    private String v_type="none";
    private List<String> v_array=new ArrayList<>();
    private String responder="none";


    public Visitors() {
    }


    public Visitors(String v_phone, String v_name, String v_pic, String thumb_v_pic, String v_purpose, String v_mail, String v_where, String flat_id, String f_no, String comm_id, String build_id, String v_vehicleno, String v_gpass, Date time, String another_uid, String v_uid, String statusOfEntry, boolean in, boolean completed, String response, String v_type, List<String> v_array, String responder) {
        this.v_phone = v_phone;
        this.v_name = v_name;
        this.v_pic = v_pic;
        this.thumb_v_pic = thumb_v_pic;
        this.v_purpose = v_purpose;
        this.v_mail = v_mail;
        this.v_where = v_where;
        this.flat_id = flat_id;
        this.f_no = f_no;
        this.comm_id = comm_id;
        this.build_id = build_id;
        this.v_vehicleno = v_vehicleno;
        this.v_gpass = v_gpass;
        this.time = time;
        this.another_uid = another_uid;
        this.v_uid = v_uid;
        this.statusOfEntry = statusOfEntry;
        this.in = in;
        this.completed = completed;
        this.response = response;
        this.v_type = v_type;
        this.v_array = v_array;
        this.responder = responder;
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

    public String getThumb_v_pic() {
        return thumb_v_pic;
    }

    public void setThumb_v_pic(String thumb_v_pic) {
        this.thumb_v_pic = thumb_v_pic;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getAnother_uid() {
        return another_uid;
    }

    public void setAnother_uid(String another_uid) {
        this.another_uid = another_uid;
    }

    public String getV_uid() {
        return v_uid;
    }

    public void setV_uid(String v_uid) {
        this.v_uid = v_uid;
    }

    public String getStatusOfEntry() {
        return statusOfEntry;
    }

    public void setStatusOfEntry(String statusOfEntry) {
        this.statusOfEntry = statusOfEntry;
    }

    public boolean isIn() {
        return in;
    }

    public void setIn(boolean in) {
        this.in = in;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
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

    public String getResponder() {
        return responder;
    }

    public void setResponder(String responder) {
        this.responder = responder;
    }
}
