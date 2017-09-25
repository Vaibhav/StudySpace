package com.example.vaibhav.pennappsxvi_app;

import android.os.Build;

import com.google.android.gms.nearby.messages.Message;
import com.google.gson.Gson;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by Vaibhav on 2017-09-09.
 */

class DeviceMessage {

    private String mBuilding;
    private Room mRoom;

    public String getBuilding() {
        return mBuilding;
    }

    public void setBuilding(String building) {
        this.mBuilding = building;
    }

    public Room getRoom() {
        return mRoom;
    }

    public void setRoom(Room room) {
        this.mRoom = room;
    }
}


class Room implements Serializable {

    public int current_occupancy;
    public int maximum_occupancy;
    List<Student> students;

    public int getCurrentOccupancy() {
        return current_occupancy;
    }

    public void setCurrentOccupancy(int currentOccupancy) {
        this.current_occupancy = currentOccupancy;
    }

    public int getMaximumOccupancy() {
        return maximum_occupancy;
    }

    public void setMaximumOccupancy(int maximumOccupancy) {
        this.maximum_occupancy = maximumOccupancy;
    }


    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}


class Student {

    public String name;
    public long timestamp_in;
    public long timestamp_out;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimestampIn() {
        return timestamp_in;
    }

    public void setTimestampIn(long timestampIn) {
        this.timestamp_in = timestampIn;
    }

    public long getTimestampOut() {
        return timestamp_out;
    }

    public void setTimestampOut(long timestampOut) {
        this.timestamp_out = timestampOut;
    }

}