package com.ztool.proxy;

/**
 * @author zhangjianshan1992
 */
public interface Service {

    /**
     * 业务操作
     *
     * @param proxyType 代理类型
     * @throws Exception 错误
     */
    void operate(String proxyType) throws Exception;
}
