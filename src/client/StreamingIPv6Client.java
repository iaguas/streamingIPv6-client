package client;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.ServerKey;

/**
 *
 * @author Daniel
 */
public class StreamingIPv6Client {

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        /**
         * The script used for reproduction of the streaming video
         */
        String reproductionScript;

        /**
         * The multicast group direction
         */
        InetAddress mdir;

        /**
         * The multicast group port
         */
        Integer mport;

        /**
         * The list of servers, each of them with their corresponding channels
         */
        HashMap servers;

        if (args.length % 2 != 0) {
            throw new IllegalArgumentException("Error; los parámetros son incorrectos");
        }

        reproductionScript = null;
        mdir = null;
        mport = null;

        //reading the parameters
        for (int i = 1; i < args.length; i++) {

            //we set the reproduction script
            if (args[i].equals("-s")) {
                reproductionScript = args[i + 1];
            }

            //we set the multicast group direction
            if (args[i].equals("-m")) {
                try {
                    mdir = InetAddress.getByName(args[i + 1]);
                    //mdir = Inet6Address.getByAddress("ServerListMulticast", args[i + 1].getBytes(), 5); //multicast scope
                } catch (UnknownHostException ex) {
                    Logger.getLogger(StreamingIPv6Client.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("ERROR; Host desconocido");
                    System.exit(-1);
                }
            }

            if (reproductionScript == null || mdir == null || mport == null) {
                System.out.println("ERROR; no todos los parámetros se han inicializado");
                System.exit(-1);
            }

            //we set the multicast group ip
            if (args[i].equals("-o")) {
                mport = Integer.parseInt(args[i + 1]);
            }
        }

        //we launch the channel updater
        servers = new HashMap<ServerKey, Server>();
        Thread updater;
        while (true) {
            try {
                System.out.println("Intentando conectarse a la lista de canales...");
                updater = new Thread(new MulticastUpdater(mdir, mport, servers));
                System.out.println("Conexión a la lista de canales con éxito.");
                break;
            } catch (IOException ex) {
                Logger.getLogger(StreamingIPv6Client.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error al conectarse; reintentando...");
            }
        }
        updater.start();
    }
}
