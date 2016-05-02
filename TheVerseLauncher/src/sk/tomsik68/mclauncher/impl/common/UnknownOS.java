package sk.tomsik68.mclauncher.impl.common;

import sk.tomsik68.mclauncher.api.common.IOperatingSystem;

import java.io.File;

final class UnknownOS implements IOperatingSystem {
    @SuppressWarnings("unused")
	private File workDir;

    @Override
    public String getDisplayName() {
        return "Unknown";
    }

    @Override
    public String getMinecraftName() {
        return "unknown";
    }

    @Override
    public boolean isCurrent() {
        // this is determined in OperatingSystems
        return false;
    }

    @Override
    public File getWorkingDirectory() {
    	return new File(System.getProperty("user.dir") + "/" + "data" + "/" + ".minecraft/");
    	/*
        if (workDir != null)
            return workDir;
        String userHome = System.getProperty("user.home");
        workDir = new File(userHome, ".minecraft");
        MCLauncherAPI.log.fine("Minecraft working directory: ".concat(workDir.getAbsolutePath()));
        return workDir;*/
    }

    @Override
    public String getArchitecture() {
        return System.getProperty("sun.arch.data.model");
    }

}
