package com.superzanti.launcher.gui;

import com.superzanti.launcher.auth.Authentication;
import com.superzanti.launcher.gui.elements.Button;
import com.superzanti.launcher.gui.elements.Frame;
import com.superzanti.launcher.gui.elements.ImagePanel;
import com.superzanti.launcher.gui.elements.PasswordField;
import com.superzanti.launcher.gui.elements.TextField;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultCaret;

import com.superzanti.launcher.GameLauncher;
import com.superzanti.launcher.GameUpdater;

import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDServiceAuthenticationException;

public class LoginFrame extends Frame {
	
	private static final int WIDTH = 340;
	private static final int HEIGHT = 520;

	private static final long serialVersionUID = 6443373591315738504L;
	private static final String json = "https://raw.githubusercontent.com/superzanti/TheVerse.minecraft/master/launcher_profiles.json";
	private static String title = "Login - TheVerse Launcher";
	private TextField emailField;
	private PasswordField passwordField;
	private JLabel formTitle;
	private JLabel formSubtitle;
	private JTextArea launchinfo;
	private JScrollPane msgScroller;
	private Button logoutButton;
	private Button submitButton;
	ISession session = null;
	private LoginFrame frame = this;
	private GameUpdater updater = null;
	
	public LoginFrame(GameUpdater update) {
		updater = update;
		Font robotoLight = Utils.getRobotoLight();
		
		setTitle(title);
		setSize(new Dimension(WIDTH, HEIGHT));
		setLocationRelativeTo(null);
		
		ImagePanel panel = new ImagePanel(Toolkit.getDefaultToolkit().getImage(LoginFrame.class.getResource("/com/superzanti/resources/images/background.jpg")));
		getContentPane().add(panel, BorderLayout.CENTER);
		
		JLabel logo = new JLabel();
		try {
			logo = new JLabel(new ImageIcon(Utils.scaleImage(ImageIO.read(LoginFrame.class.getResource("/com/superzanti/resources/images/TheVerse.png")), 300, 70)));
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		logo.setBounds(18, 70, 300, 70);
		panel.add(logo);
		
		formTitle = new JLabel("Login", SwingConstants.CENTER);
		formTitle.setForeground(Color.WHITE);
		formTitle.setBounds(0, HEIGHT-290, WIDTH, 69);
		formTitle.setFont(robotoLight.deriveFont(30f));
		panel.add(formTitle);
		
		formSubtitle = new JLabel("Enter your account information:", SwingConstants.CENTER);
		formSubtitle.setForeground(Color.WHITE);
		formSubtitle.setBounds(0, HEIGHT-230, WIDTH, 15);
		formSubtitle.setFont(robotoLight.deriveFont(15f));
		panel.add(formSubtitle);
		
		launchinfo = new JTextArea();
		launchinfo.setText("Loading...");
		launchinfo.setForeground(Color.WHITE);
		launchinfo.setFont(robotoLight.deriveFont(15f));
		launchinfo.setOpaque(false);
		launchinfo.setLineWrap(true);
		launchinfo.setWrapStyleWord(true);
		launchinfo.setEditable(false);
		
		msgScroller = new JScrollPane();        
		msgScroller.setBounds(25, HEIGHT-200, WIDTH-50, 150);
		msgScroller.setFont(robotoLight.deriveFont(15f));
		//msgScroller.setForeground(Color.WHITE);
		msgScroller.setViewportView(launchinfo);
		msgScroller.getViewport().setOpaque(false);
		msgScroller.setOpaque(false);
		msgScroller.setBorder(BorderFactory.createEmptyBorder());
		msgScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		msgScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		msgScroller.setWheelScrollingEnabled(true);
		
		DefaultCaret caret = (DefaultCaret)launchinfo.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		//launchinfo.setEditorKit(new WrapEditorKit());
		//StyledDocument doc = launchinfo.getStyledDocument();
		//SimpleAttributeSet center = new SimpleAttributeSet();
		//StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		//doc.setParagraphAttributes(0, doc.getLength(), center, false);
		msgScroller.setVisible(false);
		panel.add(msgScroller);
		
		emailField = new TextField("Email");
		emailField.setBounds(WIDTH/12, HEIGHT-200, (int) (WIDTH/1.2), 36);
		panel.add(emailField);
		emailField.setColumns(10);
		emailField.selectAll();
		emailField.setEnabled(false);
		emailField.setVisible(false);
		
		passwordField = new PasswordField("Password");
		passwordField.setBounds(WIDTH/12, HEIGHT-150, (int) (WIDTH/1.2), 36);
		panel.add(passwordField);
		passwordField.selectAll();
		passwordField.setEnabled(false);
		passwordField.setVisible(false);
		
		logoutButton = new Button("Logout");
		logoutButton.setBounds(WIDTH/4, HEIGHT-175, WIDTH/2, 36);
		logoutButton.addActionListener( new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	setTexts("Login", "Enter your account Information:");
		    	emailField.setVisible(true);
		    	emailField.setEnabled(true);
		    	passwordField.setVisible(true);
		    	passwordField.setEnabled(true);
		    	logoutButton.setVisible(false);
		    	logoutButton.setEnabled(false);
		    	session = null;
		    	File jsonfile = new File("data/.minecraft/launcher_profiles.json");
		    	jsonfile.delete();
		    	try {
					downloadFromUrl(json, "data/.minecraft/launcher_profiles.json");
				} catch (IOException e1) {
					setTexts("Login", "Logout failed. TheVerse cannot be reached.");
				}
		    }
		});
		panel.add(logoutButton);
		logoutButton.setEnabled(false);
		logoutButton.setVisible(false);
		
