package com.rokkhi.rokkhiguard.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SLastHistory {

    private String s_id="none";
    private String build_id="none";
    private ArrayList<String> flatsId= new ArrayList<>();
    private ArrayList<String> flatsNo= new ArrayList<>();
    private Date lastday=new Date();

    public SLastHistory(){
    }

    public SLastHistory(String s_id, String build_id, ArrayList<String> flatsId, ArrayList<String> flatsNo, Date lastday) {
        this.s_id = s_id;
        this.build_id = build_id;
        this.flatsId = flatsId;
        this.flatsNo = flatsNo;
        this.lastday = lastday;
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

    public ArrayList<String> getFlatsId() {
        return flatsId;
    }

    public void setFlatsId(ArrayList<String> flatsId) {
        this.flatsId = flatsId;
    }

    public ArrayList<String> getFlatsNo() {
        return flatsNo;
    }

    public void setFlatsNo(ArrayList<String> flatsNo) {
        this.flatsNo = flatsNo;
    }

    public Date getLastday() {
        return lastday;
    }

    public void setLastday(Date lastday) {
        this.lastday = lastday;
    }
}
