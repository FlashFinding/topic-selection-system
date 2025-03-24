package com.roy.service;

import com.roy.entity.Topic;
import com.roy.entity.User;
import java.util.List;
import java.util.Map;


public interface TopicService {
    List<Topic> getTopicsByTeacher(User teacher);
    List<Topic> getAllTopics();
    List<Topic> searchTopics(String keyword, List<Integer> statuses);
    Topic getTopicById(Long id);

    void addTopic(Topic topic);
    void updateTopic(Topic topic);
    
    boolean updateSTopicStatus(Long id);
    void deleteTopic(Long id);
    void updateTopicStatus(Long topicId, int status);
    
    //学生选择课题
    void selectTopic(Long topicId);
    
    // 查询学生已选课题
    List<Topic> searchSelectedTopics(Long studentId, String keyword);
    
    // 获取学生选题状态
    Integer getSelectionStatus(Long studentId);

    // 获取课题详细信息，包括课题信息和指导老师信息
    Map<String, Object> getATopicDetails(Long id);

    // 学生获取课题详细信息
    Topic getTopicDetails(Long id);

    // 根据学生ID获取选题信息
    List<Map<String, Object>> getTopicSelectionsByStudentId(Long studentId);
    
    // 学生取消选题
    void cancelTopic(Long studentId);

    // 根据教师ID获取已选课题信息
    List<Map<String, Object>> getSelectedTopicsByTeacher(Long teacherId);
    
    // 判断课题是否已满
    boolean isTopicFull(Long topicId);
    
    // 更新未满课题状态
    boolean updateNotFullStatus(Long id);
    
    // 更新已满课题状态
    boolean updateTopicStatusToFull(Long id);
    
    // 审批课题申请
    String approveTopicSelection(Long selectionId, Long topicId);
}
