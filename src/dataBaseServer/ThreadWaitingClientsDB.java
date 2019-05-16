package dataBaseServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.Provider;
import java.security.Security;

import javax.net.ssl.SSLSocket;

import server.Server;
import server.ThreadWaitingClients;

public class ThreadWaitingClientsDB extends Thread {

	private DataBaseServer serverDB;

	public ThreadWaitingClientsDB(DataBaseServer s) {
		serverDB = s;

		
	}

	@Override
	public void run() {
		try {
			while (serverDB.isWaitingClients()) {
				
				

				SSLSocket socket =(SSLSocket) serverDB.getServerSocketSSL().accept();
				
				

				DataInputStream in = new DataInputStream(
						socket.getInputStream());
				DataOutputStream out = new DataOutputStream(
						socket.getOutputStream());
				String mensaje = in.readUTF();

				if (!mensaje.equalsIgnoreCase("")) {
					String[] info = mensaje.split(",");
					String mode = info[0];

					if (mode.equals(DataBaseServer.LOGIN_DB) && serverDB.getNumberOfClients()<5) {

						String email = info[1];
						String password = info[2];
						String result = serverDB.loginPlayer(email, password);
						if (result != null) {
						
							new ThreadWaitingClients(serverDB.getServer())
									.start();
							out.writeUTF(DataBaseServer.CONF_ACCESS);
							
							out.writeUTF(result);
							
							serverDB.addPlayer(result);
							
							serverDB.setNumberOfClients(serverDB.getNumberOfClients()+1);

						} else {
							out.writeUTF(DataBaseServer.DENIED_ACCESS);
						}

					} else if (mode.equals(DataBaseServer.REGISTER_DB)&& serverDB.getNumberOfClients()<5) {

						String nick = info[1];
						String password = info[2];
						String email = info[3];
						String result = serverDB.registerPlayer(nick, password,
								email);
						if (result != null) {
							new ThreadWaitingClients(serverDB.getServer())
									.start();
							out.writeUTF(DataBaseServer.PLAYER_SAVED);
							out.writeUTF(result);
							

							serverDB.addPlayer(result);
							
							serverDB.setNumberOfClients(serverDB.getNumberOfClients()+1);

						} else {
							out.writeUTF(DataBaseServer.PLAYER_NOTSAVED);
						}

					}

				}
			}
		} catch (Exception e) {

		}

	}

}
