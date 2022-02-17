package org.example.client;

import org.example.rpc.Peer;
import org.example.rpc.utils.ReflectionUtils;
import org.example.transport.TransportClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomTransportSelector implements TransportSelector{
    /**
     * 已经连接好的client
     */
    private List<TransportClient> clients;

    public RandomTransportSelector(){
        clients = Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    public void init(List<Peer> peers, int count, Class<? extends TransportClient> clazz) {
        count = Math.max(count, 1);
        for(Peer peer: peers){
            for (int i=0; i<count; i++){
                TransportClient client = ReflectionUtils.newInstance(clazz);
                client.connect(peer);
                clients.add(client);
            }
        }
    }

    @Override
    public TransportClient select() {
        int i = new Random().nextInt(clients.size());
        return clients.remove(i);
    }

    @Override
    public void release(TransportClient client) {
        clients.add(client);
    }

    @Override
    public void close() {
        for (TransportClient client: clients){
            client.close();
        }
        clients.clear();
    }
}
