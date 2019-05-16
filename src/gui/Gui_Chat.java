                                                            package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import world.AgarIO;

public class Gui_Chat extends JPanel implements ActionListener{

	public final static String SEND = "Send";
	private JButton btnSend;
	private JTextArea areaChat;
	private JTextField txtMessage;
	private GUI_principal principal;
	
	public Gui_Chat(GUI_principal principal) {
		
		this.principal = principal;
		
		TitledBorder border = BorderFactory.createTitledBorder(":: Agario Chat ::");
		setPreferredSize(new Dimension(300,AgarIO.GAME_HEIGHT));
		border.setTitleColor(Color.BLACK);
		setBorder(border);
		
		btnSend = new JButton(SEND);
		btnSend.setActionCommand(SEND);
		btnSend.addActionListener(this);
		
		areaChat = new JTextArea();
		areaChat.setEditable(false);
		areaChat.setLineWrap(true);
		areaChat.setWrapStyleWord(true);
		
		txtMessage = new JTextField(4);
		setLayout(new BorderLayout());
		
		JPanel p1 = new JPanel();
		p1.setLayout(new BorderLayout());
		
		p1.add(txtMessage, BorderLayout.CENTER);
		p1.add(btnSend, BorderLayout.EAST);
		
		JScrollPane scroll = new JScrollPane(areaChat);
		add(scroll, BorderLayout.CENTER);
		add(p1, BorderLayout.SOUTH);
		
	}
	

	public void actionPerformed(ActionEvent e) {
		
		String command = e.getActionCommand();
		if(command.equals(SEND)) {
			String message = txtMessage.getText();
			
			if(message != null && !message.trim().isEmpty()) {
				principal.sendMessage(message);
				//receiveMessage(message);
				txtMessage.setText("");
			}			
		}
	}

	public void receiveMessage(String message) {
		//el server manda los mensajes con \n, en este caso se agrega solo para ver fallos en la visualización del jTextArea
		String[] mess = message.split(":");
		areaChat.append(mess[0]+": \n"+mess[1]+"\n");
	}
}
