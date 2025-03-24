package com.roy.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class Message {
    // Getters and Setters
    private Long id;
    private Long senderId;
    private Long receiverId;
    private Long roleId;
    private Integer type;
    private String content;
    private Integer status;
    private Long read_num;
    private Boolean delFlag = false;
    private Date createTime;
    private Date updateTime;
    private String senderUsername;
    private String recipientType;
    private String receiverUsername;
    private String realname;

}
