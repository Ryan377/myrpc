package org.example.transport;

import java.io.InputStream;
import java.io.OutputStream;

public interface RequestHandler {
    void onRequest(InputStream recive, OutputStream toResp);
}
