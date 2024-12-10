package com.pn.domain;

import java.io.Serial;
import java.lang.reflect.Type;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Long id;

    /**
     * 教师ID
     */
    private Long teacherId;

    /**
     * 教师姓名
     */
    @TableField(exist = false)
    private String teacherName;

    /**
     * 班级ID
     */
    private Long clazzId;

    /**
     * 班级名
     */
    @TableField(exist = false)
    private String clazzName;

    /**
     * 打卡开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 打卡结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 地址信息
     */
    private String address;

    /**
     * 打卡范围
     */
    private Integer distance;

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
