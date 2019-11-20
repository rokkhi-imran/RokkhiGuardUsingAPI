package com.rokkhi.rokkhiguard.Model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Users {

    private String name="none";
    private String thumb="none";
    private String pic="none";
    private String user_id="none"; //auto id
    private Date bday=new Date();
    private String gender="none";
    private String mail="none";
    private String phone="none";
    private Date joindate=new Date();
    private List<String> u_array= new ArrayList<>();

    public Users() {
    }

    public Users(String name, String thumb, String pic, String user_id, Date bday, String gender, String mail, String phone, Date joindate, List<String> u_array) {
        this.name = name;
        this.thumb = thumb;
        this.pic = pic;
        this.user_id = user_id;
        this.bday = bday;
        this.gender = gender;
        this.mail = mail;
        this.phone = phone;
        this.joindate = joindate;
        this.u_array = u_array;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Date getBday() {
        return bday;
    }

    public void setBday(Date bday) {
        this.bday = bday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getJoindate() {
        return joindate;
    }

    public void setJoindate(Date joindate) {
        this.joindate = joindate;
    }

    public List<String> getU_array() {
        return u_array;
    }

    public void setU_array(List<String> u_array) {
        this.u_array = u_array;
    }
}
