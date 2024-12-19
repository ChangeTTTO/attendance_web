package com.pn.domain;

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
 * 学生打卡关联表
 * </p>
 *
 * @author TaoJiLu
 * @since 2024-12-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("studentCheckRecord")
public class StudentCheckRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 打卡记录 ID
     */
    private Long teacherCheckRecordId;

    /**
     * 学生 ID，关联学生表的主键
     */
    private Long studentId;

    /**
     * 班级id
     */
    private Long clazzId;

    /**
     * 打卡状态：1-成功，2-迟到，3-缺卡
     */
    private String status;

    /**
     * 记录创建时间
     */
    private LocalDateTime createTime;


}
