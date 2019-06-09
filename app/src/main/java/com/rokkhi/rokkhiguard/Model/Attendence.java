package com.rokkhi.rokkhiguard.Model;

import java.util.Date;

public class Attendence {

    private String auto_id;
    private String employee_id;
    private String build_id;
    private String comm_id;
    private Date time;
    private String flat_id;
    private String f_no;
    private boolean isin;

    public Attendence(){
    }

    public Attendence(String auto_id, String employee_id, String build_id, String comm_id, Date time, String flat_id, String f_no, boolean isin) {
        this.auto_id = auto_id;
        this.employee_id = employee_id;
        this.build_id = build_id;
        this.comm_id = comm_id;
        this.time = time;
        this.flat_id = flat_id;
        this.f_no = f_no;
        this.isin = isin;
    }

    public String getAuto_id() {
        return auto_id;
    }

    public void setAuto_id(String auto_id) {
        this.auto_id = auto_id;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
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

    public boolean isIsin() {
        return isin;
    }

    public void setIsin(boolean isin) {
        this.isin = isin;
    }
}
