package com.rokkhi.rokkhiguard.Model;

import java.util.Date;

public class SCharge {

    private String flat_id;
    private String f_no;
    private String build_id;
    private String receiver; //user_id
    private String s_id; //auto_id
    private int scharge_amount;
    private int paid_amount;
    private  boolean ispaid;
    private Date paidtime;
    private Date which_month;

    public SCharge(){
    }


    public SCharge(String flat_id, String f_no, String build_id, String receiver, String s_id, int scharge_amount, int paid_amount, boolean ispaid, Date paidtime, Date which_month) {
        this.flat_id = flat_id;
        this.f_no = f_no;
        this.build_id = build_id;
        this.receiver = receiver;
        this.s_id = s_id;
        this.scharge_amount = scharge_amount;
        this.paid_amount = paid_amount;
        this.ispaid = ispaid;
        this.paidtime = paidtime;
        this.which_month = which_month;
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

    public String getBuild_id() {
        return build_id;
    }

    public void setBuild_id(String build_id) {
        this.build_id = build_id;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }

    public int getScharge_amount() {
        return scharge_amount;
    }

    public void setScharge_amount(int scharge_amount) {
        this.scharge_amount = scharge_amount;
    }

    public int getPaid_amount() {
        return paid_amount;
    }

    public void setPaid_amount(int paid_amount) {
        this.paid_amount = paid_amount;
    }

    public boolean isIspaid() {
        return ispaid;
    }

    public void setIspaid(boolean ispaid) {
        this.ispaid = ispaid;
    }

    public Date getPaidtime() {
        return paidtime;
    }

    public void setPaidtime(Date paidtime) {
        this.paidtime = paidtime;
    }

    public Date getWhich_month() {
        return which_month;
    }

    public void setWhich_month(Date which_month) {
        this.which_month = which_month;
    }
}
