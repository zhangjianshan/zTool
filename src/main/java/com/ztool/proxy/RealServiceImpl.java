package com.ztool.proxy;

/**
 * @author zhangjianshan on 2023-07-31
 */
public class RealServiceImpl implements Service {

    /**
     * 业务操作
     *
     * @param proxyType 代理类型
     * @throws Exception 错误
     */
    @Override
    public void operate(String proxyType) throws InterruptedException {
        System.out.println(proxyType + "执行方法...");
        Thread.sleep(1000);
    }
}
