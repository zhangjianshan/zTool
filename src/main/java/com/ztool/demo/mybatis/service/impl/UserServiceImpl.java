package com.ztool.demo.mybatis.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ztool.demo.mybatis.dto.UserQueryDto;
import com.ztool.demo.mybatis.manager.UserManager;
import com.ztool.demo.mybatis.mapper.SysUserMapper;
import com.ztool.demo.mybatis.model.SysUserModel;
import com.ztool.mybatis.service.BaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zhangjianshan on 2022-11-22
 */
@Service
public class UserServiceImpl extends BaseService<SysUserMapper, SysUserModel> {

    @Resource
    private UserManager userManager;

    /**
     * 分页查询用户列表
     *
     * @param pageNo       当前页
     * @param pageSize     每页多少条
     * @param userQueryDto 查询条件
     * @return 分页用户列表
     */
    public Page<SysUserModel> queryPageUserList(int pageNo, int pageSize, UserQueryDto userQueryDto) {
        return userManager.queryPageUserList(pageNo, pageSize, userQueryDto);
    }
}
