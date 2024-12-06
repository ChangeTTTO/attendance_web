package com.pn.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serial;
import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 学生表
 * </p>
 *
 * @author TaoJiLu
 * @since 2024-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("student")
public class Student implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;

    /**
     * 学生学号
     */
    private String studentNo;

    /**
     * 登陆密码
     */
    private String password;

    /**
     * 学生姓名
     */
    private String name;

    /**
     * 性别
     */
    private String gender;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 家庭住址
     */
    private String address;

    /**
     * 班级id
     */
    private Long clazzId;

    /**
     * 班级名称
     */
    @TableField(exist = false)
    private String clazzName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
