package com.rokkhi.rokkhiguard.Model;

import java.util.Date;
import java.util.List;

public class CSecurity {

    private String mem_id; //from members
    private boolean isactivated;
    private boolean isallday;
    private int settings;
    private Date starttime;
    private Date endtime;
    private String m_name;
    private String m_pic;
    private String m_thumb;
    private Date m_bday;
    private String m_gender;
    private List<String> m_array;

    public CSecurity(){

    }

    public CSecurity(String mem_id, boolean isactivated, boolean isallday, int settings, Date starttime, Date endtime, String m_name, String m_pic, String m_thumb, Date m_bday, String m_gender, List<String> m_array) {
        this.mem_id = mem_id;
        this.isactivated = isactivated;
        this.isallday = isallday;
        this.settings = settings;
        this.starttime = starttime;
        this.endtime = endtime;
        this.m_name = m_name;
        this.m_pic = m_pic;
        this.m_thumb = m_thumb;
        this.m_bday = m_bday;
        this.m_gender = m_gender;
        this.m_array = m_array;
    }

    public String getMem_id() {
        return mem_id;
    }

    public void setMem_id(String mem_id) {
        this.mem_id = mem_id;
    }

    public boolean isIsactivated() {
        return isactivated;
    }

    public void setIsactivated(boolean isactivated) {
        this.isactivated = isactivated;
    }

    public boolean isIsallday() {
        return isallday;
    }

    public void setIsallday(boolean isallday) {
        this.isallday = isallday;
    }

    public int getSettings() {
        return settings;
    }

    public void setSettings(int settings) {
        this.settings = settings;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public String getM_pic() {
        return m_pic;
    }

    public void setM_pic(String m_pic) {
        this.m_pic = m_pic;
    }

    public String getM_thumb() {
        return m_thumb;
    }

    public void setM_thumb(String m_thumb) {
        this.m_thumb = m_thumb;
    }

    public Date getM_bday() {
        return m_bday;
    }

    public void setM_bday(Date m_bday) {
        this.m_bday = m_bday;
    }

    public String getM_gender() {
        return m_gender;
    }

    public void setM_gender(String m_gender) {
        this.m_gender = m_gender;
    }

    public List<String> getM_array() {
        return m_array;
    }

    public void setM_array(List<String> m_array) {
        this.m_array = m_array;
    }
}
