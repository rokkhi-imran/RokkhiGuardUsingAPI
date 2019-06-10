package com.rokkhi.rokkhiguard.Model;

import java.util.Date;

public class S_salary {

    private String s_id;
    private String flat_id;
    private String build_id;
    private String f_no;
    private int salary;
    private int daypermonth;

    public S_salary(){
    }

    public S_salary(String s_id, String flat_id, String build_id, String f_no, int salary, int daypermonth) {
        this.s_id = s_id;
        this.flat_id = flat_id;
        this.build_id = build_id;
        this.f_no = f_no;
        this.salary = salary;
        this.daypermonth = daypermonth;
    }


    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
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

    public String getF_no() {
        return f_no;
    }

    public void setF_no(String f_no) {
        this.f_no = f_no;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getDaypermonth() {
        return daypermonth;
    }

    public void setDaypermonth(int daypermonth) {
        this.daypermonth = daypermonth;
    }
}
