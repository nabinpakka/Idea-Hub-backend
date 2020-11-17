package com.example.IdeaHub.config;

public enum ApplicationUserPermission {
    READ_ALL_USERS("all:users"),
    READ_ALL_PUBLICATION("all:publication"),
    WRITE_PUBLICATION("write:publication"),

    LIST_SUBMITTED_PUBLICATION("list:submitted_publication"),

    REVIEW("publication:review"),
    ASSIGN_REVIEWER("assign:reviewer");

    private final String permissions;

    ApplicationUserPermission(String permissions) {
        this.permissions = permissions;
    }

    public String getPermission(){
        return permissions;
    }
}
