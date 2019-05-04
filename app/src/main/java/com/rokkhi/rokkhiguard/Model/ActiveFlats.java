package com.rokkhi.rokkhiguard.Model;

import java.util.List;

public class ActiveFlats {

    private String flat_id;
    private String build_id;
    private String comm_id;
    private String f_no;
    private List<String> f_array;
    private String family_id;

    public ActiveFlats(){

    }

    public ActiveFlats(String flat_id, String build_id, String comm_id, String f_no, List<String> f_array, String family_id) {
        this.flat_id = flat_id;
        this.build_id = build_id;
        this.comm_id = comm_id;
        this.f_no = f_no;
        this.f_array = f_array;
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

    public String getF_no() {
        return f_no;
    }

    public void setF_no(String f_no) {
        this.f_no = f_no;
    }

    public List<String> getF_array() {
        return f_array;
    }

    public void setF_array(List<String> f_array) {
        this.f_array = f_array;
    }

    public String getFamily_id() {
        return family_id;
    }

    public void setFamily_id(String family_id) {
        this.family_id = family_id;
    }
}
