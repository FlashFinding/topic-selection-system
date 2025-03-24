package com.roy.enums;

import lombok.Getter;

@Getter
public enum Permission {
    USER_VIEW("user:view", "查看用户"),
    USER_CREATE("user:create", "创建用户"),
    USER_UPDATE("user:update", "修改用户"),
    USER_DELETE("user:delete", "删除用户"),
    ROLE_VIEW("role:view", "查看角色"),
    ROLE_CREATE("role:create", "创建角色"),
    ROLE_UPDATE("role:update", "修改角色"),
    ROLE_DELETE("role:delete", "删除角色"),
    TOPIC_VIEW("topic:view", "查看题目"),
    TOPIC_CREATE("topic:create", "创建题目"),
    TOPIC_UPDATE("topic:update", "修改题目"),
    TOPIC_DELETE("topic:delete", "删除题目");

    private final String permission;
    private final String description;

    Permission(String permission, String description) {
        this.permission = permission;
        this.description = description;
    }
}
