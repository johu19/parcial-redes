package server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ThreadSendSongToClient extends Thread {
	
	
	private InetAddress address;
	
	private int port;
	
	public ThreadSendSongToClient(InetAddress clientAddress,int clientPort){
		address = clientAddress;
		port = clientPort;
	}
	
	
	@Override
	public void run() {

		try {
			send(address,port);
		} catch (Exception e) {
			
			e.printStackTrace();
		} 
		
	}
	
	public void send(InetAddress clientAddress,int clientPort) throws IOException, InterruptedException {
//		System.out.println("THREAD SEND SONG TO CLIENT");
		File myFile = new File(Server.MUSIC_SERVER);
		DatagramSocket ds = null;
		BufferedInputStream bis = null;
		try {
			ds = new DatagramSocket();
			DatagramPacket dp;
			int packetsize = 1024;
			double nosofpackets;
			nosofpackets = Math.ceil(((int) myFile.length()) / packetsize);

			bis = new BufferedInputStream(new FileInputStream(myFile));
			for (double i = 0; i < nosofpackets + 1; i++) {
				byte[] mybytearray = new byte[packetsize];
				bis.read(mybytearray, 0, mybytearray.length);
//				System.out.println("Packet:" + (i + 1));
				dp = new DatagramPacket(mybytearray, mybytearray.length,clientAddress ,clientPort);
				ds.send(dp);
			}
		} finally {
			if (bis != null)
				bis.close();
			if (ds != null)
				ds.close();
		}

	}

}
