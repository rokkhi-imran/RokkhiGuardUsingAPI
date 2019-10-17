package com.rokkhi.rokkhiguard.Model;

import java.util.ArrayList;

public class Types {

    private String english="none";  //userid
    private String bangla="none";


    public Types(){
    }

    public Types(String english, String bangla) {
        this.english = english;
        this.bangla = bangla;
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
}