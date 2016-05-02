package com.superzanti.launcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.superzanti.launcher.gui.LoginFrame;

import sk.tomsik68.mclauncher.api.common.ILaunchSettings;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.backend.MinecraftLauncherBackend;
import sk.tomsik68.mclauncher.impl.common.Platform;

public class GameLauncher extends Thread{
	
    private MinecraftLauncherBackend minecraftLauncherBackend;
    private ISession session = null;
    private LoginFrame launchframe = null;
    private GameUpdater updater = null;

    public GameLauncher(ISession sess, LoginFrame frame, GameUpdater update) {
    	session = sess;
    	launchframe = frame;
    	updater = update;
    }

    public void run() {
    	launchframe.setInfoText("Creating minecraftLauncherBackend");
        minecraftLauncherBackend = new MinecraftLauncherBackend(Platform.getCurrentPlatform().getWorkingDirectory());

		//MinecraftInstance mc = new MinecraftInstance(Platform.getCurrentPlatform().getWorkingDirectory());
        
        //UPDATER HERE
		String version = null;
		try {
			//version = minecraftLauncherBackend.getVersionList().get(1);
			version = "1.7.10-Forge10.13.4.1566-1.7.10";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		launchframe.setInfoText("Updating assets...<br>This could take a while. The program is not frozen.");
		try {
			updater.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
        try {
			minecraftLauncherBackend.updateMinecraft(version, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        System.setProperty("user.dir", System.getProperty("user.dir") + "/" + "data" + "/" + ".minecraft/");
        launchframe.setInfoText("Creating Minecraft process");
		
		ProcessBuilder pb = null;
		ILaunchSettings launchsettings = new DefaultLaunchSettings();
		try {
			pb = minecraftLauncherBackend.launchMinecraft(session, null, version, launchsettings, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        pb.redirectErrorStream(true);
        Process proc = null;
		try {
			proc = pb.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));

        while (isProcessAlive(proc)) {
            String line = "";
			try {
				line = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            if (line != null && line.length() > 0)
            	launchframe.setInfoText(line);
            if (line.matches("(.*)Completed early MinecraftForge initialization(.*)")){
            	break;
            }
        }
        launchframe.dispose();
        return;
    }

    private boolean isProcessAlive(Process proc) {
        try {
            proc.exitValue();
            return false;
        } catch (Exception e) {
            return true;
        }
        
    }
}
