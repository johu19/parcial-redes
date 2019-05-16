package server;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import world.AgarIO;
import world.Ball;
import world.PlayerBall;
import client.Client;
import dataBaseServer.DataBaseServer;
import dataBaseServer.WebServer;

public class Server {

	public static final int PORT = 36556;
	public static final int PORT_INFO = 38000;
	public static final int PORT_WR = 33000;
	public final static int PORT_MUSIC = 50000;
	public final static int PORT_UDP = 45000;
	public final static int PORT_CHAT = 46567;
	public final static String CONNECTED_CLIENT = "connected_client";
	public final static String GAME_STARTED = "game already started";
	public final static String START_GAME = "Start game";
	public final static String MUSIC_SERVER = "Music/StarParty.wav";

	

	private ThreadWaitingViewers threadWV;
	
	private int gameTime;
	
	private ServerSocket SsocketInfo;

	private DataBaseServer dbServer;

	private boolean waitingClients;
	
	private boolean alive;

	private boolean runningGame;

	private ThreadWaitingClients threadWC;

	private ThreadTimer timer;

	private ArrayList<ThreadInfoGameServer> ArrayThreadIGS;

	private ArrayList<String> players;

	private AdminWindow adminWindow;

	private ArrayList<ThreadSendInfoWR> threadSIWR;

	private ServerSocket serverSocket;

	private ThreadFeeding feeding;

	private ServerSocket serverSocketGame;

	private int numberOfClients;

	private AgarIO game;

	private ThreadGameTime threadGT;
	
	private int discClients;
	
//	private ThreadSendMusic threadSM;
	
	private ServerSocket serverSocketChat;

	private boolean runningChatService;
	
	private ThreadChatMulticast threadCM;
	
	private ThreadUsersMessages threadUM;
	
	private ThreadUsersMessagesHandler ThreadUMH;

	private ArrayList<String> messages;
	
	private ArrayList<String> users;

	private ArrayList<Socket> chatSockets;
	
	private boolean sendMulticast;

	
	public Server() throws IOException {
		
		
		
		alive = true;
		
		threadWV = new ThreadWaitingViewers(this);
		threadWV.start();

		setDiscClients(0);
		threadGT = new ThreadGameTime(this);

		adminWindow = new AdminWindow(this);
		adminWindow.setVisible(true);
		SsocketInfo = new ServerSocket(PORT_WR);
		players = new ArrayList<String>();
		serverSocket = new ServerSocket(PORT);

		System.out.println("Server online");

		game = new AgarIO();

		feeding = new ThreadFeeding(this);

		threadWC = new ThreadWaitingClients(this);
		waitingClients = true;
		timer = new ThreadTimer(this);
		threadSIWR = new ArrayList<ThreadSendInfoWR>();
		dbServer = new DataBaseServer(this);

//		threadSM = new ThreadSendMusic(this);
//		//THREAD MUSIC ESTA AQUI-------------------------------------------
//		threadSM.start();

		serverSocketGame = new ServerSocket(PORT_INFO);
		chatSockets = new ArrayList<Socket>();
		startChatService();
	}
	
