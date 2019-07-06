package com.rokkhi.rokkhiguard.Model;

import java.util.Date;
import java.util.List;

public class UDetails {

    private String user_id;
    private String flat_id;
    private String f_no;
    private String build_id;
    private String comm_id;
    private List<String> atoken;
    private List<String> itoken;
    private boolean admin;
    private Date fjoindate;

    public UDetails(){
    }

    public UDetails(String user_id, String flat_id, String f_no, String build_id, String comm_id, List<String> atoken, List<String> itoken, boolean admin, Date fjoindate) {
        this.user_id = user_id;
        this.flat_id = flat_id;
        this.f_no = f_no;
        this.build_id = build_id;
        this.comm_id = comm_id;
        this.atoken = atoken;
        this.itoken = itoken;
        this.admin = admin;
        this.fjoindate = fjoindate;
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
}
