package org.example.server;

import org.example.rpc.Request;
import org.example.rpc.ServiceDescriptor;
import org.example.rpc.utils.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理rpc暴露的服务
 */
public class ServiceManager {
    private Map<ServiceDescriptor, ServiceInstance> services;

    public ServiceManager() {
        this.services = new ConcurrentHashMap<>();
    }

    public <T> void register(Class<T> interfaceClass, T bean) {
        Method[] methods = ReflectionUtils.getPublicMethod(interfaceClass);
        for (Method method : methods) {
            ServiceInstance sis = new ServiceInstance(bean, method);
            ServiceDescriptor sdp = ServiceDescriptor.from(interfaceClass, method);
            services.put(sdp, sis);
        }
    }

    public ServiceInstance lookup(Request request){
        ServiceDescriptor sdp = request.getServiceDescriptor();
        return services.get(sdp);
    }

}
