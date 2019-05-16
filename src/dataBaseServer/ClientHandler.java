package dataBaseServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

public class ClientHandler extends Thread{
	
	private final Socket socket;
	
	private DataBaseServer dataBase;

	public ClientHandler(Socket socket, DataBaseServer db)
	{
		this.dataBase = db;
		this.socket =  socket;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
//		System.out.println("\nClientHandler Started for " + this.socket);
		while(true) 
		{
			handleRequest(this.socket);
		}		
		//System.out.println("ClientHandler Terminated for "+  this.socket + "\n");
	}
	
	public void handleRequest(Socket socket)
	{
		try {
			
			
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String headerLine = in.readLine();
			if(headerLine!=null)
			{
				
			
//				System.out.println(headerLine);
				// A tokenizer is a process that splits text into a series of tokens
				StringTokenizer tokenizer =  new StringTokenizer(headerLine);
				//The nextToken method will return the next available token
				String httpMethod = tokenizer.nextToken();
				// The next code sequence handles the GET method. A message is displayed on the
				// server side to indicate that a GET method is being processed
				if(httpMethod.equals("GET"))
				{
//					System.out.println("Get method processed");
					String httpQueryString = tokenizer.nextToken();
					System.out.println(httpQueryString);
					if(httpQueryString.equals("/"))
					{
						StringBuilder responseBuffer =  new StringBuilder();
						String str="";
						BufferedReader buf = new BufferedReader(new FileReader(System.getProperty("user.dir") +"/src/web/templates/primeraPag.html"));
//						BufferedReader buf = new BufferedReader(new FileReader("C:/Users/jagn1/Desktop/7MO/REDES/taller_HTML_Galvis-Jose/index.html"));
						
						while ((str = buf.readLine()) != null) {
							responseBuffer.append(str);
					    }
						sendResponse(socket, 200, responseBuffer.toString());		
					    buf.close();
					}
					if(httpQueryString.contains("/?email="))
					{
						
//						responseBuffer
//						.append("<html>")
//						.append("<head> <title> hacked! </title> "
//								+ "<script> "
//								+ "window.setTimeout(kuky, 1000);"
//								+ "function kuky(){"
//								+ "while(true){"
//								+ "alert(\"coffee!!\");"
//								+ "};"
//								+ "};"								
//								+ "</script> "
//								+ "<style>"
//								+ "body{"								
//								+ "cursor: url(\"http://www.banderas-del-mundo.com/America_del_Sur/Colombia/colombia_mwd.gif\"), auto;"
//								+ "}"
//								+ "</style>"
//								+ "</head> ")
//						.append("<body bgcolor='black'>")
//						.append("<font color='white'>[0][1][0]</font><br>")
//						.append("<font color='white'>[0][0][1]</font><br>")
//						.append("<font color='white'>[1][1][1]</font><br>")
//						.append("<h1 style='color:white'> NO!!!" + response[1] +"</h1>")
//						.append("<img src='https://s2.glbimg.com/QJD0YP7szRqJuSEUdGHPF_2Dwqs=/850x446/s.glbimg.com/po/tt/f/original/2012/06/01/pirata-e1314380534977.jpg'>")
//						.append("<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/mK4t8U3eSAI?autoplay=1\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" ></iframe>")
//						.append("</body>")
//						.append("</html>");
						
						
						
						
						System.out.println("Get method processed");
						System.out.println("Message received "+httpQueryString);
						String[] response =  httpQueryString.split("&");
						String email = response[0].split("=")[1];
						String pass = response[1].split("=")[1];
						String nick = dataBase.loginPlayer(email, pass);
						StringBuilder responseBuffer =  new StringBuilder();
						
						if(nick!=null){
							String r = buildResponsePage(nick);
							if(r==null){
								responseBuffer.append("<html><h1>You don't have any games registred yet</h1></html>");
							}else{
								responseBuffer.append(r);
							}
								
						}else{
							
							responseBuffer.append("<html><h1>Invalid email or password</h1></html>");
						}
						
						
						
						sendResponse(socket, 200, responseBuffer.toString());		
					    
					}
										    
				}
				
				else
				{
					System.out.println("The HTTP method is not recognized");
					sendResponse(socket, 405, "Method Not Allowed");
				}
			
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public String buildResponsePage(String nickname){
		
		
		
		try {
			
			BufferedReader buf = new BufferedReader(new FileReader(System.getProperty("user.dir") +"/src/web/templates/resultado.html"));
			
			String message = "";
			
			
//			<td>1000</td>
//		   	<td>NO</td>
//		   	<td>Monday</td>
//		   	<td>Jose</td>
			
			for(int i=0; i<52;i++) {
				message += buf.readLine();
			}
			
			File text = new File(DataBaseServer.ROOT_P+nickname+".txt");

			FileReader reader = new FileReader(text);

			BufferedReader in = new BufferedReader(reader);

			int num = Integer.parseInt(in.readLine());
			
			
			message += "<h2>"+num +"</h2>";

			
			for(int i=0;i<18;i++){
				
				message += buf.readLine();
			}


			

		

			String str = "";
			
			while ((str = in.readLine()) != null) {
				
				System.out.println(str);
				
				if(!str.equals("")){
				
					
					
					String[] data = str.split(",");
					String date = htmlFormat(data[0]);
					String numF = htmlFormat(data[1]);
					String score = htmlFormat(data[2]);
					String win = htmlFormat(data[3]);
					String time = htmlFormat(data[4]);
					
					
					message += "<tr>";
					
					message+=date;
					message+=numF;
					message+=score;
					message+=win;
					message+=time;
					
					message += "</tr>";
				}
				
				
				

			}
			
			
			while((str = buf.readLine()) != null){
				message+=str;
				
			}
			
			in.close();
			buf.close();

			return message;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
		
	}
	
	private String htmlFormat(String m){
		
		return "<td>"+m+"</td>";
	}
	
	
	
	
	
	public void sendResponse(Socket socket, int statusCode, String responseString)
	{
		String statusLine;
		String serverHeader = "Server: WebServer\r\n";
		String contentTypeHeader = "Content-Type: text/html\r\n";
		
		try {
			DataOutputStream out =  new DataOutputStream(socket.getOutputStream());
			if (statusCode == 200) 
			{
				statusLine = "HTTP/1.0 200 OK" + "\r\n";
				String contentLengthHeader = "Content-Length: "
				+ responseString.length() + "\r\n";
				out.writeBytes(statusLine);
				out.writeBytes(serverHeader);
				out.writeBytes(contentTypeHeader);
				out.writeBytes(contentLengthHeader);
				out.writeBytes("\r\n");
				out.writeBytes(responseString);
				} 
			else if (statusCode == 405) 
			{
				statusLine = "HTTP/1.0 405 Method Not Allowed" + "\r\n";
				out.writeBytes(statusLine);
				out.writeBytes("\r\n");
			} 
			else 
			{
				statusLine = "HTTP/1.0 404 Not Found" + "\r\n";
				out.writeBytes(statusLine);
				out.writeBytes("\r\n");
			}
			//out.close();
		} catch (IOException e) {
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
