package com.pn.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pn.config.R;
import com.pn.domain.Teacher;
import com.pn.mapper.TeacherMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 教师表 前端控制器
 * </p>
 *
 * @author TaoJiLu
 * @since 2024-12-03
 */
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherMapper teacherMapper;

    // 增加教师
    @PostMapping
    @Operation(summary = "新增教师信息")
    public R addTeacher(@RequestBody Teacher teacher) {
        teacher.setCreateTime(LocalDateTime.now());
        teacher.setUpdateTime(LocalDateTime.now());
        int result = teacherMapper.insert(teacher);
        if (result > 0) {
            return R.success("教师信息添加成功！");
        }
        return R.error("教师信息添加失败！");
    }

    // 删除教师
    @DeleteMapping("/{id}")
    @Operation(summary = "删除教师信息")
    public R deleteTeacher(@PathVariable("id") Long id) {
        int result = teacherMapper.deleteById(id);
        if (result > 0) {
            return R.success("教师信息删除成功！");
        }
        return R.error("教师信息删除失败！");
    }

    // 更新教师信息
    @PutMapping
    @Operation(summary = "更新教师信息")
    public R updateTeacher(@RequestBody Teacher teacher) {
        teacher.setUpdateTime(LocalDateTime.now());
        int result = teacherMapper.updateById(teacher);
        if (result > 0) {
            return R.success("教师信息更新成功！");
        }
        return R.error("教师信息更新失败！");
    }

    // 根据ID查询教师信息
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询教师信息")
    public R queryById(@PathVariable("id") Long id) {
        Teacher teacher = teacherMapper.selectById(id);
        if (teacher == null) {
            return R.error("未找到匹配的教师信息！");
        }
        return R.success(teacher);
    }

    // 根据教师工号模糊查询教师信息
    @GetMapping("/queryByTeacherNo")
    @Operation(summary = "根据教师工号模糊查询教师信息（支持空工号查询所有）")
    public R queryByTeacherNo(@RequestParam(required = false) String teacherNo) {
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        if (teacherNo != null && !teacherNo.trim().isEmpty()) {
            queryWrapper.like(Teacher::getTeacherNo, teacherNo);
        }
        List<Teacher> teachers = teacherMapper.selectList(queryWrapper);
        if (teachers.isEmpty()) {
            return R.error("未找到匹配的教师信息！");
        }
        return R.success(teachers);
    }

    // 获取所有教师信息
    @GetMapping
    @Operation(summary = "获取所有教师信息")
    public R queryAllTeachers() {
        List<Teacher> teachers = teacherMapper.selectList(null);
        return R.success(teachers);
    }
    @GetMapping("/page")
    @Operation(summary = "分页查询教师信息")
    public R getTeacherPage(
            @RequestParam(defaultValue = "1") int page,          // 当前页码，默认值为1
            @RequestParam(defaultValue = "10") int size,        // 每页大小，默认值为10
            @RequestParam(required = false) String teacherNo,   // 可选教师工号，支持模糊查询
            @RequestParam(required = false) String name         // 可选姓名，支持模糊查询
    ) {
        // 创建分页对象
        Page<Teacher> teacherPage = new Page<>(page, size);

        // 创建查询条件
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        if (teacherNo != null && !teacherNo.trim().isEmpty()) {
            queryWrapper.like(Teacher::getTeacherNo, teacherNo);
        }
        if (name != null && !name.trim().isEmpty()) {
            queryWrapper.like(Teacher::getName, name);
        }
        queryWrapper.orderByAsc(Teacher::getId); // 根据 ID 升序排序

        // 执行分页查询
        Page<Teacher> result = teacherMapper.selectPage(teacherPage, queryWrapper);
        return R.success(result);
    }

}

