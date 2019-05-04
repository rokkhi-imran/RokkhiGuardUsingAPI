package com.rokkhi.rokkhiguard.Model;

public class Vsearch {

    private String v_phone;
    private String v_name;
    private String v_thumb;
    private String v_purpose;
    private String v_mail;
    private String v_org;
    private String o_mail;
    private String v_type;





    public Vsearch() {
    }

    public Vsearch(String v_phone, String v_name, String v_thumb, String v_purpose, String v_mail, String v_org, String o_mail, String v_type) {
        this.v_phone = v_phone;
        this.v_name = v_name;
        this.v_thumb = v_thumb;
        this.v_purpose = v_purpose;
        this.v_mail = v_mail;
        this.v_org = v_org;
        this.o_mail = o_mail;
        this.v_type = v_type;
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

    public String getV_org() {
        return v_org;
    }

    public void setV_org(String v_org) {
        this.v_org = v_org;
    }

    public String getO_mail() {
        return o_mail;
    }

    public void setO_mail(String o_mail) {
        this.o_mail = o_mail;
    }

    public String getV_type() {
        return v_type;
    }

    public void setV_type(String v_type) {
        this.v_type = v_type;
    }
}
