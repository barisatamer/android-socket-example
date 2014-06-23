package com.barisatamer.socketdemoclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	// UI
	private EditText m_tfIp;
	private EditText m_tfPort;
	private EditText m_tfUserInput;
	private TextView m_tvStatus;
	private TextView m_tvChatText;
	
	private Socket client;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		m_tfIp 			= (EditText)findViewById(R.id.editText_ip);
		m_tfPort 		= (EditText)findViewById(R.id.editText_port);
		m_tfUserInput 	= (EditText)findViewById(R.id.editText_userInput);
		m_tvStatus  	= (TextView)findViewById(R.id.textView_status);
		m_tvChatText	= (TextView)findViewById(R.id.textView_chatLog);
		
	}

	public void connectEvent(View v){
		hideSoftKeyboard();
		new Thread( new Client() ).start();
	}
	
	public void sendEvent(View v){
		
		String text = m_tfUserInput.getText().toString();
		m_tfUserInput.setText("");
		addChatText(text, "Client");
		hideSoftKeyboard();
		OutputStream outToServer;
		try {
			outToServer = client.getOutputStream();
			DataOutputStream out = new DataOutputStream(outToServer);
			out.writeUTF( text );
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	
	class Client implements Runnable{
		
		@Override
		public void run(){
			
			setStatus("Connecting...");

			// Get IP and port
			String serverName = m_tfIp.getText().toString();
			int port = Integer.parseInt( m_tfPort.getText().toString() );
			
			try {
				client = new Socket(serverName, port);
				
				setStatus("Connected");
				
				OutputStream outToServer = client.getOutputStream();
				DataOutputStream out = new DataOutputStream(outToServer);

				out.writeUTF("Hello from client" + client.getLocalSocketAddress());
				InputStream inFromServer = client.getInputStream();
				
				DataInputStream in = new DataInputStream(inFromServer);
				Log.d("TAG", "Server says " + in.readUTF());
				
				String inputLine = "";
				while((inputLine = in.readUTF() )!= null){
					System.out.println("Server : " + inputLine);
					addChatText(inputLine, "Server");
					
				}
				
			} catch (UnknownHostException e) {
				setStatus("Failed");
				e.printStackTrace();
			} catch (IOException e) {
				setStatus("Failed");
				e.printStackTrace();
			}
		}
	}
	
	
	// Set the status text on UI
	public void setStatus(final String statusText){
		MainActivity.this.runOnUiThread(new Runnable() {
            public void run(){
            	m_tvStatus.setText(statusText);
            }
        });
	}
	
	// Add text to chat textview
	public void addChatText(final String msg, final String from){
		MainActivity.this.runOnUiThread(new Runnable() {
            public void run(){
            	m_tvChatText.setText(m_tvChatText.getText().toString() + "\n" + from + " : " + msg);
            }
        });
	}
	
	public void hideSoftKeyboard() {
	    if(getCurrentFocus()!=null) {
	        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
	        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	    }
	}
}
