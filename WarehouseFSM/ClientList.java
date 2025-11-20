// Name: Carson Stallcup
// StarID: po1278cm
// Group 3
// Purpose: This class contains many clients.  It can get, 
// add, or delete clients and modify the list.

import java.util.*;

public class ClientList {
    private ArrayList<Client> clients;
    private static ClientList clientList;

    private ClientList() {
        this.clients = new ArrayList<Client>();
    }

    public static ClientList instance() {
        if (clientList == null)
            clientList = new ClientList();
        return clientList;
      }

    public boolean insertClient(Client client) {
        for (Client c : this.clients) {
            if (client.getId().equals(c.getId())) {
                return false;
            }
        }
        this.clients.add(client);
        return true;
    }

    public Client searchClient(String clientId) {
        for (Client c : this.clients) {
            if (c.getId().equals(clientId)) {
                return c;
            }
        }
        return null;
    }

    public Iterator<Client> getClients() {
        return this.clients.iterator();
    }
}
