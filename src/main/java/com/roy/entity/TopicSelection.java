package com.roy.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class TopicSelection {
    public Long getTopicId() {
        return topic != null ? topic.getId() : null;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    private Integer status;
}
