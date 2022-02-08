package com.cocoon.enums;

public enum ProductStatus {
    ACTIVE("Active"), PASSIVE("Passive");

    private final String value;

    private ProductStatus(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
