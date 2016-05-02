package sk.tomsik68.mclauncher.impl.common;

import sk.tomsik68.mclauncher.api.common.IOperatingSystem;

import java.io.File;

final class MacintoshOS implements IOperatingSystem {
    @SuppressWarnings("unused")
	private File workDir;

    @Override
    public String getDisplayName() {
        return "MAC OS";
    }

    @Override
    public String getMinecraftName() {
        return "osx";
    }

    @Override
    public boolean isCurrent() {
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }

    @Override
    public File getWorkingDirectory() {
    	return new File(System.getProperty("user.dir") + "/" + "data" + "/" + ".minecraft/");
    	/*
        if (workDir != null)
            return workDir;
        workDir = new File(System.getProperty("user.home"), "Library/Application Support/.minecraft");
        MCLauncherAPI.log.fine("Minecraft working directory: ".concat(workDir.getAbsolutePath()));
        return workDir;*/
    }

    @Override
    public String getArchitecture() {
        return System.getProperty("sun.arch.data.model");
    }

}
