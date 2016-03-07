/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.net.Inet6Address;
import java.util.HashMap;

/**
 *
 * @author inigo
 */
public class Server {
    private final Inet6Address ip;
    private final int port;
    private final HashMap<Integer, Channel> channelList = new HashMap<Integer, Channel>();
    
    public Server(Inet6Address ipv6, int port) {
        ip = ipv6;
        this.port = port;
    }
    
    public void addChannel(Channel ch) {
        Integer key = ch.getNumChannel();
        channelList.put(key, ch);
    }
    
    public void delChannel(Channel ch) {
        Integer key = ch.getNumChannel();
        channelList.remove(key, ch);
    }
    
    public Channel getChannel(Integer key) {
        return channelList.get(key);
    }
    
    public Channel getChannel(int key) {
        return channelList.get(key);
    }
    
    public Inet6Address getIP() {
        return ip;
    }
    
    public int getPort() {
        return port;
    }
}
