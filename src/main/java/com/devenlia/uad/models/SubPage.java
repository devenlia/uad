package com.devenlia.uad.models;

import lombok.Data;

@Data
public class SubPage {
    private String id;
    private String name;
    private String path;

    public SubPage(){}

    public SubPage(String id, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }
}
