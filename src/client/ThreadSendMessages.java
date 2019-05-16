package client;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ThreadSendMessages extends Thread {

	private Client client;

	private ArrayList<String> messages;

	private int size;

	public void addMessage(String m) {
		messages.add(m);
		size++;
	}

	public ThreadSendMessages(Client client) {
		messages = new ArrayList<String>();
		this.client = client;
	}

	public void run() {

		try {
			// Realizar lectura de los mensajes del usuario
			DataOutputStream out;

			String mensaje = "";
			Socket socket;
		
			while (client.isChatService()) {
				
				System.out.print("");
				
				if (this.client.getUserMessages().size() > 0) {

					socket = client.getChatSocket();
					out = new DataOutputStream(socket.getOutputStream());

					for (int i = 0; i < client.getUserMessages().size(); i++) {
						mensaje = client.getNickname()+ ";";
						mensaje += client.getUserMessages().get(i);
						out.writeUTF(mensaje);

					}
					client.eraseMessages();
				}
				Thread.sleep(20);
			}

		} catch (Exception e) {
			
			
//			e.printStackTrace();

		}

	}

}
