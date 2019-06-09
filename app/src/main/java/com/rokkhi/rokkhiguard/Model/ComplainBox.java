package com.rokkhi.rokkhiguard.Model;

import java.util.Date;

public class ComplainBox {

    private String subject;
    private String user_id;
    private String flat_id;
    private String f_no;
    private String build_id;
    private String comm_id;
    private String body;
    private Date when;


    public ComplainBox() {

    }

    public ComplainBox(String subject, String user_id, String flat_id, String f_no, String build_id, String comm_id, String body, Date when) {
        this.subject = subject;
        this.user_id = user_id;
        this.flat_id = flat_id;
        this.f_no = f_no;
        this.build_id = build_id;
        this.comm_id = comm_id;
        this.body = body;
        this.when = when;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }
}
