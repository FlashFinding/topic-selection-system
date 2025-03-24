package com.roy.controller;

import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.HashMap;

import com.roy.entity.Topic;
import com.roy.entity.User;
import com.roy.entity.CustomUserDetails;
import com.roy.service.TopicService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    private final TopicService topicService;

    public TeacherController(TopicService topicService) {
        this.topicService = topicService;
    }

    //教师工作台（已验证）
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User teacher = userDetails.getUser();
        model.addAttribute("topics", topicService.getTopicsByTeacher(teacher));
        return "teacher/dashboard";
    }

    //删除课题（已验证）
    @DeleteMapping("/delete-topic/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteTopic(@PathVariable Long id) {
        topicService.deleteTopic(id);
        return ResponseEntity.ok("删除成功");
    }

    //课题管理页面（已验证）
    @GetMapping("/project-management")
    public String showProjectManagement(Model model,
                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        User teacher = userDetails.getUser();
        model.addAttribute("topics", topicService.getTopicsByTeacher(teacher));
        return "teacher/project-management";
    }

    @GetMapping("/process-management")
    public String showProcessManagement(Model model) {
        // 教师端-进程管理
        return "teacher/process-management";
    }

    @GetMapping("/getSelectedTopics")
    @ResponseBody
    public ResponseEntity<?> getSelectedTopics(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long teacherId = userDetails.getUser().getId();
        return ResponseEntity.ok(topicService.getSelectedTopicsByTeacher(teacherId));
    }

    @GetMapping("/message-management")
    public String showMessageManagement(Model model) {
        // 教师端-消息管理
        return "teacher/message-management";
    }

    //编辑课题中课题id显示当前教师所有课题（已验证）
    @GetMapping("/getTopic/{id}")
    @ResponseBody
    public ResponseEntity<Topic> getTopic(@PathVariable Long id) {
        Topic topic = topicService.getTopicById(id);
        return ResponseEntity.ok(topic);
    }
    //添加课题保存（已验证）
    @PostMapping("/saveTopic")
    public String saveTopic(@ModelAttribute Topic topic,
                          @RequestParam("requirement") String requirements,
                          @RequestParam("max_students") int maxStudents,
                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        User teacher = userDetails.getUser();
        topic.setTeacherId(teacher.getId());
        topic.setRequirements(requirements);
        topic.setMaxStudents(maxStudents);
        topicService.addTopic(topic);
        return "redirect:/teacher/project-management";
    }

    //编辑课题保存（已验证）
    @PostMapping("/updateTopic")
    public String updateTopic(@ModelAttribute Topic topic,
                            @RequestParam("requirement") String requirements,
                            @RequestParam("max_students") int maxStudents,
                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        User teacher = userDetails.getUser();

        topic.setTeacherId(teacher.getId());
        topic.setRequirements(requirements);
        topic.setMaxStudents(maxStudents);
        topicService.updateTopic(topic);
        return "redirect:/teacher/project-management";
    }

    // 更新学生课题状态
    @PostMapping("/updateSTopicStatus")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateSTopicStatus(@RequestParam Long id) {
        boolean result = topicService.updateSTopicStatus(id);
        Map<String, Object> response = new HashMap<>();
        if (result) {
            response.put("success", true);
            response.put("message", "驳回成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "驳回失败：操作未成功");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 通过学生课题申请
    @PostMapping("/passSTopic")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> passSTopic(@RequestParam Long id) {
        Map<String, Object> response = new HashMap<>();
        boolean isFull = topicService.isTopicFull(id);
        
        if (!isFull) {
            boolean result = topicService.updateNotFullStatus(id);
            if (result) {
                response.put("success", true);
                response.put("message", "通过成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "通过失败：操作未成功");
                return ResponseEntity.badRequest().body(response);
            }
        } else {
            boolean result = topicService.updateTopicStatusToFull(id);
            if (result) {
                response.put("success", true);
                response.put("message", "该课题人数已满，请驳回该生课题申请！");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "操作失败");
                return ResponseEntity.badRequest().body(response);
            }
        }
    }
}
