package com.rokkhi.rokkhiguard.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Vehicle implements Parcelable {

    private String description;
    private String type;
    private String vehicle_number;
    private String pic;
    private String thumb;
    private String vehicle_id; //auto
    private String whose; //userid
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

    protected Vehicle(Parcel in) {
        description = in.readString();
        type = in.readString();
        vehicle_number = in.readString();
        pic = in.readString();
        thumb = in.readString();
        vehicle_id = in.readString();
        whose = in.readString();
        vehicle_array = in.createStringArrayList();
    }

    public static final Creator<Vehicle> CREATOR = new Creator<Vehicle>() {
        @Override
        public Vehicle createFromParcel(Parcel in) {
            return new Vehicle(in);
        }

        @Override
        public Vehicle[] newArray(int size) {
            return new Vehicle[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(type);
        dest.writeString(vehicle_number);
        dest.writeString(pic);
        dest.writeString(thumb);
        dest.writeString(vehicle_id);
        dest.writeString(whose);
        dest.writeStringList(vehicle_array);
    }
}
