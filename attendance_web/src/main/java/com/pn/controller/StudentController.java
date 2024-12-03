package com.pn.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pn.config.R;
import com.pn.domain.Clazz;
import com.pn.domain.Student;
import com.pn.mapper.ClazzMapper;
import com.pn.mapper.StudentMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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


}
