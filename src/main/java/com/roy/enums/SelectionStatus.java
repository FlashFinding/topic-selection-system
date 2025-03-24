package com.roy.enums;

public enum SelectionStatus {
    //学生选题对应状态
    REJECTED,//已驳回 status==0
    UNSELECTED,//未选择 status==null or 1
    AUDITING,//审核中 status==2
    SELECTED,//已选择 status==3
}
