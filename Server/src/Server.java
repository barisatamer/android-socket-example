
import java.net.*;
import java.io.*;

public class Server extends Thread {
	private ServerSocket m_serverSocket;
	private DataOutputStream m_outputStream;
	
	public Server(int port) throws IOException{
		m_serverSocket = new ServerSocket(port);
		m_serverSocket.setSoTimeout(100000);

	}

	public void run(){

		while(true)
		{
			try
			{
				System.out.println("----Waiting for client on port " + m_serverSocket.getLocalPort() + "...");
				Socket server = m_serverSocket.accept();
				System.out.println("----Connected to " + server.getRemoteSocketAddress());
				DataInputStream in = new DataInputStream(server.getInputStream());
				System.out.println(in.readUTF());
				
				m_outputStream = new DataOutputStream(server.getOutputStream());
				m_outputStream.writeUTF("Connected to " + server.getLocalSocketAddress() );

				ServerGUI.getInstance().updateButton("Disconnect");
				
				String inputLine, outputLine = "";
				
				// Initiate conversation with client
				while ((inputLine = in.readUTF() ) != null) {
					System.out.println("Client : " + inputLine);
	                ServerGUI.getInstance().updateChatText(inputLine, "Client");
	                if (outputLine.equals("Bye."))
	                    break;
	            }
				
			}catch(SocketTimeoutException s)
			{
				System.out.println("Socket timed out!");
				break;
			}catch(IOException e)
			{
				e.printStackTrace();
				break;
			}
		}
	}
	
	public void sendMessage(String msg){
		try {
			m_outputStream.writeUTF(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
