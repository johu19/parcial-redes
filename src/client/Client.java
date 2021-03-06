package client;

import gui.GUI_principal;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.Security;

//import Cliente.HiloAtentoAlMulticast;

import java.util.ArrayList;

import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;


import dataBaseServer.DataBaseServer;
import server.Server;
import world.AgarIO;
import world.Ball;
import world.PlayerBall;

public class Client {

	public static final int PORT_TCP = 3425;
	
	public final static int PORT_CHAT = 46567;
	
//	public static final String MUSIC_ROOT = "gameMusic.wav";
	
	public static final int MUSIC_SIZE = 41009;
	
	
	private boolean viewer;
	
	
	private String musicRoot;
	

	private String IpServer;
	
	private ThreadReceiveInfoUDP threadRIUDP;

	private SSLSocket clientConnectionDB;

	private Socket clientConnectionServer;

	private ThreadWaitingRoom threadWR;

	private boolean waitingForPlay;

	private boolean startedGame;

	private ThreadInfoGameClient threadIGC;

//	private ThreadReceiveMusic threadRM;

	private String nickname;

	private int id;

	private GUI_principal gui;

	private Socket clientSocket;

	private Socket gameSocket;

	private ArrayList<Integer> eatenBalls;
	
	private Socket chatSocket;

	private ThreadInfoChatServer ThreadICS;
	
	private ThreadSendMessages ThreadSM;

	private boolean chatService;
	
	private ArrayList<String> userMessages;
	
	public Client(GUI_principal theGui) throws IOException {
		
		
		
		viewer = false;
		
		System.setProperty("javax.net.ssl.trustStore", "myTrustStore.jts");
		System.setProperty("javax.net.ssl.trustStorePassword", "123456");

		
		gui = theGui;

		
		eatenBalls = new ArrayList<Integer>();

	}

