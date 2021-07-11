package com.ushwamala.simplebankingapp.model;


public enum Status {
    SUBMITTED("submitted"),
    PENDING("pending"),
    APPROVED("approved"),
    REJECTED("rejected");

    public final String value;

    Status(String value) {
        this.value = value;
    }
}
