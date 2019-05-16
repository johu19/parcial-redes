package server;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class AdminWindow extends JFrame implements ActionListener{
	
	public final static String START = "Start Game";
	
	
	private Server server;
	
	private JLabel lblNumPlayers;
	
	private JButton btnStart;
	
	public boolean btnEnabled(){
		return btnStart.isEnabled();
	}
	
	public AdminWindow(Server s){
		
		server = s;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("Admin");
		
		
		
		
		btnStart = new JButton(START);
		btnStart.setActionCommand(START);
		btnStart.addActionListener(this);
		
		lblNumPlayers = new JLabel("  Number of players connected: 0  ");
		
		btnStart.setEnabled(false);
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1,2));
	    TitledBorder tb = new TitledBorder("Manager display");
	    p.setBorder(tb);
	    p.add(lblNumPlayers);
	    p.add(btnStart);
	    
	    
	    this.add(p);
		
		
		pack();
		
		
	}
	
	public void activateButton(){
		btnStart.setEnabled(true);
	}
	
	public void refreshNumPlayers(String n){
		
		
		lblNumPlayers.setText("  Number of players connected: "+n+"  ");
		this.pack();
	}

	public void actionPerformed(ActionEvent e) {
		String event = e.getActionCommand();
		if(event.equals(START)){
			
			server.startGame();
			
			
			
		}
		
	}

}
