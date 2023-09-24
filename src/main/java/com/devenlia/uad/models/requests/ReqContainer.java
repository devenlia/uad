package com.devenlia.uad.models.requests;

import com.devenlia.uad.models.Container;
import lombok.Data;

@Data
public class ReqContainer {
    private String parent;
    private Container container;
}
