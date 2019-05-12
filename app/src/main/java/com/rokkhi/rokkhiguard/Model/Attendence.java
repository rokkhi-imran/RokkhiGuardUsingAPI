package com.rokkhi.rokkhiguard.Model;

import java.util.Date;
import java.util.List;

public class Attendence {

    private String auto_id;
    private String employee_id;
    private String build_id;
    private String comm_id;
    private Date time;
    private List<String> fam_array;
    private boolean isin;

    public Attendence(){

    }

    public Attendence(String auto_id, String employee_id, String build_id, String comm_id, Date time, List<String> fam_array, boolean isin) {
        this.auto_id = auto_id;
        this.employee_id = employee_id;
        this.build_id = build_id;
        this.comm_id = comm_id;
        this.time = time;
        this.fam_array = fam_array;
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

    public List<String> getFam_array() {
        return fam_array;
    }

    public void setFam_array(List<String> fam_array) {
        this.fam_array = fam_array;
    }

    public boolean isIsin() {
        return isin;
    }

    public void setIsin(boolean isin) {
        this.isin = isin;
    }
}
