package client;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

import server.Server;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import world.PlayerBall;

public class ThreadReceiveMusic extends Thread {

	private boolean cond;
	
	private String rootM;
	
	private Client client;

	public ThreadReceiveMusic(Client c, String root) {
		client = c;
		String s = "gameMusic"+root;
		rootM = s+".wav";
		cond = false;
	}

	public void run() {

		System.out.println("UDP Multicast Time Client Started");
		try {
			
			
			DatagramSocket socketMusic = new DatagramSocket(); // asigna un puerto aleatoriamente

			byte[] firstData = "holi".getBytes();
			InetAddress adressServer = InetAddress.getByName(client.getIpServer());
			DatagramPacket firstPakcet = new DatagramPacket(firstData,firstData.length, adressServer, Server.PORT_MUSIC);
			socketMusic.send(firstPakcet);
			
			int packetsize = 1024;
			FileOutputStream fos = null;
			System.out.println(client.getMusicRoot());
			fos = new FileOutputStream(rootM);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			double nosofpackets = Math.ceil(((int) (new File(Server.MUSIC_SERVER)).length()) / packetsize);
			byte[] mybytearray = new byte[packetsize];
			DatagramPacket receivePacket = new DatagramPacket(mybytearray,mybytearray.length);

			

			
			for (double i = 0; i < 23000.0; i++) {

				socketMusic.receive(receivePacket);
				byte audioData[] = receivePacket.getData();

				bos.write(audioData, 0, audioData.length);
			}
			
			bos.close();
			fos.close();
			
			
			playMusic();

		} catch (Exception e) {
			e.printStackTrace();

		}
		System.out.println("UDP Multicast Time Client Terminated");

	}

	private void playMusic() throws Exception {
		
		
		
		InputStream music = new FileInputStream(new File(rootM));
		AudioStream audio = new AudioStream(music);
		AudioPlayer.player.start(audio);
		
//		File audioFile = new File(Client.MUSIC_ROOT);
//		 
//		AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
//		
//		AudioFormat format = audioStream.getFormat();
//		 
//		DataLine.Info info = new DataLine.Info(Clip.class, format);
//		
//		Clip audioClip = (Clip) AudioSystem.getLine(info);
//		
//		audioClip.open(audioStream);
//		audioClip.start();
//		
//		audioClip.close();
//		audioStream.close();
		
		
	}
	
	public void setCond(boolean b) {
		cond=b;
	}

}
