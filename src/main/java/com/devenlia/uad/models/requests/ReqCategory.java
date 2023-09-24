package com.devenlia.uad.models.requests;

import com.devenlia.uad.models.Category;
import lombok.Data;

@Data
public class ReqCategory {
    private String parent;
    private Category category;
}
