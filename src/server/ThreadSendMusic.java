package server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Date;

public class ThreadSendMusic extends Thread{
	
	private Server server;
	
	public ThreadSendMusic(Server s){
		server = s;
	}
	
	
	public void run() {
		System.out.println("UDP Multicast Time Server Started");
		try {

			
			DatagramSocket socketMusic = new DatagramSocket(Server.PORT_MUSIC);
			
			byte[] data = new byte[1024];
			while(server.isAlive())
			{
				
				
				
				DatagramPacket firstPacket = new DatagramPacket(data, data.length);
				
				socketMusic.receive(firstPacket);
				
				int clientPort = firstPacket.getPort();
				InetAddress clientAddress = firstPacket.getAddress();
				
				
				
				ThreadSendSongToClient th = new ThreadSendSongToClient(clientAddress, clientPort);
				th.start();

				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
			System.out.println(
			"UDP Multicast Time Server Terminated");
	}
	
	
	
	

}
