package com.superzanti.launcher.auth;

import java.io.File;

import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.impl.common.Platform;
import sk.tomsik68.mclauncher.impl.login.legacy.LegacyProfile;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDAuthProfile;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDLoginService;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDProfileIO;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDServiceAuthenticationException;

public class Authentication {
	
	public static File directory = Platform.getCurrentPlatform().getWorkingDirectory();
	static YDProfileIO io = new YDProfileIO(new File(Platform.getCurrentPlatform().getWorkingDirectory().getAbsolutePath()));
	public static IProfile[] profiles = null;
	static ISession currentSession = null;
	static int currentProfile = 0;
	
	public static IProfile[] getProfiles() {
		return profiles;
	}
	
	public static void loadProfiles() {
		try {
		    profiles = io.read();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	public static void saveProfiles(IProfile[] p) {
		try {
			io.write(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ISession sessionLogin(int p) throws Exception {
		ISession session = null;
		if (profiles.length > 0) {
			IProfile profile = profiles[p];
			YDLoginService loginService = new YDLoginService();
			loginService.load(Platform.getCurrentPlatform().getWorkingDirectory());	
			try {
				session = loginService.login(profile);
				System.out.println("[YGGDRASIL] Loggato con ID: *****" +session.getSessionID().substring(5));
				updateProfile(p, session);
			} catch (YDServiceAuthenticationException e) {
				e.printStackTrace();
				throw e;
			}
		}
		setCurrentSession(session);
		return session;
	}
	
	public static ISession legacyLogin(String username, String password) throws Exception {
		ISession session = null;
		YDLoginService loginService = new YDLoginService();
		LegacyProfile profile = new LegacyProfile(username, password);
		try {
			session = loginService.login(profile);
			System.out.println("[LEGACY] Logging in with ID: *****" +session.getSessionID().substring(5));
			if (profiles.length > 0) {
				updateProfile(0, session);
			}
		} catch (YDServiceAuthenticationException e) {
			e.printStackTrace();
			throw e;
		}
		setCurrentSession(session);
		return session;
	}
	
	public static void updateProfile(int p, ISession sess) {
		if (profiles != null) {
			IProfile profile = profiles[p];
			IProfile[] newprofiles = {profiles[p]};
			if (profile instanceof YDAuthProfile) {
				((YDAuthProfile) profiles[p]).setPassword(sess.getSessionID());
				((YDAuthProfile) profiles[p]).setName(sess.getUsername().toString());
				((YDAuthProfile) profiles[p]).setUUID(sess.getUUID().toString());
				//((YDAuthProfile) profiles[p]).
				saveProfiles(newprofiles);
			}
		}
	}
	
	public static ISession getCurrentSession() {
		return currentSession;
	}
	
	public static void setCurrentSession(ISession s) {
		currentSession = s;
	}
}