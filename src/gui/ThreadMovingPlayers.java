package gui;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import world.AgarIO;

public class ThreadMovingPlayers extends Thread{

		public static final int TIME = 20;
		
		private int id;
		private GUI_principal principal;
	    private double posXfinal,posYfinal;
	    
	    public ThreadMovingPlayers(int id,GUI_principal principal) {
	    	this.id = id;
	    	this.principal = principal;
	    	this.posXfinal = 0;
	    	this.posYfinal = 0;
	    }

	    private boolean first;
	    
	    public void run() {
	    	first=false;
	    	while(principal.getAgario().getStatus().equals(AgarIO.PLAYING)) {
	    		
	    		try {
	    			
	    			if(principal.getMoved()==true){
	    				first=true;
	    			
	    			this.updatePositionMouse();
	    			
		    		principal.movePlayer(id, posXfinal, posYfinal);
		    		principal.checkCollisionPlayerFood(id);
	    			}
					Thread.sleep(TIME);
				} catch (InterruptedException e) {
//					e.printStackTrace();
				}
	    		
	    	}
	    }
		
	  
	
	 private void updatePositionMouse(){
		 
		 this.posXfinal = principal.getPosX();
		 this.posYfinal = principal.getPosY();
		 
		 
		 
	 }



	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
