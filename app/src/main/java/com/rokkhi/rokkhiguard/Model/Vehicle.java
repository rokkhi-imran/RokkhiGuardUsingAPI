package com.rokkhi.rokkhiguard.Model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

@Entity

public class Vehicle implements Parcelable {

    @PrimaryKey
    @NotNull
    private String vehicle_id; //auto
    private String description;
    private String type;
    private String vehicle_number;
    private String pic;
    private String thumb_pic;
    private String whose; //userid
    private String flat_id;
    private String f_no;
    private String build_id;
    @Ignore
    private Date lastin;
    @Ignore private Date lastout;
    @Ignore
    private List<String> vehicle_array;

    public Vehicle(){
    }

    public Vehicle(String description, String type, String vehicle_number, String pic, String thumb_pic, String vehicle_id, String whose, String flat_id, String f_no, String build_id, Date lastin, Date lastout, List<String> vehicle_array) {
        this.description = description;
        this.type = type;
        this.vehicle_number = vehicle_number;
        this.pic = pic;
        this.thumb_pic = thumb_pic;
        this.vehicle_id = vehicle_id;
        this.whose = whose;
        this.flat_id = flat_id;
        this.f_no = f_no;
        this.build_id = build_id;
        this.lastin = lastin;
        this.lastout = lastout;
        this.vehicle_array = vehicle_array;
    }

    protected Vehicle(Parcel in) {
        description = in.readString();
        type = in.readString();
        vehicle_number = in.readString();
        pic = in.readString();
        thumb_pic = in.readString();
        vehicle_id = in.readString();
        whose = in.readString();
        flat_id = in.readString();
        f_no = in.readString();
        build_id = in.readString();
        vehicle_array = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(type);
        dest.writeString(vehicle_number);
        dest.writeString(pic);
        dest.writeString(thumb_pic);
        dest.writeString(vehicle_id);
        dest.writeString(whose);
        dest.writeString(flat_id);
        dest.writeString(f_no);
        dest.writeString(build_id);
        dest.writeStringList(vehicle_array);
    }

    @Override
    public int describeContents() {
        return 0;
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

    @NotNull
    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(@NotNull String vehicle_id) {
        this.vehicle_id = vehicle_id;
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

    public String getThumb_pic() {
        return thumb_pic;
    }

    public void setThumb_pic(String thumb_pic) {
        this.thumb_pic = thumb_pic;
    }

    public String getWhose() {
        return whose;
    }

    public void setWhose(String whose) {
        this.whose = whose;
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

    public Date getLastin() {
        return lastin;
    }

    public void setLastin(Date lastin) {
        this.lastin = lastin;
    }

    public Date getLastout() {
        return lastout;
    }

    public void setLastout(Date lastout) {
        this.lastout = lastout;
    }

    public List<String> getVehicle_array() {
        return vehicle_array;
    }

    public void setVehicle_array(List<String> vehicle_array) {
        this.vehicle_array = vehicle_array;
    }

    public static Creator<Vehicle> getCREATOR() {
        return CREATOR;
    }
}