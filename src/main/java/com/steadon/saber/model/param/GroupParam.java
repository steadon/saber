package com.steadon.saber.model.param;

import lombok.Data;

import java.util.List;

@Data
public class GroupParam {
    private String name;
    private String description;
    private List<Integer> uidList;
}