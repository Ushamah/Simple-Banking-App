package com.ushwamala.simplebankingapp.model.enums;

public enum ContractType {
    PERMANENT("permanent"),
    SEASONAL("seasonal");

    public final String value;

    ContractType(String value) {
        this.value = value;
    }
}
