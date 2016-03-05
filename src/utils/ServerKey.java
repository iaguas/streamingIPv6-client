/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.net.Inet6Address;

/**
 *
 * @author inigo
 */
public class ServerKey {
    Inet6Address ip;
    int port;
    
    public ServerKey(Inet6Address ipv6, int port){
        ip = ipv6;
        this.port = port;
    }
    
}
