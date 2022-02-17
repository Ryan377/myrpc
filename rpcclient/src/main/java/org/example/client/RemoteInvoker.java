package org.example.client;

import org.apache.commons.io.IOUtils;
import org.example.codec.Decoder;
import org.example.codec.Encoder;
import org.example.rpc.Request;
import org.example.rpc.Response;
import org.example.rpc.ServiceDescriptor;
import org.example.transport.TransportClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RemoteInvoker implements InvocationHandler {
    private Class clazz;
    private Encoder encoder;
    private Decoder decoder;
    private TransportSelector selector;

    public RemoteInvoker(Class clazz, Encoder encoder, Decoder decoder, TransportSelector selector) {
        this.clazz = clazz;
        this.decoder = decoder;
        this.encoder = encoder;
        this.selector = selector;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Request request = new Request();
        request.setServiceDescriptor(ServiceDescriptor.from(clazz, method));
        request.setParameters(args);

        Response resp = invokeRemote(request);
        if (resp == null || resp.getCode() != 0) {
            throw new IllegalStateException("fail to invoke remote: " + resp);
        }

        return resp.getData();
    }

    private Response invokeRemote(Request request) {
        Response resp = null;
        TransportClient client = null;

        try {
            client = selector.select();

            byte[] outBytes = encoder.encode(request);
            InputStream recive = client.write(new ByteArrayInputStream(outBytes));

            byte[] inBytes = IOUtils.readFully(recive, recive.available());
            resp = decoder.decode(inBytes, Response.class);

        } catch (IOException e) {
            resp = new Response();
            resp.setCode(1);
            resp.setMessage("RpcClient got error: " + e.getClass() + ":" + e.getMessage());
        } finally {
            if (client != null) {
                selector.release(client);
            }
        }
        return resp;
    }
}
