package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import world.PlayerBall;

public class Gui_finalTop extends JDialog {
	
	private JLabel lblTitle;
	private JTable jtResults;
	
	public Gui_finalTop(ArrayList<PlayerBall> topPlayers){
		
		setResizable(false);
		
		
		setTitle("Icesi Games SA - AgarIO");
		setResizable(false);
		
		//this.setMinimumSize(new Dimension(350, 200));
		setLayout(new BorderLayout());
		
		lblTitle = new JLabel("TOP PLAYERS");
		lblTitle.setHorizontalAlignment(JLabel.CENTER);
		Font font = new Font("Snap ITC", Font.PLAIN, 36);
		lblTitle.setFont(font);
		
		String [][] data = new String[3][3];
		
		for (int i = 0; i < data.length && i<topPlayers.size(); i++) {
			for (int j = 0; j < data[0].length; j++) {
				
				if(j==0){
					data[i][j]= i+1 + ".";
				}else if(j==1){
					data[i][j]= topPlayers.get(i).getNickname();
				}else if(j==2){
					data[i][j]= topPlayers.get(i).getMass()+"";
				}
			}
		}
		
		String[] nameColumns = new String[3];
		nameColumns[0]="   POSITION";
		nameColumns[1]="   NICKNAME";
		nameColumns[2]="   SCORE";
		DefaultTableModel dtm = new DefaultTableModel(data,nameColumns){
			 @Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		 };
		jtResults = new JTable(dtm);
		
		
		JScrollPane scroll = new JScrollPane(jtResults);
		JPanel p1= new JPanel();
		p1.setLayout(new FlowLayout());
		p1.add(lblTitle);
		
		add(p1, BorderLayout.NORTH);
		add(scroll, BorderLayout.CENTER);
//		setSize(new Dimension(500, 500));
	//	pack();
		setSize(new Dimension(350,190));
	}

}
