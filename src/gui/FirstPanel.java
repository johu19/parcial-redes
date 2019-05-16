package gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class FirstPanel extends JDialog implements ActionListener {

	public final static String LOG_IN="Log in", SIGN_UP="Sign up";
	
	private JButton btnLogIn;
	
	private JButton btnSignUp;
	
	private JLabel lblTitle;
	
	private GUI_principal principal;
	
	public FirstPanel(GUI_principal principal) {

		this.principal = principal;
		//this.setMinimumSize(new Dimension(400, 200));
		setTitle("Icesi Games SA - AgarIO");
		setResizable(false);

		setLayout(new BorderLayout());
		
		lblTitle = new JLabel("\n      Agar.IO      \n");
		lblTitle.setHorizontalAlignment(JLabel.CENTER);
		Font font = new Font("Snap ITC", Font.PLAIN, 36);
		lblTitle.setFont(font);
		
		btnLogIn = new JButton(LOG_IN);
		btnLogIn.setActionCommand(LOG_IN);
		btnLogIn.addActionListener(this);

		btnSignUp = new JButton(SIGN_UP);
		btnSignUp.setActionCommand(SIGN_UP);
		btnSignUp.addActionListener(this);
		
		
		JPanel p1= new JPanel();
		p1.setLayout(new FlowLayout());
		p1.add(btnLogIn);
		p1.add(btnSignUp);
		
		add(new JLabel(" "), BorderLayout.NORTH);
		add(lblTitle, BorderLayout.CENTER);
		add(p1, BorderLayout.SOUTH);
		
		this.pack();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		// TODO Auto-generated constructor stub
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String command = e.getActionCommand();
		
		if(command.equals(LOG_IN)) {
			principal.jdLogIn();
			this.dispose();
		}else if(command.equals(SIGN_UP)) {
			principal.jdSignUp();
			this.dispose();
		}
	}
	
}
