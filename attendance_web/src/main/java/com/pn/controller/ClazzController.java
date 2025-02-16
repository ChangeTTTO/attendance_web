package com.pn.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pn.config.QrCodeUtils;
import com.pn.config.R;
import com.pn.domain.Clazz;
import com.pn.domain.Student;
import com.pn.mapper.ClazzMapper;
import com.pn.mapper.StudentLeaveMapper;
import com.pn.mapper.StudentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 班级表 前端控制器
 * </p>
 *
 * @author TaoJiLu
 * @since 2024-12-03
 */
@RestController
@RequestMapping("/clazz")
@RequiredArgsConstructor
public class ClazzController {

    private final ClazzMapper clazzMapper;
    private final StudentMapper studentMapper;
    private final StudentLeaveMapper studentLeaveMapper;

    /**
     * 根据教师 ID 获取对应的班级列表
     *
     * @param teacherId 教师 ID
     * @return 响应对象，包含班级列表
     */
    @GetMapping("/byTeacher")
    public R getClazzByTeacherId(@RequestParam Long teacherId) {
        if (teacherId == null) {
            return R.error("教师 ID 不能为空");
        }
        // 查询班级列表
        List<Clazz> clazzList = clazzMapper.selectList(
                new LambdaQueryWrapper<Clazz>().eq(Clazz::getTeacherId, teacherId)
        );
        return R.success(clazzList);
    }

    /**
     * 根据班级 ID 获取班级人数
     *
     * @param clazzId 班级 ID
     * @return 响应对象，包含班级人数
     */
    @GetMapping("/studentCount")
    public R getStudentCountByClazzId(@RequestParam Long clazzId) {
        if (clazzId == null) {
            return R.error("班级 ID 不能为空");
        }
        // 查询班级人数
        Long studentCount = studentMapper.selectCount(
                new LambdaQueryWrapper<Student>().eq(Student::getClazzId, clazzId)
        );
        return R.success(studentCount);
    }

    /**
     * 创建班级
     *
     * @param clazz 班级信息
     * @return 响应对象，包含创建结果
     */
    @PostMapping
    public R createClazz(@RequestBody Clazz clazz) {
        if (clazz == null || clazz.getClazzName() == null) {
            return R.error("班级信息不能为空");
        }
        clazzMapper.insert(clazz);
        return R.success("班级创建成功");
    }

    /**
     * 根据班级 ID 获取班级信息
     *
     * @param id 班级 ID
     * @return 响应对象，包含班级信息
     */
    @GetMapping("/{id}")
    public R getClazzById(@PathVariable Long id) {
        if (id == null) {
            return R.error("班级 ID 不能为空");
        }
        Clazz clazz = clazzMapper.selectById(id);
        if (clazz == null) {
            return R.error("班级不存在");
        }
        return R.success(clazz);
    }

    /**
     * 更新班级信息
     *
     * @param clazz 班级信息
     * @return 响应对象，包含更新结果
     */
    @PutMapping
    public R updateClazz(@RequestBody Clazz clazz) {
        if (clazz == null || clazz.getId() == null) {
            return R.error("班级信息不能为空");
        }
        clazzMapper.updateById(clazz);
        return R.success("班级信息更新成功");
    }

    /**
     * 根据班级 ID 删除班级
     *
     * @param id 班级 ID
     * @return 响应对象，包含删除结果
     */
    @DeleteMapping("/{id}")
    public R deleteClazz(@PathVariable Long id) {
        if (id == null) {
            return R.error("班级 ID 不能为空");
        }
        clazzMapper.deleteById(id);
        return R.success("班级删除成功");
    }
}
