/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.HashMap;
import utils.ServerKey;

/**
 *
 * @author inigo
 */
public class ServerList {
    private final HashMap<ServerKey, Server> list = new HashMap<ServerKey, Server>();
    
    public void addServer(Server sev) {
        ServerKey key = new ServerKey(sev.getIP(), sev.getPort());
        list.put(key, sev);
    }
    
    public void delServer(Server sev){
        ServerKey key = new ServerKey(sev.getIP(), sev.getPort());
        list.remove(key);
    }
    
    public Server getServer(ServerKey key){
        return list.get(key);
    }
}
