package com.pn.controller;


import com.pn.config.R;
import com.pn.entity.Student;
import com.pn.mapper.StudentMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        List<Student> students = studentMapper.selectList(null);
        return R.success(students);
    }
}
