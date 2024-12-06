package com.pn.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pn.config.QrCodeUtils;
import com.pn.config.R;
import com.pn.domain.Clazz;
import com.pn.mapper.ClazzMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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


    /**
     * 根据教师 ID 获取对应的班级列表
     *
     * @param teacherId 教师 ID
     * @return 响应对象，包含班级列表
     */
    @GetMapping("byTeacher")
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
}
