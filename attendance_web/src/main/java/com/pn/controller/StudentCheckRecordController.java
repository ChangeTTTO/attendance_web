package com.pn.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pn.config.R;
import com.pn.domain.Bo.StudentCheckBo;
import com.pn.domain.StudentCheckRecord;
import com.pn.domain.TeacherCheckRecord;
import com.pn.mapper.StudentCheckRecordMapper;
import com.pn.mapper.TeacherCheckRecordMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.Metrics;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;


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
    private final TeacherCheckRecordMapper teacherCheckRecordMapper;
    private final RedisTemplate<String,String> redisTemplate;

    @PostMapping
    @Operation(summary = "新增学生打卡记录")
    public R addStudentCheckRecord(@RequestBody StudentCheckBo record) {

        // 获取学生的经纬度
        Double  studentLatitude = record.getLatitude();
        Double  studentLongitude = record.getLongitude();
        Long recordId = record.getTeacherCheckRecordId();
        Long studentId = record.getStudentId();
        if (studentLatitude == null || studentLongitude == null) {
            return R.error("学生经纬度不能为空");
        }
        Long count = studentCheckRecordMapper.selectCount(new LambdaQueryWrapper<StudentCheckRecord>()
                .eq(StudentCheckRecord::getTeacherCheckRecordId, recordId)
                .eq(StudentCheckRecord::getStudentId, studentId));
        if (count > 0) {
            return R.error("请勿重复签到");
        }
        // 获取教师打卡记录 ID
        Long teacherCheckRecordId = record.getTeacherCheckRecordId();
        if (teacherCheckRecordId == null) {
            return R.error("教师打卡记录 ID 不能为空");
        }

        // 查询教师打卡记录
        TeacherCheckRecord teacherRecord = teacherCheckRecordMapper.selectById(teacherCheckRecordId);
        if (teacherRecord == null) {
            return R.error("无法找到对应的教师打卡记录");
        }

        LocalDateTime teacherEndTime = teacherRecord.getEndTime();
        // 教师允许打卡的最后时间（迟到界限）
        LocalDateTime teacherLateTime = teacherEndTime.plusMinutes(10);

        LocalDateTime now = LocalDateTime.now();

        // 检查时间是否超过教师的打卡结束时间
        if (now.isAfter(teacherLateTime)) {
            return R.error("打卡失败，已超过允许打卡时间");
        }

        // 判断是否是迟到
        String status = "成功";
        if (now.isAfter(teacherEndTime)) {
            status = "迟到";
        }

        String geoKey = "teacherCheckRecord:geo:"+recordId;
        GeoOperations<String, String> geo = redisTemplate.opsForGeo();

        // 将学生位置存入 Redis
        geo.add("teacherCheckRecord:geo:"+recordId, new Point(studentLongitude, studentLatitude), "studentLocation");

        // 计算学生位置与教师位置的距离（米）
        Distance distance = geo.distance(
                geoKey,
                "teacherLocation",
                "studentLocation",
                Metrics.METERS
        );
        System.out.println("嘻嘻嘻哈哈"+distance.getValue()/10000);
        // 检查距离是否超过 1000 米
        if (distance == null || distance.getValue()/10000 > 1000) {
            geo.remove(studentId.toString(), "studentLocation");
            return R.error("不在允许打卡的范围内，距离超过 1000 米");
        }

        // 如果距离合规，插入学生打卡记录
        StudentCheckRecord studentCheckRecord = new StudentCheckRecord();
        studentCheckRecord.setTeacherCheckRecordId(teacherCheckRecordId);
        studentCheckRecord.setStudentId(record.getStudentId());
        studentCheckRecord.setStatus(status);
        studentCheckRecord.setCreateTime(now);

        geo.remove(studentId.toString(), "studentLocation");

        int result = studentCheckRecordMapper.insert(studentCheckRecord);
        return result > 0 ? R.success("打卡成功，状态：" + status) : R.error("打卡失败");
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

