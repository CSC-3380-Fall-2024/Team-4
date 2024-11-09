package org.eru.enums;

public enum ProfileType {
    athena("athena"),
    commonCore("common_core"),
    collections("collections"),
    creative("creative");

    private final String value;

    ProfileType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}