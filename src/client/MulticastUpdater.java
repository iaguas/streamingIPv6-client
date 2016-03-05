package client;

import java.util.HashMap;

/**
 * Updates the server and channel list periodically, when it receives a message
 * via multicast
 * 
 * @author rsng06
 */
public class MulticastUpdater implements Runnable {

    HashMap<ServerKey, Server> servers;

    /**
     * Constructor
     * @param servers The list of servers created in the main class
     */
    public MulticastUpdater(HashMap<ServerKey, Server> servers) {
        this.servers = servers;
    }

    /**
     * Creates a multicast socket and checks in the background if there are new
     * servers/channels or have been modified
     */
    @Override
    public void run() {
        //TODO: create multicast socket
    }
}
