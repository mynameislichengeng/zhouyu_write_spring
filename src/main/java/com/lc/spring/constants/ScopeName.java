package com.lc.spring.constants;

public enum ScopeName {

    SINGLETON("singleton"),

    PROTOTYPE("protoType")
    ;

    private final String type;

    ScopeName(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
