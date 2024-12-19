package com.pn.domain.Bo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 11473
 */
@Data
public class TeacherCheckBo {
    private Long id;
    public Long teacherId;
    public Long clazzId;
    public LocalDateTime endTime;
    public Double latitude;
    public Double longitude;
    private String address;
    private Integer distance;
}
