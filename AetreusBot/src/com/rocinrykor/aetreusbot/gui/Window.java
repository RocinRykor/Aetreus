package com.rocinrykor.aetreusbot.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.GeneralSecurityException;
import java.awt.Button;
import java.awt.TextField;
import java.awt.Choice;
import java.awt.GridLayout;
import javax.swing.JTextPane;
import java.awt.TextArea;
import javax.swing.SwingConstants;
import java.awt.Label;

public class Window {
	private static TextArea consoleArea;
	private static TextField inputField;
	/**
	 * @wbp.parser.entryPoint
	 */
	public static void StartAppWindow() {
		//Creating the Frame
        JFrame frmAetreusBot = new JFrame("Frame Testing");
        frmAetreusBot.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmAetreusBot.setTitle("Aetreus Bot");
        frmAetreusBot.setSize(400, 400);
        frmAetreusBot.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
        
        JPanel panel = new JPanel();
        frmAetreusBot.getContentPane().add(panel);
        panel.setLayout(null);
        
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		SubmitButtonEvent();
        	}
        });
        submitButton.setBounds(132, 279, 116, 47);
        panel.add(submitButton);
        
        consoleArea = new TextArea();
        consoleArea.setEditable(false);
        consoleArea.setBounds(0, 0, 380, 234);
        panel.add(consoleArea);
        
        inputField = new TextField();
        inputField.setBounds(0, 240, 380, 22);
        panel.add(inputField);
        
        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(258, 279, 116, 47);
        panel.add(exitButton);
        
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		ClearButtonEvent();
        	}
        });
        clearButton.setBounds(6, 279, 116, 47);
        panel.add(clearButton);
        
        JMenuBar menuBar = new JMenuBar();
        frmAetreusBot.setJMenuBar(menuBar);
        
        JMenu mnFile = new JMenu("FILE");
        menuBar.add(mnFile);
        
        JMenu mnOptions = new JMenu("OPTIONS");
        menuBar.add(mnOptions);
        
        JMenu mnHelp = new JMenu("HELP");
        menuBar.add(mnHelp);
        frmAetreusBot.setVisible(true);
	}
	
	public static TextArea getConsoleArea() {
		return consoleArea;
	}
	public static TextField getInputField() {
		return inputField;
	}

	protected static void ClearButtonEvent() {
		consoleArea.setText("");
	}

	protected static void SubmitButtonEvent() {
		String newLine = getInputField().getText() + " \n";
		
		consoleArea.setText(getConsoleArea().getText() + newLine);
		
		inputField.setText("");
	}


	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
