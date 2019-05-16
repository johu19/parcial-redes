package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ThreadSendInfoUDP extends Thread {

	private Server server;

	private InetAddress address;

	private int port;

	public ThreadSendInfoUDP(Server s, InetAddress a, int p) {
		server = s;
		address = a;
		port = p;
	}

	@Override
	public void run() {
		try {

			DatagramSocket socket = new DatagramSocket();
			
			byte[] waiting = "WAITING   ".getBytes();

			while (!server.isRunningGame()) {

				
				
				DatagramPacket packet = new DatagramPacket(waiting, waiting.length,address,port); 
				socket.send(packet);
				
			}
			Thread.sleep(100);
			String s = server.sendInfoFirstTime()+"   ";
			byte[] send = s.getBytes();
			DatagramPacket packet = new DatagramPacket(send,send.length,address,port);
			socket.send(packet);
			
		    while (server.isRunningGame()) {

		    	s = server.infoGame()+"   ";
		    	
		    	
				send = s.getBytes();
				
				DatagramPacket packet1 = new DatagramPacket(send,send.length,address,port);
				socket.send(packet1);
				
			}
		    
		    s="#end#";
		    server.setAlive(false);
		    send = s.getBytes();
		    DatagramPacket packet1 = new DatagramPacket(send,send.length,address,port);
			socket.send(packet1);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
