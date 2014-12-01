package main;
import javax.swing.*;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainGUI extends JFrame implements ActionListener {

	public JLabel enterLbl = null;
	public JTextField email = null;
	public JButton searchBtn = null;
	public JButton addBtn = null;
	public JTextArea note = null;
	
	public MainGUI (String title) {
		//setDefaultLookAndFeelDecorated(true);
		setName(title);
	    setLayout(new FlowLayout());
	    
		enterLbl = new JLabel("Enter the email address to search: ");
		email = new JTextField("username@example.com");
		email.setForeground(Color.GRAY);
		email.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (email.getForeground().equals(Color.GRAY) || email.getText().trim().equals("")) {
					email.setText("");
					email.setForeground(Color.BLACK);
				}
			}
			
			public void mouseExited(MouseEvent e) {
				if (email.getText().trim().equals("")) {
					email.setText("username@example.com");
					email.setForeground(Color.GRAY);
				}
			}
		});
		
		searchBtn = new JButton("Search");
		searchBtn.addActionListener(this);
		
		add(enterLbl);
		add(email);
		add(searchBtn);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
        //setSize(280, 190);
        setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		BufferedReader br = null;
		BufferedWriter bw = null;
		File storage = null;		
		String line = null;
		String emailAddr = email.getText().trim();
		boolean isStored = false;
		
		if (e.getSource() == searchBtn) {
			try {
				storage = new File("./src/data/storage.txt");
				br = new BufferedReader(new FileReader(storage));
				
				while ((line = br.readLine()) != null) {
					if (line.contains(emailAddr)) {
						isStored = true;
						if (note == null) {
							note = new JTextArea("Already in the database!");
							add(note);
						} else if (addBtn != null) {
							    note.setText("Already in the database!");
							    remove(addBtn);
						}
						break;
					} 
				}
				
				if (note == null) {
					note = new JTextArea("User not in the database yet...");
					add(note);
					addBtn = new JButton("Add");
					add(addBtn);
					addBtn.addActionListener(this);
				} else if (!isStored) {
					note.setText("User not in the database yet...");
					if (addBtn == null) {
						addBtn = new JButton("Add");
						add(addBtn);
						addBtn.addActionListener(this);
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}finally {
				try {
					if (br != null) {
						br.close();
					}	
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
		} else if (e.getSource() == addBtn) {
			int result = JOptionPane.showOptionDialog(null,"Are you sure to put this email in database?","Message Dialog",JOptionPane.YES_NO_OPTION,
				      JOptionPane.QUESTION_MESSAGE,null, new Object[]{"Yes","No"}, "No");
			
			if (result == 0) {
				try {
					storage = new File("./src/data/storage.txt");
					bw = new BufferedWriter(new FileWriter(storage,true));
					bw.newLine();
					bw.write(emailAddr);
					note.setText("User added to database.");
					remove(addBtn);
				} catch(IOException e3) {
					e3.printStackTrace();
				} finally {
					try {
						if (bw != null) {
							bw.close();
						}	
					} catch (IOException e4) {
						e4.printStackTrace();
					}
				}
			}
			pack();
		}
	}

    public static void main(String[] args) {
    	MainGUI gui = new MainGUI("Dummy");
    }
}