import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;


public class ClientThread extends Thread{

	private RF_Server server;
	private Socket clieSocket;
	private String myName;
	private boolean run;
	
	public PrintWriter out;
	public DataInputStream in;
	
	public ClientThread(RF_Server server, Socket client_socket) throws Exception{
		
		this.server=server;
		this.clieSocket=client_socket;
		this.run=true;
		this.out= new PrintWriter(client_socket.getOutputStream(),true);
		this.in = new DataInputStream(new BufferedInputStream(client_socket.getInputStream()));
		this.myName=giveName();
		server.getClients().add(this);
		System.out.println(myName+" connected!");
	
	}

	@Override
	public void run() {
		while(run){
			listen();
		}
	}
	
	private void listen(){
		
		while(run){
			messageHandling();
		}
		
	}
	
	private String giveName(){
		return "CLIENT_"+clieSocket.getPort();
	}
	
	public String getMyName(){
		return myName;
	}
	
	private void killMe() {
		
		try{
			run=false;
			server.getClients().remove(this);
			clieSocket.close();
		}catch(IOException ex){
			System.out.println("Could not close the clientSocket!");
		}
	}
	
	private void messageHandling(){
		int messageLength=0;
		byte[] byteArray= new byte[1024];
		try{
			messageLength=in.read(byteArray);
			if(messageLength==-1){
                messageLength=0;
            }
		}catch(IOException ex){
			messageLength=0;
		}
		if(messageLength!=0){
			
			String temp=new String(byteArray);
            temp=temp.trim();
			if(!(temp.isEmpty()) && !temp.equals("$")){
				String message=temp.replace("$", "");
				System.out.println(myName+": "+message);
				server.sendToAll(message, myName);
			}
		}else{
			killMe();
			sendAllImDead();
		}
	}
	
	private void sendAllImDead(){

		String gone=myName+" "+"disconnected!";
		System.out.println(gone);
		server.sendToAll("I'm disconnected!",myName);
	}

}
