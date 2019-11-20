package com.rokkhi.rokkhiguard.Model;

public class Types {

    private String english="none";  //userid
    private String bangla="none";
    private String type_id="none";


    public Types(){
    }

    public Types(String english, String bangla, String type_id) {
        this.english = english;
        this.bangla = bangla;
        this.type_id = type_id;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getBangla() {
        return bangla;
    }

    public void setBangla(String bangla) {
        this.bangla = bangla;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }
}