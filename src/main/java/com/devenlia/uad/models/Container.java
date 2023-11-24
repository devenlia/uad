package com.devenlia.uad.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "containers")
public class Container {
    @Id
    private String id;
    private String parentId;
    private int sortIndex;
    private String name;
    @DBRef
    private List<Category> categories = new ArrayList<>();

    @JsonIgnore
    public boolean isValid() {
        return this.name != null && !this.name.isEmpty() && categories.stream().allMatch(Category::isValid);
    }
}
