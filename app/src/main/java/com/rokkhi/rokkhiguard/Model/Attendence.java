package com.rokkhi.rokkhiguard.Model;

import java.util.Date;

public class Attendence {

    private String auto_id="none";
    private String s_id="none";
    private String build_id="none";
    private String comm_id="none";
    private Date time= new Date();
    private String flat_id="none";
    private String f_no="none";
    private boolean in=false;

    public Attendence(){
    }

    public Attendence(String auto_id, String s_id, String build_id, String comm_id, Date time, String flat_id, String f_no, boolean in) {
        this.auto_id = auto_id;
        this.s_id = s_id;
        this.build_id = build_id;
        this.comm_id = comm_id;
        this.time = time;
        this.flat_id = flat_id;
        this.f_no = f_no;
        this.in = in;
    }

    public String getAuto_id() {
        return auto_id;
    }

    public void setAuto_id(String auto_id) {
        this.auto_id = auto_id;
    }

    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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

    public boolean isIn() {
        return in;
    }

    public void setIn(boolean in) {
        this.in = in;
    }
}
