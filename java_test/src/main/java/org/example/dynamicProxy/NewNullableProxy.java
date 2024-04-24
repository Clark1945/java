package org.example.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

public class NewNullableProxy implements Nullable, InvocationHandler {
    private boolean enabled;
    private Object target;

    public NewNullableProxy(Object target) {
        this.target = target;
    }

    @Override
    public void enable() {
        enabled = true;
    }

    @Override
    public void disable() {
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if(methodName.equals("enable") || methodName.equals("disable")) {
            return method.invoke(this, args);
        }

        if(!isEnabled() && Arrays.stream(args).anyMatch(arg -> arg == null)) { // 參數是否為null
            throw new IllegalArgumentException(
                    String.format("argument(s) of %s.%s cannot be null",
                            target.getClass().getName(),
                            method.getName()
                    )
            );
        }

        return method.invoke(target, args);
    }
}
