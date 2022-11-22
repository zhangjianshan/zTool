package com.ztool.demo.mybatis.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ztool.demo.mybatis.dto.UserQueryDto;
import com.ztool.demo.mybatis.mapper.SysUserMapper;
import com.ztool.demo.mybatis.model.SysUserModel;
import com.ztool.mybatis.manager.BaseManager;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author zhangjianshan on 2022-11-22
 */
@Service
public class UserManager extends BaseManager<SysUserMapper, SysUserModel> {

    /**
     * 分页查询用户列表
     *
     * @param pageNo       当前页
     * @param pageSize     每页多少条
     * @param userQueryDto 查询条件
     * @return
     */
    public Page<SysUserModel> queryPageUserList(int pageNo, int pageSize, UserQueryDto userQueryDto) {
        Page<SysUserModel> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<SysUserModel> queryWrapper = new LambdaQueryWrapper<>();
        //用户姓名
        String userName = userQueryDto.getUserName();
        if (!StringUtils.isEmpty(userName)) {
            queryWrapper.like(SysUserModel::getUsername, userName);
        }
        //用户状态
        String userStatus = userQueryDto.getUserStatus();
        if (!StringUtils.isEmpty(userStatus)) {
            queryWrapper.eq(SysUserModel::getUserStatus, userStatus);
        }
        return super.page(page, queryWrapper);
    }
}
