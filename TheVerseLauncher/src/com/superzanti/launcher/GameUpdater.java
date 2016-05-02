package com.superzanti.launcher;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.ResetCommand.ResetType;
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
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class GameUpdater extends Thread {
	
	public boolean gitFailed = false;

    public GameUpdater() {
    }

    public void run() {
    	String localPath, remotePath;
	    
	    localPath = ".";
        remotePath = "https://github.com/superzanti/TheVerse.git";
        
        try {
			doPull(remotePath, localPath);
		} catch (Throwable e) {
			gitFailed = true;
		}
    }
    
    private static void doPull(String fromRepoPath, String toRepoPath) throws WrongRepositoryStateException, InvalidConfigurationException, DetachedHeadException, InvalidRemoteException, CanceledException, RefNotFoundException, RefNotAdvertisedException, NoHeadException, TransportException, GitAPIException, IOException{
		
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = builder.setGitDir(new File(toRepoPath + "/.git"))
		  .readEnvironment()
		  .findGitDir()
		  .build();
		
		Git git = new Git(repository);
		
		File gitrepo = new File( toRepoPath );
		Git.init().setDirectory(gitrepo).call();
					
		StoredConfig config = git.getRepository().getConfig();
		config.setString("core", "origin", "sparsecheckout", "true");
		config.setString("remote", "origin", "fetch", "+refs/heads/*:refs/remotes/origin/*");
		config.setString("remote", "origin", "url", fromRepoPath);
		config.setString("branch", "master", "remote", "origin");
		config.setString("branch", "master", "merge", "refs/heads/master");
		
		config.save();

		git.add().addFilepattern(".").call();
		git.fetch().call();
		git.reset().setMode( ResetType.HARD ).call();
		git.clean();
		
		PullCommand a = git.pull();
		a.call();
		git.close();
	}
    
}

