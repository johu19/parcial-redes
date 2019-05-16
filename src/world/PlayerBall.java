package world;

import java.awt.Color;

public class PlayerBall extends Ball {

	private static final double INIT_VEL = 6;
	
	private String nickname;
	
	private double velocity;
	
	private boolean isPlaying;
	
	private double vectorX;
	
	private double vectorY;
	
	private boolean validating;
	
	private int id;
	
	public PlayerBall( int id,String nickname, int posXmax, int posYmax) {
		super(posXmax, posYmax);
		this.setFood(false);
		this.velocity = 0;
		this.vectorX = 0;
		this.vectorY = 0;
		this.isPlaying = true;
		this.id = id;
		this.nickname=nickname;
		validating = false;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}
	
	public double getVectorX() {
		return vectorX;
	}

	public void setVectorX(double vectorX) {
		this.vectorX = vectorX;
	}

	public double getVectorY() {
		return vectorY;
	}

	public void setVectorY(double vectorY) {
		this.vectorY = vectorY;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void calculateVelocity() {
		
		this.velocity = INIT_VEL/Math.log10(this.getMass());
	}
	
	public void calculateVector(double posXfinal, double posYfinal) {
		 	double y = posYfinal - this.getPosY();
	        double x = posXfinal - this.getPosX();
	        double r = Math.sqrt(y*y + x*x);
	        if(this.getRadio()/3 > r){
	            y = 0;
	            x = 0;
	        }
	        if((this.getPosX() >= AgarIO.GAME_WIDTH && x > 0)|| (this.getPosX() <= 0 && x < 0))
	            x = 0;
	        if((this.getPosY() >= AgarIO.GAME_HEIGHT && y > 0) || (this.getPosY() <= 0 && y < 0))
	            y = 0;
	        this.vectorY = (velocity * y/r)+this.getPosY();
	        this.vectorX = (velocity * x/r)+this.getPosX();
	        
	        
	}


	@Override
	public void moveBall(double x, double y) {
		// TODO Auto-generated method stub
		this.calculateVelocity();
		this.calculateVector(x, y);
//		super.moveBall(x+this.vectorX, y+this.vectorY);
		super.moveBall(this.getVectorX(), this.getVectorY());
		
//		System.out.println("ORIGINAL:  "+x+"  "+y);
//		System.out.println("VECTOR:  "+vectorX+"  "+vectorY);
	}

	 private double distance(double xi, double yi, double xf, double yf){
	        return Math.sqrt((yf-yi)*(yf-yi) + (xf-xi)*(xf-xi));
	 }
	public boolean checkCollision(PlayerBall player) {
		
		boolean collision =false;
        double dist = distance(this.getPosX(),this.getPosY(),player.getPosX(), player.getPosY());
		int radio= this.getRadio();
		int radioPlayer = player.getRadio();
		if(dist < radio) {
			double xMax = this.getPosX() + radio;
			double xMin = this.getPosX() - radio;
			
			double yMax = this.getPosY() + radio;
			double yMin = this.getPosY() - radio;
			
			
			double xPlayerMax = player.getPosX() + radioPlayer;
			double xPlayerMin = player.getPosX() - radioPlayer;
			
			double yPlayerMax = player.getPosY() + radioPlayer;
			double yPlayerMin = player.getPosY() - radioPlayer;
			
			if(xMax > xPlayerMax && xMin < xPlayerMin && yMax > yPlayerMax && yMin < yPlayerMin) {
				
				collision = true;
			}
		}
//	
//		if(collision == 1) {
//			this.increaseMass(player.getMass());
//			player.setPlaying(false);
//			return true;
//		} else if(collision == -1) {
//			player.increaseMass(this.getMass());
//			this.setPlaying(false);
//			return true;
//		}

		return collision;
	}
	
	
	public boolean checkCollisionFood(Ball ball) {
		  int collision = super.checkCollision(ball);
		  
		  if (collision != 0 && ball.isFood()){
              this.increaseMass(ball.getMass());
              return true;
          }
		  
		  return false;
	}

	public boolean isValidating() {
		return validating;
	}

	public void setValidating(boolean validating) {
		this.validating = validating;
	}
	
	
}
