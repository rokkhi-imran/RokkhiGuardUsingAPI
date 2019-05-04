package com.rokkhi.rokkhiguard.Model;

import java.util.List;

public class Vehicle {

    private String description;
    private String type;
    private String vehicle_number;
    private String pic;
    private String thumb;
    private String vehicle_id; //auto
    private String whose; //auto
    private List<String> vehicle_array;

    public Vehicle(){
    }

    public Vehicle(String description, String type, String vehicle_number, String pic, String thumb, String vehicle_id, String whose, List<String> vehicle_array) {
        this.description = description;
        this.type = type;
        this.vehicle_number = vehicle_number;
        this.pic = pic;
        this.thumb = thumb;
        this.vehicle_id = vehicle_id;
        this.whose = whose;
        this.vehicle_array = vehicle_array;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVehicle_number() {
        return vehicle_number;
    }

    public void setVehicle_number(String vehicle_number) {
        this.vehicle_number = vehicle_number;
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

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getWhose() {
        return whose;
    }

    public void setWhose(String whose) {
        this.whose = whose;
    }

    public List<String> getVehicle_array() {
        return vehicle_array;
    }

    public void setVehicle_array(List<String> vehicle_array) {
        this.vehicle_array = vehicle_array;
    }
}
