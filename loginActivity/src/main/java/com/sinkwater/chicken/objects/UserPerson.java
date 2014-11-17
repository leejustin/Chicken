package com.sinkwater.chicken.objects;

/**
 * Created by justin on 11/16/14.
 */

import android.util.Pair;
import java.util.Vector;

public class UserPerson extends Person {

    //Vector of pairs
    private Vector<Pair<Organization, Integer>> attendance;

    //Superclass contains name and general identifier
    public UserPerson(String name, String uniqueID) {
        super();
        this.name = name;
        this.uniqueID = uniqueID;
        this.attendance = new Vector();
    }

    public Vector<Pair<Organization, Integer>> getAttendance() {
        return attendance;
    }

    public void addOrganization(Organization org) {
        Vector addVec = new Vector();
        Pair addPair = new Pair(org, 0);
        addVec.add(addPair);
    }

    //Find where the organization is. Increment it.
    public void increaseAttendance(Organization org) {
        Integer vecSize = attendance.size();

        for (Integer x = 0; x < vecSize; x++) {
            if (attendance.elementAt(x).first == org) {
                Integer addAttenNum = attendance.elementAt(x).second + 1;
                Pair addPair = new Pair<Organization, Integer>(org, addAttenNum);

                attendance = new Vector<Pair<Organization,Integer>>();
                attendance.add(addPair);
            }
        }
    }


}