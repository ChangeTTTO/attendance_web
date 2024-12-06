package com.pn.domain.Bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author TaoJiLu
 */
@Data
public class StudentCheckBo {

    /**
     * 打卡记录 ID
     */
    private Long teacherCheckRecordId;

    /**
     * 学生 ID
     */
    private Long studentId;

    /**
     * 学生纬度
     */
    private BigDecimal latitude;

    /**
     * 学生经度
     */
    private BigDecimal longitude;
}
