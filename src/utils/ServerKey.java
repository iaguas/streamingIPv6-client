/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.net.InetAddress;

/**
 *
 * @author inigo
 */
public class ServerKey {
    InetAddress ip;
    int port;
    
    public ServerKey(InetAddress ipv6, int port) {
        ip = ipv6;
        this.port = port;
    }
    
    @Override
    public boolean equals (Object obj) {
        if (obj instanceof ServerKey) {
            ServerKey skey = (ServerKey) obj;
            if (this.ip.equals(skey.ip)) {
                if (this.port == skey.port) {
                    return true;
                }
            }
        } 
        return false;
    } 

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.ip != null ? this.ip.hashCode() : 0);
        hash = 37 * hash + this.port;
        return hash;
    }
}
