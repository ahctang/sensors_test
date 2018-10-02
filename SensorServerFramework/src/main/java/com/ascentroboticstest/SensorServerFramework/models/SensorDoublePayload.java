package com.ascentroboticstest.SensorServerFramework.models;

import java.util.Date;

public class SensorDoublePayload<T> {
    private String origin;
    private T payload;
    private String date;

    public SensorDoublePayload(String origin, T payload, String date) {
        this.origin = origin;
        this.payload = payload;
        this.date = date;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public Date getDate() {
        try {
            return new Date(Long.parseLong(date));
        }
        catch (Exception e) {
            return null;
        }
    }
}
