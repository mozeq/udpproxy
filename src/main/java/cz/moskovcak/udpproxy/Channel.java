package cz.moskovcak.udpproxy;

/**
 * Created by jmoskovc on 2/25/14.
 */
public class Channel {
    public String name;
    public String address;
    public int destPort = 11111;

    public Channel(String name, String address) {
        this.name = name;
        this.address = address;
    }
}
