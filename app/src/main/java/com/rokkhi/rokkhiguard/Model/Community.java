package com.rokkhi.rokkhiguard.Model;

import java.util.List;

public class Community {


    private String comm_id;
    private String c_name;
    private String c_mail;
    private String c_tbuilding;
    private String c_tgate;
    private String c_internet;
    private String c_dish;
    private String c_type;
    private boolean c_status;
    private List<String> c_array;
    private List<String> contacts;
    private List<String> address;

    public Community(){

    }

    public Community(String comm_id, String c_name, String c_mail, String c_tbuilding, String c_tgate, String c_internet, String c_dish, String c_type, boolean c_status, List<String> c_array, List<String> contacts, List<String> address) {
        this.comm_id = comm_id;
        this.c_name = c_name;
        this.c_mail = c_mail;
        this.c_tbuilding = c_tbuilding;
        this.c_tgate = c_tgate;
        this.c_internet = c_internet;
        this.c_dish = c_dish;
        this.c_type = c_type;
        this.c_status = c_status;
        this.c_array = c_array;
        this.contacts = contacts;
        this.address = address;
    }


    public String getComm_id() {
        return comm_id;
    }

    public void setComm_id(String comm_id) {
        this.comm_id = comm_id;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getC_mail() {
        return c_mail;
    }

    public void setC_mail(String c_mail) {
        this.c_mail = c_mail;
    }

    public String getC_tbuilding() {
        return c_tbuilding;
    }

    public void setC_tbuilding(String c_tbuilding) {
        this.c_tbuilding = c_tbuilding;
    }

    public String getC_tgate() {
        return c_tgate;
    }

    public void setC_tgate(String c_tgate) {
        this.c_tgate = c_tgate;
    }

    public String getC_internet() {
        return c_internet;
    }

    public void setC_internet(String c_internet) {
        this.c_internet = c_internet;
    }

    public String getC_dish() {
        return c_dish;
    }

    public void setC_dish(String c_dish) {
        this.c_dish = c_dish;
    }

    public String getC_type() {
        return c_type;
    }

    public void setC_type(String c_type) {
        this.c_type = c_type;
    }

    public boolean isC_status() {
        return c_status;
    }

    public void setC_status(boolean c_status) {
        this.c_status = c_status;
    }

    public List<String> getC_array() {
        return c_array;
    }

    public void setC_array(List<String> c_array) {
        this.c_array = c_array;
    }

    public List<String> getContacts() {
        return contacts;
    }

    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }

    public List<String> getAddress() {
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }
}
