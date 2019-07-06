package com.rokkhi.rokkhiguard.Model;

import java.util.Date;
import java.util.List;

public class Visvehicle {

    private String build_id;
    private String comm_id;
    private String flat_id;
    private String f_no;
    private String phone;
    private String vehicle_number;
    private Date checkin;
    private Date checkout;
    private String name;
    private String pic;
    private String thumb;
    private String id; //auto
    private boolean in;
    private List<String> vehicle_array;

    public Visvehicle(){
    }

    public Visvehicle(String build_id, String comm_id, String flat_id, String f_no, String phone, String vehicle_number, Date checkin, Date checkout, String name, String pic, String thumb, String id, boolean in, List<String> vehicle_array) {
        this.build_id = build_id;
        this.comm_id = comm_id;
        this.flat_id = flat_id;
        this.f_no = f_no;
        this.phone = phone;
        this.vehicle_number = vehicle_number;
        this.checkin = checkin;
        this.checkout = checkout;
        this.name = name;
        this.pic = pic;
        this.thumb = thumb;
        this.id = id;
        this.in = in;
        this.vehicle_array = vehicle_array;
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

    public String getFlat_id() {
        return flat_id;
    }

    public void setFlat_id(String flat_id) {
        this.flat_id = flat_id;
    }

    public String getF_no() {
        return f_no;
    }

    public void setF_no(String f_no) {
        this.f_no = f_no;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVehicle_number() {
        return vehicle_number;
    }

    public void setVehicle_number(String vehicle_number) {
        this.vehicle_number = vehicle_number;
    }

    public Date getCheckin() {
        return checkin;
    }

    public void setCheckin(Date checkin) {
        this.checkin = checkin;
    }

    public Date getCheckout() {
        return checkout;
    }

    public void setCheckout(Date checkout) {
        this.checkout = checkout;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isIn() {
        return in;
    }

    public void setIn(boolean in) {
        this.in = in;
    }

    public List<String> getVehicle_array() {
        return vehicle_array;
    }

    public void setVehicle_array(List<String> vehicle_array) {
        this.vehicle_array = vehicle_array;
    }
}
