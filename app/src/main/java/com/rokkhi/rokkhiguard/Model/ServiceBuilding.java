package com.rokkhi.rokkhiguard.Model;

import java.util.ArrayList;
import java.util.Date;

public class ServiceBuilding {

    private String build_id="none";  //userid
    private String s_id="none";
    private Date lastday=new Date();
    private ArrayList<String> search= new ArrayList<>();

    public ServiceBuilding(){
    }

    public ServiceBuilding(String build_id, String s_id, Date lastday, ArrayList<String> search) {
        this.build_id = build_id;
        this.s_id = s_id;
        this.lastday = lastday;
        this.search = search;
    }

    public String getBuild_id() {
        return build_id;
    }

    public void setBuild_id(String build_id) {
        this.build_id = build_id;
    }

    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }

    public Date getLastday() {
        return lastday;
    }

    public void setLastday(Date lastday) {
        this.lastday = lastday;
    }

    public ArrayList<String> getSearch() {
        return search;
    }

    public void setSearch(ArrayList<String> search) {
        this.search = search;
    }
}