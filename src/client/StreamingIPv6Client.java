package client;

import java.io.IOException;
import java.net.InetAddress;
//import java.net.Inet6Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        ServerList sl;

        System.out.println("Streaming Client IPv6\n\n");

        if (args.length % 2 != 0) {
            System.out.println("StreamingIPv6Client -s scriptreproducir -m mdir -o mpuerto");
            throw new IllegalArgumentException("Error; los parámetros son incorrectos");
        }

        reproductionScript = null;
        mdir = null;
        mport = null;

        //reading the parameters
        for (int i = 0; i < args.length; i++) {

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

            //we set the multicast group ip
            if (args[i].equals("-o")) {
                mport = Integer.parseInt(args[i + 1]);
            }
        }

        if (reproductionScript == null || mdir == null || mport == null) {
            System.out.println("ERROR; no todos los parámetros se han inicializado:");
            System.out.println("StreamingIPv6Client -s scriptreproducir -m mdir -o mpuerto");
            System.out.println("Dirección de multicast: " + mdir);
            System.out.println("Puerto de multicast: " + mport);
            System.out.println("Script de reproducción: " + reproductionScript);
            System.exit(-1);
        } else {
            System.out.println("Lectura de datos correcta:");
            System.out.println("Dirección de multicast: " + mdir);
            System.out.println("Puerto de multicast: " + mport);
            System.out.println("Script de reproducción: " + reproductionScript);
        }
        System.out.println("\n");

        //we launch the channel updater
        sl = new ServerList();
        Thread updater;
        while (true) {
            try {
                System.out.println("Intentando conectarse a la lista de canales...");
                updater = new Thread(new MulticastUpdater(mdir, mport, sl));
                System.out.println("Conexión a la lista de canales con éxito.");
                break;
            } catch (IOException ex) {
                Logger.getLogger(StreamingIPv6Client.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error al conectarse a la lista de canales; reintentando...");
            }
        }
        updater.start();

        //Shell
        System.out.println("\n");
        System.out.println("Comandos:");
        System.out.println("EXIT: finaliza el programa");
        System.out.println("SERVERS: lista de servidores");
        System.out.println("CONNECT servidor canal puerto_cliente [ip_cliente]: conexión a un canal de un servidor\n");

        Scanner scanner = new Scanner(System.in);
        String s; //keyboard input

        Server server; //the server to which we connect
        while (true) {
            System.out.print(">");
            s = scanner.nextLine();

            if (s.toLowerCase().equals("exit")) {
                System.exit(0);
            } else if (s.toLowerCase().equals("servers")) {
                System.out.println("lista de servidores:");

                //we show the list of servers (with their channels)
                ArrayList<Server> servers = sl.toArrayList();
                for (int i = 0; i < servers.size(); i++) {
                    System.out.println("servidor" + (i + 1) + ":"); //the client sees the servers starting by 1
                    System.out.println(servers.get(i).toString() + "\n");
                }
            } else if (s.toLowerCase().startsWith("connect")) {
                StringTokenizer tokens = new StringTokenizer(s);
                if ((tokens.countTokens() != 4) || (tokens.countTokens() != 5)) { //CONNECT servidor canal puerto_cliente [ip_cliente]
                    System.out.println("Error: la llamada a CONNECT es incorrecta");
                } else {
                    tokens.nextToken(); //we remove the "connect" token
                    int serverIndex = Integer.parseInt(tokens.nextToken()) - 1;
                    int channelIndex = Integer.parseInt(tokens.nextToken());
                    int clientPort = Integer.parseInt(tokens.nextToken());

                    server = sl.toArrayList().get(serverIndex);
                    Streamer streamer = new Streamer(server, server.getChannel(channelIndex), clientPort, reproductionScript);
                    streamer.run();
                }
            } else {
                System.out.println("Comando inválido");
            }
        }//end while(true)
    }
}
