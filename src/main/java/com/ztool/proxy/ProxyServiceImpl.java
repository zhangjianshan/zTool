package com.ztool.proxy;

/**
 * @author zhangjianshan on 2023-07-31
 */
public class ProxyServiceImpl implements Service {
    private Service service;

    public ProxyServiceImpl(Service service) {
        this.service = service;
    }

    @Override
    public void operate(String proxyType) throws Exception {
        long startTime = System.currentTimeMillis();
        service.operate(proxyType);
        System.out.println("执行耗时:" + (System.currentTimeMillis() - startTime));
    }
}
