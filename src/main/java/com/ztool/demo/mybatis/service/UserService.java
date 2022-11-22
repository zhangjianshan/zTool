package com.ztool.demo.mybatis.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ztool.demo.mybatis.dto.UserQueryDto;
import com.ztool.demo.mybatis.model.SysUserModel;
import com.ztool.mybatis.service.IBaseService;


/**
 * @author zhangjianshan on 2022-11-22
 */
public interface UserService extends IBaseService<SysUserModel> {
    /**
     * 分页查询用户列表
     *
     * @param pageNo       当前页
     * @param pageSize     每页多少条
     * @param userQueryDto 查询条件
     * @return f分页用户列表
     */
    Page<SysUserModel> queryPageUserList(int pageNo, int pageSize, UserQueryDto userQueryDto);

}
