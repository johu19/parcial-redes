package server;

import java.io.DataInputStream;
import java.net.Socket;

public class ThreadUsersMessages extends Thread{

	private Server server;
	
	public ThreadUsersMessages(Server server) {
		this.server = server;
	}
	
	public void run() {
		try {
			
			
			DataInputStream in;
			
			while(server.isRunningChatService()) {
				Socket socketReceived = server.getServerSocketChat().accept();
				in = new DataInputStream(socketReceived.getInputStream());
				
				//System.out.println("LLEGA AQUI");
				
				String clientMessage = in.readUTF();
				
			//	System.out.println(clientMessage);
				
//				server.verifyUserRegistered(socketReceived, clientMessage);
				server.newMessage(clientMessage);
				Thread.sleep(5);
				server.setSendMulticast(true);
			}
			
		} catch (Exception e) {
//			e.printStackTrace();
		}
		
	}
}
