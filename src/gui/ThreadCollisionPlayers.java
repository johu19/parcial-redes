package gui;

import world.AgarIO;




public class ThreadCollisionPlayers extends Thread{

	private static final int TIME = 10;
	private GUI_principal principal;

	public ThreadCollisionPlayers(GUI_principal principal) {

		this.principal = principal;
	}

	public void run() {
		while(principal.getAgario().getPlayer(principal.getPlayer().getId()).isPlaying() && principal.getAgario().getStatus().equals(AgarIO.PLAYING)) {			
			try {
				principal.checkCollisionPlayers();
				Thread.sleep(TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(principal.getAgario().getPlayer(principal.getPlayer().getId()).isPlaying() == false){
			principal.showDeath();
		}
		
	}
}
