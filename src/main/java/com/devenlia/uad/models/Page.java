package com.devenlia.uad.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "pages")
public class Page {
    @Id
    private String id;
    private String name;
    private List<SubPage> subpages = new ArrayList<>();
    @DBRef
    private List<Container> containers = new ArrayList<>();

    @JsonIgnore
    public boolean isValid() {
        return this.name != null && !this.name.isEmpty() && containers.stream().allMatch(Container::isValid);
    }
}

