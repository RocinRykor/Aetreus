package studio.rrprojects.aetreusbot.gui;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.SystemTray;

import javax.swing.JOptionPane;

import studio.rrprojects.aetreusbot.Controller;

public class SystemTrayController {

	public void startSystemTray() {
		if (!SystemTray.isSupported()) {
		      System.out.println("SystemTray is not supported");
		      return;
		    }

		    SystemTray tray = SystemTray.getSystemTray();
		    Toolkit toolkit = Toolkit.getDefaultToolkit();
		    Image image = toolkit.getImage("C:\\Users\\Rocin Rykor\\git\\Aetreus\\AetreusBot\\Ampersand.png"); //Default Icon

		    PopupMenu menu = new PopupMenu();
		    
		    MenuItem windowItem = new MenuItem("Show");
		    windowItem.addActionListener(new ActionListener() {
			      public void actionPerformed(ActionEvent e) {
			        MainWindowController.getFrmAetruesBot().setVisible(true);
			      }
			    });
			    menu.add(windowItem);

		    MenuItem messageItem = new MenuItem("Show Message");
		    messageItem.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		        JOptionPane.showMessageDialog(null, "Hello!");
		      }
		    });
		    menu.add(messageItem);

		    MenuItem closeItem = new MenuItem("Exit");
		    closeItem.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		        Controller.ShutdownBot();
		      }
			    });
		    menu.add(closeItem);
		    TrayIcon icon = new TrayIcon(image, "Discord Bot - Aetreus", menu);
		    icon.setImageAutoSize(true);

		    try {
				tray.add(icon);
			} catch (AWTException e1) {
				e1.printStackTrace();
			}
	}
}
