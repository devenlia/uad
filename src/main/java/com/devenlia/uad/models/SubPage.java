package com.devenlia.uad.models;

import lombok.Data;

@Data
public class SubPage {
    private String id;
    private String name;

    public SubPage(){}

    public SubPage(String name, String id) {
        this.name = name;
        this.id = id;
    }
}
