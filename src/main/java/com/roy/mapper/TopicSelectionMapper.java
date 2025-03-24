package com.roy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.roy.entity.TopicSelection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface TopicSelectionMapper extends BaseMapper<TopicSelection> {
    TopicSelection selectedStatus(Long studentId);
    
    List<Map<String, Object>> selectByStudentId(Long studentId);
    
    void deleteTopicSelection(Long studentId, Long topicId);

    List<Map<String, Object>> selectTTopic(Long teacherId);

    int updateStatus(@Param("id") Long id, @Param("status") int status);
    
    Map<String, Object> selectMaxTopicInfo(@Param("topicId") Long topicId);

    int getSelectionStatus(@Param("id") Long id);

    int updateNotFullStatus(@Param("id") Long id, @Param("status") int status);
}
