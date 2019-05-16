package server;

import java.io.DataInputStream;
import java.net.Socket;

public class ThreadUsersMessagesHandler extends Thread {
	
//	private Socket socket;
	
	private Server server;

	public ThreadUsersMessagesHandler(Server server) {

//		this.socket = socket;
		this.server = server;
	}

	
	public void run() {
		
		try {
			
		//	System.out.println("COMIENZA HILO");
			
			DataInputStream in;
			
			

			Socket socket = server.getServerSocketChat().accept();
			
			server.addChatSocket(socket);
			

			in = new DataInputStream(socket.getInputStream());
			
			while(server.isRunningChatService()) {
				
				
				
				String userMessage = in.readUTF();
				
//				System.out.println("ANTES");
				
				server.newMessage(userMessage);
				
//				System.out.println("DSPS");
				
				Thread.sleep(20);
						
			}
			
			
			
		} catch (Exception e) {
			System.out.println("User disconnected");

		}
		
		
	}
	
}
