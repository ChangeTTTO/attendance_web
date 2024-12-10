package com.pn.domain.Bo;

import lombok.Data;



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
    private Double latitude;

    /**
     * 学生经度
     */
    private Double longitude;
}
