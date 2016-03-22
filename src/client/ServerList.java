package client;

import java.util.ArrayList;
import java.util.HashMap;
import utils.ServerKey;

/**
 *
 * @author inigo
 */
public class ServerList {

    private final HashMap<ServerKey, Server> list;

    public ServerList() {
        this.list = new HashMap<ServerKey, Server>();
    }

    public void addServer(Server sev) {
        ServerKey key = new ServerKey(sev.getIP(), sev.getPort());
        list.put(key, sev);
    }

    public void delServer(Server sev) {
        ServerKey key = new ServerKey(sev.getIP(), sev.getPort());
        list.remove(key);
    }

    public Server getServer(ServerKey key) {
        return list.get(key);
    }

    public ArrayList<Server> toArrayList() {
        return new ArrayList<Server>(list.values());
    }
}
