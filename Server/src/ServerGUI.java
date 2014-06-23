import java.awt.FlowLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ServerGUI extends JFrame{
	
	// Singleton
	private static ServerGUI s_instance = null;
	
	private JTextField 	m_tfPort;
	private JTextField  m_tfUserInput;
	private JTextArea	m_taChatText;
	private JButton 	m_buttonConnect;
	private JButton 	m_buttonSend;
	
	private Server		m_serverThread;
	// Get singleton instance
	public static ServerGUI getInstance() {
		if(s_instance == null) {
			s_instance = new ServerGUI();
		}
		return s_instance;
	}
	
	// Constructor
	protected ServerGUI(){
		setLayout( new FlowLayout() );
		
		// Port text field
		m_tfPort = new JTextField("3131", 20);
		add(m_tfPort);
		
		// Connect button
		m_buttonConnect = new JButton("Start server");
		
		m_buttonConnect.addActionListener(new ActionListener() {
			
	         public void actionPerformed(ActionEvent e) {
	        	 
	        	 m_buttonConnect.setText("Waiting....");
	        	 int port = Integer.parseInt( m_tfPort.getText().toString() );
	             try
	             {
	                m_serverThread = new Server(port);
	                m_serverThread.start();
	             }catch(IOException eIO)
	             {
	            	 eIO.printStackTrace();
	             }
	            
	          }
	    });
		
		add(m_buttonConnect);
		
		// User input textfield
		m_tfUserInput = new JTextField("", 20);
		add(m_tfUserInput);
		
		// Send button
		m_buttonSend = new JButton("Send");
		m_buttonSend.addActionListener(new ActionListener() {
			
	         public void actionPerformed(ActionEvent e) {
	        	m_serverThread.sendMessage(m_tfUserInput.getText().toString());
	        	updateChatText(m_tfUserInput.getText().toString(), "Server");
	            m_tfUserInput.setText("");
	            
	          }
	    });
		add(m_buttonSend);
		
		
		m_taChatText = new JTextArea(10,25);
		add(m_taChatText);
	}
	
	
	
	public void updateButton(final String value){

		if (!SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					updateButton(value);
				}
			});
		}
		
		// Update ui
		m_buttonConnect.setText(value);
	}
	
	public void updateChatText(final String value, final String from ){
		if (!SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
				}
			});
		}
		
		// Update ui
		m_taChatText.setText(m_taChatText.getText().toString() + "\n" + from + " : " + value);
	}
	
	public static void main(String[] args){

		ServerGUI gui_server = ServerGUI.getInstance();
		gui_server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui_server.setSize(350,300);
		gui_server.setVisible(true);
		gui_server.setTitle("Server");
		
	}
}
