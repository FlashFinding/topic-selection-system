package com.roy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.roy.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    List<Message> selectListWithPage(
        @Param("type") Integer type,
        @Param("content") String content,
        @Param("status") Integer status,
        @Param("offset") Long offset,
        @Param("pageSize") Integer pageSize);

    List<Message> searchContents(@Param("content") String content);

    long countMessages(
        @Param("type") Integer type,
        @Param("content") String content,
        @Param("status") Integer status);

    int updateById(@Param("et") Message message);

    List<Message> selectMessagesByReceiverAndRoleNull();

    List<Message> selectMessagesByRoleId(@Param("roleId") int roleId);
    
    List<Message> selectTMessage(@Param("currentUserId") Long currentUserId);

    List<Message> selectMessagesByUserId(@Param("userId") Long userId);
}
