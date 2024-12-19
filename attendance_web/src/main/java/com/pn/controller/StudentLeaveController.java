package com.pn.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pn.config.R;
import com.pn.domain.Bo.StudentLeaveBo;
import com.pn.domain.Clazz;
import com.pn.domain.Student;
import com.pn.domain.StudentLeave;
import com.pn.mapper.ClazzMapper;
import com.pn.mapper.StudentLeaveMapper;
import com.pn.mapper.StudentMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 学生请假表 前端控制器
 * </p>
 *
 * @author TaoJiLu
 * @since 2024-12-03
 */
@RestController
@RequestMapping("/studentLeave")
@RequiredArgsConstructor
public class StudentLeaveController {

    private final StudentLeaveMapper studentLeaveMapper;
    private final StudentMapper studentMapper;
    private  final ClazzMapper clazzMapper;

    /**
     * 请假
     *
     * @return 响应结果
     */
    @PostMapping
    @Operation(summary = "请假")
    public R createStudentLeave(@RequestBody StudentLeaveBo studentLeaveBo) {
        StudentLeave studentLeave = BeanUtil.copyProperties(studentLeaveBo, StudentLeave.class);
        int result = studentLeaveMapper.insert(studentLeave);
        return result > 0 ? R.success() : R.error("新增请假记录失败");
    }

    /**
     * 删除请假记录
     *
     * @param id 请假记录ID
     * @return 响应结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "根据ID删除请假申请")
    public R deleteStudentLeave(@PathVariable Long id) {
        int result = studentLeaveMapper.deleteById(id);
        return result > 0 ? R.success() : R.error("删除请假记录失败");
    }

    /**
     * 请假审批
     */
    @PutMapping("/approveOrReject")
    @Operation(summary = "请假审批")
    public R updateStudentLeave(Long id, String status) {

        // 查询请假记录
        StudentLeave studentLeave = studentLeaveMapper.selectOne(new LambdaQueryWrapper<StudentLeave>().eq(StudentLeave::getId, id));
        if (studentLeave == null) {
            return R.error("请假记录未找到");
        }

        // 更新请假记录状态
        studentLeave.setStatus(status);
        int result = studentLeaveMapper.updateById(studentLeave);

        return result > 0 ? R.success() : R.error("更新请假记录失败");
    }

    /**
     * 查询请假记录（仅限当前用户班级的学生）
     *
     * @param page      当前页
     * @param size      每页条数
     * @param studentId 学生ID（可选）
     * @param clazzId   当前用户的班级ID
     * @return 响应结果
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询请假记录")
    public R getStudentLeaveList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long studentId,
            @RequestParam Long clazzId) {
        // 构建分页对象
        Page<StudentLeave> studentLeavePage = new Page<>(page, size);

        // 查询该班级的所有学生ID
        QueryWrapper<Student> studentQuery = new QueryWrapper<>();
        studentQuery.eq("clazz_id", clazzId).select("id");
        List<Long> studentIds = studentMapper.selectList(studentQuery).stream()
                .map(Student::getId)
                .toList();

        if (studentIds.isEmpty()) {
            return R.success(new Page<>()); // 如果该班级没有学生，直接返回空分页
        }

        // 构建请假记录查询条件
        QueryWrapper<StudentLeave> leaveQuery = new QueryWrapper<>();
        leaveQuery.in("student_id", studentIds); // 限制查询范围为该班级的学生
        if (studentId != null) {
            leaveQuery.eq("student_id", studentId); // 按学生ID过滤（如果提供）
        }

        // 分页查询请假记录
        Page<StudentLeave> result = studentLeaveMapper.selectPage(studentLeavePage, leaveQuery);
        return R.success(result);
    }

    /**
     * 查询请假记录（仅限当前用户班级的学生）
     *
     * @param studentId 学生ID（可选）
     * @param clazzId   当前用户的班级ID
     * @return 响应结果
     */
    @GetMapping
    @Operation(summary = "查询请假记录")
    public R getStudentLeaveList(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) String status,
            @RequestParam Long clazzId) {
        // 查询该班级的所有学生ID、姓名及班级名
        QueryWrapper<Student> studentQuery = new QueryWrapper<>();
        studentQuery.eq("clazz_id", clazzId).select("id", "name", "clazz_id");
        List<Student> students = studentMapper.selectList(studentQuery);

        if (students.isEmpty()) {
            return R.success(new ArrayList<>()); // 如果该班级没有学生，直接返回空列表
        }

        // 构建学生ID与姓名、班级ID的映射关系
        Map<Long, String> studentIdToNameMap = students.stream()
                .collect(Collectors.toMap(Student::getId, Student::getName));

        Map<Long, Long> studentIdToClazzIdMap = students.stream()
                .collect(Collectors.toMap(Student::getId, Student::getClazzId));

        // 查询班级名称
        Clazz clazz = clazzMapper.selectById(clazzId);
        String clazzName = clazz != null ? clazz.getClazzName() : "未知班级";

        // 构建请假记录查询条件
        QueryWrapper<StudentLeave> leaveQuery = new QueryWrapper<>();
        leaveQuery.in("student_id", studentIdToNameMap.keySet()); // 限制查询范围为该班级的学生
        if (studentId != null) {
            leaveQuery.eq("student_id", studentId); // 按学生ID过滤（如果提供）
        }
        if (status != null) {
            leaveQuery.eq("status", status); // 按状态过滤（如果提供）
        }

        // 查询所有符合条件的请假记录
        List<StudentLeave> studentLeaves = studentLeaveMapper.selectList(leaveQuery);

        // 设置学生姓名和班级名称
        studentLeaves.forEach(leave -> {
            String studentName = studentIdToNameMap.get(leave.getStudentId());
            leave.setStudentName(studentName); // 设置学生姓名
            leave.setClazzName(clazzName); // 设置班级名称
        });

        return R.success(studentLeaves); // 返回查询结果
    }



}

