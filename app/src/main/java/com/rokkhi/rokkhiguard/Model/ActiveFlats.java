package com.rokkhi.rokkhiguard.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.List;


@Entity
public class ActiveFlats {

    @PrimaryKey
    @NotNull  private String flat_id;

    private String build_id;
    private String comm_id;
    private String f_no;
    private boolean vacant;
    @Ignore  private List<String> f_array;

    public ActiveFlats(){

    }

    public ActiveFlats(String flat_id, String build_id, String comm_id, String f_no, boolean vacant, List<String> f_array) {
        this.flat_id = flat_id;
        this.build_id = build_id;
        this.comm_id = comm_id;
        this.f_no = f_no;
        this.vacant = vacant;
        this.f_array = f_array;
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

    public boolean isVacant() {
        return vacant;
    }

    public void setVacant(boolean vacant) {
        this.vacant = vacant;
    }

    public List<String> getF_array() {
        return f_array;
    }

    public void setF_array(List<String> f_array) {
        this.f_array = f_array;
    }
}
