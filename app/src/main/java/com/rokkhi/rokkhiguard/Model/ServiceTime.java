package com.rokkhi.rokkhiguard.Model;

import java.util.Date;

public class ServiceTime {

    private int totalDay;
    private String employee_id;
    private String flatid;
    private Date lastday;

    public ServiceTime(){
    }

    public ServiceTime(int totalDay, String employee_id, String flatid, Date lastday) {
        this.totalDay = totalDay;
        this.employee_id = employee_id;
        this.flatid = flatid;
        this.lastday = lastday;
    }

    public int getTotalDay() {
        return totalDay;
    }

    public void setTotalDay(int totalDay) {
        this.totalDay = totalDay;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getFlatid() {
        return flatid;
    }

    public void setFlatid(String flatid) {
        this.flatid = flatid;
    }

    public Date getLastday() {
        return lastday;
    }

    public void setLastday(Date lastday) {
        this.lastday = lastday;
    }
}
