package com.superzanti.launcher;

import sk.tomsik68.mclauncher.api.common.ILaunchSettings;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

final class DefaultLaunchSettings implements ILaunchSettings {
    @Override
    public String getInitHeap() {
        return "1G";
    }

    @Override
    public String getHeap() {
        return "3G";
    }

    @Override
    public Map<String, String> getCustomParameters() {
        return null;
    }

    @Override
    public List<String> getJavaArguments() {
    	List<String> strings = Arrays.asList("-XX:+UseConcMarkSweepGC", "-XX:+CMSIncrementalMode", "-XX:-UseAdaptiveSizePolicy", "-XX:ParallelGCThreads=4");
    	return strings;
    }

    @Override
    public List<String> getCommandPrefix() {
        return null;
    }

    @Override
    public File getJavaLocation() {
        return null;
    }

    @Override
    public boolean isModifyAppletOptions() {
        return false;
    }
}
