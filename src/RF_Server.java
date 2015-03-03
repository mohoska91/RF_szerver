import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class RF_Server {
	
	ArrayList<ClientThread> clients;
	ServerSocket serverSocket;

	
	public RF_Server() throws IOException {
		// TODO Auto-generated constructor stub
		clients= new ArrayList<ClientThread>();
		serverSocket=new ServerSocket(8888);
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			RF_Server rf_Server= new RF_Server();
			rf_Server.listen();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public ArrayList<ClientThread> getClients() {
		return clients;
	}

	public void sendToAll(String message,String sender){
		
		for(int i=0; i < clients.size(); i++){
			ClientThread clientThread=clients.get(i);
			if(!clientThread.getMyName().equals(sender)){
				clientThread.out.println(sender+" sent:"+message+"$");
			}
			
		}
		
	}
	
	private void listen() throws Exception{
		while(true){
			Socket client=serverSocket.accept();
			new ClientThread(this, client).start();
		}
	}
}
