package client;

import java.net.InetAddress;
import java.util.HashMap;

/**
 *
 * @author inigo
 */
public class Server {
    private final InetAddress ip;
    private final int port;
    private boolean isComitted;
    private final HashMap<Integer, Channel> channelList = new HashMap<Integer, Channel>();
    
    public Server(InetAddress ipv6, int port) {
        ip = ipv6;
        this.port = port;
        isComitted = false;
    }
    
    public void addChannel(Channel ch) {
        Integer key = ch.getNumChannel();
        channelList.put(key, ch);
    }
    
    public void delChannel(Channel ch) {
        Integer key = ch.getNumChannel();
        channelList.remove(key, ch);
    }
    
    public void delAllChannel() {
        channelList.clear();
    }
    
    public Channel getChannel(Integer key) {
        return channelList.get(key);
    }
    
    public Channel getChannel(int key) {
        return channelList.get(key);
    }
    
    public void commitServer(){
        isComitted = true;
    }
    
    public void notCommitServer(){
        isComitted = true;
    }
    
    public boolean isComitted(){
        return isComitted;
    }
    
    public InetAddress getIP() {
        return ip;
    }
    
    public int getPort() {
        return port;
    }
}
