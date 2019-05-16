package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ThreadSendInfoWR extends Thread {
	

	private Server server;

	public ThreadSendInfoWR(Server s) throws IOException {

		server = s;
	}

	@Override
	public void run() {

		try {
			
			Socket socketInfo= server.getSsocketInfo().accept();	
			
			DataOutputStream outInfo = new DataOutputStream(
					socketInfo.getOutputStream());
			
			int lastTime = 0;
			while (server.isWaitingClients()) {

				

				String clients = new String();
				ArrayList<String> clientsArray = server.getPlayers();
				for (int i = 0; i < clientsArray.size(); i++) {
					if (i == clientsArray.size() - 1) {
						clients += clientsArray.get(i);
					} else {
						String m = clientsArray.get(i) + ",";
						clients += m;
					}
				}

				int theTime = server.getTimer().getSeconds();

				while(theTime==lastTime){
					theTime = server.getTimer().getSeconds();
				}
				
				
				outInfo.writeUTF(theTime + "," + clients);
				
				
				
			}

			
			
			
			outInfo.writeUTF(Server.START_GAME);
			
			
			
			
			outInfo.close();
			
			
			
		} catch (Exception e) {
		}

	}

}
