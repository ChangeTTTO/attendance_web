package com.pn.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pn.config.R;
import com.pn.domain.TeacherCheckRecord;
import com.pn.mapper.TeacherCheckRecordMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

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
    private final RedisTemplate<String,String> redisTemplate;


    @PostMapping
    @Operation(summary = "新增教师发起打卡记录")
    public R addTeacherCheckRecord(@RequestBody TeacherCheckRecord record) {
        // 插入教师打卡记录到数据库
        int result = teacherCheckRecordMapper.insert(record);
        if (result <= 0) {
            return R.error("新增失败");
        }

        // 检查经纬度是否有效
        BigDecimal latitude = record.getLatitude();
        BigDecimal longitude = record.getLongitude();
        if (latitude == null || longitude == null) {
            return R.error("教师发起的打卡记录需要提供有效的经纬度");
        }

        // Redis GEO 操作
        GeoOperations<String, String> geo = redisTemplate.opsForGeo();

        String geoKey = "teacherCheckRecord:geo:" + record.getId();
        // 将教师位置存入 Redis 并设置过期时间
        geo.add(geoKey, new Point(longitude.doubleValue(), latitude.doubleValue()), "teacherLocation");
        Duration ttl = Duration.between(record.getStartTime(), record.getEndTime());
        if (!ttl.isNegative()) {
            redisTemplate.expire(geoKey, ttl);
        }

        return R.success("新增成功并已将位置存储到 Redis");
    }



    @GetMapping("/{id}")
    @Operation(summary = "根据 ID 查询教师发起打卡记录")
    public R getTeacherCheckRecordById(@PathVariable Long id) {
        TeacherCheckRecord record = teacherCheckRecordMapper.selectById(id);
        return record != null ? R.success(record) : R.error("没有该行数据");
    }

    @GetMapping("/byClazz/{clazzId}")
    @Operation(summary = "根据 班级ID 查询教师发起打卡记录")
    public R getTeacherCheckRecordByClazzId(@PathVariable Long clazzId) {
        List<TeacherCheckRecord> records = teacherCheckRecordMapper.selectList(
                new LambdaQueryWrapper<TeacherCheckRecord>().eq(TeacherCheckRecord::getClazzId, clazzId));
        return records != null ? R.success(records) : R.error("没有该行数据");
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

