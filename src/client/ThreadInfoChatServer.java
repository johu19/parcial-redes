package client;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ThreadInfoChatServer extends Thread{

	public Client client;
	
	public ThreadInfoChatServer(Client client) {

		this.client = client;
	}
	public void run() {
		DataInputStream in;
		//Socket declarlo como atributo para no tener que obtenerlo siempre durante el while, que problema hay con ello
		Socket socket ;
		

		try {
			
			while(client.isChatService()) {
				socket = client.getChatSocket();
				in = new DataInputStream(socket.getInputStream());
				String serverMessage = in.readUTF();
				client.receiveMessage(serverMessage.replaceAll(";", " : ")+"\n");
				
			}
			
			
			
		} catch (Exception e) {
//			e.printStackTrace();
		}
	}
	
	
	
}
