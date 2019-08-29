package com.rokkhi.rokkhiguard.Model;


import java.util.List;


public class Users {

    private String name;
    private String thumb;
    private String user_id; //auto id
    private List<String> u_array;

    public Users() {
    }

    public Users(String name, String thumb, String user_id, List<String> u_array) {
        this.name = name;
        this.thumb = thumb;
        this.user_id = user_id;
        this.u_array = u_array;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<String> getU_array() {
        return u_array;
    }

    public void setU_array(List<String> u_array) {
        this.u_array = u_array;
    }
}
