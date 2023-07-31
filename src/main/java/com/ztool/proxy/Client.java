package com.ztool.proxy;

import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Proxy;

/**
 * @author zhangjianshan on 2023-07-31
 */
public class Client {
    public static void main(String[] args) throws Exception {
        //需要被代理的类
        Service realService = new RealServiceImpl();
        //代理类
        Service proxy = new ProxyServiceImpl(realService);
        //代理类执行方法
        proxy.operate("静态代理");

        System.out.println("===========================================");

        DynamicProxyServiceImpl dynamicProxyService = new DynamicProxyServiceImpl(realService);
        Service jdkProxy = (Service) Proxy
                .newProxyInstance(dynamicProxyService.getClass().getClassLoader(), realService.getClass().getInterfaces(),
                        dynamicProxyService);
        //代理类执行方法
        jdkProxy.operate("jdk动态代理");

        System.out.println("===========================================");
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(RealServiceImpl.class);
        enhancer.setCallback(new ProxyMethodInterceptor());
        RealServiceImpl cglibProxy = (RealServiceImpl) enhancer.create();
        cglibProxy.operate("CGlib");
    }
}
