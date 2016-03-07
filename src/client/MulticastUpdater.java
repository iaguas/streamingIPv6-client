package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.ServerKey;

/**
 * Updates the server and channel list periodically, when it receives a message
 * via multicast
 *
 * @author rsng06
 */
public class MulticastUpdater implements Runnable {

    /**
     * The multicast group direction
     */
    private InetAddress mdir;

    /**
     * The multicast group port
     */
    private int mport;

    /**
     * The list of servers, each of them with their corresponding channels
     */
    private HashMap<ServerKey, Server> servers;

    /**
     * The multicast socket used to listen the servers
     */
    private MulticastSocket ms;

    /**
     * Constructor. Creates a multicast socket and adds it to a multicast group
     *
     * @param mdir The multicast group direction
     * @param mport The multicast group port
     * @param servers The list of servers created in the main class
     * @throws java.io.IOException When creating the multicastSocket fails
     */
    public MulticastUpdater(InetAddress mdir, int mport, HashMap<ServerKey, Server> servers) throws IOException {
        this.mdir = mdir;
        this.mport = mport;
        this.servers = servers;
        this.ms = new MulticastSocket(mport);
        this.ms.joinGroup(mdir);
    }

    /**
     * Checks in the background if there are new servers/channels or have been
     * modified
     */
    @Override
    public void run() {
        byte[] buf = new byte[2000];
        while (true) {
            try {
                DatagramPacket p = new DatagramPacket(buf, 2000);
                ms.receive(p);
                String datos = new String(buf);
            } catch (IOException ex) {
                Logger.getLogger(MulticastUpdater.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("ERROR; no se ha podido leer la lista de servidores/canales");
            }
        }
    }
}
