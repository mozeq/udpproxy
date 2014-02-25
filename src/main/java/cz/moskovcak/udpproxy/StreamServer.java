package cz.moskovcak.udpproxy;

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Created by jmoskovc on 2/11/14.
 */
public class StreamServer {

    public List<Channel> channels;
    public String address;
    public int port;

    public StreamServer (String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void serve() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(this.address, this.port), 0);
        for (Channel c: channels) {
            server.createContext("/ct4", new ChannelHandler(c));
        }
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    static class ChannelHandler implements HttpHandler {
        Channel channel;
        public ChannelHandler(Channel c) {
            channel = c;
        }
        public void handle(HttpExchange t) throws IOException {
            int BUFFER_LENGTH = 512000;
            //String response = "This is the response";
            t.sendResponseHeaders(200, 0);
            OutputStream output = t.getResponseBody();
            byte[] b = new byte[BUFFER_LENGTH];
            DatagramPacket dgram = new DatagramPacket(b, b.length);
            MulticastSocket socket =
                    new MulticastSocket(channel.destPort); // must bind receive side
            socket.joinGroup(InetAddress.getByName(channel.address));

            System.out.println("Listening to: " + channel.address + ":" + channel.destPort);

            //FileOutputStream output = new FileOutputStream("stream.out");
            //BufferedOutputStream writer = new BufferedOutputStream(output);
            long wrote = 0;
            while(true) {
                socket.receive(dgram); // blocks until a datagram is received
                //System.err.println("Received " + dgram.getLength() +
                //        " bytes from " + dgram.getAddress());
                wrote += dgram.getLength();
                //System.err.print("dgram from: " + dgram.getAddress());
                output.write(dgram.getData(), 0, dgram.getLength());
                System.out.println("wrote: \r" + wrote);
                dgram.setLength(b.length); // must reset length field!
                output.flush();
            }
            //writer.close();
            //os.write(response.getBytes());
            //os.close();
        }
    }

}
