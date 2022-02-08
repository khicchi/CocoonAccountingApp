package com.cocoon.enums;

public enum Unit {

    LIBRE("Libre"), GALON("Galon"), PIECES("Pieces"), KILOGRAM("Kilogram"),
    METER("Meter"), INCH("Inch"), FEET("Feet");

    private final String value;

    private Unit(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
