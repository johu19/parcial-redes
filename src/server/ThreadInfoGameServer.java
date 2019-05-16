package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import world.AgarIO;

public class ThreadInfoGameServer extends Thread {

	private Server server;

	private boolean firstSend;

	private boolean running;

	public ThreadInfoGameServer(Server s) {

		server = s;

		firstSend = false;
		running = false;
	}

	@Override
	public void run() {

		running = true;

		try {

			Socket socket = server.getServerSocketGame().accept();

			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(
					socket.getOutputStream());

			while (running) {

				if (!firstSend) {

					out.writeUTF(server.sendInfoFirstTime());

					firstSend = true;

				} else if (server.getGame().getStatus()
						.equals(AgarIO.GAME_FINISHED)) {

				

					out.writeUTF("#end#");
					server.saveScores();
					server.setRunningGame(false);
					running = false;

					// server.setDiscClients(server.getDiscClients()+1);
					// if(server.getDiscClients() >=
					// server.getNumberOfClients()){
					// server.setRunningGame(false);
					// }

				} else {

					String info = server.infoGame();

					out.writeUTF(info);

				}

				if (running) {

					String received = in.readUTF();

					String[] s1 = received.split(",");
					String[] player = s1[0].split("/");
					String[] food = new String[0];
					if (s1.length > 1) {
						food = s1[1].split("/");
					}

					server.updateGame(player, food);
				}
			}

		} catch (Exception e) {

//			e.printStackTrace();

		}

	}

}
