package com.lms.lmscommon.common;

public enum AppLogLevel {

    NONE(0),
    BASIC(1),
    HEADERS(2),
    BODY(3);

    private final int level;

    AppLogLevel(final int level) {
        this.level = level;
    }

    public boolean lte(AppLogLevel level) {
        return this.level <= level.level;
    }

}
