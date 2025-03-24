package com.roy.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.roy.entity.Message;
import com.roy.entity.User;
import com.roy.entity.CustomUserDetails;
import com.roy.mapper.MessageMapper;
import com.roy.mapper.UserMapper;
import com.roy.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    public int createMessage(Message message) {
        // 获取当前登录用户ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long currentUserId = userDetails.getUser().getId();
        message.setSenderId(currentUserId);

        // 根据接收者类型处理receiver_id和role_id
        String recipientType = message.getRecipientType();
        if ("ALL".equals(recipientType)) {
            message.setReceiverId(null);
            message.setRoleId(null);
        } else if ("CHOOSE".equals(recipientType)) {
            // 根据用户名查找用户ID
            User user = userMapper.selectUserByUsername(message.getReceiverUsername());
            if (user != null) {
                message.setReceiverId(user.getId());
                message.setRoleId(null);
            } else {
                throw new RuntimeException("未找到指定用户");
            }
        } else {
            // 根据角色名称查找角色ID
            Long roleId = roleMapper.getRoleIdByName(recipientType);
            message.setReceiverId(null);
            message.setRoleId(roleId);
        }

        // 设置默认状态为未读
        message.setStatus(0);
        message.setDelFlag(false);
        message.setCreateTime(new Date());
        message.setUpdateTime(new Date());

        return messageMapper.insert(message);
    }

    public Message getMessageById(Long id) {
        return messageMapper.selectById(id);
    }

    public List<Message> getAllMessages() {
        return messageMapper.selectList(null);
    }

    public List<Message> searchMessages(Integer type, String content, Integer status) {
        if (content != null && !content.isEmpty()) {
            return messageMapper.searchContents(content);
        }
        return messageMapper.selectList(new QueryWrapper<Message>()
            .eq(type != null, "type", type)
            .eq(status != null, "status", status));
    }

    public List<Message> searchMessagesWithPage(Integer type, String content, Integer status, Long offset, Integer pageSize) {
        return messageMapper.selectListWithPage(type, content, status, offset, pageSize);
    }

    public long countMessages(Integer type, String content, Integer status) {
        return messageMapper.countMessages(type, content, status);
    }

    public int updateMessage(Message message) {
        return messageMapper.updateById(message);
    }

    public int deleteMessage(Long id) {
        return messageMapper.deleteById(id);
    }

    public List<Message> getMessagesByReceiverAndRoleNull() {
        return messageMapper.selectMessagesByReceiverAndRoleNull();
    }

    public List<Message> getMessagesByRoleId(int roleId) {
        return messageMapper.selectMessagesByRoleId(roleId);
    }

    public int updateMessageStatus(Long id, int status) {
        Message existingMessage = messageMapper.selectById(id);
        if (existingMessage == null) {
            throw new RuntimeException("消息不存在");
        }
        existingMessage.setStatus(status);
        existingMessage.setUpdateTime(new Date());
        return messageMapper.updateById(existingMessage);
    }

    public List<Message> selectTMessage(Long currentUserId) {
        return messageMapper.selectTMessage(currentUserId);
    }

    public List<Message> getMessagesByUserId(Long userId) {
        return messageMapper.selectMessagesByUserId(userId);
    }
}
