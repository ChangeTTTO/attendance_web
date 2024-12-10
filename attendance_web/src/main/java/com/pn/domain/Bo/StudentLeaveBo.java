package com.pn.domain.Bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class StudentLeaveBo {

    /**
     * 学生ID，关联学生表
     */
    private Long studentId;


    /**
     * 请假类型（如病假、事假等）
     */
    private String leaveType;

    /**
     * 请假开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate  startDate;

    /**
     * 请假结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    /**
     * 请假原因
     */
    private String reason;
}
