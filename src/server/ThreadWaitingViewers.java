package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import world.AgarIO;

public class ThreadWaitingViewers extends Thread {

	private Server server;

	public ThreadWaitingViewers(Server s) {

		server = s;

	}

	@Override
	public void run() {

		try {

			DatagramSocket socket = new DatagramSocket(Server.PORT_UDP);

			byte[] data = new byte[1024];

			while (server.isAlive()) {

				DatagramPacket firstPacket = new DatagramPacket(data,
						data.length);

				socket.receive(firstPacket);

				int clientPort = firstPacket.getPort();
				InetAddress clientAddress = firstPacket.getAddress();
				
				String user = new String(firstPacket.getData());
				
				boolean cond = server.verifyUserRegistered(user);
				
				if(cond==false){
				
					data = "ACEPTA".getBytes();
					DatagramPacket packet = new DatagramPacket(data, data.length,clientAddress,clientPort);
					socket.send(packet);
					
					ThreadSendInfoUDP th = new ThreadSendInfoUDP(server, clientAddress, clientPort);
					th.start();
					
					ThreadUsersMessagesHandler usm = new ThreadUsersMessagesHandler(server);
					
					usm.start();
					
					
					
				}else{
					data = "NOACEPTA".getBytes();
					DatagramPacket packet = new DatagramPacket(data, data.length,clientAddress,clientPort);
					socket.send(packet);
					
				}
				
				
			}

		} catch (Exception e) {
//			e.printStackTrace();
		}

	}
	
	

}
