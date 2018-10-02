package common;

import java.util.Date;

public class SensorPayload<T> {
    private String origin;
    private T payload;
    private Date date;

    public SensorPayload(String origin, T payload) {
        this.origin = origin;
        this.payload = payload;
        this.date = new Date();
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

    public Date getDate() {
        return date;
    }
}
