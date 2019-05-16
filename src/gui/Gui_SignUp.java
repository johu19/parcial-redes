package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Gui_SignUp extends JDialog implements ActionListener{

	public final static String SIGN_UP="Sign up";
	
	private JLabel lblTitle, lblEmail, lblNickname, lblPassword;
	
	private JTextField txtEmail,txtNickname, txtPassword;
	
	private JButton btnSignUp;
	
	private GUI_principal principal;
	
	public Gui_SignUp(GUI_principal principal) {
		
		setResizable(false);
		
		this.principal = principal;
		setTitle("Icesi Games SA - AgarIO");
		setResizable(false);
		
		this.setMinimumSize(new Dimension(350, 250));
		setLayout(new BorderLayout());
		
		lblTitle = new JLabel("Create my account");
		lblTitle.setHorizontalAlignment(JLabel.LEFT);
		Font font = new Font("Arial", Font.BOLD, 24);
		lblTitle.setFont(font);
		
		lblEmail = new JLabel("Email:");
		lblEmail.setHorizontalAlignment(JLabel.CENTER);
		
		lblNickname = new JLabel("Nickname: ");
		lblNickname.setHorizontalAlignment(JLabel.CENTER);
		
		lblPassword = new JLabel("Password:");
		lblPassword.setHorizontalAlignment(JLabel.CENTER);
		
		txtEmail = new JTextField();
		txtNickname = new JTextField();
		txtPassword = new JTextField();
		
		btnSignUp = new JButton(SIGN_UP);
		btnSignUp.setActionCommand(SIGN_UP);
		btnSignUp.addActionListener(this);
		
		JPanel p1= new JPanel();
		p1.setLayout(new FlowLayout());
		
		JPanel p2= new JPanel();
		p2.setLayout(new GridLayout(7,2));
		
		JPanel p3= new JPanel();
		p3.setLayout(new FlowLayout());
		
		p1.add(lblTitle);
		
		p2.add(new JLabel(""));p2.add(new JLabel(""));
		p2.add(lblEmail); p2.add(txtEmail);
		p2.add(new JLabel(""));p2.add(new JLabel(""));
		p2.add(lblNickname); p2.add(txtNickname);
		p2.add(new JLabel(""));p2.add(new JLabel(""));
		p2.add(lblPassword); p2.add(txtPassword);
		p2.add(new JLabel(""));p2.add(new JLabel(""));

		p3.add(btnSignUp);
		
		add(p1, BorderLayout.NORTH);
		add(p2,BorderLayout.CENTER); add(p3,BorderLayout.SOUTH);
		add(new JLabel("    "),BorderLayout.EAST);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
	}

	public JLabel getLblTitle() {
		return lblTitle;
	}

	public void setLblTitle(JLabel lblTitle) {
		this.lblTitle = lblTitle;
	}

	public JLabel getLblEmail() {
		return lblEmail;
	}

	public void setLblEmail(JLabel lblEmail) {
		this.lblEmail = lblEmail;
	}

	public JLabel getLblNickname() {
		return lblNickname;
	}

	public void setLblNickname(JLabel lblNickname) {
		this.lblNickname = lblNickname;
	}

	public JLabel getLblPassword() {
		return lblPassword;
	}

	public void setLblPassword(JLabel lblPassword) {
		this.lblPassword = lblPassword;
	}

	public JTextField getTxtEmail() {
		return txtEmail;
	}

	public void setTxtEmail(JTextField txtEmail) {
		this.txtEmail = txtEmail;
	}

	public JTextField getTxtNickname() {
		return txtNickname;
	}

	public void setTxtNickname(JTextField txtNickname) {
		this.txtNickname = txtNickname;
	}

	public JTextField getTxtPassword() {
		return txtPassword;
	}

	public void setTxtPassword(JTextField txtPassword) {
		this.txtPassword = txtPassword;
	}

	public JButton getBtnSignUp() {
		return btnSignUp;
	}

	public void setBtnSignUp(JButton btnSignUp) {
		this.btnSignUp = btnSignUp;
	}

	public GUI_principal getPrincipal() {
		return principal;
	}

	public void setPrincipal(GUI_principal principal) {
		this.principal = principal;
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String command = e.getActionCommand();
		
		try {
			String email = txtEmail.getText();
			String nickname = txtNickname.getText();
			String password = txtPassword.getText();
			
			if(email.trim().isEmpty()|| nickname.trim().isEmpty() || password.trim().isEmpty()) {
				throw new Exception();
			}
			if(command.equals(SIGN_UP)) {
				principal.newPlayer(email, nickname, password);
				if(principal.isConnectionResult()){

					this.dispose();	
				}
			}
		}catch(Exception ex) {
			JOptionPane.showMessageDialog(null, "Complete all the information","Error", JOptionPane.ERROR_MESSAGE );
		}
	}
}
