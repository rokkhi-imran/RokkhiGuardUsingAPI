package com.rokkhi.rokkhiguard.Model;

public class Requests {

    private String userid;
    private int response;
    private String flatid;
    private String famid;
    private boolean newfamily;


    public Requests(){
    }

    public Requests(String userid, int response, String flatid, String famid, boolean newfamily) {
        this.userid = userid;
        this.response = response;
        this.flatid = flatid;
        this.famid = famid;
        this.newfamily = newfamily;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public String getFlatid() {
        return flatid;
    }

    public void setFlatid(String flatid) {
        this.flatid = flatid;
    }

    public String getFamid() {
        return famid;
    }

    public void setFamid(String famid) {
        this.famid = famid;
    }

    public boolean isNewfamily() {
        return newfamily;
    }

    public void setNewfamily(boolean newfamily) {
        this.newfamily = newfamily;
    }
}
