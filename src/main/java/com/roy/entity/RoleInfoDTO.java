package com.roy.entity;

import lombok.Data;

@Data
public class RoleInfoDTO {
    private Long id;
    private String name;
    private String description;
    private Integer statusOneCount;
    private Integer userCount;
}
