package com.pn.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 学生请假表
 * </p>
 *
 * @author TaoJiLu
 * @since 2024-12-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("student_leave")
public class StudentLeave implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 请假记录ID
     */
    @TableId
    private Long id;

    /**
     * 学生ID，关联学生表
     */
    private Long studentId;

    /**
     * 班级id
     */
    private Long clazzId;

    /**
     * 学生姓名
     */
    @TableField(exist = false)
    private String studentName;

    /**
     * 班级名
     */
    @TableField(exist = false)
    private String clazzName;

    /**
     * 请假类型（如病假、事假等）
     */
    private String leaveType;

    /**
     * 请假开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    /**
     * 请假结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    /**
     * 请假原因
     */
    private String reason;

    /**
     * 请假状态（如待审核、已批准、已拒绝等）
     */
    private String status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
