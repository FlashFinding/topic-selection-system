package com.roy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.roy.entity.Topic;
import com.roy.entity.TopicSelection;
import com.roy.entity.User;
import com.roy.mapper.TopicMapper;
import com.roy.mapper.TopicSelectionMapper;
import com.roy.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicMapper topicMapper;
    
    @Autowired
    private TopicSelectionMapper topicSelectionMapper;
    

    //教师页面通过teacherId查询除Status字段以外的字段
    @Override
    public List<Topic> getTopicsByTeacher(User teacher) {
        return topicMapper.selectTList(teacher.getId());
    }

    //教师端新增更新课题
    @Override
    public void addTopic(Topic topic) {
        System.out.println("Topic to save in service: " + topic);
        topicMapper.insert(topic);
        System.out.println("Topic saved with ID: " + topic.getId());
    }


    //教师编辑更新课题
    public void updateTopic(Topic topic) {
        // 获取当前用户角色
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        
        // 如果是管理员或学生，禁止修改teacher_id
        if (role.equals("ROLE_ADMIN") || role.equals("ROLE_STUDENT")) {
            // 获取原始topic数据
            Topic originalTopic = topicMapper.selectById(topic.getId());
            // 保持teacher_id不变
            topic.setTeacherId(originalTopic.getTeacherId());
        }
        
        topicMapper.updateById(topic);
    }

    //教师删除课题
    @Override
    public void deleteTopic(Long id) {
        topicMapper.deleteById(id);
    }

    //管理员获取所有状态课题
    @Override
    public List<Topic> getAllTopics() {
        return topicMapper.selectList(new QueryWrapper<Topic>());
    }

    //教师根据课题id查询当前教师所有课题
    @Override
    public Topic getTopicById(Long id) {
        return topicMapper.selectById(id);
    }

   

    //管理员更新教师发布的课题状态
    @Override
    public void updateTopicStatus(Long topicId, int status) {
        Topic topic = new Topic();
        topic.setId(topicId);
        topic.setStatus(status);
        topicMapper.updateStatusById(topic);
    }
    //学生页面课题列表查询所有状态课题
    @Override
    public List<Topic> searchTopics(String keyword, List<Integer> statuses) {
        return topicMapper.topicsSearchS(keyword, statuses);
    }

    //学生选择课题
    @Override
    public void selectTopic(Long topicId) {
        // 获取当前认证用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User student = new User();
        student.setId(((com.roy.entity.CustomUserDetails) authentication.getPrincipal()).getUser().getId());
        
        // 创建选题记录
        TopicSelection selection = new TopicSelection();
        selection.setStudent(student);
        Topic topic = new Topic();
        topic.setId(topicId);
        selection.setTopic(topic);
        selection.setStatus(2); // AUDITING对应的状态值
        
        // 插入选题记录
        topicSelectionMapper.insert(selection);
    }

    // 查询学生已选课题
    @Override
    public List<Topic> searchSelectedTopics(Long studentId, String keyword) {
        return topicMapper.topicSelectedSearch(studentId, keyword);
    }

    // 获取学生选题状态
    @Override
    public Integer getSelectionStatus(Long studentId) {
        TopicSelection selection = topicSelectionMapper.selectedStatus(studentId);
        return selection != null ? selection.getStatus() : null;
    }
    @Override
    public boolean updateNotFullStatus(Long id) {
        return topicSelectionMapper.updateNotFullStatus(id, 3) > 0;
    }

    @Override
    public boolean updateTopicStatusToFull(Long id) {
        return topicMapper.updateTopicStatusToFull(id, 3) > 0;
    }

    @Override
    public String approveTopicSelection(Long selectionId, Long topicId) {
        if (isTopicFull(topicId)) {
            updateTopicStatusToFull(topicId);
            return "该课题人数已满，请驳回该生课题申请！";
        } else {
            updateNotFullStatus(selectionId);
            return "通过成功";
        }
    }

    // 获取学生课题详细信息

    @Override
    public Topic getTopicDetails(Long id) {
        return topicMapper.selectDetailsById(id);
    }

    // 根据学生ID获取选题信息
    @Override
    public List<Map<String, Object>> getTopicSelectionsByStudentId(Long studentId) {
        return topicSelectionMapper.selectByStudentId(studentId);
    }

    // 获取课题详细信息，包括课题信息和指导老师信息
    @Override
    public Map<String, Object> getATopicDetails(Long id) {
        Topic topic = topicMapper.selectDetailsById(id);
        if (topic == null) {
            return null;
        }
        
        Map<String, Object> details = new HashMap<>();
        details.put("id", topic.getId());
        details.put("title", topic.getTitle());
        details.put("description", topic.getDescription());
        details.put("requirements", topic.getRequirements());
        details.put("status", topic.getStatus());
        details.put("teacherName", topic.getTeacherName() != null ? topic.getTeacherName() : "未知教师");
        
        return details;
    }

    // 学生取消选题
    @Override
    public void cancelTopic(Long studentId) {
        // 获取当前选题
        List<Map<String, Object>> selections = topicSelectionMapper.selectByStudentId(studentId);
        
        if (selections != null && !selections.isEmpty()) {
            Map<String, Object> selection = selections.get(0);
            Long topicId = (Long) selection.get("id");
            
            // 调用mapper删除选题记录
            topicSelectionMapper.deleteTopicSelection(studentId, topicId);
        }
    }

    // 根据教师ID获取已选课题信息
    @Override
    public List<Map<String, Object>> getSelectedTopicsByTeacher(Long teacherId) {
        return topicSelectionMapper.selectTTopic(teacherId);
    }

    // 更新学生课题状态为驳回
    @Override
    public boolean updateSTopicStatus(Long id) {
        int rows = topicSelectionMapper.updateStatus(id, 0);
        return rows > 0;
    }

    @Override
    public boolean isTopicFull(Long topicId) {
        Map<String, Object> topicInfo = topicSelectionMapper.selectMaxTopicInfo(topicId);
        if (topicInfo != null) {
            int maxStudents = (int) topicInfo.get("max_students");
            int studentCount = ((Long) topicInfo.get("student_count")).intValue();
            return studentCount >= maxStudents;
        }
        return false;
    }
}
