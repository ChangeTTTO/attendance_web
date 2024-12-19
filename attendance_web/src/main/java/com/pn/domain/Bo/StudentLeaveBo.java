package com.pn.domain.Bo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.unit.DataUnit;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.time.DateUtils;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime  startDate;

    /**
     * 请假结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    /**
     * 请假原因
     */
    private String reason;
}
