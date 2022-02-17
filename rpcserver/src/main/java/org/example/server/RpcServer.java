package org.example.server;


import org.apache.commons.io.IOUtils;
import org.example.codec.Decoder;
import org.example.codec.Encoder;
import org.example.rpc.Request;
import org.example.rpc.Response;
import org.example.rpc.utils.ReflectionUtils;
import org.example.transport.RequestHandler;
import org.example.transport.TransportServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RpcServer {
    private RpcServerConfig config;
    private TransportServer net;
    private Encoder encoder;
    private Decoder decoder;
    private ServiceManager serviceManager;
    private ServiceInvoker serviceInvoker;

    public RpcServer(){
        this(new RpcServerConfig());
    }

    public RpcServer(RpcServerConfig config) {
        this.config = config;

        // net
        this.net = ReflectionUtils.newInstance(config.getTransportClass());
        this.net.init(config.getPort(), this.handler);

        // codec
        this.encoder = ReflectionUtils.newInstance(config.getEncodeClass());
        this.decoder = ReflectionUtils.newInstance(config.getDecodeClass());

        // service
        this.serviceManager = new ServiceManager();
        this.serviceInvoker = new ServiceInvoker();
    }

    public <T> void register(Class<T> interfaceClass, T bean) {
        serviceManager.register(interfaceClass, bean);
    }

    public void start() {
        this.net.start();
    }

    public void stop(){
        this.net.stop();
    }

    private RequestHandler handler = new RequestHandler() {
        @Override
        public void onRequest(InputStream recive, OutputStream toResp) {
            Response resp = new Response();

            try {
                byte[] inBytes = IOUtils.readFully(recive, recive.available());

                Request request = decoder.decode(inBytes, Request.class);

                ServiceInstance sis = serviceManager.lookup(request);
                Object ret = serviceInvoker.invoke(sis, request);
                resp.setData(ret);

            } catch (Exception e) {

                resp.setCode(1);
                resp.setMessage("RpcServer got error: " + e.getClass().getName() + " : " + e.getMessage());

            } finally {
                try {
                    byte[] outBytes = encoder.encode(resp);
                    toResp.write(outBytes);

                    System.out.println("response client");
                } catch (IOException e) {

                }
            }
        }
    };

}
