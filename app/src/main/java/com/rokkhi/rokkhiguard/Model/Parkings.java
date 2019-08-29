package com.rokkhi.rokkhiguard.Model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;




public class Parkings {

    @PrimaryKey @NotNull
    private String flat_id;
    private String build_id;
    private String f_no;
    private boolean vacant;
    private Date lastTime;
    private String vehicleid;
    @Ignore
    private List<String> p_array;

    public Parkings(){
    }

    public Parkings(@NotNull String flat_id, String build_id, String f_no, boolean vacant, Date lastTime, String vehicleid, List<String> p_array) {
        this.flat_id = flat_id;
        this.build_id = build_id;
        this.f_no = f_no;
        this.vacant = vacant;
        this.lastTime = lastTime;
        this.vehicleid = vehicleid;
        this.p_array = p_array;
    }

    @NotNull
    public String getFlat_id() {
        return flat_id;
    }

    public void setFlat_id(@NotNull String flat_id) {
        this.flat_id = flat_id;
    }

    public String getBuild_id() {
        return build_id;
    }

    public void setBuild_id(String build_id) {
        this.build_id = build_id;
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

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public String getVehicleid() {
        return vehicleid;
    }

    public void setVehicleid(String vehicleid) {
        this.vehicleid = vehicleid;
    }

    public List<String> getP_array() {
        return p_array;
    }

    public void setP_array(List<String> p_array) {
        this.p_array = p_array;
    }
}
