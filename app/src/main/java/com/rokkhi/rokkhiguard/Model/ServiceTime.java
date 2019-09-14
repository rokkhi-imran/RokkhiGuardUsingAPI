package com.rokkhi.rokkhiguard.Model;

import java.util.Date;

public class ServiceTime {

    private int totalDay=0;
    private int totalTimes=0;
    private String s_id="none";
    private String flatid="none";
    private String f_no="none";
    private Date lastday= new Date();

    public ServiceTime(){
    }


    public ServiceTime(int totalDay, int totalTimes, String s_id, String flatid, String f_no, Date lastday) {
        this.totalDay = totalDay;
        this.totalTimes = totalTimes;
        this.s_id = s_id;
        this.flatid = flatid;
        this.f_no = f_no;
        this.lastday = lastday;
    }

    public int getTotalDay() {
        return totalDay;
    }

    public void setTotalDay(int totalDay) {
        this.totalDay = totalDay;
    }

    public int getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(int totalTimes) {
        this.totalTimes = totalTimes;
    }

    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }

    public String getFlatid() {
        return flatid;
    }

    public void setFlatid(String flatid) {
        this.flatid = flatid;
    }

    public String getF_no() {
        return f_no;
    }

    public void setF_no(String f_no) {
        this.f_no = f_no;
    }

    public Date getLastday() {
        return lastday;
    }

    public void setLastday(Date lastday) {
        this.lastday = lastday;
    }
}
