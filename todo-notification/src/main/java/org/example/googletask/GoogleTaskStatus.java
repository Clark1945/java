package org.example.googletask;

public enum GoogleTaskStatus {
    NEEDS_ACTION("needsAction"), COMPLETED("completed");

    private String status;
    GoogleTaskStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
}
