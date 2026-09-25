package com.example.http.model;

public class SensorReadingDto {

    private String deviceId;
    private double value;
    private long timestamp;
    private String unit;
    private String location;
    private double batteryLevel;
    private String status;
    private String firmwareVersion;

    public SensorReadingDto() {
    }

    public SensorReadingDto(String deviceId, double value, long timestamp, String unit, String location,
                             double batteryLevel, String status, String firmwareVersion) {
        this.deviceId = deviceId;
        this.value = value;
        this.timestamp = timestamp;
        this.unit = unit;
        this.location = location;
        this.batteryLevel = batteryLevel;
        this.status = status;
        this.firmwareVersion = firmwareVersion;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }
}
