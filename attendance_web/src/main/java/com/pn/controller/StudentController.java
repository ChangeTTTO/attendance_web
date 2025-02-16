package com.pn.controller;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pn.config.R;
import com.pn.domain.Bo.StudentLoginRequest;
import com.pn.domain.Clazz;
import com.pn.domain.Student;
import com.pn.mapper.ClazzMapper;
import com.pn.mapper.StudentMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 学生表 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-12-02
 */
@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentMapper studentMapper;
    private final ClazzMapper clazzMapper;

    @PostMapping
    @Operation(summary = "添加学生")
    public R addStudent(@RequestBody Student student) {
        studentMapper.insert(student);
        return R.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除学生")
    public R deleteStudent(@PathVariable Long id) {
        studentMapper.deleteById(id);
        return R.success();
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新学生信息")
    public  R updateStudent(@PathVariable Long id, @RequestBody Student student) {
        student.setId(id);
        studentMapper.updateById(student);
        return R.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据id查询学生")
    public R getStudent(@PathVariable Long id) {
        Student student = studentMapper.selectById(id);
        if (student == null) {
            return R.error("没有该行数据");
        }
        return R.success(student);
    }

    @GetMapping
    @Operation(summary = "查询所有学生")
    public R getAllStudents() {
        // 查询所有学生
        List<Student> students = studentMapper.selectList(null);

        // 查询班级名称并设置到学生对象中
        students = students.stream()
                .peek(student -> {
                    // 根据 clazzId 查询对应的班级
                    Clazz clazz = clazzMapper.selectById(student.getClazzId());
                    if (clazz != null) {
                        student.setClazzName(clazz.getClazzName());  // 设置班级名称
                    }
                }).collect(Collectors.toList());

        // 返回成功的结果
        return R.success(students);
    }


    /**
     * 根据学号查询学生信息
     *
     * @param studentNo 学号
     * @return 查询结果
     */
    @GetMapping("/queryByStudentNo")
    @Operation(summary = "根据学号模糊查询学生信息（支持空学号查询所有）")
    public R queryByStudentNo(@RequestParam(required = false) String studentNo) {
        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        if (studentNo != null && !studentNo.trim().isEmpty()) {
            queryWrapper.like(Student::getStudentNo, studentNo);
        }
        List<Student> students = studentMapper.selectList(queryWrapper);
        if (students.isEmpty()) {
            return R.error("未找到匹配的学生信息！");
        }
        return R.success(students);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询学生信息")
    public R getStudentPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String studentNo
    ) {
        // 创建分页对象
        Page<Student> studentPage = new Page<>(page, size);

        // 创建查询条件
        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        if (studentNo != null && !studentNo.trim().isEmpty()) {
            queryWrapper.like(Student::getStudentNo, studentNo);
        }
        queryWrapper.orderByAsc(Student::getId);

        // 执行分页查询
        Page<Student> result = studentMapper.selectPage(studentPage, queryWrapper);

        // 查询班级名称并组装结果
        List<Student> records = result.getRecords().stream()
                .peek(student -> {
                    // 查询班级名称
                    Clazz clazz = clazzMapper.selectById(student.getClazzId());
                    if (clazz != null) {
                        student.setClazzName(clazz.getClazzName()); // 设置班级名称
                    }
                }).toList();

        // 设置返回的分页数据
        Page<Student> finalPage = new Page<>();
        finalPage.setRecords(records);
        finalPage.setCurrent(result.getCurrent());
        finalPage.setSize(result.getSize());
        finalPage.setTotal(result.getTotal());

        return R.success(finalPage);
    }

    // 学生登录接口
    @PostMapping("/login")
    public R login(@RequestBody StudentLoginRequest loginRequest) {
        // 根据学号查询学生信息
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_no", loginRequest.getStudentNo());
        Student student = studentMapper.selectOne(queryWrapper);

        // 校验学生是否存在以及密码是否匹配
        if (student == null) {
            return R.error("学号不存在");
        }
        if (!student.getPassword().equals(loginRequest.getPassword())) {
            return R.error("密码错误");
        }

        // 返回成功并携带学生信息
        return R.success(student);
    }

    // 学生注册接口
    @PostMapping("/register")
    public R register(@RequestBody Student registerRequest) {
        // 校验是否已存在此学号
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_no", registerRequest.getStudentNo());
        Student existingStudent = studentMapper.selectOne(queryWrapper);

        if (existingStudent != null) {
            return R.error("学号已存在");
        }

        // 创建新学生对象
        Student newStudent = new Student();
        newStudent.setStudentNo(registerRequest.getStudentNo());
        newStudent.setPassword(registerRequest.getPassword());
        newStudent.setPhone(registerRequest.getPhone());
        newStudent.setName(registerRequest.getName());
        newStudent.setGender(registerRequest.getGender());
        newStudent.setAddress(registerRequest.getAddress());
        newStudent.setClazzId(registerRequest.getClazzId());
        newStudent.setCreateTime(LocalDateTime.now());
        newStudent.setUpdateTime(LocalDateTime.now());

        // 插入数据到数据库
        int result = studentMapper.insert(newStudent);
        if (result > 0) {
            return R.success("注册成功");
        } else {
            return R.error("注册失败");
        }
    }

    // 退出登录接口
    @PostMapping("/logout")
    public R logout() {
        // 退出登录不需要特别处理，直接返回成功响应
        return R.success("退出登录成功");
    }

    @GetMapping("/export")
    @Operation(summary = "导出学生数据")
    public ResponseEntity<byte[]> exportStudentData(@RequestParam(required = false) String studentNo) {
        // 创建查询条件
        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        if (studentNo != null && !studentNo.trim().isEmpty()) {
            queryWrapper.like(Student::getStudentNo, studentNo);
        }

        List<Student> students = studentMapper.selectList(queryWrapper);

        // 使用 EasyExcel 导出数据
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        EasyExcel.write(outputStream, Student.class).sheet("学生数据").doWrite(students);

        byte[] content = outputStream.toByteArray();

        // 设置响应头，指示为下载文件
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=students.xlsx")
                .body(content);
    }


    @PostMapping("/import")
    @Operation(summary = "导入学生数据")
    public R importStudentData(@RequestParam("file") MultipartFile file) {
        try {
            // 解析Excel文件
            InputStream inputStream = file.getInputStream();
            List<Student> students = EasyExcel.read(inputStream)
                    .head(Student.class) // 这里指定你的实体类
                    .sheet()
                    .doReadSync();

            // 将解析到的学生数据批量插入数据库
            students.forEach(studentMapper::insert);

            return R.success("导入成功");

        } catch (Exception e) {
            return R.error("导入失败：" + e.getMessage());
        }
    }


}
