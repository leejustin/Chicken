package com.sinkwater.chicken.objects;

/**
 * Created by justin on 11/16/14.
 */
import com.sinkwater.chicken.objects.Organization;

public class AdminPerson extends Person {

    //Keep track if it's the user's first time or not
    private Organization organization;

    //Superclass contains name and general identifier
    public AdminPerson(String name, String uniqueID, Organization org) {
        super();
        this.name = name;
        this.uniqueID = uniqueID;
        this.organization = org;
    }

    //Get the organization
    public Organization getOrganization() {
        return this.organization;
    }
    //Set the organization
    public void setOrganization(Organization org) {
        this.organization = org;
    }



}
