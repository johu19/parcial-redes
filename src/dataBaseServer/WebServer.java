package dataBaseServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer extends Thread{
	
	private DataBaseServer dataBase;

	
	public WebServer(DataBaseServer db)
	{
		System.out.println("Webserver Started");
		
		setDataBase(db);
		
		
		
	}
	
	@Override
	public void run() {
		try {
			ServerSocket serverSocket =  new ServerSocket(80);
			while(true) 
			{
//				System.out.println("Waiting for the client request");
				Socket remote = serverSocket.accept();
//				System.out.println("Connection made");
				new Thread(new ClientHandler(remote,dataBase)).start();
				
				this.sleep(1000);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public DataBaseServer getDataBase() {
		return dataBase;
	}


	public void setDataBase(DataBaseServer dataBase) {
		this.dataBase = dataBase;
	}

	
	
	
}
