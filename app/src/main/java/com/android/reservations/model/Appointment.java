package com.android.reservations.model;

import java.io.Serializable;

public class Appointment implements Serializable {

    private String id;
    private String uName;
    private String uId;
    private String uPhone;
    private String uLocation;
    private String cName;
    private String cDesc;
    private String cLocation;
    private String cPhone;
    private String cId;
    private long date;
    private String time;
    private String df;

    public Appointment() {
    }

    public Appointment(String id, String uName, String uId, String uPhone,String uLocation, String cName,
                       String cDesc, String cLocation, String cPhone, String cId,
                       String time, String df) {
        this.id = id;
        this.uName = uName;
        this.uId = uId;
        this.uPhone = uPhone;
        this.uLocation = uLocation;
        this.cName = cName;
        this.cDesc = cDesc;
        this.cLocation = cLocation;
        this.cPhone = cPhone;
        this.cId = cId;
        this.time = time;
        this.df = df;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getuPhone() {
        return uPhone;
    }

    public void setuPhone(String uPhone) {
        this.uPhone = uPhone;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getcDesc() {
        return cDesc;
    }

    public void setcDesc(String cDesc) {
        this.cDesc = cDesc;
    }

    public String getcLocation() {
        return cLocation;
    }

    public void setcLocation(String cLocation) {
        this.cLocation = cLocation;
    }

    public String getcPhone() {
        return cPhone;
    }

    public void setcPhone(String cPhone) {
        this.cPhone = cPhone;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getuLocation() {
        return uLocation;
    }

    public void setuLocation(String uLocation) {
        this.uLocation = uLocation;
    }

    public String getDf() {
        return df;
    }

    public void setDf(String df) {
        this.df = df;
    }
}
