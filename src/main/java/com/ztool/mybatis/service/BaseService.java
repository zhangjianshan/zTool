package com.ztool.mybatis.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztool.mybatis.mapper.CommonMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;

/**
 * @author zhangjianshan on 2022-11-22
 */
public class BaseService<M extends CommonMapper<T>, T> extends ServiceImpl<M, T> implements ApplicationContextAware {

    @Autowired
    protected M commonMapper;

    public static ApplicationContext applicationContext;


    /**
     * 自定义语句返回数据
     *
     * @param sql sql语句
     * @return
     */
    public List<T> queryObjectListBySql(String sql) {
        return commonMapper.queryObjectListBySql(sql);
    }

    /**
     * 分页查询
     *
     * @param sql      sql语句
     * @param pageNo   当前页
     * @param pageSize 每页多少条
     * @return
     */
    public PageInfo<T> queryObjectListBySql(String sql, int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, 1);
        List<T> dataList = commonMapper.queryObjectListBySql(sql);
        PageInfo<T> pageInfoData = new PageInfo(dataList);
        return pageInfoData;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BaseService.applicationContext = applicationContext;
    }
}
