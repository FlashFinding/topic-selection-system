package com.roy.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Set;
import java.util.HashSet;
import java.util.Date;
import java.util.Optional;

@Entity
@TableName("topic")
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId
    private Long id;
    private String title;
    private String description;
    @TableField(exist = false)
    private Set<String> currentStudents;
    private int maxStudents;
    private int status; // 课题状态
    private Date createTime; // 选题创建时间
    private Long teacherId; // 指导教师ID
    @TableField("is_full")
    private boolean isFull; // 是否已满
    @TableField("requirements") 
    private String requirements;
    private String teacherName;

    public Topic() {
        this.currentStudents = new HashSet<>();
        this.status = 0;
        this.createTime = new Date();
        this.isFull = false;
        this.requirements = "";
    }

    public Topic(String title, String description) {
        this();
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getCurrentStudents() {
        return currentStudents;
    }

    public void setCurrentStudents(Set<String> currentStudents) {
        this.currentStudents = currentStudents;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Optional<Topic> asOptional() {
        return Optional.ofNullable(this);
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }
    
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherName() {
        return teacherName;
    }
}
