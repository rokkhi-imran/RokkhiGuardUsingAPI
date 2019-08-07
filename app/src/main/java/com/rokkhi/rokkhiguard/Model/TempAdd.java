package com.rokkhi.rokkhiguard.Model;

public class TempAdd {


    private String r_uid;
    private String name;
    private String phone;
    private String pic;
    private String flat_id;



    public TempAdd(){
    }


    public TempAdd(String r_uid, String name, String phone, String pic, String flat_id) {
        this.r_uid = r_uid;
        this.name = name;
        this.phone = phone;
        this.pic = pic;
        this.flat_id = flat_id;
    }

    public String getR_uid() {
        return r_uid;
    }

    public void setR_uid(String r_uid) {
        this.r_uid = r_uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getFlat_id() {
        return flat_id;
    }

    public void setFlat_id(String flat_id) {
        this.flat_id = flat_id;
    }
}
