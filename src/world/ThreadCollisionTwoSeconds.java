package world;

import server.Server;

public class ThreadCollisionTwoSeconds extends Thread {
	private static long TIME_MAX=1000;
	private static long TIME = 1;
	private PlayerBall player1, player2;
	private double millis;
	private boolean eated;
	private AgarIO game;
	
	public ThreadCollisionTwoSeconds(PlayerBall player1, PlayerBall player2, AgarIO game) {
		this.game = game;
		this.player1 = player1;
		this.player2 = player2;
		this.millis = 0;
		eated = true;

	}
	
	@Override
	public void run() {
		
		while (millis < TIME_MAX && eated) {
	

			
			
			millis ++;
					
			eated = player1.checkCollision(player2);
			
//
//			System.out.println(millis + " "+eated);
			
			 
			try {
				Thread.sleep(TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
		if(millis == TIME_MAX && eated == true) {
			
		
			game.getPlayer(player1.getId()).setValidating(false);
			
			game.playerEated(player1, player2);
			game.setPlayersCounter(game.getPlayersCounter()-1);;
		}else{
			
			game.getPlayer(player1.getId()).setValidating(false);
			game.getPlayer(player2.getId()).setValidating(false);
		}
	}

}
