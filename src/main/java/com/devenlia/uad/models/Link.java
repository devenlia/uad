package com.devenlia.uad.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "links")
public class Link {
    @Id
    private String id;
    private String parentId;
    private int sortIndex;
    private String name;
    private String href;

    @JsonIgnore
    public boolean isValid() {
        return this.name != null && !this.name.isEmpty();
    }
}