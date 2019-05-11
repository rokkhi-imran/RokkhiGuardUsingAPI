package com.rokkhi.rokkhiguard.Model;

import java.util.List;

public class Attendence {

    private String auto_id;
    private String employee_id;
    private String build_id;
    private String comm_id;
    private String e_checkin;
    private String e_checkout;
    private List<String> flat_array;
    private boolean isin;

    public Attendence(){

    }

    public Attendence(String auto_id, String employee_id, String build_id, String comm_id, String e_checkin, String e_checkout, List<String> flat_array, boolean isin) {
        this.auto_id = auto_id;
        this.employee_id = employee_id;
        this.build_id = build_id;
        this.comm_id = comm_id;
        this.e_checkin = e_checkin;
        this.e_checkout = e_checkout;
        this.flat_array = flat_array;
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

    public String getE_checkin() {
        return e_checkin;
    }

    public void setE_checkin(String e_checkin) {
        this.e_checkin = e_checkin;
    }

    public String getE_checkout() {
        return e_checkout;
    }

    public void setE_checkout(String e_checkout) {
        this.e_checkout = e_checkout;
    }

    public List<String> getFlat_array() {
        return flat_array;
    }

    public void setFlat_array(List<String> flat_array) {
        this.flat_array = flat_array;
    }

    public boolean isIsin() {
        return isin;
    }

    public void setIsin(boolean isin) {
        this.isin = isin;
    }
}
