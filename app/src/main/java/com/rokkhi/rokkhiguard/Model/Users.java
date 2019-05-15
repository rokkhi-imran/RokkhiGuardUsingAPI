package com.rokkhi.rokkhiguard.Model;


import java.util.Date;
import java.util.List;


public class Users {

    private String name;
    private Date bday;
    private String gender;
    private String pic;
    private String thumb;
    private String phone;
    private String mail;
    private String token;
    private boolean istoken;
    private String userid; //auto id
    private List<String> u_array;




    public Users() {
    }

    public Users(String name, Date bday, String gender, String pic, String thumb, String phone, String mail, String token, boolean istoken, String userid, List<String> u_array) {
        this.name = name;
        this.bday = bday;
        this.gender = gender;
        this.pic = pic;
        this.thumb = thumb;
        this.phone = phone;
        this.mail = mail;
        this.token = token;
        this.istoken = istoken;
        this.userid = userid;
        this.u_array = u_array;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isIstoken() {
        return istoken;
    }

    public void setIstoken(boolean istoken) {
        this.istoken = istoken;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public List<String> getU_array() {
        return u_array;
    }

    public void setU_array(List<String> u_array) {
        this.u_array = u_array;
    }
}
