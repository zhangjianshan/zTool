package com.ztool.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 自定义sql查询
 *
 * @author zhangjianshan1992
 */
public interface CommonMapper<T> extends BaseMapper<T> {

    /**
     * 自定义sql语句获取List<>T</>
     *
     * @param sql sql语句
     * @return list
     */
    @Select("${sql}")
    List<T> queryObjectListBySql(@Param("sql") String sql);

}