package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import client.Client;
import world.AgarIO;
import world.Ball;
import world.PlayerBall;

public class GUI_principal extends JFrame {
	public static final int MAX_TOP_PLAYERS = 3;
	private Gui_SignUp jdSignUp;
	private Gui_LogIn jdLogIn;
	private FirstPanel jdFirstPanel;
	private Gui_IP jdIp;
	private Gui_WaitingRoom jdWaitingRoom;
	private AgarIO agario;
	private JFrame gameSpace;
	private JFrame streamingSpace;
	private ThreadMovingPlayers movingPlayers;
	private ThreadCollisionPlayers collisionPlayers;
	private ThreadRepaint repaint;
	private boolean connectionResult;
	private Gui_Game space;
	private Gui_finalTop jdFt;
	private Gui_Chat chat;
	private Client player;

	public GUI_principal() throws IOException {

		

		player = new Client(this);
		setTitle("Icesi Games SA - AgarIO");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(700, 600));
		setResizable(false);
		setLocationRelativeTo(null);

		jdSignUp = new Gui_SignUp(this);
		jdLogIn = new Gui_LogIn(this);
		jdFirstPanel = new FirstPanel(this);
		jdIp = new Gui_IP(this);
		jdIp.setVisible(true);
		

	}
	

	public void initializeGameSpace() {
		
		Dimension d = new Dimension(AgarIO.GAME_WIDTH, AgarIO.GAME_HEIGHT);

		space = new Gui_Game(agario.getPlayers(), agario.getFoods(), this);
		
		 
		gameSpace = new JFrame("Icesi Games SA - AgarIO");
		
		gameSpace.add(space);

		gameSpace.setSize(d);

		gameSpace.setLocationRelativeTo(null);
		gameSpace.setDefaultCloseOperation(EXIT_ON_CLOSE);

		gameSpace.setVisible(true);
	
	}
	
	public void initializeStreaming() {
		space = new Gui_Game(agario.getPlayers(), agario.getFoods(), this);
		Dimension d = new Dimension(AgarIO.GAME_WIDTH + 300, AgarIO.GAME_HEIGHT);
		streamingSpace = new JFrame("Icesi Games SA - AgarIO");
		streamingSpace.setSize(d);
		chat = new Gui_Chat(this);
		streamingSpace.setLayout(new BorderLayout());
		
		streamingSpace.add(space, BorderLayout.CENTER);
		streamingSpace.add(chat, BorderLayout.EAST);
		
		streamingSpace.setResizable(false);
		streamingSpace.setLocationRelativeTo(null);
		streamingSpace.setDefaultCloseOperation(EXIT_ON_CLOSE);
		streamingSpace.setVisible(true);
		
	}
	
	public void finishGame(){

		agario.setStatus(AgarIO.GAME_FINISHED);

		gameSpace.dispose();
		space.setVisible(false);
		
		ArrayList<PlayerBall> topPl = getPlayersTop();
		
		jdFt = new Gui_finalTop(topPl);
		
		if(topPl.get(0).getId()==player.getId()){
			JOptionPane.showMessageDialog(this, "CONGRATULATIONS, YOU'VE WON", "NUMBER 1", JOptionPane.INFORMATION_MESSAGE );
		}else if(topPl.get(1).getId()==player.getId()){
			JOptionPane.showMessageDialog(this, "YOU'VE FINISHED SECOND", "NUMBER 2", JOptionPane.INFORMATION_MESSAGE );
		}else if(topPl.size()>=2){
			
			if(topPl.get(2).getId()==player.getId()){
				JOptionPane.showMessageDialog(this, "YOU'VE FINISHED THIRD", "NUMBER 3", JOptionPane.INFORMATION_MESSAGE );
			}
			
		}
		
		
		
		jdFt.setLocationRelativeTo(null);
		jdFt.setVisible(true);
		
		
		
		
	}
	
	

	public void initializeWorld(ArrayList<PlayerBall> players, ArrayList<Ball> foods) {
		agario = new AgarIO(players, foods);
		agario.setStatus(AgarIO.PLAYING);
		
		if(player.isViewer() == false) {
			initializeGameSpace();
			collisionPlayers = new ThreadCollisionPlayers(this);
			collisionPlayers.start();
			movingPlayers = new ThreadMovingPlayers(player.getId(), this);
			movingPlayers.start();
		}else {
			initializeStreaming();

		}
	
		
		
		repaint = new ThreadRepaint(this);
		repaint.start();
	}

	public void initializePlayers(ArrayList<String> nicks) {
		if (nicks.size() < AgarIO.MAX_PLAYERS) {
			agario.initializePlayers(nicks);
		} else {
			JOptionPane.showMessageDialog(null, "More players than the allowed.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		
	}
	
	public void repaintGame() {
		space.setPlayers(agario.getPlayers());
		space.setFoods( agario.getFoods());
		space.removeAll();
		space.repaint();
		space.revalidate();
	
	}
	public int getPosX() {
		return space.getPosX();
	}
	
	public int getPosY() {
		return space.getPosY();
	}

	public void jdIp() {
		jdIp.setVisible(true);
	}

	public void firstPanel() {
		jdFirstPanel.setVisible(true);
	}

	public void jdLogIn() {
		jdLogIn.setVisible(true);
	}

	public void jdSignUp() {
		jdSignUp.setVisible(true);
	}

	public void oldPlayer(String email, String password) throws IOException {

		player.loginPlayer(email, password);
	}

	public void newPlayer(String email, String nickname, String password) throws IOException {
		player.registerPlayer(email, password, nickname);
	}

	public void goToWaitingRoom() {

		jdWaitingRoom = new Gui_WaitingRoom(this);
		jdWaitingRoom.setVisible(true);

	}

	public void refreshWR(String[] data) {
		jdWaitingRoom.refresh(data);
		pack();
	}
	public void closeWatingRoom(){
		jdWaitingRoom.setVisible(false);
	}
	

	public Client getPlayer() {
		return player;
	}

	public void setPlayer(Client player) {
		this.player = player;
	}

	public void connectionResult(String result) {

		JOptionPane.showMessageDialog(this, result);

	}

	public boolean isConnectionResult() {
		return connectionResult;
	}

	public void setConnectionResult(boolean connectionResult) {
		this.connectionResult = connectionResult;
	}

	public void movePlayer(int id, double posX, double posY) {
		agario.movePlayer(id, posX, posY);
	}

	public void checkCollisionPlayerFood(int idPlayer) {

		int n = agario.checkCollisionPlayerFood(idPlayer);
		if (n != -1) {
			player.getEatenBalls().add(n);
			player.setEatenBalls(player.getEatenBalls());
		}
	}

	public void checkCollisionPlayers() {
		agario.checkCollisionPlayers();
		
	}

	public ArrayList<PlayerBall> getPlayersTop() {
		ArrayList<PlayerBall> playersTop = agario.getTop(MAX_TOP_PLAYERS);
		return playersTop;
	}

	public Gui_SignUp getJdSignUp() {
		return jdSignUp;
	}

	public void setJdSignUp(Gui_SignUp jdSignUp) {
		this.jdSignUp = jdSignUp;
	}

	public Gui_LogIn getJdLogIn() {
		return jdLogIn;
	}

	public void setJdLogIn(Gui_LogIn jdLogIn) {
		this.jdLogIn = jdLogIn;
	}

	public FirstPanel getJdFirstPanel() {
		return jdFirstPanel;
	}

	public void setJdFirstPanel(FirstPanel jdFirstPanel) {
		this.jdFirstPanel = jdFirstPanel;
	}

	public Gui_IP getJdIp() {
		return jdIp;
	}

	public void setJdIp(Gui_IP jdIp) {
		this.jdIp = jdIp;
	}

	public Gui_WaitingRoom getJdWaitingRoom() {
		return jdWaitingRoom;
	}

	public void setJdWaitingRoom(Gui_WaitingRoom jdWaitingRoom) {
		this.jdWaitingRoom = jdWaitingRoom;
	}

	public AgarIO getAgario() {
		return agario;
	}

	public void setAgario(AgarIO agario) {
		this.agario = agario;
	}

	public JFrame getGameSpace() {
		return gameSpace;
	}

	public void setGameSpace(JFrame gameSpace) {
		this.gameSpace = gameSpace;
	}

	public ThreadMovingPlayers getMovingPlayers() {
		return movingPlayers;
	}


	public void setMovingPlayers(ThreadMovingPlayers movingPlayers) {
		this.movingPlayers = movingPlayers;
	}


	public ThreadCollisionPlayers getCollisionPlayers() {
		return collisionPlayers;
	}


	public void setCollisionPlayers(ThreadCollisionPlayers collisionPlayers) {
		this.collisionPlayers = collisionPlayers;
	}


	public ThreadRepaint getRepaint() {
		return repaint;
	}


	public void setRepaint(ThreadRepaint repaint) {
		this.repaint = repaint;
	}


	public Gui_Game getSpace() {
		return space;
	}



	public void setSpace(Gui_Game space) {
		this.space = space;
	}

	public boolean getMoved(){
		return space.isMoved();
	}
	public static void main(String[] args) {

		try {

			GUI_principal principal = new GUI_principal();

		} catch (IOException e) {

//			e.printStackTrace();
		}

	}


	public void showDeath() {
		JOptionPane.showMessageDialog(this, "YOU'VE BEEN ELIMINATED :C", "YOU LOSE", JOptionPane.ERROR_MESSAGE );
		
	}


	public void shutDown() {
		gameSpace.dispose();
		space.setVisible(false);
		this.dispose();
	}

	public boolean connectServer(String ip) {

		if(!player.stablishConnection(ip)){
			JOptionPane.showMessageDialog(this, "WRONG IP", "ERROR IP", JOptionPane.ERROR_MESSAGE);
			return false;
		}else{
			jdIp.dispose();
			firstPanel();
			return true;
		}
		
	}
	
	public boolean connectAsViewer(String ip,String nick) {
		try {
			
			player.setIpServer(ip);
			player.startViewer(nick);
		    
			return true;
		} catch (Exception e) {
//			e.printStackTrace();
			JOptionPane.showMessageDialog(this,"WRONG IP" );
			return false;
		}
		
		
	}


	public void receiveMessage(String message) {
		
		chat.receiveMessage(message);
		
	}


	public void sendMessage(String message) {
		player.sendMessage(message);
	}


	public void gameAlreadyStarted() {
		
		int option = JOptionPane.showConfirmDialog(null, "The game already started. Do you want to spectate it?", "Icesi Games SA - AgarIO", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		
		if(option == 0) {
			connectAsViewer(player.getIpServer(), player.getNickname());
		}
	}

}
