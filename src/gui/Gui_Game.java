package gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.rmi.*;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import world.AgarIO;
import world.Ball;
import world.PlayerBall;

public class Gui_Game extends JPanel implements MouseMotionListener, MouseListener {

	private ArrayList<PlayerBall> players;
	private ArrayList<Ball> foods;
	private GUI_principal principal;
	private int posX;
	private int posY;
	private boolean moved;


	public Gui_Game(ArrayList<PlayerBall> players, ArrayList<Ball> foods, GUI_principal principal) {
		
		

		this.players = players;
		this.foods = foods;
		this.principal = principal;
		addMouseMotionListener(this);
		addMouseListener(this);
		moved = false;

	}

	public void paintComponent(Graphics g) {

		super.paintComponents(g);

		g.setColor(new Color(220, 220, 220));
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, AgarIO.GAME_WIDTH, AgarIO.GAME_HEIGHT);

		g.setColor(new Color(220, 220, 220));

		int espacio = 40;
		for (int i = 0; i < this.getWidth(); i += espacio)
			g.drawLine(i, 0, i, this.getHeight());
		for (int j = 0; j < this.getHeight(); j += espacio)
			g.drawLine(0, j, this.getWidth(), j);

		if (this.players != null) {
			showPlayers(players, g);

		} else {
			System.out.println("Players null");
		}

		if (this.foods != null) {
			showFoods(foods, g);
		} else {
			System.out.println("Foods null");
		}

		ArrayList<PlayerBall> playersTop = principal.getPlayersTop();
		paintTop(playersTop, g);
		g.dispose();

	}

	private void showPlayers(ArrayList<PlayerBall> players, Graphics g) {

		for (int i = 0; i < players.size(); i++) {

			PlayerBall player = players.get(i);

			if (player.isPlaying() == true) {
				double x = player.getPosX();
				double y = player.getPosY();

				int r = player.getRadio();
				g.setColor(player.getColor());
				g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
				g.setColor(Color.BLACK);

				g.drawOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
				Font font = new Font("Century Schoolbook", Font.BOLD, r / 2);
				FontMetrics metrics = g.getFontMetrics(font);
				int xt = (int) x - metrics.stringWidth(player.getNickname()) / 2;
				int yt = (int) (y + r / 4);
				g.setFont(font);

				g.drawString(player.getNickname(), xt, yt);
			}
		}
	}

	private void showFoods(ArrayList<Ball> foods, Graphics g) {

		for (int i = 0; i < foods.size(); i++) {
			Ball food = foods.get(i);
			int r = food.getRadio();
			g.setColor(food.getColor());
			g.fillOval((int) (food.getPosX() - r), (int) (food.getPosY() - r), 2 * r, 2 * r);
			g.setColor(Color.BLACK);
			g.drawOval((int) (food.getPosX() - r), (int) (food.getPosY() - r), 2 * r, 2 * r);

		}
	}

	private void paintTop(ArrayList<PlayerBall> playersTop, Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.setFont(new Font("Century Schoolbook", Font.BOLD, 15));
		g.drawString("TOP PLAYERS", (int) this.getWidth() - 170, 30);
		g.drawString("---------------------------", (int) this.getWidth() - 180, 40);
		int i = 30;
		int pos = 1;
		for (PlayerBall p : playersTop) {
			i += 20;
			g.setFont(new Font("Century Schoolbook", Font.BOLD, 12));
			g.drawString(pos + ". " + p.getNickname(), (int) this.getWidth() - 180, i);
			g.drawString(p.getMass() + "", (int) this.getWidth() - 75, i);
			pos += 1;
		}
	}

	public void mouseDragged(MouseEvent e) {

	}

	public void mouseMoved(MouseEvent e) {

		if (!principal.getPlayer().isViewer()) {

			if (moved == false) {
				moved = true;
			}
			posX = (e.getX());
			posY = (e.getY());
		}
	}

	public ArrayList<PlayerBall> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<PlayerBall> players) {
		this.players = players;
	}

	public ArrayList<Ball> getFoods() {
		return foods;
	}

	public void setFoods(ArrayList<Ball> foods) {
		this.foods = foods;
	}

	public GUI_principal getPrincipal() {
		return principal;
	}

	public void setPrincipal(GUI_principal principal) {
		this.principal = principal;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public boolean isMoved() {
		return moved;
	}

	public void setMoved(boolean moved) {
		this.moved = moved;
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}


}
