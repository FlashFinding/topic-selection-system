package com.roy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.roy.entity.Topic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface TopicMapper extends BaseMapper<Topic> {
    int updateById(@Param("topic") Topic topic);
    int updateStatusById(@Param("topic") Topic topic);
    List<Topic> selectTList(@Param("teacherId") Long teacherId);
    
    List<Topic> topicsSearchS(
        @Param("keyword") String keyword,
        @Param("statuses") List<Integer> statuses);

    List<Topic> topicSelectedSearch(
        @Param("studentId") Long studentId,
        @Param("keyword") String keyword);

    Topic selectDetailsById(@Param("id") Long id);

    int deleteByTeacherId(@Param("teacherId") Long teacherId);

    int updateTopicStatus(@Param("id") Long id, @Param("status") int status);

    int updateTopicStatusToFull(@Param("id") Long id, @Param("status") int status);

    int countStudentSubmissions();

    int countTeacherSubmissions();
}
