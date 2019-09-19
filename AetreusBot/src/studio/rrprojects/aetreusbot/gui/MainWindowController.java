package studio.rrprojects.aetreusbot.gui;

import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import studio.rrprojects.aetreusbot.Controller;
import studio.rrprojects.aetreusbot.InputCollection;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainWindowController {

	private static TextArea consoleArea;
	private static TextField inputField;
	
	private static JFrame frmAetreusBot;
	private final static JToggleButton tglbtnFudgeButton = new JToggleButton("Fudge Rolls");
	/**
	 * @wbp.parser.entryPoint
	 */
	public void StartAppWindow() {
		//Creating the Frame
        frmAetreusBot = new JFrame("Frame Testing");
        frmAetreusBot.setResizable(false);
        frmAetreusBot.setTitle("Aetreus Bot");
        frmAetreusBot.setSize(405, 422);
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
        consoleArea.setBounds(10, 0, 364, 234);
        panel.add(consoleArea);
        
        inputField = new TextField();
        inputField.setBounds(10, 240, 364, 22);
        inputField.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyPressed(KeyEvent evt) {
        		 if(evt.getKeyCode() == KeyEvent.VK_ENTER)
                 {
        			 SubmitButtonEvent();
                 }
        	}
        });
        panel.add(inputField);
        
        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(258, 279, 116, 47);
        exitButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		ExitButtonEvent();
        	}
        });
        panel.add(exitButton);
        
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		ClearButtonEvent();
        	}
        });
        clearButton.setBounds(6, 279, 116, 47);
        panel.add(clearButton);
        
        tglbtnFudgeButton.setBounds(105, 337, 159, 31);
        tglbtnFudgeButton.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				if (tglbtnFudgeButton.isSelected())  {
					tglbtnFudgeButton.setText("FUDGE (ON)");
				 	Controller.setFudge(true);
				} else {
		 			tglbtnFudgeButton.setText("FUDGE (OFF)");
					Controller.setFudge(false);
		 		}
			}
		});
        panel.add(tglbtnFudgeButton);
        
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
	
	public static JFrame getFrmAetruesBot() {
		return frmAetreusBot;
	}
	
	protected static void ClearButtonEvent() {
		InputCollection.ClearArray();
	}
	
	protected static void ExitButtonEvent() {
		Controller.ShutdownBot();
	}
	
	protected static void SubmitButtonEvent() {
		if (getInputField().getText().matches("")) {
			return;
		}
		
		String newInput = getInputField().getText();
		
		InputCollection.ParseNewInput(newInput);
		
		inputField.setText("");
	}

	public static void UpdateConsoleArea(String[] consoleArray) {
		String consoleText = "";
		
		for (int i = 0; i < consoleArray.length; i++) {
			if (consoleArray[i] != null) {
				consoleText += consoleArray[i];
			}
		}
		
		consoleArea.setText(consoleText);
	}


	@SuppressWarnings("unused")
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
