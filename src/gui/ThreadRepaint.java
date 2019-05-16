package gui;

import world.AgarIO;

public class ThreadRepaint extends Thread{

	public GUI_principal principal;
	
	public ThreadRepaint(GUI_principal principal)  {

		this.principal = principal;
		
	}
	
	
	@Override
	public void run() {
	
		while (principal.getAgario().getStatus().equals(AgarIO.PLAYING)) {
				principal.repaintGame();
			
		}
		
	}

}
