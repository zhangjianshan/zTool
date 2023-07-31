package com.ztool.proxy;

import lombok.Getter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author zhangjianshan on 2023-07-31
 */
@Getter
public class DynamicProxyServiceImpl implements InvocationHandler {
    private final Object targetObject;

    public DynamicProxyServiceImpl(Object targetObject) {
        this.targetObject = targetObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long startTime = System.currentTimeMillis();
        method.invoke(targetObject, args);
        System.out.println("方法执行耗时：" + (System.currentTimeMillis() - startTime));
        return null;
    }
}
