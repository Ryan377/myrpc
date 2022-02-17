package org.example.server;

import lombok.Data;
import org.example.codec.Decoder;
import org.example.codec.Encoder;
import org.example.codec.JSONDecoder;
import org.example.codec.JSONEncoder;
import org.example.transport.HTTPTransportServer;
import org.example.transport.TransportServer;

/**
 * server配置
 */
@Data
public class RpcServerConfig {
    private Class<? extends TransportServer> transportClass = HTTPTransportServer.class;
    private Class<? extends Encoder> encodeClass = JSONEncoder.class;
    private Class<? extends Decoder> decodeClass = JSONDecoder.class;
    private int port = 3000;
}
