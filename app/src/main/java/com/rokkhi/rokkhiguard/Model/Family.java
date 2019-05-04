package com.rokkhi.rokkhiguard.Model;

public class Family {

    //private String flat_id;
    private String family_id; //userid
    private String flat_id;
    private String build_id;
    private String comm_id;

    public Family(){

    }

    public Family(String family_id, String flat_id, String build_id, String comm_id) {
        this.family_id = family_id;
        this.flat_id = flat_id;
        this.build_id = build_id;
        this.comm_id = comm_id;
    }

    public String getFamily_id() {
        return family_id;
    }

    public void setFamily_id(String family_id) {
        this.family_id = family_id;
    }

    public String getFlat_id() {
        return flat_id;
    }

    public void setFlat_id(String flat_id) {
        this.flat_id = flat_id;
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
}
