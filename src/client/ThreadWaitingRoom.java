package client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import server.Server;

public class ThreadWaitingRoom extends Thread {

	private Client client;


	public ThreadWaitingRoom(Client c) throws UnknownHostException, IOException {
		client = c;
		
	}

	@Override
	public void run() {

		try {

			String info[] = null;
			String lastTime = "0";

			DataInputStream in = new DataInputStream(client.getClientSocket().getInputStream());

			while (client.isWaitingForPlay()) {

				info = in.readUTF().split(",");
				while (info[0].equals(lastTime)) {
					info = in.readUTF().split(",");
				}

				if (info[0].equals(Server.START_GAME)) {
					client.setWaitingForPlay(false);
					client.closeWaitingRoom();
					
				}else{
				
					lastTime = info[0];

					client.refreshWaitingRoom(info);

					
				}
				
				
			}

			in.close();
			client.startGame();
			

		} catch (Exception e) {
//			e.printStackTrace();
			client.getGui().getJdWaitingRoom().dispose();
			client.getGui().dispose();
			
		}

	}

}
