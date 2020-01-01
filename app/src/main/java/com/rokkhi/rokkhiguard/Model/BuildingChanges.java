package com.rokkhi.rokkhiguard.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BuildingChanges {


    //sobgula array tei guardphone er UI thakbe... konta change hoy bujhar jnno
    private ArrayList<String> flats= new ArrayList<>();
    private ArrayList<String> whitelists= new ArrayList<>();
    private ArrayList<String> blacklists= new ArrayList<>();
    private ArrayList<String> vehicles= new ArrayList<>();

    public BuildingChanges() {
    }

    /*public BuildingChanges(ArrayList<String> flats, ArrayList<String> whitelists, ArrayList<String> vehicles) {
        this.flats = flats;
        this.whitelists = whitelists;
        this.vehicles = vehicles;
    }*/

    public BuildingChanges(ArrayList<String> flats, ArrayList<String> whitelists, ArrayList<String> blacklists, ArrayList<String> vehicles) {
        this.flats = flats;
        this.whitelists = whitelists;
        this.blacklists = blacklists;
        this.vehicles = vehicles;
    }

    public ArrayList<String> getBlacklists() {
        return blacklists;
    }

    public void setBlacklists(ArrayList<String> blacklists) {
        this.blacklists = blacklists;
    }

    public ArrayList<String> getFlats() {
        return flats;
    }

    public void setFlats(ArrayList<String> flats) {
        this.flats = flats;
    }

    public ArrayList<String> getWhitelists() {
        return whitelists;
    }

    public void setWhitelists(ArrayList<String> whitelists) {
        this.whitelists = whitelists;
    }

    public ArrayList<String> getVehicles() {
        return vehicles;
    }

    public void setVehicles(ArrayList<String> vehicles) {
        this.vehicles = vehicles;
    }
}
