CREATE TABLE `clazz` (
                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                         `clazz_name` varchar(50) NOT NULL COMMENT '班级名称',
                         `teacher_id` bigint DEFAULT NULL COMMENT '班主任ID',
                         `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='班级表'

CREATE TABLE `student` (
                           `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                           `student_no` varchar(50) NOT NULL COMMENT '学生学号',
                           `password` bigint DEFAULT NULL COMMENT '登录密码',
                           `name` varchar(100) NOT NULL COMMENT '学生姓名',
                           `gender` varchar(10) NOT NULL COMMENT '性别',
                           `phone` varchar(20) NOT NULL COMMENT '手机号',
                           `address` varchar(255) NOT NULL COMMENT '家庭住址',
                           `clazz_id` bigint NOT NULL COMMENT '班级ID',
                           `create_time` datetime NOT NULL COMMENT '创建时间',
                           `update_time` datetime NOT NULL COMMENT '更新时间',
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `student_no_UNIQUE` (`student_no`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学生表'

CREATE TABLE `student_leave` (
                                 `id` bigint NOT NULL AUTO_INCREMENT COMMENT '请假记录ID',
                                 `student_id` bigint NOT NULL COMMENT '学生ID，关联学生表',
                                 `leave_type` varchar(50) NOT NULL COMMENT '请假类型（如病假、事假等）',
                                 `start_date` date NOT NULL COMMENT '请假开始时间',
                                 `end_date` date NOT NULL COMMENT '请假结束时间',
                                 `reason` varchar(255) NOT NULL COMMENT '请假原因',
                                 `status` varchar(20) NOT NULL DEFAULT '待审核' COMMENT '请假状态（如待审核、已批准、已拒绝等）',
                                 `create_time` datetime NOT NULL COMMENT '创建时间',
                                 `update_time` datetime NOT NULL COMMENT '更新时间',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学生请假表'


CREATE TABLE `studentcheckrecord` (
                                      `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                      `teacher_check_record_id` bigint NOT NULL COMMENT '打卡记录 ID',
                                      `student_id` bigint NOT NULL COMMENT '学生 ID',
                                      `status` varchar(20) DEFAULT '缺卡' COMMENT '打卡状态：1-成功，2-迟到，3-缺卡',
                                      `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
                                      PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学生打卡关联表'

CREATE TABLE `teacher` (
                           `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                           `teacher_no` varchar(50) NOT NULL COMMENT '教师工号',
                           `password` bigint DEFAULT NULL COMMENT '登录密码',
                           `name` varchar(100) NOT NULL COMMENT '姓名',
                           `gender` varchar(10) NOT NULL COMMENT '性别',
                           `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
                           `create_time` datetime NOT NULL COMMENT '创建时间',
                           `update_time` datetime NOT NULL COMMENT '更新时间',
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `teacher_no_UNIQUE` (`teacher_no`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='教师表'

CREATE TABLE `teachercheckrecord` (
                                      `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                      `teacher_id` bigint NOT NULL COMMENT '教师ID',
                                      `clazz_id` bigint DEFAULT NULL COMMENT '班级ID',
                                      `start_time` datetime NOT NULL COMMENT '打卡开始时间',
                                      `end_time` datetime NOT NULL COMMENT '打卡结束时间',
                                      `latitude` decimal(10,7) DEFAULT NULL COMMENT '纬度',
                                      `longitude` decimal(10,7) DEFAULT NULL COMMENT '经度',
                                      `address` varchar(50) DEFAULT NULL COMMENT '地址信息',
                                      `distance` int NOT NULL COMMENT '打卡范围',
                                      `status` varchar(10) DEFAULT '进行中' COMMENT '打卡状态：进行中，已结束',
                                      `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
                                      `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
                                      PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学生打卡记录表'

