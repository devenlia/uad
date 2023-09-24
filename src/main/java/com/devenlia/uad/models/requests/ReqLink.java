package com.devenlia.uad.models.requests;

import com.devenlia.uad.models.Link;
import lombok.Data;

@Data
public class ReqLink {
    private String parent;
    private Link link;
}
