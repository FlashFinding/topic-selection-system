package com.roy.controller;

import com.roy.entity.Topic;
import com.roy.service.TopicService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final TopicService topicService;

    public StudentController(TopicService topicService) {
        this.topicService = topicService;
    }

    //学生工作台（已验证）
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        return "student/dashboard";
    }

    @GetMapping("/project-management")
    public String showProjectManagement(Model model) {
        // 学生端-课程管理
        return "student/project-management";
    }

    //学生课题列表搜索查询所有状态课题（已验证）
    @GetMapping("/search-topics")
    @ResponseBody
    public ResponseEntity<?> searchTopics(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String statuses) {
        try {
            List<Integer> statusList = null;
            if (statuses != null && !statuses.isEmpty()) {
                statusList = Arrays.stream(statuses.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            }
            List<Topic> topics = topicService.searchTopics(keyword, statusList);
            return ResponseEntity.ok(topics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Failed to search topics: " + e.getMessage()));
        }
    }

    // 查询学生已选课题
    @GetMapping("/search-selected-topics")
    @ResponseBody
    public ResponseEntity<?> searchSelectedTopics(
            @RequestParam(required = false) String keyword) {
        try {
            // 获取当前认证用户
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long studentId = ((com.roy.entity.CustomUserDetails) authentication.getPrincipal()).getUser().getId();
            
            List<Topic> topics = topicService.searchSelectedTopics(studentId, keyword);
            return ResponseEntity.ok(topics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Failed to search selected topics: " + e.getMessage()));
        }
    }

    @GetMapping("/process-management")
    public String showProcessManagement(Model model) {
        // 获取当前认证用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long studentId = ((com.roy.entity.CustomUserDetails) authentication.getPrincipal()).getUser().getId();
        
        // 查询学生选题信息
        List<Map<String, Object>> selections = topicService.getTopicSelectionsByStudentId(studentId);
        model.addAttribute("selections", selections);
        
        return "student/process-management";
    }

    @GetMapping("/message-management")
    public String showMessageManagement(Model model) {
        // 学生端-消息管理
        return "student/message-management";
    }

    // 根据学生ID获取课题详细信息
    @GetMapping("/get-topic-info-by-student-id")
    @ResponseBody
    public ResponseEntity<?> getTopicInfoByStudentId() {
        try {
            // 获取当前认证用户
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long studentId = ((com.roy.entity.CustomUserDetails) authentication.getPrincipal()).getUser().getId();
            
            // 查询课题信息
            List<Map<String, Object>> topicInfo = topicService.getTopicSelectionsByStudentId(studentId);
            return ResponseEntity.ok(topicInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "获取课题信息失败: " + e.getMessage()));
        }
    }

    // 获取课题详细信息
    @GetMapping("/get-topic-details/{id}")
    @ResponseBody
    public ResponseEntity<?> getTopicDetails(@PathVariable Long id) {
        try {
            Topic topic = topicService.getTopicDetails(id);
            return ResponseEntity.ok(topic);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "获取课题详情失败: " + e.getMessage()));
        }
    }

    // 获取当前用户选题状态
    @GetMapping("/get-selection-status")
    @ResponseBody
    public ResponseEntity<?> getSelectionStatus() {
        try {
            // 获取当前认证用户
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long studentId = ((com.roy.entity.CustomUserDetails) authentication.getPrincipal()).getUser().getId();
            
            // 查询当前用户选题状态
            Integer status = topicService.getSelectionStatus(studentId);
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "获取选题状态失败: " + e.getMessage()));
        }
    }

    // 学生选择课题
    @PostMapping("/select-topic/{topicId}")
    @ResponseBody
    public ResponseEntity<?> selectTopic(@PathVariable Long topicId) {
        try {
            // 获取当前认证用户
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long studentId = ((com.roy.entity.CustomUserDetails) authentication.getPrincipal()).getUser().getId();

            // 查询当前用户选题状态
            Integer status = topicService.getSelectionStatus(studentId);

            if (status == null || status == 0 || status == 1) {
                topicService.selectTopic(topicId);
                return ResponseEntity.ok("课题选择成功，等待审核！");
            } else if (status == 2) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "你已选题，课题正在审核中！若需更换课题，请联系指导老师！"));
            } else if (status == 3) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "你已经选择课题成功，无需再选题！"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "未知的选题状态！"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "选择课题失败: " + e.getMessage()));
        }
    }

     // 学生取消选题
     @PostMapping("/cancel-topic")
     @ResponseBody
     public ResponseEntity<?> cancelTopic() {
         try {
             // 获取当前认证用户
             Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
             Long studentId = ((com.roy.entity.CustomUserDetails) authentication.getPrincipal()).getUser().getId();
             
             // 调用服务层方法取消选题
             topicService.cancelTopic(studentId);
             return ResponseEntity.ok("课题取消成功！");
         } catch (Exception e) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                 .body(Map.of("message", "取消课题失败: " + e.getMessage()));
         }
     }

}
