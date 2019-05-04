package com.rokkhi.rokkhiguard.Model;

import java.util.Date;

public class Reports {

    private String subject;
    private String user_id;
    private String flat_id;
    private String body;
    private Date when;


    public Reports() {

    }

    public Reports(String subject, String user_id, String flat_id, String body, Date when) {
        this.subject = subject;
        this.user_id = user_id;
        this.flat_id = flat_id;
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
