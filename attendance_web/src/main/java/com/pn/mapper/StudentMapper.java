package com.pn.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pn.entity.Student;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 学生表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-12-02
 */
@Mapper
public interface StudentMapper extends BaseMapper<Student> {

}
