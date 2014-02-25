package cz.moskovcak.udpproxy;

import org.springframework.stereotype.Component;

import java.io.IOException;

public class UdpProxyService {
    StreamServer server;

    public void setServer(StreamServer server) {
        this.server = server;
    }

    public void serve() throws IOException {
        try {
            server.serve();
        } catch (Exception e) {
            System.err.println("Can't run server: " + e);
        }
    }
}
