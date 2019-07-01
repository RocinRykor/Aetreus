package studio.rrprojects.aetreusbot.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import studio.rrprojects.aetreusbot.ConfigController;

public class ConfigControllerGUI {
	
	private static JTextField txtFieldToken;
	private static JTextField txtFieldPrefix;
	private static JTextField txtFieldSecondaryPrefix;
	private static JTextField txtFieldMessage;
	
	private static String helpToken = "This is the private key that discord uses so that the programs sends the correct bot online. Do not share this key with anyone.";
	private static String helpPrefix = "This is the symbol that must preface a command for the bot to recognize that it is being talked to.";
	private static String helpSecondPrefix = "Behaves like the normal prefix, but given as an extra option for the users";
	private static String helpMessage = "This string of text will display below the bot in the user list for the server. This text will appear where it would normally say \"Playing: {Game Info}\" on a normal user.\r\n\r\nMaking this text too long can cause it to be cutoff, so keep the information short and simple. Make sure that if you are using this space as an example command, that the exaple properly reflects the actual prefix the bot is using.\r\n\r\nDefault is \"Type &help to begin\"";
	
	/**
	 * @wbp.parser.entryPoint
	 * This window will be used to supply the config file with the needed information
	 */
	public static void OpenConfigWindow() {
		JFrame frmFirstTimeSetup = new JFrame("First Time Setup");
		frmFirstTimeSetup.setResizable(false);
		frmFirstTimeSetup.setSize(300, 475);
		frmFirstTimeSetup.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		frmFirstTimeSetup.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmFirstTimeSetup.getContentPane().setLayout(null);
		
		JTextArea txtArea_InfoSplash = new JTextArea();
		txtArea_InfoSplash.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		txtArea_InfoSplash.setBackground(UIManager.getColor("Button.background"));
		txtArea_InfoSplash.setEditable(false);
		txtArea_InfoSplash.setBounds(10, 0, 268, 131);
		txtArea_InfoSplash.setWrapStyleWord(true);
		txtArea_InfoSplash.setText("Welcome! I did not find a valid config file, so I believe this may be the first time you are trying to run this bot.\r\n\r\nIn order to begin, I just need some basic information.");
		txtArea_InfoSplash.setLineWrap(true);
		frmFirstTimeSetup.getContentPane().add(txtArea_InfoSplash);
		
		JLabel lblBotToken = new JLabel("BOT TOKEN:");
		lblBotToken.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblBotToken.setBounds(10, 137, 100, 20);
		frmFirstTimeSetup.getContentPane().add(lblBotToken);
		
		txtFieldToken = new JTextField();
		txtFieldToken.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtFieldToken.setBounds(114, 137, 164, 20);
		frmFirstTimeSetup.getContentPane().add(txtFieldToken);
		txtFieldToken.setColumns(10);
		
		JLabel lblPrefix = new JLabel("BOT PREFIX");
		lblPrefix.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblPrefix.setBounds(10, 191, 100, 20);
		frmFirstTimeSetup.getContentPane().add(lblPrefix);
		
		txtFieldPrefix = new JTextField();
		txtFieldPrefix.setText("&");
		txtFieldPrefix.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtFieldPrefix.setColumns(10);
		txtFieldPrefix.setBounds(114, 191, 164, 20);
		frmFirstTimeSetup.getContentPane().add(txtFieldPrefix);
		
		JLabel lblSecondaryPrefix = new JLabel("BOT 2ND PREFIX:");
		lblSecondaryPrefix.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblSecondaryPrefix.setBounds(10, 245, 100, 20);
		frmFirstTimeSetup.getContentPane().add(lblSecondaryPrefix);
		
		txtFieldSecondaryPrefix = new JTextField();
		txtFieldSecondaryPrefix.setText("!");
		txtFieldSecondaryPrefix.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtFieldSecondaryPrefix.setColumns(10);
		txtFieldSecondaryPrefix.setBounds(114, 245, 164, 20);
		frmFirstTimeSetup.getContentPane().add(txtFieldSecondaryPrefix);
		
		JLabel lblMessage = new JLabel("BOT MESSAGE:");
		lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblMessage.setBounds(10, 299, 100, 20);
		frmFirstTimeSetup.getContentPane().add(lblMessage);
		
		txtFieldMessage = new JTextField();
		txtFieldMessage.setText("Type &Help to begin");
		txtFieldMessage.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtFieldMessage.setColumns(10);
		txtFieldMessage.setBounds(114, 299, 164, 20);
		frmFirstTimeSetup.getContentPane().add(txtFieldMessage);
		
		JButton btnNewButton = new JButton("DONE");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (GrabConfigInfo()) {
					frmFirstTimeSetup.dispose();
				}
			}
		});
		btnNewButton.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		btnNewButton.setBounds(10, 353, 264, 57);
		frmFirstTimeSetup.getContentPane().add(btnNewButton);
		
		JButton bttnTokenHelp = new JButton("?");
		bttnTokenHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, helpToken);
			}
		});
		bttnTokenHelp.setBounds(10, 157, 37, 24);
		frmFirstTimeSetup.getContentPane().add(bttnTokenHelp);
		
		JButton bttnPrefixHelp = new JButton("?");
		bttnPrefixHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, helpPrefix);
			}
		});
		bttnPrefixHelp.setBounds(10, 211, 37, 23);
		frmFirstTimeSetup.getContentPane().add(bttnPrefixHelp);
		
		JButton bttnSecondPrefixHelp = new JButton("?");
		bttnSecondPrefixHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, helpSecondPrefix);
			}
		});
		bttnSecondPrefixHelp.setBounds(10, 265, 37, 23);
		frmFirstTimeSetup.getContentPane().add(bttnSecondPrefixHelp);
		
		JButton bttnMessageHelp = new JButton("?");
		bttnMessageHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, helpMessage);
			}
		});
		bttnMessageHelp.setBounds(10, 318, 37, 24);
		frmFirstTimeSetup.getContentPane().add(bttnMessageHelp);
		
		frmFirstTimeSetup.setVisible(true);
	}

	protected static boolean GrabConfigInfo() {
		String tempBotToken;
		String tempBotPrefix, tempBotSecondaryPrefix, tempBotMessage;
		tempBotToken = getTxtFieldToken().getText();
		tempBotPrefix = getTxtFieldPrefix().getText();
		tempBotSecondaryPrefix = getTxtFieldSecondaryPrefix().getText();
		tempBotMessage = getTxtFieldMessage().getText();
		
		System.out.println(tempBotToken);
		System.out.println(tempBotPrefix);
		System.out.println(tempBotSecondaryPrefix);
		System.out.println(tempBotMessage);
		
		if (PassEmptyCheck(tempBotToken, tempBotPrefix, tempBotSecondaryPrefix, tempBotMessage)) {
			ConfigController.CreateConfigFile(tempBotToken, tempBotPrefix, tempBotSecondaryPrefix, tempBotMessage);
			return true;
		} else {
			return false;
		}
	}
	
	public static JTextField getTxtFieldSecondaryPrefix() {
		return txtFieldSecondaryPrefix;
	}
	public static JTextField getTxtFieldPrefix() {
		return txtFieldPrefix;
	}
	public static JTextField getTxtFieldMessage() {
		return txtFieldMessage;
	}
	public static JTextField getTxtFieldToken() {
		return txtFieldToken;
	}
	
	private static boolean PassEmptyCheck(String tempBotToken, String prefix, String name, String message) {
		if (tempBotToken.toString().length() == 0 || prefix.length() == 0 || name.length() == 0 || message.length() == 0) {
			return false;
		} else {
			return true;
		}
		
	}
}
