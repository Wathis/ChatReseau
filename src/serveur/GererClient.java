package serveur;

import java.io.IOException;
import java.net.Socket;

public class GererClient implements Runnable {

    private TCPServer server;
    private Socket client;

    public GererClient(TCPServer server, Socket client) {
        this.server = server;
        this.client = client;
    }

    public void run() {
        try {
            this.enregisterLeClient();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void enregisterLeClient() throws IOException {
        System.out.println(client.getInetAddress() + " connecté");
        ClientTCP newClient = new ClientTCP(client);
        newClient.envoyerMessage("Votre nom : ");
        String nom = newClient.getInput().readLine();
        System.out.println("[" + client.getInetAddress() + "] s'est enregistré sous le nom de " + nom);
        newClient.setNom(nom);
        server.getClients().add(newClient);
        server.envoyerAuxClients("+ " + nom + " a rejoint le chat\n", newClient);
        // Debut de la communication avec le client
        server.communiquerAvecClient(newClient);
    }
}
