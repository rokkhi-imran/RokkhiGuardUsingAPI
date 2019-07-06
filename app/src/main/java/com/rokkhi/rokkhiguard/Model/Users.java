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
    private List<String> atoken;
    private List<String> itoken;
    private boolean token;
    private String user_id; //auto id
    private List<String> u_array;

    public Users() {
    }

    public Users(String name, Date bday, String gender, String pic, String thumb, String phone, String mail, List<String> atoken, List<String> itoken, boolean token, String user_id, List<String> u_array) {
        this.name = name;
        this.bday = bday;
        this.gender = gender;
        this.pic = pic;
        this.thumb = thumb;
        this.phone = phone;
        this.mail = mail;
        this.atoken = atoken;
        this.itoken = itoken;
        this.token = token;
        this.user_id = user_id;
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

    public List<String> getAtoken() {
        return atoken;
    }

    public void setAtoken(List<String> atoken) {
        this.atoken = atoken;
    }

    public List<String> getItoken() {
        return itoken;
    }

    public void setItoken(List<String> itoken) {
        this.itoken = itoken;
    }

    public boolean isToken() {
        return token;
    }

    public void setToken(boolean token) {
        this.token = token;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<String> getU_array() {
        return u_array;
    }

    public void setU_array(List<String> u_array) {
        this.u_array = u_array;
    }
}
