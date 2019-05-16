package server;

public class ThreadFeeding extends Thread{

    private static final int TIME_FEED = 250;  

    private Server server;
	public ThreadFeeding(Server server) {
		
		this.server = server;
	}

	public void run() {
		while(server.isRunningGame()) {
			try {
				server.createFood();
				
				
				Thread.sleep(TIME_FEED);
			}catch(Exception e) {
//				e.printStackTrace();
			}
		}
	}
}
