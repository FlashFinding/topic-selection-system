package com.roy.controller;

import com.roy.entity.Message;
import com.roy.entity.CustomUserDetails;
import com.roy.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    public int createMessage(@RequestBody Message message) {
        return messageService.createMessage(message);
    }

    @GetMapping("/{id}")
    public Message getMessageById(@PathVariable Long id) {
        return messageService.getMessageById(id);
    }

    @GetMapping
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("/search")
    public List<Message> searchMessages(
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) Integer status) {
        return messageService.searchMessages(type, content, status);
    }

    @GetMapping("/search/page")
    public Map<String, Object> searchMessagesWithPage(
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "0") Long offset,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        List<Message> messages = messageService.searchMessagesWithPage(type, content, status, offset, pageSize);
        long total = messageService.countMessages(type, content, status);
        return Map.of(
            "messages", messages,
            "total", total
        );
    }

    @PutMapping("/{id}")
    public int updateMessage(@PathVariable Long id, @RequestBody Message message) {
        // 获取原有消息
        Message existingMessage = messageService.getMessageById(id);
        if (existingMessage == null) {
            throw new RuntimeException("消息不存在");
        }
        
        // 保留必要字段
        message.setId(id);
        message.setSenderId(existingMessage.getSenderId());
        message.setReceiverId(existingMessage.getReceiverId());
        message.setRoleId(existingMessage.getRoleId());
        message.setType(existingMessage.getType());
        message.setStatus(existingMessage.getStatus());
        message.setDelFlag(existingMessage.getDelFlag());
        
        return messageService.updateMessage(message);
    }

    @DeleteMapping("/{id}")
    public int deleteMessage(@PathVariable Long id) {
        return messageService.deleteMessage(id);
    }

    @PatchMapping("/{id}/status")
    public int updateMessageStatus(@PathVariable Long id, @RequestParam int status) {
        return messageService.updateMessageStatus(id, status);
    }

    @PutMapping("/updateStatus/{id}")
    public int updateTMessageStatus(@PathVariable Long id) {
        return messageService.updateMessageStatus(id, 1);
    }

    @GetMapping("/null-receiver-role")
    public List<Message> getMessagesByReceiverAndRoleNull() {
        return messageService.getMessagesByReceiverAndRoleNull();
    }

    @GetMapping("/role/{roleId}")
    public List<Message> getMessagesByRoleId(@PathVariable int roleId) {
        return messageService.getMessagesByRoleId(roleId);
    }

    @PostMapping("/selectTMessage")
    public List<Message> selectTMessage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long currentUserId = userDetails.getUser().getId();
        return messageService.selectTMessage(currentUserId);
    }

    @GetMapping("/user")
    public List<Message> getMessagesByUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long currentUserId = userDetails.getUser().getId();
        return messageService.getMessagesByUserId(currentUserId);
    }
}
