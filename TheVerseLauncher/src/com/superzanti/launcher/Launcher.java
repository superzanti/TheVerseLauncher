package com.superzanti.launcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import com.superzanti.launcher.gui.LoginFrame;
public class Launcher {
	
	public static String LAUNCHER_VERSION = "0.000";
	
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
		updater.start();
		LoginFrame loginFrame = new LoginFrame(updater);
		loginFrame.setVisible(true);
		loginFrame.setValues();
	}
}