package world;

import java.awt.Color;
import java.util.Random;

public class Ball {
	
	public static final int INIT_MASS = 150;

	private Color color;
	
	private double posX;
	
	private double posY;
	
	private int mass;
	
	private boolean food;
	
	private int foodID;
	
	private int ttl;
	
    private static Random rand = new Random();

    public Ball( int posXmax, int posYmax) {
    	
		this.posX =  rand.nextInt(3*posXmax/4)+ posXmax/8;
		
		if(this.posX >= posXmax-50) {
		this.posX = this.posX-posXmax/8;
		}
		
		this.posY =  rand.nextInt(3*posYmax/4)+ posYmax/8;
		
		if(this.posY >= posYmax-50) {
			this.posY = this.posY-posYmax/8;
		}
		
		int r = rand.nextInt(256);
        int g = rand.nextInt(256);
        int b = rand.nextInt(256);
        this.color = new Color(r,g,b);
		this.mass = INIT_MASS;
		this.ttl = 0;
		}
    
	public Ball( int posXmax, int posYmax,boolean food, int foodID) {
		
		this.posX = rand.nextInt(posXmax-4)+4;
		this.posY = rand.nextInt(posYmax-4)+4;
		int r = rand.nextInt(256);
        int g = rand.nextInt(256);
        int b = rand.nextInt(256);
        this.color = new Color(r,g,b);
		this.mass = INIT_MASS/2;
		this.food = food;
		this.foodID = foodID;
		this.ttl = 0;
		
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public double getPosX() {
		return posX;
	}

	public void setPosX(double posX) {
		this.posX = posX;
	}

	public double getPosY() {
		return posY;
	}

	public void setPosY(double posY) {
		this.posY = posY;
	}

	public int getMass() {
		return mass;
	}

	public void setMass(int mass) {
		this.mass = mass;
	}

	public boolean isFood() {
		return food;
	}

	public int getFoodID() {
		return foodID;
	}

	public void setFoodID(int foodID) {
		this.foodID = foodID;
	}

	public int getTtl() {
		return ttl;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	public void setFood(boolean food) {
		this.food = food;
	}
	
	public void moveBall(double x, double y) {
		this.posX = x;
		this.posY = y;
		
	}
	public void increaseMass(int increment) {
		this.mass+=increment;
	}

	public int getRadio(){
        return (int)Math.sqrt(this.mass / Math.PI );
    }
	
	 private double distance(double xi, double yi, double xf, double yf){
	        return Math.sqrt((yf-yi)*(yf-yi) + (xf-xi)*(xf-xi));
   }
	 
	 public int checkCollision(Ball other){
	        double d = distance(this.posX,this.posY,other.posX, other.posY);
	        if (d < this.getRadio() + other.getRadio()){
	            if (this.mass > other.mass){
	                return 1;
	            } else if (this.mass < other.mass){
	                return -1;
	            } else
	                return 0;
	        } else{
	            return 0;
	        }
	    }
	 
}
