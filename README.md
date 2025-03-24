# 毕业设计选题管理系统

基于java的毕业设计选题管理系统，提供管理员、教师和学生三种角色的选题管理功能。
本系统采用Spring Boot框架搭建后端服务，结合MySQL数据库进行数据存储，并利用Bootstrap与Thymeleaf技术实现前端交互界面。


## 主要功能

### 通用功能
- 用户注册与登录
- 个人信息管理
- 消息通知系统
- 密码加密存储（BCrypt）

### 管理员功能
- 用户管理（增删改查）
- 角色权限管理
- 系统日志查看
- 全局消息发布

### 教师功能
- 发布选题
- 审核学生选题申请
- 查看选题统计
- 消息接收

### 学生功能
- 浏览选题
- 提交选题申请
- 查看选题状态
- 与教师消息互动

## 技术栈

### 后端
- Spring Boot 3.1.0
- MyBatis-Plus 3.5.3.1
- Spring Security
- Logback日志系统

### 前端
- Thymeleaf模板引擎
- Bootstrap 5
- jQuery
- Chart.js

### 数据库
- MySQL 8.0

### 构建工具
- Maven 3.9.9

## 数据库设计

### 主要表结构
- 用户表（user）：存储用户基本信息
- 角色表（role）：定义用户角色
- 权限表（permission）：定义系统权限
- 选题表（topic）：存储选题信息
- 选题记录表（topic_selection）：记录选题过程
- 消息表（message）：存储系统消息

## 快速开始

### 环境要求
- JDK 17
- MySQL 8.0
- Maven 3.9.9

### 安装步骤

1. 克隆仓库
   ```bash
   git clone https://github.com/roy/topic-selection-system.git
   ```

2. 创建数据库
   ```sql
   CREATE DATABASE topic_selection;
   ```

3. 修改配置文件
   编辑`src/main/resources/application.yml`，配置数据库连接信息

4. 启动应用
   ```bash
   mvn spring-boot:run
   ```

5. 访问系统
   - 管理员：http://localhost:8080/admin
   - 教师：http://localhost:8080/teacher
   - 学生：http://localhost:8080/student

## API文档

### 用户相关
- **POST /api/user/register** 用户注册
- **POST /api/user/login** 用户登录
- **GET /api/user/info** 获取用户信息

### 选题相关
- **GET /api/topic/list** 获取选题列表
- **POST /api/topic/apply** 提交选题申请
- **PUT /api/topic/approve** 审批选题


