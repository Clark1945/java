package org.example.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class LogProxy implements InvocationHandler {
    Object subject;

    public Object getLogProxy(Object subject) {
        this.subject = subject;
        return Proxy.newProxyInstance(
                subject.getClass().getClassLoader(),
                subject.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
        System.out.println("NPC 開始行動..." + method);

        Object result = method.invoke(subject, args);

        System.out.println("NPC 結束行動" + method);

        return result;
    }
}
