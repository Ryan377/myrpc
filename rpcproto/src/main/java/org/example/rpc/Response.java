package org.example.rpc;

import lombok.Data;

//表示rpc的返回
@Data
public class Response {
    /**
     * 服务返回编码，0-成功，非0失败
     */
    private int code = 0;
    /**
     * 具体的错误信息
     */
    private String message = "ok";

    private Object data;

}
