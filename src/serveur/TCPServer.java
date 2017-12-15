package serveur;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class TCPServer {

	private LinkedList<ClientTCP> clients = new LinkedList<ClientTCP>();
	private ServerSocket serverSocket;
	private int port;

	public TCPServer()  {
		this.port = 6565;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e)  {
			System.out.print(e.getMessage());
		}
	}

	/**
	 * Permet de lancer le serveur de chat et d'accepter les requetes des clients
	 */
	public void runServeur() {
		System.out.println("Serveur de chat lancé sur le port " + port + "...");
		while (true) {
			try {
				Socket socketClient = serverSocket.accept();
				// Gerer le nouveau client accepté sur un nouveau thread
				new Thread(new GererClient(this, socketClient)).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Methode permettant d'attendre une réponse d'un client.
	 * La méthode envoie aussi a tous les autres le message recu
	 * Quand un client se déconnecte, il ferme la connexion avec celui-ci et previent les autres utilisateurs
	 * @param client client avec lequel on communique
	 * @throws IOException
	 */

	public void communiquerAvecClient(ClientTCP client) throws IOException {
		String reponseBrute = "";
		String reponse = "";
		while (!reponseBrute.equals("/quit")) {
			reponseBrute = client.getInput().readLine();
			if (reponseBrute.equals("/which")) {
				reponse = donnerClientsConnectes();
				System.out.println("[" + client.getName() + "] Demande de which");
				client.envoyerMessage(reponse);
			} else {
				reponse = "[" + client.getName() + "] " + reponseBrute + "\n";
				System.out.print(reponse);
				envoyerAuxClients(reponse, client);
			}
		}
		envoyerAuxClients("+ " + client.getName() + " a quitté le chat\n", client);
		client.getSocket().close();
		clients.remove(client);

	}

	/**
	 * Permet de donner la liste des clients actuellement connectés
	 * @return
	 */
	public String donnerClientsConnectes() {
		StringBuilder str = new StringBuilder();
		str.append("Membres connectés : \n");
		for (int i = 0; i < clients.size(); i++) {
			str.append("\t- " + clients.get(i).getName() + "\n");
		}
		return str.toString();
	}

	/**
	 * Permet d'envoyer un message a tout les clients du serveur de chat
	 * @param message
	 * @param clientEnvoyé Permet de dire a quel client il ne faut pas envoyer le message ( Celui qui à envoyé par exemple )
	 * @throws IOException
	 */
	public void envoyerAuxClients(String message, ClientTCP clientEnvoyé) throws IOException {
		for (int i = 0; i < clients.size(); i++) {
			// Ne pas envoyer a celui passé en parametre
			if (!clients.get(i).equals(clientEnvoyé)) {
				clients.get(i).envoyerMessage(message);
			}
		}
	}


	public LinkedList<ClientTCP> getClients() {
		return clients;
	}

	public void setClients(LinkedList<ClientTCP> clients) {
		this.clients = clients;
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
