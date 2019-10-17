package com.rokkhi.rokkhiguard.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UDetails {

    private String user_id="none";
    private String name="none";
    private String pic="none";
    private String thumb_pic="none";
    private Date bday=new Date();
    private String gender="none";
    private String mail="none";
    private boolean token=false;
    private String phone="none";
    private String flat_id="none";
    private String f_no="none";
    private String build_id="none";
    private String comm_id="none";
    private String who_add="none";
    private List<String> atoken= new ArrayList<>();
    private List<String> itoken= new ArrayList<>();
    private boolean admin=false;
    private Date fjoindate= new Date();
    private Date lastActive= new Date();
    private boolean active=false;
    private List<String> u_array= new ArrayList<>();

    public UDetails(){
    }

    public UDetails(String user_id, String name, String pic, String thumb_pic, Date bday, String gender, String mail, boolean token, String phone, String flat_id, String f_no, String build_id, String comm_id, String who_add, List<String> atoken, List<String> itoken, boolean admin, Date fjoindate, Date lastActive, boolean active, List<String> u_array) {
        this.user_id = user_id;
        this.name = name;
        this.pic = pic;
        this.thumb_pic = thumb_pic;
        this.bday = bday;
        this.gender = gender;
        this.mail = mail;
        this.token = token;
        this.phone = phone;
        this.flat_id = flat_id;
        this.f_no = f_no;
        this.build_id = build_id;
        this.comm_id = comm_id;
        this.who_add = who_add;
        this.atoken = atoken;
        this.itoken = itoken;
        this.admin = admin;
        this.fjoindate = fjoindate;
        this.lastActive = lastActive;
        this.active = active;
        this.u_array = u_array;
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getThumb_pic() {
        return thumb_pic;
    }

    public void setThumb_pic(String thumb_pic) {
        this.thumb_pic = thumb_pic;
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

    public boolean isToken() {
        return token;
    }

    public void setToken(boolean token) {
        this.token = token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getWho_add() {
        return who_add;
    }

    public void setWho_add(String who_add) {
        this.who_add = who_add;
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

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public Date getFjoindate() {
        return fjoindate;
    }

    public void setFjoindate(Date fjoindate) {
        this.fjoindate = fjoindate;
    }

    public Date getLastActive() {
        return lastActive;
    }

    public void setLastActive(Date lastActive) {
        this.lastActive = lastActive;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<String> getU_array() {
        return u_array;
    }

    public void setU_array(List<String> u_array) {
        this.u_array = u_array;
    }
}
