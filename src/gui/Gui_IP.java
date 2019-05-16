package gui;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


import world.AgarIO;

public class Gui_IP extends JDialog implements ActionListener {

	public final static String CONNECT = "Connect ";

	private JLabel lblTitle, lblIp, lblNick;
	private JTextField txtIp,txtNick;
	private JButton btnConnect;
	private Checkbox checkBox;

	private GUI_principal principal;

	public Gui_IP(GUI_principal principal) {

		checkBox = new Checkbox();

		setResizable(false);

		this.principal = principal;
		setTitle("Icesi Games SA - AgarIO");
		setResizable(false);
		setLayout(new BorderLayout());
		this.setMinimumSize(new Dimension(300, 190));

		lblTitle = new JLabel("Server IP Address");
		lblTitle.setHorizontalAlignment(JLabel.LEFT);
		Font font = new Font("Arial", Font.BOLD, 24);
		lblTitle.setFont(font);

		lblIp = new JLabel("IP: ");
		lblIp.setHorizontalAlignment(JLabel.CENTER);

		txtIp = new JTextField(20);

		lblNick = new JLabel("Nickname viewer: ");
		txtNick = new JTextField(10);
		
		btnConnect = new JButton(CONNECT);
		btnConnect.setActionCommand(CONNECT);
		btnConnect.addActionListener(this);

		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout());

		JPanel p2 = new JPanel();
		p2.setLayout(new FlowLayout());

		JPanel p3 = new JPanel();
		p3.setLayout(new FlowLayout());

		p1.add(lblTitle);
		p2.add(lblIp);
		p2.add(txtIp);
		p2.add(lblNick);
		p2.add(txtNick);
		p2.add(new JLabel("  Viewer mode:  "));
		p2.add(checkBox);
		p3.add(btnConnect);

		add(p1, BorderLayout.NORTH);
		add(p2, BorderLayout.CENTER);
		add(p3, BorderLayout.SOUTH);
		add(new JLabel("  "), BorderLayout.EAST);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

	}

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		try {
			String ip = txtIp.getText();

			if (ip.trim().isEmpty()) {
				throw new Exception("Complete all the information.");
			}
			if (command.equals(CONNECT)) {

				
				if (checkBox.getState() == false) {
					boolean result = principal.connectServer(ip);
					if( result == true) {
						this.setVisible(false);
					}
				} else {

					String nick = txtNick.getText();
					
					boolean r = principal.connectAsViewer(ip,nick);
					if(r){
						this.setVisible(false);
					}
					
				}

			}
		} catch (Exception ex) {
			String ExMessage = ex.getMessage();
			if(ExMessage == null) {
				JOptionPane.showMessageDialog(null, "Connection failed. Try again.",
					"Error", JOptionPane.ERROR_MESSAGE);
			}else {
				JOptionPane.showMessageDialog(null, ExMessage,
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public static void main(String[] args) {
		Gui_IP p = new Gui_IP(null);
		p.setVisible(true);
	}
}
