package com.devenlia.uad.models.requests;

import com.devenlia.uad.models.Page;
import lombok.Data;

@Data
public class ReqPage {
    private String parent;
    private Page page;
}