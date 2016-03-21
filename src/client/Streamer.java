package client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class Streamer implements Runnable {

    private Server server;
    private Channel channel;
    private Socket serverSocket;
    private int clientPort;

    /**
     * The path to the reproduction script
     */
    private String reproductionScript;

    /**
     * It's possible to specify an ip direction to receive the streaming,
     * instead of using the default oned
     */
    private InetAddress dir;

    public Streamer(Server server, Channel channel, int clientPort, String reproductionScript) {
        this.server = server;
        this.channel = channel;
        this.clientPort = clientPort;
        this.reproductionScript = reproductionScript;
        this.dir = null;
    }

    public Streamer(Server server, Channel channel, InetAddress dir, int clientPort, String reproductionScript) {
        this(server, channel, clientPort, reproductionScript);
        this.dir = dir;
    }

    @Override
    public void run() {
        DataOutputStream outToServer;
        BufferedReader inFromServer;

        try {
            serverSocket = new Socket(server.getIP(), server.getPort());

            outToServer = new DataOutputStream(serverSocket.getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            System.out.println("Conexión al servidor con éxito");
        } catch (IOException ex) {
            Logger.getLogger(Streamer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ERROR; no ha podido conectarse al servidor/canal");
            return;
        }

        try {
            //launch bash
            Runtime.getRuntime().exec(new String[]{this.reproductionScript, String.valueOf(clientPort)});
        } catch (IOException ex) {
            Logger.getLogger(Streamer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ERROR; no se ha podido abrir ejecutar el script de reproducción");
            return;
        }

        String message = "REQ " //REQ
                + this.channel.getNumChannel() //id
                + " " + this.clientPort; //clientPort
        if (this.dir != null) {
            message += dir.toString(); //clientDirection
        }

        try {
            outToServer.writeBytes(message);

            String res = inFromServer.readLine();

            if (res.toLowerCase().equals("REQ OK")) {
                System.out.println("El servidor ha comenzado el envío del vídeo");
            } else if (res.toLowerCase().startsWith("REQ FAIL")) {
                System.out.println("ERROR; El servidor no ha enviado el vídeo");
                System.out.println("Motivo: " + res.substring(res.lastIndexOf(" ") + 1));
            } else {
                System.out.println("ERROR; El servidor ha respondido con un tipo de mensaje no soportado");
            }
        } catch (IOException ex) {
            Logger.getLogger(Streamer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ERROR; no se ha podido leer la respuesta del servidor");
            return;
        }
    }
}
