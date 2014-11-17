package com.sinkwater.chicken.objects;

/**
 * Created by justin on 11/16/14.
 */
import java.util.Vector;
import com.sinkwater.chicken.GPSData;

public class Organization {

    private String name;
    private String unique_identifier;
    private String general_identifier;
    private Vector<String> members;
    //private Vector<Attendance> attendance; TODO. should I store attendance data here??????...or no idk yet
    private GPSData location;

    public Organization(String name, String unique_identifier, String general_identifier,
                        Vector<String> members, GPSData location) {
        this.name = name;
        this.unique_identifier = unique_identifier;
        this.general_identifier = general_identifier;
        this.members = members;
        this.location = location;
    }

    //Accessors
    public String getName() {
        return this.name;
    }
    public String getUniqueIden() {
        return this.unique_identifier;
    }
    public String getGeneralIden() {
        return this.general_identifier;
    }
    public Vector<String> getMembers() {
        return this.members;
    }
    public GPSData getLocation() {
        return this.location;
    }

    //Mutators
    public void setName(String name) {
        this.name = name;
    }
    public void setUniqueIden(String unique_identifier) {
        this.unique_identifier = unique_identifier;
    }
    public void setGeneralIden(String general_identifier) {
        this.general_identifier = general_identifier;
    }
    public void setMembers(Vector<String> members) {
        this.members = members;
    }
    public void setLocation(GPSData location) {
        this.location = location;
    }

}
