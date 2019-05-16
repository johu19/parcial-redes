package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import world.AgarIO;

public class ThreadWaitingClients extends Thread {

	private Server server;
	private boolean keepAlive;
	public ThreadWaitingClients(Server s) throws IOException {
		server = s;
		keepAlive = true;
	}

	@Override
	public void run() {

		try {

			Socket socket = server.getServerSocket().accept();
			DataInputStream in = new DataInputStream(
					socket.getInputStream());
			DataOutputStream out = new DataOutputStream(
					socket.getOutputStream());
			if(server.getGame().getStatus().equals(AgarIO.PLAYING)) {
				out.writeUTF(Server.GAME_STARTED);

			}
			
			while (server.isWaitingClients() || keepAlive) {
				
				String mensaje = in.readUTF();
				
				//System.out.println(mensaje);

				if (mensaje.equals(Server.CONNECTED_CLIENT)) {
					
				
					server.setNumberOfClients(server.getNumberOfClients() + 1);
					
					if(server.getNumberOfClients()>1){
						server.activateBtnWindow();
						server.refreshPlayersWindow();
					}else if(server.getNumberOfClients()==1){
						server.refreshPlayersWindow();
					}
					
					if (!server.getTimer().isAlive()) {
						server.getTimer().start();

					}

					
					
					
					

					new ThreadSendInfoWR(server).start();

					// server.getThreadSIWR().add(new ThreadSendInfoWR(server));

//					out.writeUTF(Server.CONNECTED_CLIENT);
				}
				if(server.getGame().getStatus().equals(AgarIO.WAITING)) {
					out.writeUTF(Server.CONNECTED_CLIENT);
				}

			}
			
			
			
			
			in.close();
			out.flush();
			out.close();
			socket.close();
			
			
			
			

		} catch (Exception e) {
			
//			e.printStackTrace();

		}
	}
}
