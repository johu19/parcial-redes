package client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import server.Server;

public class ThreadReceiveInfoUDP extends Thread {
	
	private Client client;
	
	private ViewersWR window;
	
	private DatagramSocket socket;
	
	public ThreadReceiveInfoUDP(Client c){
		client = c;
	}
	
	public void startConnection(String nick) throws Exception{
		
		socket = new DatagramSocket();
		String n = nick + ";";
		byte[] first = n.getBytes();
		DatagramPacket fPacket = new DatagramPacket(first, first.length,InetAddress.getByName(client.getIpServer()),Server.PORT_UDP);
		socket.send(fPacket);
		first = new byte[1024];
		fPacket = new DatagramPacket(first, first.length);
		socket.receive(fPacket);
		String m = new String(fPacket.getData());
		if(m.startsWith("ACEPTA")) {
			this.start();	
		}else {
			throw new Exception();
		}
		
	}
	
	@Override
	public void run() {

		try {
		
			byte[] fInfo = new byte[3000];
			DatagramPacket fPacket = new DatagramPacket(fInfo, fInfo.length);
			socket.receive(fPacket);
			
			String firstInfo = new String(fPacket.getData());
			
			String[] a = firstInfo.split("   ");
			
			firstInfo = a[0];
			
			initizalizeWindow();
			
			
//			ThreadReceiveMusic th = new ThreadReceiveMusic(client,client.getNickname());
//			th.start();
			
			
			client.chatService();
			boolean cond = false;
			
			while(!cond){
				
				
//				System.out.println(firstInfo);
				
				if(firstInfo.startsWith("#f#")){

                    closeWindow();
					
					firstInfo = firstInfo.substring(3);
					
//					System.out.println("FIRST INFO: "+firstInfo);
                    
					String[] fInfoBig = firstInfo.split("_");

					String[] fInfoPlayers = fInfoBig[0].split(",");

					String[] fInfoBalls = fInfoBig[1].split(",");
					
					client.setViewer(true);
				
					client.initializeWorld(fInfoPlayers, fInfoBalls);
					
				}else if(firstInfo.startsWith("#end#")){
					
					
					cond=true;
					
					
					
				}else if(firstInfo.startsWith("WAITING")){
				
				
				}else{
					
					String info = firstInfo;
					
//					System.out.println(firstInfo.length());
					
					String[] infoBig = info.split("_");

					String[] infoPlayers = infoBig[0].split(",");


					String[] infoBalls = infoBig[1].split(",");

					client.updateGame(infoPlayers, infoBalls);
					
					
					
				}
				
				fInfo = new byte[3000];
				fPacket = new DatagramPacket(fInfo, fInfo.length);
				socket.receive(fPacket);

				firstInfo = new String(fPacket.getData());
				
				a = firstInfo.split("   ");
				
				firstInfo = a[0];
				
			}
			
			client.stopGame();
			
			
			
		} catch (Exception e) {
//			e.printStackTrace();
			
		}
		
		
		
		
		
	}

	private void closeWindow() {
		
		window.setVisible(false);
	}

	private void initizalizeWindow() {
		
		System.out.println("STARTS WR VIEWERS WINDOW");
		
		window = new ViewersWR();
		window.setVisible(true);
	}

}
