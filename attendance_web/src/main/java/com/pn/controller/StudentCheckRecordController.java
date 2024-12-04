package com.pn.controller;


import com.pn.config.R;
import com.pn.domain.StudentCheckRecord;
import com.pn.mapper.StudentCheckRecordMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 学生打卡关联表 前端控制器
 * </p>
 *
 * @author TaoJiLu
 * @since 2024-12-04
 */
@RestController
@RequestMapping("/studentCheckRecord")
@RequiredArgsConstructor
public class StudentCheckRecordController {

    private final StudentCheckRecordMapper studentCheckRecordMapper;

    @PostMapping
    @Operation(summary = "新增学生打卡记录")
    public R addStudentCheckRecord(@RequestBody StudentCheckRecord record) {
        int result = studentCheckRecordMapper.insert(record);
        return result > 0 ? R.success("新增成功") : R.error("新增失败");
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据 ID 查询学生打卡记录")
    public R getStudentCheckRecordById(@PathVariable Long id) {
        StudentCheckRecord record = studentCheckRecordMapper.selectById(id);
        return record != null ? R.success(record) : R.error("没有该行数据");
    }

    @PutMapping
    @Operation(summary = "更新学生打卡记录")
    public R updateStudentCheckRecord(@RequestBody StudentCheckRecord record) {
        int result = studentCheckRecordMapper.updateById(record);
        return result > 0 ? R.success("更新成功") : R.error("更新失败");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "根据 ID 删除学生打卡记录")
    public R deleteStudentCheckRecord(@PathVariable Long id) {
        int result = studentCheckRecordMapper.deleteById(id);
        return result > 0 ? R.success("删除成功") : R.error("删除失败");
    }
}

