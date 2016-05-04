package com.superzanti.launcher;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;


import com.superzanti.launcher.gui.LoginFrame;

import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
public class Launcher {
	
	public static String LAUNCHER_VERSION = "0.000";
	public static BufferedWriter bw = null;
	
	public static void main(String[] args) {
		//Popup test = new Popup("This is a test");
		//test.setVisible(true);
		//test.dispose();
		File f = new File("data/.minecraft/launcher_profiles.json");
		if(!(f.exists())) { 
			try {
				Files.copy(Paths.get("data/.minecraft/launcher_profiles_distribute.json"), Paths.get("data/.minecraft/launcher_profiles.json"), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		GameUpdater updater;
		updater = new GameUpdater();
		LoginFrame loginFrame = new LoginFrame(updater);
		updater.setFrame(loginFrame);
		updater.start();
		loginFrame.setVisible(true);
		loginFrame.setValues();
		try {
			bw = new BufferedWriter(new FileWriter("./data/log.txt", true));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrintStream printStream = new PrintStream(new OutputStream()
		{
			@Override
	        public void write(int b) throws IOException {
				loginFrame.setInfoText(String.valueOf((char)b));
				bw.write(String.valueOf((char)b));
			}
		});
		System.setOut(printStream);
		System.setErr(printStream);
		//Logger log = Logger.getLogger(MCLauncherAPI.class.getName());
		MCLauncherAPI.log.setUseParentHandlers(true);
		SimpleFormatter fmt = new SimpleFormatter();
		StreamHandler sh = new StreamHandler(System.out, fmt);
		MCLauncherAPI.log.addHandler(sh);
		/*
		FileHandler fh;
		try {  

	        // This block configure the logger with handler and formatter  
	        fh = new FileHandler("./data/log.txt");
	        MCLauncherAPI.log.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);  
	    } catch (Throwable e) {  }
		MCLauncherAPI.log.info("TEST");
		*/
		try {
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}