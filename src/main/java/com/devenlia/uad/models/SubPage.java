package com.devenlia.uad.models;

import lombok.Data;

@Data
public class SubPage {
    private String id;
    private int sortIndex;
    private String name;
    private String path;

    public SubPage(){}

    public SubPage(String id, int sortIndex, String name, String path) {
        this.id = id;
        this.sortIndex = sortIndex;
        this.name = name;
        this.path = path;
    }
}
