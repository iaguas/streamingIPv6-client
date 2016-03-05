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
    Inet6Address ip;
    int port;
    HashMap<Integer, Channel> channelList = new HashMap<Integer, Channel>();
    
    public Server(Inet6Address ipv6, int port){
        ip = ipv6;
        this.port = port;
    }
    
    public void addChannel(Channel ch) {
        
    }
    
    public void delChannel(Channel ch) {
        
    }
    
    public Channel getChannel(){
        return null;
    }
    
}
