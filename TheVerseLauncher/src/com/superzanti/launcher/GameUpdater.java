package com.superzanti.launcher;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.StatusCommand;
import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.DetachedHeadException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.RefNotAdvertisedException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import com.superzanti.launcher.gui.LoginFrame;

import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;

public class GameUpdater extends Thread {
	
	private LoginFrame launchframe = null;
	public static BufferedWriter bw = null;

    public GameUpdater() {
    }
    
    public void setFrame(LoginFrame frame) {
    	launchframe = frame;
    }

    public void run() {
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
				launchframe.setInfoText(String.valueOf((char)b));
				bw.write(String.valueOf((char)b));
			}
		});
		System.setOut(printStream);
		System.setErr(printStream);
		//Logger log = Logger.getLogger(MCLauncherAPI.class.getName());
		SimpleFormatter fmt = new SimpleFormatter();
		StreamHandler sh = new StreamHandler(System.out, fmt);
		MCLauncherAPI.log.addHandler(sh);
    
    	String localPath, remotePath;
	    
	    localPath = ".";
        remotePath = "https://github.com/superzanti/TheVerse.git";
        
		try {
			File file = new File("./.git/index.lock");
			file.deleteOnExit();
			Files.delete(Paths.get("./.git/index.lock"));
		} catch (IOException e1) {
		}
        
        while(true){
	        try {
	        	System.out.println("Starting Pull");
				doPull(remotePath, localPath);
				break;
			} catch (Throwable e) {
				e.printStackTrace();
				try {
					Files.delete(Paths.get("./.git/index.lock"));
				} catch (IOException e1) {
				}
			}
        }

		try {
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void doPull(String fromRepoPath, String toRepoPath) throws WrongRepositoryStateException, InvalidConfigurationException, DetachedHeadException, InvalidRemoteException, CanceledException, RefNotFoundException, RefNotAdvertisedException, NoHeadException, TransportException, GitAPIException, IOException{
		
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = builder.setGitDir(new File(toRepoPath + "/.git"))
		  .readEnvironment()
		  .findGitDir()
		  .build();
		
		Git git = new Git(repository);

		File gitrepo = new File( toRepoPath );
		Git.init().setDirectory(gitrepo).call();

		StoredConfig config = git.getRepository().getConfig();
		config.setString("remote", "origin", "fetch", "+refs/heads/*:refs/remotes/origin/*");
		config.setString("remote", "origin", "url", fromRepoPath);
		config.setString("branch", "master", "remote", "origin");
		config.setString("branch", "master", "merge", "refs/heads/master");

		config.save();
		
		try{
			git.checkout().setName("master").setCreateBranch(true).setForce(true).call();
		}catch(Throwable e){
			//no big deal, it already exists.
		}
		
		git.add().addFilepattern("*").call();

		//get progress monitor
		git.fetch().setThin(true).setProgressMonitor(new TextProgressMonitor(new OutputStreamWriter(System.out))).call();

		git.reset().setMode( ResetType.HARD ).setRef("origin/master").call();
		
		//clean
		StatusCommand command = git.status();
		Status status = command.call();
		Set<String> files = new HashSet<String>(); 
		files.addAll(status.getUntracked());
		files.addAll(status.getUntrackedFolders());
		files.addAll(status.getIgnoredNotInIndex());
		for (Iterator<String> iterator = files.iterator(); iterator.hasNext();) {
		    String string = iterator.next();
		    if (string.startsWith("data/.minecraft/assets"))
		        iterator.remove();
		    if (string.startsWith("data/.minecraft/saves"))
		        iterator.remove();
		    if (string.startsWith("data/.minecraft/journeymap/data"))
		        iterator.remove();
		    if (string.startsWith("data/.minecraft/hats"))
		        iterator.remove();
		    if (string.startsWith("data/.minecraft/asm"))
		        iterator.remove();
		    if (string.startsWith("TheVerseLauncher.jar"))
		        iterator.remove();
		    if (string.startsWith("data/.minecraft/launcher_profiles.json"))
		        iterator.remove();
		    if (string.startsWith("data/.minecraft/local"))
		        iterator.remove();
		}
		git.clean().setPaths(files).setIgnore(false).setCleanDirectories(true).call();
		
		git.pull().setProgressMonitor(new TextProgressMonitor(new OutputStreamWriter(System.out))).call();
		
		//reset one more time incase of deletes.
		git.reset().setMode( ResetType.HARD ).setRef("origin/master").call();
		
		git.close();
	}
    
}

