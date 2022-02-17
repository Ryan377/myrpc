package org.example.codec;

import com.alibaba.fastjson.JSON;

public class JSONEncoder implements Encoder{
    @Override
    public byte[] encode(Object obj) {
        return JSON.toJSONBytes(obj);
    }
}
