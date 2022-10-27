package com.winshare.demo.proxyconfig;


import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * jdk  可改其他 如cglib
 * @param <T>
 */
public class RemoteServiceProxy<T> implements InvocationHandler, Serializable {

    private final ZyApiInvoker zyApiInvoker;

    private final String remoteRealm;

    private final Class<T> mapperInterface;

    private final Map<Method, MapperMethod> mapperMethodMap = new ConcurrentHashMap<>();

    public RemoteServiceProxy(ZyApiInvoker zyApiInvoker, Class<T> mapperInterface) {
        this.zyApiInvoker = zyApiInvoker;
        this.mapperInterface = mapperInterface;
        this.remoteRealm = obtainMapperInterfaceAllocatedRealm(mapperInterface, zyApiInvoker.getInvokerConfiguration().getDefaultRealm());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            method.invoke(this, args);
        }
        MapperMethod mapperMethod = cacheMethodMapper(method);
        return mapperMethod.execute(zyApiInvoker, remoteRealm, args);
    }

    private MapperMethod cacheMethodMapper(Method method) {
        MapperMethod mapperMethod = mapperMethodMap.get(method);
        if (mapperMethod != null) {
            return mapperMethod;
        }
        mapperMethod = new MapperMethod(method, zyApiInvoker.getInvokerConfiguration().getMethodParameterParser());
        mapperMethodMap.put(method, mapperMethod);
        return mapperMethod;
    }

    private String obtainMapperInterfaceAllocatedRealm(Class<?> clzz, String defaultRealm) {
        String remoteRealm = null;
        if (Objects.nonNull(clzz) && clzz.isAnnotationPresent(RemoteRealm.class)) {
            remoteRealm = clzz.getAnnotation(RemoteRealm.class).value();
        }
        if (remoteRealm == null && defaultRealm != null && defaultRealm.length() > 0) {
            remoteRealm = defaultRealm;
        }
        return remoteRealm;
    }

    public T newInstance() {
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, this);
    }

    @Override
    public String toString() {
        return "RemoteServiceProxy#" + mapperInterface.getSimpleName();
    }
}
