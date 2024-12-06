package com.pn.domain;

import java.io.Serial;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 学生打卡记录表
 * </p>
 *
 * @author TaoJiLu
 * @since 2024-12-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("teacherCheckRecord")
public class TeacherCheckRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 教师ID
     */
    private Long teacherId;

    /**
     * 班级ID
     */
    private Long clazzId;

    /**
     * 打卡开始时间
     */
    private LocalDateTime startTime;

    /**
     * 打卡结束时间
     */
    private LocalDateTime endTime;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 打卡状态：进行中，已结束
     */
    private String status;

    /**
     * 记录创建时间
     */
    private LocalDateTime createTime;

    /**
     * 记录更新时间
     */
    private LocalDateTime updateTime;


}
