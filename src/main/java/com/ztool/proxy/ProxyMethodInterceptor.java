package com.ztool.proxy;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author zhangjianshan on 2023-07-31
 */
public class ProxyMethodInterceptor implements MethodInterceptor {
    /**
     * @param proxyObj    代理生产的对象
     * @param method      被代理对象的方法
     * @param args        方法入参
     * @param methodProxy 代理对象的方法
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object proxyObj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object obj = methodProxy.invokeSuper(proxyObj, args);
        System.out.println("方法耗时：" + (System.currentTimeMillis() - startTime));
        return obj;
    }
}