		submitButton = new Button("Launch");
		submitButton.setBounds(WIDTH/4, HEIGHT-100, WIDTH/2, 36);
		submitButton.addActionListener( new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	if (session == null){
			    	try {
			    		submitButton.setEnabled(false);
			    		emailField.setEnabled(false);
			    		passwordField.setEnabled(false);
						session = Authentication.legacyLogin(emailField.getText(), new String(passwordField.getPassword()));
			    	} catch (Throwable e1) {
			    		e1.printStackTrace();
						setTexts("FAILED", "Failed to authenticate with Mojang servers.");
						submitButton.setEnabled(true);
			    		emailField.setEnabled(true);
			    		passwordField.setEnabled(true);
					}
		    	}
		    	if (session != null){
		    		logoutButton.setVisible(false);
		    		emailField.setVisible(false);
		    		passwordField.setVisible(false);
		    		submitButton.setVisible(false);
		    		msgScroller.setVisible(true);
		            GameLauncher launcher = null;
		            launcher = new GameLauncher(session, frame, updater);
		            launcher.start();
		    		setTexts("Launching", "Please wait for Forge to launch...");
		    	}
		    }
		});
		submitButton.setEnabled(false);
		submitButton.setVisible(false);
		panel.add(submitButton);
	}
	
	public void setValues(){
		setTexts("Loading", "Looking for saved profiles");
		Authentication.loadProfiles();
		setTexts("Loading", "Logging in...");
		try {
			session = Authentication.sessionLogin(0);
		} catch (YDServiceAuthenticationException e){
			File f = new File("data/.minecraft/launcher_profiles.json");
			if(!(f.exists())) { 
				try {
					Files.copy(Paths.get("data/.minecraft/launcher_profiles_distribute.json"), Paths.get("data/.minecraft/launcher_profiles.json"), StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			setTexts("Loading", "Failed to login to existing profile");
		}
		submitButton.setEnabled(true);
		submitButton.setVisible(true);
		if(session != null){
			emailField.setEnabled(false);
			emailField.setVisible(false);
			passwordField.setEnabled(false);
			passwordField.setVisible(false);
			logoutButton.setEnabled(true);
			logoutButton.setVisible(true);
			formTitle.setText("Welcome");
			formSubtitle.setText("You are logged in as " + session.getUsername());
		}else{
			emailField.setEnabled(true);
			emailField.setVisible(true);
			passwordField.setEnabled(true);
			passwordField.setVisible(true);
			setTexts("Login", "Enter your account Information:");
		}
	}
	
	public void setTexts(String maintext, String subtext){
		formTitle.setText(maintext);
	    formSubtitle.setText(subtext);
	}
	
	public void setInfoText(String line){
	    if ((line != null) && (!line.isEmpty())){
	    	launchinfo.append(line);
	    	launchinfo.setCaretPosition(launchinfo.getDocument().getLength());
	    }
	}
	
	private void downloadFromUrl(String loc, String localFilename) throws IOException {
	    InputStream is = null;
	    FileOutputStream fos = null;

	    URL url = new URL(loc);
	    
	    try {
	        URLConnection urlConn = url.openConnection();//connect

	        is = urlConn.getInputStream();               //get connection inputstream
	        fos = new FileOutputStream(localFilename);   //open outputstream to local file

	        byte[] buffer = new byte[4096];              //declare 4KB buffer
	        int len;

	        //while we have availble data, continue downloading and storing to local file
	        while ((len = is.read(buffer)) > 0) {  
	            fos.write(buffer, 0, len);
	        }
	    } finally {
	        try {
	            if (is != null) {
	                is.close();
	            }
	        } finally {
	            if (fos != null) {
	                fos.close();
	            }
	        }
	    }
	}

}
