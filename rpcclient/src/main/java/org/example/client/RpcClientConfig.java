package org.example.client;

import lombok.Data;
import org.example.codec.Decoder;
import org.example.codec.Encoder;
import org.example.codec.JSONDecoder;
import org.example.codec.JSONEncoder;
import org.example.rpc.Peer;
import org.example.transport.HTTPTransportClient;
import org.example.transport.TransportClient;

import java.util.Arrays;
import java.util.List;

@Data
public class RpcClientConfig {
    private Class<? extends TransportClient> transportClass = HTTPTransportClient.class;
    private Class<? extends Encoder> encoderClass = JSONEncoder.class;
    private Class<? extends Decoder> decoderClass = JSONDecoder.class;
    private Class<? extends TransportSelector> selectorClass = RandomTransportSelector.class;
    private int connectCount = 1;
    private List<Peer> servers = Arrays.asList(
            new Peer("127.0.0.1", 3000)
    );
}
