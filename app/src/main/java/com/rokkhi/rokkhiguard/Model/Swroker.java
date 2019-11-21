package com.rokkhi.rokkhiguard.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Swroker {
    private String s_id="none";//
    private  String s_name="none";//
    private  String s_phone="none";//
    private  String s_pic="none";//
    private  String thumb_s_pic="none";//
    private String s_mail="none";//
    private Date s_bday=new Date();//
    private Date experience=new Date();//
    private int starttime=0;//
    private int endtime=0;//
    private String nid="none";//
    private String type="none";//
    private String s_pass="none";//
    private String who_add="none";
    private Date when_add=new Date();
    private List<String> address=new ArrayList<>();//
    private List<String> s_array=new ArrayList<>();//

    public Swroker(){
    }

    public Swroker(String s_id, String s_name, String s_phone, String s_pic, String thumb_s_pic, String s_mail, Date s_bday, Date experience, int starttime, int endtime, String nid, String type, String s_pass, String who_add, Date when_add, List<String> address, List<String> s_array) {
        this.s_id = s_id;
        this.s_name = s_name;
        this.s_phone = s_phone;
        this.s_pic = s_pic;
        this.thumb_s_pic = thumb_s_pic;
        this.s_mail = s_mail;
        this.s_bday = s_bday;
        this.experience = experience;
        this.starttime = starttime;
        this.endtime = endtime;
        this.nid = nid;
        this.type = type;
        this.s_pass = s_pass;
        this.who_add = who_add;
        this.when_add = when_add;
        this.address = address;
        this.s_array = s_array;
    }


    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public String getS_phone() {
        return s_phone;
    }

    public void setS_phone(String s_phone) {
        this.s_phone = s_phone;
    }

    public String getS_pic() {
        return s_pic;
    }

    public void setS_pic(String s_pic) {
        this.s_pic = s_pic;
    }

    public String getThumb_s_pic() {
        return thumb_s_pic;
    }

    public void setThumb_s_pic(String thumb_s_pic) {
        this.thumb_s_pic = thumb_s_pic;
    }

    public String getS_mail() {
        return s_mail;
    }

    public void setS_mail(String s_mail) {
        this.s_mail = s_mail;
    }

    public Date getS_bday() {
        return s_bday;
    }

    public void setS_bday(Date s_bday) {
        this.s_bday = s_bday;
    }

    public Date getExperience() {
        return experience;
    }

    public void setExperience(Date experience) {
        this.experience = experience;
    }

    public int getStarttime() {
        return starttime;
    }

    public void setStarttime(int starttime) {
        this.starttime = starttime;
    }

    public int getEndtime() {
        return endtime;
    }

    public void setEndtime(int endtime) {
        this.endtime = endtime;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getS_pass() {
        return s_pass;
    }

    public void setS_pass(String s_pass) {
        this.s_pass = s_pass;
    }

    public String getWho_add() {
        return who_add;
    }

    public void setWho_add(String who_add) {
        this.who_add = who_add;
    }

    public Date getWhen_add() {
        return when_add;
    }

    public void setWhen_add(Date when_add) {
        this.when_add = when_add;
    }

    public List<String> getAddress() {
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }

    public List<String> getS_array() {
        return s_array;
    }

    public void setS_array(List<String> s_array) {
        this.s_array = s_array;
    }
}
