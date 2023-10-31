package com.devenlia.uad.models;

import lombok.Data;

@Data
public class SubPage {
    private String id;
    private String name;
    private String path;

    public SubPage(){}

    public SubPage(String name, String id, String path) {
        this.name = name;
        this.id = id;
        this.path = path;
    }
}
