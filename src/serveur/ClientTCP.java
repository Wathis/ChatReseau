package serveur;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientTCP {

	private Socket socket;
	private BufferedReader input;
	private BufferedWriter output;
	private String nom;

	public ClientTCP(Socket socket) throws IOException {
		this.socket = socket;
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	}

	public void envoyerMessage(String msg) throws IOException {
		output.write(msg);
		output.flush();
	}

	public String getName() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public BufferedReader getInput() {
		return input;
	}

	public void setInput(BufferedReader input) {
		this.input = input;
	}

	public BufferedWriter getOutput() {
		return output;
	}

	public void setOutput(BufferedWriter output) {
		this.output = output;
	}

}
