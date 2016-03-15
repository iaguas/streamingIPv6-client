package client;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.ServerKey;

/**
 * Updates the server and channel list periodically, when it receives a message
 * via multicast
 *
 * @author Daniel
 */
public class MulticastUpdater implements Runnable {

    /**
     * The multicast group direction
     */
    private final InetAddress mdir;

    /**
     * The multicast group port
     */
    private final int mport;

    /**
     * The list of servers, each of them with their corresponding channels
     */
    private final ServerList sl;

    /**
     * The multicast socket used to listen the servers
     */
    private final MulticastSocket ms;

    /**
     * Constructor. Creates a multicast socket and adds it to a multicast group
     *
     * @param mdir The multicast group direction
     * @param mport The multicast group port
     * @param sl The list of servers created in the main class
     * @throws java.io.IOException When creating the multicastSocket fails
     */
    public MulticastUpdater(InetAddress mdir, int mport, ServerList sl) throws IOException {
        this.mdir = mdir;
        this.mport = mport;
        this.sl = sl;
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

                //String data = new String(buf); // convertir a BufferedReader
                // Recoger los datos del string
                BufferedReader data = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buf)));
                String line = data.readLine();
                final StringTokenizer tokens = new StringTokenizer(line);
                tokens.nextToken();
                final int port = new Integer(tokens.nextToken());
                InetAddress ip;
                try {
                    ip = InetAddress.getByName(tokens.nextToken());
                } catch (NoSuchElementException e){
                    ip = ms.getInetAddress();
                }
                
                // Si existe el servidor, lo recupero y borro la lista de canales. 
                // Sino lo creo
                Server s = sl.getServer(new ServerKey(ip, port));
                if(s==null)
                    s = new Server(ip, port);
                else
                    s.delAllChannel();
                
                // Relleno la lista con los canales.
                line = data.readLine();
                while(! (line.equals("MORE") || line.equals("END"))){
                    tokens.nextToken();
                    int numChannel = Integer.parseInt(tokens.nextToken());
                    String channelTitle = tokens.nextToken();
                    s.addChannel(new Channel(numChannel, channelTitle));
                    line = data.readLine();
                }
                
                if (line.equals("MORE"))
                    s.commitServer();
                else if(line.equals("END"))
                    s.notCommitServer();
                

                String datos = new String(buf);
                //TODO: tratamiento los paquetes

            } catch (IOException ex) {
                Logger.getLogger(MulticastUpdater.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("ERROR; no se ha podido leer la lista de servidores/canales");
            }
        }
    }
}
