package com.superzanti.launcher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import com.superzanti.launcher.gui.LoginFrame;

import sk.tomsik68.mclauncher.api.common.ILaunchSettings;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
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
    	PrintStream printStream = new PrintStream(new OutputStream()
		{
			@Override
	        public void write(int b) throws IOException {
					launchframe.setInfoText(String.valueOf((char)b));
					BufferedWriter bw = new BufferedWriter(new FileWriter("./data/log.txt", true));
					bw.write(String.valueOf((char)b));
					bw.close();
			}
		});
		System.setOut(printStream);
		System.setErr(printStream);
		//Logger log = Logger.getLogger(MCLauncherAPI.class.getName());
		SimpleFormatter fmt = new SimpleFormatter();
		StreamHandler sh = new StreamHandler(System.out, fmt);
		MCLauncherAPI.log.addHandler(sh);
		
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
		
		launchframe.setInfoText("Updating assets...\n\rThis could take a while. The program is not frozen.");
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
        
        //System.setProperty("user.dir", System.getProperty("user.dir") + "/" + "data" + "/" + ".minecraft/");
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
        pb.directory(new File("./data/.minecraft/"));
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
