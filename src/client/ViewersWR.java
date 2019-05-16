package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ViewersWR extends JFrame {
	
	public final static String GIF_ROOT = "Img/Gif.gif";
	
	public ViewersWR(){
		
		setTitle("WAITING FOR GAME TO START");
		setLayout(new BorderLayout());
		
		JLabel lbl = new JLabel();
		ImageIcon img = new ImageIcon(GIF_ROOT);
		lbl.setIcon(img);
		
		add(lbl,BorderLayout.CENTER);
		
		pack();
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
	}

}
