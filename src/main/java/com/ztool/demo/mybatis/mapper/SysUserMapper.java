package com.ztool.demo.mybatis.mapper;


import com.ztool.demo.mybatis.model.SysUserModel;
import com.ztool.mybatis.mapper.CommonMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户
 *
 * @author zhangjianshan1992
 */
@Mapper
public interface SysUserMapper extends CommonMapper<SysUserModel> {
}
