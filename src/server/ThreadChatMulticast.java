package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ThreadChatMulticast extends Thread{

	private Server server;
	
	public ThreadChatMulticast(Server server) {
		this.server = server;
	}
	
	public void run() {
		
		try {
			
			//DataInputStream in;
			DataOutputStream out;
			
			while (server.isRunningChatService()) {
				System.out.print("");
				if(server.isSendMulticast() && server.getMessages().size() > 0) {
					
					for (int i = 0; i < server.getChatSockets().size(); i++) {
						
						Socket socket = server.getChatSockets().get(i);
						out = new DataOutputStream(socket.getOutputStream());
						
						for (int j = 0; j < server.getMessages().size(); j++) {
							
							
						//	System.out.println("ANTES");
							
							String message = server.getMessages().get(j);
							out.writeUTF(message);
							
							
						//	System.out.println("DSPS");
						}
						
					}
					
					server.eraseMessages();
				}
				
			}
		} catch (Exception e) {

		}
	}
}