	public void chatService() {
		
		try {
			
			
			chatSocket = new Socket(IpServer, PORT_CHAT);
		
			chatService = true;
			userMessages = new ArrayList<String>();
			ThreadICS = new ThreadInfoChatServer(this);
			ThreadICS.start();
			ThreadSM = new ThreadSendMessages(this);
			ThreadSM.start();
			System.out.println(" :: Chat Service ON ::");
			
			
//			threadRM = new ThreadReceiveMusic(this, nickname);
//			threadRM.start();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String message) {
		userMessages.add(message);
		ThreadSM.addMessage(message);
	}
	
	public void receiveMessage(String message) {
		
		gui.receiveMessage(message);
	}
	
	public void eraseMessages() {
		userMessages = new ArrayList<String>();
	}
	public ArrayList<Integer> getEatenBalls() {
		return eatenBalls;
	}

	public GUI_principal getGui() {
		return gui;
	}

	public void setEatenBalls(ArrayList<Integer> e) {
		eatenBalls = e;
	}

	public void registerPlayer(String email, String pass, String nick)
			throws IOException {

		String message = DataBaseServer.REGISTER_DB + "," + nick + "," + pass
				+ "," + email;
		connectWithDB(message);

	}

	public void closeWaitingRoom(){
		gui.closeWatingRoom();
	}
	public void loginPlayer(String email, String password) throws IOException {

		String message = DataBaseServer.LOGIN_DB + "," + email + "," + password;
		connectWithDB(message);

	}

	public void startGame() {

		startedGame = true;

		threadIGC = new ThreadInfoGameClient(this);

		threadIGC.start();
		
		
//		threadRM = new ThreadReceiveMusic(this,this.nickname);
//		
//		threadRM.start();

	}


	public void updateGame(String[] players, String[] food) {

		for (int i = 0; i < players.length; i++) {
			if (i != id && !viewer) {
				String[] player = players[i].split("/");
				int id = Integer.parseInt(player[0]);
				double x = Double.parseDouble(player[1]);
				double y = Double.parseDouble(player[2]);

				boolean isPlaying = false;

				
				if (player[3].equalsIgnoreCase("true")) {
					isPlaying = true;
					
				}

				int mass = Integer.parseInt(player[4]);
				gui.getAgario().updatePlayer(id, x, y, isPlaying, mass);	
			}else if(!viewer){
				
				String[] player = players[i].split("/");
				int id = Integer.parseInt(player[0]);
				

				boolean isPlaying = false;

				if (player[3].equalsIgnoreCase("true")) {
					isPlaying = true;
					
				}
				int mass = Integer.parseInt(player[4]);
				
				if(isPlaying==false){
					
					gui.getAgario().updateOwnPlayer(id,isPlaying, mass);
						
				}
				
				
				
			}else{
				
				String[] player = players[i].split("/");
				int id = Integer.parseInt(player[0]);
				double x = Double.parseDouble(player[1]);
				double y = Double.parseDouble(player[2]);

				boolean isPlaying = false;

				
				if (player[3].equalsIgnoreCase("true")) {
					isPlaying = true;
					
				}

				int mass = Integer.parseInt(player[4]);
				gui.getAgario().updatePlayer(id, x, y, isPlaying, mass);
				
				
			}

		}

		gui.getSpace().setPlayers(gui.getAgario().getPlayers());
		gui.getAgario().upDateFoodList(food);


	}



	private void connectWithDB(String message) {

		try {

			SSLSocketFactory factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
			
			clientConnectionDB = (SSLSocket)factory.createSocket(IpServer, DataBaseServer.DB_PORT);
			
			DataInputStream in = new DataInputStream(
					clientConnectionDB.getInputStream());
			DataOutputStream out = new DataOutputStream(
					clientConnectionDB.getOutputStream());

			out.writeUTF(message);

			String result = in.readUTF();

			gui.connectionResult(result);
			boolean r = false;
			if (result.equals(DataBaseServer.CONF_ACCESS)
					|| result.equals(DataBaseServer.PLAYER_SAVED)) {
				r = true;
				String information = in.readUTF();
				nickname = information;
				
//				musicRoot = "gameMusic"+information+".wav";

				connectWithServer();
			}
			gui.setConnectionResult(r);

			clientConnectionDB.close();

		} catch (Exception e) {
			System.out.println("Error connecting to data base");
		}

	}

	private void connectWithServer() {

		try {

			clientConnectionServer = new Socket(IpServer, Server.PORT);
			DataInputStream in = new DataInputStream(
					clientConnectionServer.getInputStream());
			DataOutputStream out = new DataOutputStream(
					clientConnectionServer.getOutputStream());

			out.writeUTF(Server.CONNECTED_CLIENT);
			String mess= in.readUTF();
			if (mess.equals(Server.CONNECTED_CLIENT)) {
				gui.goToWaitingRoom();
				waitingForPlay = true;
				threadWR = new ThreadWaitingRoom(this);
				threadWR.start();
			}else {
				gui.gameAlreadyStarted();
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void refreshWaitingRoom(String[] data) {
		gui.refreshWR(data);
	}

	public String getIpServer() {
		return IpServer;
	}

	public void setIpServer(String iP) {
		IpServer = iP;
	}

	public boolean isWaitingForPlay() {
		return waitingForPlay;
	}

	public void setWaitingForPlay(boolean waitingForPlay) {
		this.waitingForPlay = waitingForPlay;
	}

	public boolean isStartedGame() {
		return startedGame;
	}

	public void setStartedGame(boolean startedGame) {
		this.startedGame = startedGame;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Socket getClientSocket() {
		return clientSocket;
	}

	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}





	public Socket getClientConnectionServer() {
		return clientConnectionServer;
	}

	public void setClientConnectionServer(Socket clientConnectionServer) {
		this.clientConnectionServer = clientConnectionServer;
	}

	public ThreadWaitingRoom getThreadWR() {
		return threadWR;
	}

	public void setThreadWR(ThreadWaitingRoom threadWR) {
		this.threadWR = threadWR;
	}

	public ThreadInfoGameClient getThreadIGC() {
		return threadIGC;
	}

	public void setThreadIGC(ThreadInfoGameClient threadIGC) {
		this.threadIGC = threadIGC;
	}

	public Socket getGameSocket() {
		return gameSocket;
	}

	public void setGameSocket(Socket gameSocket) {
		this.gameSocket = gameSocket;
	}


	public void initializeWorld(String[] infoPlayers, String[] infoBalls) {

		ArrayList<PlayerBall> p1 = new ArrayList<PlayerBall>();

		for (int i = 0; i < infoPlayers.length; i++) {
			String[] playerInfo = infoPlayers[i].split("/");
			int id = Integer.parseInt(playerInfo[0]);
			String nickname = playerInfo[1];
			if (nickname.equals(this.nickname)) {
				this.id = id;
			}
			double posX = Double.parseDouble(playerInfo[2]);
			double posY = Double.parseDouble(playerInfo[3]);
			int rgb = Integer.parseInt(playerInfo[4]);

			PlayerBall pb = new PlayerBall(id, nickname, 10, 10);
			pb.setColor(new Color(rgb));
			pb.setPosX(posX);
			pb.setPosY(posY);

			
			
			p1.add(pb);
		}

		ArrayList<Ball> b = new ArrayList<Ball>();

		for (int i = 0; i < infoBalls.length; i++) {

			String[] ballInfo = infoBalls[i].split("/");

			int rgb = Integer.parseInt(ballInfo[0]);
			double posX = Double.parseDouble(ballInfo[1]);
			double posY = Double.parseDouble(ballInfo[2]);
			int id = Integer.parseInt(ballInfo[3]);
			Ball bl = new Ball(10, 10, true, id);
			bl.setColor(new Color(rgb));
			bl.setPosX(posX);
			bl.setPosY(posY);

			b.add(bl);
		}
		
			gui.initializeWorld(p1, b);
	}

	public void stopGame() {
		
		gui.finishGame();
		startedGame=false;
		
	}

	public boolean stablishConnection(String ip) {
		try {
			
			clientSocket = new Socket(ip, Server.PORT_WR);
			gameSocket = new Socket(ip, Server.PORT_INFO);
			IpServer=ip;
			return true;
		} catch (Exception e) {
			return false;
		}
		
		
	}

	public ThreadInfoChatServer getThreadICS() {
		return ThreadICS;
	}

	public void setThreadICS(ThreadInfoChatServer threadICS) {
		ThreadICS = threadICS;
	}

	public ThreadSendMessages getThreadSM() {
		return ThreadSM;
	}

	public void setThreadSM(ThreadSendMessages threadSM) {
		ThreadSM = threadSM;
	}

	public Socket getChatSocket() {
		return chatSocket;
	}

	public void setChatSocket(Socket chatSocket) {
		this.chatSocket = chatSocket;
	}

	public boolean isChatService() {
		return chatService;
	}

	public void setChatService(boolean chatService) {
		this.chatService = chatService;
	}

	public void startViewer(String n) throws Exception {
		threadRIUDP = new ThreadReceiveInfoUDP(this);
		nickname = n;
		
		musicRoot = "gameMusic"+n+".wav";
		
		threadRIUDP.startConnection(n);
		viewer = true;
	}

	public ThreadReceiveInfoUDP getThreadRIUDP() {
		return threadRIUDP;
	}

	public void setThreadRIUDP(ThreadReceiveInfoUDP threadRIUDP) {
		this.threadRIUDP = threadRIUDP;
	}

	public boolean isViewer() {
		return viewer;
	}

	public void setViewer(boolean viewer) {
		this.viewer = viewer;
	}

	public ArrayList<String> getUserMessages() {
		return userMessages;
	}

	public void setUserMessages(ArrayList<String> userMessages) {
		this.userMessages = userMessages;
	}

	public String getMusicRoot() {
		return musicRoot;
	}

	public void setMusicRoot(String musicRoot) {
		this.musicRoot = musicRoot;
	}

	

}