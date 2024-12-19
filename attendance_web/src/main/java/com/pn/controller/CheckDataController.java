package com.pn.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pn.config.R;
import com.pn.domain.Student;
import com.pn.domain.StudentCheckRecord;
import com.pn.domain.StudentLeave;
import com.pn.mapper.ClazzMapper;
import com.pn.mapper.StudentCheckRecordMapper;
import com.pn.mapper.StudentLeaveMapper;
import com.pn.mapper.StudentMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/checkData")
@RequiredArgsConstructor
public class CheckDataController {

    private final ClazzMapper clazzMapper;
    private final StudentMapper studentMapper;
    private final StudentLeaveMapper studentLeaveMapper;
    private final StudentCheckRecordMapper studentCheckRecordMapper;

    /**
     * 获取班级考勤和请假详细统计信息
     *
     * @param clazzId 班级ID
     * @return 考勤统计响应对象
     */
    @Operation(summary = "根据班级id获取班级考勤和请假详细统计信息")
    @GetMapping("/attendanceStats")
    public R getAttendanceStatistics(@RequestParam Long clazzId) {
        if (clazzId == null) {
            return R.error("班级 ID 不能为空");
        }

        // 获取班级总人数
        Long totalStudentCount = studentMapper.selectCount(
                new LambdaQueryWrapper<Student>().eq(Student::getClazzId, clazzId)
        );

        // 当前日期
        LocalDate today = LocalDate.now();
        LocalDateTime todayStart = LocalDateTime.of(today, LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(today, LocalTime.MAX);

        // 本日统计
        Long todayAbsentCount = studentCheckRecordMapper.selectCount(
                Wrappers.<StudentCheckRecord>lambdaQuery()
                        .eq(StudentCheckRecord::getClazzId, clazzId)
                        .eq(StudentCheckRecord::getStatus, "缺卡")
                        .between(StudentCheckRecord::getCreateTime, todayStart, todayEnd)
        );

        Long todayLateCount = studentCheckRecordMapper.selectCount(
                Wrappers.<StudentCheckRecord>lambdaQuery()
                        .eq(StudentCheckRecord::getClazzId, clazzId)
                        .eq(StudentCheckRecord::getStatus, "迟到")
                        .between(StudentCheckRecord::getCreateTime, todayStart, todayEnd)
        );

        Long todayLeaveCount = studentLeaveMapper.selectCount(
                Wrappers.<StudentLeave>lambdaQuery()
                        .eq(StudentLeave::getClazzId, clazzId)
                        .eq(StudentLeave::getStatus, "已批准")
                        .between(StudentLeave::getStartDate, todayStart, todayEnd)
        );

        // 本周统计 (从本周第一天到当前日期)
        LocalDate weekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDateTime weekStartDateTime = LocalDateTime.of(weekStart, LocalTime.MIN);

        Long weekAbsentCount = studentCheckRecordMapper.selectCount(
                Wrappers.<StudentCheckRecord>lambdaQuery()
                        .eq(StudentCheckRecord::getClazzId, clazzId)
                        .eq(StudentCheckRecord::getStatus, "缺卡")
                        .between(StudentCheckRecord::getCreateTime, weekStartDateTime, todayEnd)
        );

        Long weekLateCount = studentCheckRecordMapper.selectCount(
                Wrappers.<StudentCheckRecord>lambdaQuery()
                        .eq(StudentCheckRecord::getClazzId, clazzId)
                        .eq(StudentCheckRecord::getStatus, "迟到")
                        .between(StudentCheckRecord::getCreateTime, weekStartDateTime, todayEnd)
        );

        Long weekLeaveCount = studentLeaveMapper.selectCount(
                Wrappers.<StudentLeave>lambdaQuery()
                        .eq(StudentLeave::getClazzId, clazzId)
                        .eq(StudentLeave::getStatus, "已批准")
                        .between(StudentLeave::getStartDate, weekStartDateTime, todayEnd)
        );

        // 本月统计
        LocalDate monthStart = today.withDayOfMonth(1);
        LocalDateTime monthStartDateTime = LocalDateTime.of(monthStart, LocalTime.MIN);

        Long monthAbsentCount = studentCheckRecordMapper.selectCount(
                Wrappers.<StudentCheckRecord>lambdaQuery()
                        .eq(StudentCheckRecord::getClazzId, clazzId)
                        .eq(StudentCheckRecord::getStatus, "缺卡")
                        .between(StudentCheckRecord::getCreateTime, monthStartDateTime, todayEnd)
        );

        Long monthLateCount = studentCheckRecordMapper.selectCount(
                Wrappers.<StudentCheckRecord>lambdaQuery()
                        .eq(StudentCheckRecord::getClazzId, clazzId)
                        .eq(StudentCheckRecord::getStatus, "迟到")
                        .between(StudentCheckRecord::getCreateTime, monthStartDateTime, todayEnd)
        );

        Long monthLeaveCount = studentLeaveMapper.selectCount(
                Wrappers.<StudentLeave>lambdaQuery()
                        .eq(StudentLeave::getClazzId, clazzId)
                        .eq(StudentLeave::getStatus, "已批准")
                        .between(StudentLeave::getStartDate, monthStartDateTime, todayEnd)
        );

        // 组装统计数据
        AttendanceStatistics stats = new AttendanceStatistics();
        stats.setTotalStudentCount(totalStudentCount);

        // 设置日统计
        stats.setTodayAbsentCount(todayAbsentCount);
        stats.setTodayLateCount(todayLateCount);
        stats.setTodayLeaveCount(todayLeaveCount);

        // 设置周统计
        stats.setWeekAbsentCount(weekAbsentCount);
        stats.setWeekLateCount(weekLateCount);
        stats.setWeekLeaveCount(weekLeaveCount);

        // 设置月统计
        stats.setMonthAbsentCount(monthAbsentCount);
        stats.setMonthLateCount(monthLateCount);
        stats.setMonthLeaveCount(monthLeaveCount);

        return R.success(stats);
    }

    /**
     * 考勤统计数据传输对象
     */
    @Data
    public static class AttendanceStatistics {
        // 总学生数
        private Long totalStudentCount;

        // 日统计
        private Long todayAbsentCount;
        private Long todayLateCount;
        private Long todayLeaveCount;

        // 周统计
        private Long weekAbsentCount;
        private Long weekLateCount;
        private Long weekLeaveCount;

        // 月统计
        private Long monthAbsentCount;
        private Long monthLateCount;
        private Long monthLeaveCount;
    }
}