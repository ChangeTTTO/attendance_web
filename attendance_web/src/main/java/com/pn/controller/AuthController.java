package com.pn.controller;

import com.pn.config.R;
import com.pn.domain.Bo.LoginRequest;

import com.pn.domain.Teacher;
import com.pn.mapper.TeacherMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.time.LocalDateTime;

/**
 * @author 11473
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final TeacherMapper teacherMapper;

    // 登录接口
    @PostMapping("/login")
    public R login(@RequestBody LoginRequest loginRequest) {
        // 根据工号查询教师信息
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_no", loginRequest.getTeacherNo());
        Teacher teacher = teacherMapper.selectOne(queryWrapper);

        // 校验用户是否存在以及密码是否匹配
        if (teacher == null) {
            return R.error("教师工号不存在");
        }
        if (!teacher.getPassword().equals(loginRequest.getPassword())) {
            return R.error("密码错误");
        }

        return R.success(teacher);
    }

    // 注册接口
    @PostMapping("/register")
    public R register(@RequestBody Teacher registerRequest) {
        // 校验是否已存在此工号
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_no", registerRequest.getTeacherNo());
        Teacher existingTeacher = teacherMapper.selectOne(queryWrapper);

        if (existingTeacher != null) {
            return R.error("工号已存在");
        }

        // 创建新教师对象
        Teacher newTeacher = new Teacher();
        newTeacher.setTeacherNo(registerRequest.getTeacherNo());
        newTeacher.setPassword(registerRequest.getPassword());
        newTeacher.setPhone(registerRequest.getPhone());
        newTeacher.setName(registerRequest.getName());
        newTeacher.setGender(registerRequest.getGender());
        newTeacher.setCreateTime(LocalDateTime.now());
        newTeacher.setUpdateTime(LocalDateTime.now());

        // 插入数据到数据库
        int result = teacherMapper.insert(newTeacher);
        if (result > 0) {
            return R.success("注册成功");
        } else {
            return R.error("注册失败");
        }
    }

    // 退出登录接口
    @PostMapping("/logout")
    public R logout() {
        // 对于最简单的实现，退出登录不需要特别处理
        // 直接返回成功响应
        return R.success("退出登录成功");
    }
}
