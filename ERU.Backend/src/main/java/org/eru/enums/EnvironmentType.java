package org.eru.enums;

public enum EnvironmentType {
    LIVE("live"),
    PROD("prod"),
    STAGE("stage"),
    GAMEDEV("gamedev"),
    CI("ci"),
    LOADTEST("loadtest"),
    LOCALHOST("localhost"),
    LOCALNET("localnet");

    private final String value;

    EnvironmentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}