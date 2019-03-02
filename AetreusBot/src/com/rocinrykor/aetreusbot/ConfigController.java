package com.rocinrykor.aetreusbot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import java.awt.Font;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ConfigController {
	private static JPasswordField txtFieldToken;
	private static JTextField txtFieldPrefix;
	private static JTextField txtFieldName;
	private static JTextField txtFieldMessage;
	/*
	 * This is going to be the section that checks for a valid config file, generates one if needed, and loads on every subsequent startup
	 * 
	 * If a valid config file is not found a message box will appear prompting the user to supply the needed information so that seam-less first time launching can happen
	 * 
	 * */
	
	private static String BOT_TOKEN = ""; //Token for loading the bot.
	private static String BOT_PREFIX = ""; //Prefix character for chat messages.
	private static String BOT_NAME = ""; //Name for the bot
	private static String BOT_MESSAGE = ""; //Message for the bot, displays next to "Currently Playing:"
	
	static File file;
	static Properties prop;
	static FileReader reader;
	
	static String configFile;
	
	public static void Init() {
		boolean modeTesting = false;
		String directory;
		
		if (modeTesting) {
			directory = "Testing Bot";
		} else {
			directory = "Aetreus Bot";
		}
		
		
		//Initializes the file location of the config file set to C://User/USERNAME/Documents/Aetreus Bot/BotInfo.cfg"
		String fileName = "BotInfo.cfg";
		String configDir = System.getProperty("user.home") + File.separator + "Documents" + File.separator + directory;
		configFile = configDir + File.separator + fileName;
		
		//Initializes Properties and FileReader
		prop = new Properties();
		reader = null;
		
		//Checks if directories of config file exist and create them if necessary.
		File dir = new File(configDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		//Checks if the actual config files exists, creates if necessary.
		file = new File(configFile);
		
		System.out.println("Init Starting");
		
		if (!file.exists()) {
			System.out.println("Config File Not Found!");
			RunFirstTimeSetup();
		} else {
			LoadConfigFile();
		}
		
}

	private static void LoadConfigFile() {
		//Loads the valid file
				try {
					reader = new FileReader(configFile);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					prop.load(reader);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				BOT_TOKEN = prop.getProperty("BOT_TOKEN");
				BOT_PREFIX = prop.getProperty("BOT_PREFIX");
				BOT_NAME = prop.getProperty("BOT_NAME");
				BOT_MESSAGE = prop.getProperty("BOT_MESSAGE");
	}

	private static void RunFirstTimeSetup() {
		OpenConfigWindow();
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	private static void OpenConfigWindow() {
		JFrame frmFirstTimeSetup = new JFrame("First Time Setup");
		frmFirstTimeSetup.setSize(300, 475);
		frmFirstTimeSetup.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		frmFirstTimeSetup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		
		txtFieldToken = new JPasswordField();
		txtFieldToken.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtFieldToken.setBounds(114, 137, 164, 20);
		frmFirstTimeSetup.getContentPane().add(txtFieldToken);
		txtFieldToken.setColumns(10);
		
		JLabel lblTokenInfo = new JLabel("Hold Cursor HERE for more info.");
		lblTokenInfo.setToolTipText("The Token is needed so that the discord knows which bot is trying to be controlled.\r\n\r\nPlease visit https://discordapp.com/developers/applications/ to locate your bot and it's bot token.\r\n\r\nPlease note that this token is important and must be kept private.\r\n\r\nThis Option has no Default and a valid token must be entered.");
		lblTokenInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblTokenInfo.setBounds(10, 160, 268, 20);
		frmFirstTimeSetup.getContentPane().add(lblTokenInfo);
		
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
		
		JLabel lblPrefixInfo = new JLabel("Hold Cursor HERE for more info.");
		lblPrefixInfo.setToolTipText("This is the sysmbol or string that the bot will look for at the start of messages to know when you are issuing a command. The prefix can be symbol, word, or string of characters that you want, but for ease of access I reccomend a single symbol.\r\n\r\nDefault is \"&\"");
		lblPrefixInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblPrefixInfo.setBounds(10, 214, 268, 20);
		frmFirstTimeSetup.getContentPane().add(lblPrefixInfo);
		
		JLabel lblName = new JLabel("BOT NAME:");
		lblName.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblName.setBounds(10, 245, 100, 20);
		frmFirstTimeSetup.getContentPane().add(lblName);
		
		txtFieldName = new JTextField();
		txtFieldName.setText("Aetreus");
		txtFieldName.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtFieldName.setColumns(10);
		txtFieldName.setBounds(114, 245, 164, 20);
		frmFirstTimeSetup.getContentPane().add(txtFieldName);
		
		JLabel lblNameInfo = new JLabel("Hold Cursor HERE for more info.");
		lblNameInfo.setToolTipText("This is the name that the the bot will apear in the server as.\r\n\r\nDefault is \"Aetreus\"");
		lblNameInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblNameInfo.setBounds(10, 268, 268, 20);
		frmFirstTimeSetup.getContentPane().add(lblNameInfo);
		
		JLabel lblMessage = new JLabel("BOT MESSAGE:");
		lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblMessage.setBounds(10, 299, 100, 20);
		frmFirstTimeSetup.getContentPane().add(lblMessage);
		
		txtFieldMessage = new JTextField();
		txtFieldMessage.setText("Type &Help to begin");
		txtFieldMessage.setToolTipText("This string of text will display below the bot in the user list for the server. This text will appear where it would normally say \"Playing: {Game Info}\" on a normal user.\r\n\r\nMaking this text too long can cause it to be cutoff, so keep the information short and simple. Make sure that if you are using this space as an example command, that the exaple properly reflects the actual prefix the bot is using.\r\n\r\nDefault is \"Type &help to begin\"");
		txtFieldMessage.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtFieldMessage.setColumns(10);
		txtFieldMessage.setBounds(114, 299, 164, 20);
		frmFirstTimeSetup.getContentPane().add(txtFieldMessage);
		
		JLabel lblMessageInfo = new JLabel("Hold Cursor HERE for more info.");
		lblMessageInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblMessageInfo.setBounds(10, 322, 268, 20);
		frmFirstTimeSetup.getContentPane().add(lblMessageInfo);
		
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
		
		frmFirstTimeSetup.setVisible(true);
	}

	protected static boolean GrabConfigInfo() {
		String tempBotToken, tempBotPrefix, tempBotName, tempBotMessage;
		tempBotToken = getTxtFieldToken().getText();
		tempBotPrefix = getTxtFieldPrefix().getText();
		tempBotName = getTxtFieldName().getText();
		tempBotMessage = getTxtFieldMessage().getText();
		
		System.out.println(tempBotToken);
		System.out.println(tempBotPrefix);
		System.out.println(tempBotName);
		System.out.println(tempBotMessage);
		
		if (PassEmptyCheck(tempBotToken, tempBotPrefix, tempBotName, tempBotMessage)) {
			CreateConfigFile(tempBotToken, tempBotPrefix, tempBotName, tempBotMessage);
			return true;
		} else {
			return false;
		}
	}
	private static void CreateConfigFile(String tempBotToken, String tempBotPrefix, String tempBotName, String tempBotMessage) {
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//If file was created, sets the properties
		prop.setProperty("BOT_TOKEN", tempBotToken);
		prop.setProperty("BOT_PREFIX", tempBotPrefix);
		prop.setProperty("BOT_NAME", tempBotName);
		prop.setProperty("BOT_MESSAGE", tempBotMessage);
		
		//Writes properties to the newly created file
		try {
			prop.store(new FileOutputStream(configFile), null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LoadConfigFile();
	}

	private static boolean PassEmptyCheck(String token, String prefix, String name, String message) {
		if (token.length() == 0 || prefix.length() == 0 || name.length() == 0 || message.length() == 0) {
			return false;
		} else {
			return true;
		}
		
	}

	public static JTextField getTxtFieldName() {
		return txtFieldName;
	}
	public static JTextField getTxtFieldPrefix() {
		return txtFieldPrefix;
	}
	public static JTextField getTxtFieldMessage() {
		return txtFieldMessage;
	}
	public static JPasswordField getTxtFieldToken() {
		return txtFieldToken;
	}
	
	public static String getBOT_TOKEN() {
		return BOT_TOKEN; 
	}
	
	public static String getBOT_PREFIX() {
		return BOT_PREFIX; 
	}
	
	public static String getBOT_NAME() {
		return BOT_NAME; 
	}
	
	public static String getBOT_MESSAGE() {
		return BOT_MESSAGE; 
	}
	
}
