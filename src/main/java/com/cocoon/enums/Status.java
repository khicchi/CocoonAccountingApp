package com.cocoon.enums;

public enum Status {
    ACTIVE("Active"), PASSIVE("Passive");

    private final String value;

    private Status(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