	public void startChatService() {
		try {
			users = new ArrayList<String>();
			messages = new ArrayList<String>();
			serverSocketChat = new ServerSocket(PORT_CHAT);
			runningChatService = true;
			sendMulticast = true;

			threadCM = new ThreadChatMulticast(this);
			threadCM.start();
			
			//threadUM = new ThreadUsersMessages(this);
			//threadUM.start();
			
			System.out.println(":: Chat service ON ::");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void updateGame(String[] player, String[] food) {

		int id = Integer.parseInt(player[0]);
		double x = Double.parseDouble(player[1]);
		double y = Double.parseDouble(player[2]);

		boolean isPlaying = false;

		if (player[3].equalsIgnoreCase("true")) {
			isPlaying = true;

		}

		int mass = Integer.parseInt(player[4]);

		game.updatePlayer(id, x, y, isPlaying, mass);
		game.updateFood(food);

	}


	public void activateBtnWindow() {
		if (!adminWindow.btnEnabled()) {
			adminWindow.activateButton();
		}

	}

	public int numbPlayersPlaying() {
		int nPlayers = 0;

		for (int i = 0; i < game.getPlayers().size(); i++) {
			if (game.getPlayers().get(i).isPlaying() == true) {
				nPlayers++;
			}
		}
		return nPlayers;
	}

	public void endGame() {
		game.setStatus(AgarIO.GAME_FINISHED);
	}

	public void refreshPlayersWindow() {
		adminWindow.refreshNumPlayers(numberOfClients + "");
	}

	public void startMulticast() throws IOException {

		game.setStatus(AgarIO.PLAYING);

		game.initializePlayers(players);

		runningGame = true;

		feeding.start();

		threadGT.start();


		System.out.println("starts multicast");

		ArrayThreadIGS = new ArrayList<ThreadInfoGameServer>();
		for (int i = 0; i < players.size(); i++) {
			ThreadInfoGameServer th = new ThreadInfoGameServer(this);

			th.start();
			ArrayThreadIGS.add(th);
		}
		
		

	}

	public String sendInfoFirstTime() {

		String message = "#f#";

		ArrayList<PlayerBall> p1 = game.getPlayers();
		for (int i = 0; i < game.getPlayers().size(); i++) {
			String id = game.getPlayers().get(i).getId() + "";
			String nickname = game.getPlayers().get(i).getNickname();
			String posX = game.getPlayers().get(i).getPosX() + "";
			String posY = game.getPlayers().get(i).getPosY() + "";
			String rgb = game.getPlayers().get(i).getColor().getRGB() + "";
			String player = "";

			if (i < p1.size() - 1) {
				player = id + "/" + nickname + "/" + posX + "/" + posY + "/"
						+ rgb + ",";
			} else {
				player = id + "/" + nickname + "/" + posX + "/" + posY + "/"
						+ rgb;
			}

			message += player;
		}

		message += "_";

		ArrayList<Ball> food = game.getFoods();

		for (int i = 0; i < food.size(); i++) {
			String rgb = food.get(i).getColor().getRGB() + "";
			String posX = food.get(i).getPosX() + "";
			String posY = food.get(i).getPosY() + "";
			String id = food.get(i).getFoodID() + "";
			String ball = "";

			ball += rgb + "/" + posX + "/" + posY + "/" + id;

			if (i < food.size() - 1) {
				ball += ",";
			}

			message += ball;
		}

		return message;

	}

	public String infoGame() {
		String message = "";
		ArrayList<PlayerBall> p1 = game.getPlayers();
		for (int i = 0; i < p1.size(); i++) {
			String id = p1.get(i).getId() + "";
			String posX = p1.get(i).getPosX() + "";
			String posY = p1.get(i).getPosY() + "";
			String isPlaying = p1.get(i).isPlaying() + "";
			String mass = p1.get(i).getMass() + "";
			String player = "";

			if (i < p1.size() - 1) {
				player = id + "/" + posX + "/" + posY + "/" + isPlaying + "/"
						+ mass + ",";
			} else {
				player = id + "/" + posX + "/" + posY + "/" + isPlaying + "/"
						+ mass;
			}



			message += player;
		}

		message += "_";

		ArrayList<Ball> food = game.getFoods();


		for (int i = 0; i < food.size(); i++) {

			if (food.get(i) != null && i<food.size()) {

				
				try {
					
					String rgb = food.get(i).getColor().getRGB() + "";
					String posX = food.get(i).getPosX() + "";
					String posY = food.get(i).getPosY() + "";
					String id = food.get(i).getFoodID() + "";
					String ball = "";

					ball += rgb + "/" + posX + "/" + posY + "/" + id;

					if (i < food.size() - 1) {
						ball += ",";
					}

					message += ball;
					
				} catch (Exception e) {

					
					
				}
				
				

			}
		}

		return message;

	}

	public void startGame() {

		adminWindow.setVisible(false);
		waitingClients = false;
		Thread th = new Thread();
		th.start();
		try {
			th.sleep(100);
			startMulticast();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void createFood() {
		if (game.getFoods().size() < AgarIO.MAX_FOOD) {
			game.createFood();
		}
	}

	public static void main(String[] args) {

		try {
			Server serv = new Server();
		} catch (IOException e) {
			
			e.printStackTrace();
		}



	}

	public boolean isRunningGame() {
		return runningGame;
	}

	public void setRunningGame(boolean b) {
		runningGame = b;
	}

	public ThreadWaitingClients getThreadWC() {
		return threadWC;
	}

	public void setThreadWC(ThreadWaitingClients threadWC) {
		this.threadWC = threadWC;
	}



	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public boolean isWaitingClients() {
		return waitingClients;
	}

	public void setWaitingClients(boolean waitingClients) {
		this.waitingClients = waitingClients;
	}

	public ThreadTimer getTimer() {
		return timer;
	}

	public void setTimer(ThreadTimer timer) {
		this.timer = timer;
	}

	public int getNumberOfClients() {
		return numberOfClients;
	}

	public void setNumberOfClients(int numberOfClients) {
		this.numberOfClients = numberOfClients;
	}

	public DataBaseServer getDbServer() {
		return dbServer;
	}

	public void setDbServer(DataBaseServer dbServer) {
		this.dbServer = dbServer;
	}

	public ArrayList<String> getPlayers() {
		return players;
	}

	public void addPlayer(String p) {
		players.add(p);
	}

	public void setPlayers(ArrayList<String> players) {
		this.players = players;
	}

	public ArrayList<ThreadSendInfoWR> getThreadSIWR() {
		return threadSIWR;
	}

	public void setThreadSIWR(ArrayList<ThreadSendInfoWR> threadSIWR) {
		this.threadSIWR = threadSIWR;
	}

	public ServerSocket getSsocketInfo() {
		return SsocketInfo;
	}

	public void setSsocketInfo(ServerSocket ssocketInfo) {
		SsocketInfo = ssocketInfo;
	}

	public AdminWindow getAdminWindow() {
		return adminWindow;
	}

	public void setAdminWindow(AdminWindow adminWindow) {
		this.adminWindow = adminWindow;
	}

	public ServerSocket getServerSocketGame() {
		return serverSocketGame;
	}

	public void setServerSocketGame(ServerSocket serverSocketGame) {
		this.serverSocketGame = serverSocketGame;
	}

	public AgarIO getGame() {
		return game;
	}

	public void setGame(AgarIO game) {
		this.game = game;
	}

	public void checkCollisionPlayers() {
		game.checkCollisionPlayers();

	}

	public void stopGame() {

		

		game.setStatus(AgarIO.GAME_FINISHED);

	}

	public ThreadGameTime getThreadGT() {
		return threadGT;
	}

	public void setThreadGT(ThreadGameTime threadGT) {
		this.threadGT = threadGT;
	}

	public int getDiscClients() {
		return discClients;
	}

	public void setDiscClients(int discClients) {
		this.discClients = discClients;
	}

	public ServerSocket getServerSocketChat() {
		return serverSocketChat;
	}

	public void setServerSocketChat(ServerSocket serverSocketChat) {
		this.serverSocketChat = serverSocketChat;
	}

	public boolean isRunningChatService() {
		return runningChatService;
	}

	public void setRunningChatService(boolean runningChatService) {
		this.runningChatService = runningChatService;
	}

	public ArrayList<String> getMessages() {
		return messages;
	}

	public void setMessages(ArrayList<String> messages) {
		this.messages = messages;
	}
	
	public void eraseMessages() {
		
		messages = new ArrayList<String>();
		System.out.print("");
	}


	public void newMessage(String userMessage) {
		
		messages.add(userMessage);
		
	}

	public ArrayList<Socket> getChatSockets() {
		return chatSockets;
	}

	public void setChatSockets(ArrayList<Socket> chatSockets) {
		this.chatSockets = chatSockets;
	}

//	public boolean verifyUserRegistered(Socket socketReceived, String message) throws IOException {
//		String[] components = message.split(";");
//		String userNick = components[0];
//		boolean flag = false;
//		if(users.contains(userNick)) {
//			
//			flag = true;
//		}
//		else {
//			
//			chatSockets.add(socketReceived);
//			users.add(components[0]);
//			ThreadUsersMessagesHandler usm = new ThreadUsersMessagesHandler(socketReceived, this);
//			usm.start();
//		}
//		return flag;
//	}
	
	
	public boolean verifyUserRegistered(String message) throws IOException {
		String[] components = message.split(";");
		String userNick = components[0];
		boolean flag = false;
		if(users.contains(userNick)) {
			
			flag = true;
		}
		else {
			
			users.add(components[0]);
			
		}
		return flag;
	}

	public ArrayList<String> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<String> users) {
		this.users = users;
	}

	public boolean isSendMulticast() {
		return sendMulticast;
	}

	public void setSendMulticast(boolean sendMulticast) {
		this.sendMulticast = sendMulticast;
	}

	public void addChatSocket(Socket usm) {

		chatSockets.add(usm);
		
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	private PlayerBall getWinner() {
		PlayerBall win = null;
		int mass = 0;
		for(int i=0;i<game.getPlayers().size();i++){
			
			if(game.getPlayers().get(i).getMass()>mass){
				win = game.getPlayers().get(i);
				mass=game.getPlayers().get(i).getMass();
			}
		}
		
		return win;
	}
	
	private ArrayList<PlayerBall> getOtherPlayers(PlayerBall p){
		
		ArrayList<PlayerBall> pla =new ArrayList<PlayerBall>();
		ArrayList<PlayerBall> allP =game.getPlayers();
		for (int i = 0; i < allP.size(); i++) {
			
			if(!allP.get(i).equals(p)){
				pla.add(allP.get(i));
			}
			
		}
		
		
		return pla ;
	}
	
	public void saveScores(){
		try {
		
			PlayerBall winner = getWinner();
			ArrayList<PlayerBall> allP = game.getPlayers();
			
			for(int i=0;i<allP.size();i++) {
				PlayerBall p = allP.get(i);
				if(p.getNickname().equals(winner.getNickname())) {
					dbServer.saveIndividualScore(p, true, gameTime);
				}else {
					dbServer.saveIndividualScore(p, false, gameTime);
				}
			}
			
			
//			dbServer.saveIndividualScore(winner,otherP , true);
//			for(int i=0;i<otherP.size();i++){
//				PlayerBall p = otherP.get(i);
//				dbServer.saveIndividualScore(p, getOtherPlayers(p), false);
//				
//			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public int getGameTime() {
		return gameTime;
	}

	public void setGameTime(int gameTime) {
		this.gameTime = gameTime;
	}
	
	
	
	




}
