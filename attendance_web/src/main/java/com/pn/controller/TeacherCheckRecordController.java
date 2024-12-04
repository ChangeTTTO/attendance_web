package com.pn.controller;


import com.pn.config.R;
import com.pn.domain.TeacherCheckRecord;
import com.pn.mapper.TeacherCheckRecordMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 学生打卡记录表 前端控制器
 * </p>
 *
 * @author TaoJiLu
 * @since 2024-12-04
 */
@RestController
@RequestMapping("/teacherCheckRecord")
@RequiredArgsConstructor
public class TeacherCheckRecordController {

    private final TeacherCheckRecordMapper teacherCheckRecordMapper;

    @PostMapping
    @Operation(summary = "新增教师发起打卡记录")
    public R addTeacherCheckRecord(@RequestBody TeacherCheckRecord record) {
        int result = teacherCheckRecordMapper.insert(record);
        return result > 0 ? R.success("新增成功") : R.error("新增失败");
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据 ID 查询教师发起打卡记录")
    public R getTeacherCheckRecordById(@PathVariable Long id) {
        TeacherCheckRecord record = teacherCheckRecordMapper.selectById(id);
        return record != null ? R.success(record) : R.error("没有该行数据");
    }

    @PutMapping
    @Operation(summary = "更新教师发起打卡记录")
    public R updateTeacherCheckRecord(@RequestBody TeacherCheckRecord record) {
        int result = teacherCheckRecordMapper.updateById(record);
        return result > 0 ? R.success("更新成功") : R.error("更新失败");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "根据 ID 删除教师发起打卡记录")
    public R deleteTeacherCheckRecord(@PathVariable Long id) {
        int result = teacherCheckRecordMapper.deleteById(id);
        return result > 0 ? R.success("删除成功") : R.error("删除失败");
    }
}

