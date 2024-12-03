package com.pn.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serial;
import java.time.LocalDateTime;
import java.io.Serializable;
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
     * 学生姓名
     */
    @TableField(exist = false)
    private String studentName;

    /**
     * 请假类型（如病假、事假等）
     */
    private String leaveType;

    /**
     * 请假开始时间
     */
    private LocalDateTime startDate;

    /**
     * 请假结束时间
     */
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
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
