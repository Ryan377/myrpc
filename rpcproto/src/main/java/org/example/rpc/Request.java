package org.example.rpc;

import lombok.Data;

// 表示rpc的一个请求
@Data
public class Request {
    private ServiceDescriptor serviceDescriptor;
    private Object[] parameters;
}
