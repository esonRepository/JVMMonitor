package com.busi.jvm.agent.bootstrap;

import java.io.File;
import java.lang.reflect.Field;
import java.util.jar.JarFile;

/**
 * Created by eson on 2017/11/17.
 */
public class AgentLocation {
	private File	bootstrapBaseDir;

	private JarFile	agentJarFile;
	private String	agentJarFilePath;
	private JarFile	attachToolsJarFile;
	private JarFile	attachProviderJarFile;



	public AgentLocation() {

		this.bootstrapBaseDir = BootstrapAgentEnvironment.getAgentBaseDir();
		File agentDir = new File(bootstrapBaseDir, "lib");
		File attchFileDir = new File(agentDir, "attach");
		File sigarFileDir = new File(agentDir, "sigar");

		try {
			String sigarFilePath = sigarFileDir.getCanonicalPath();
			System.setProperty("sigar.file.path", sigarFilePath);

			File agentFile = new File(agentDir, "jvm-agent-1.0.0-jar-with-dependencies.jar");
			this.agentJarFile = new JarFile(agentFile);
			this.agentJarFilePath = agentFile.getAbsolutePath();

			System.setProperty("agent.jar.file.path", agentJarFilePath);

			this.attachToolsJarFile = new JarFile(new File(attchFileDir, "attachTools.jar"));
			if (BootstrapAgentEnvironment.isWindows()) {
				this.attachProviderJarFile = new JarFile(new File(attchFileDir, "attachProviderWindows.jar"));
			} else if (BootstrapAgentEnvironment.isLinux()) {
				this.attachProviderJarFile = new JarFile(new File(attchFileDir, "attachProviderLinux.jar"));
			}
		} catch (Exception e) {
			throw new RuntimeException("找不到agent.jar", e);
		}
	}



	public File getBootstrapBaseDir() {
		return this.bootstrapBaseDir;
	}



	public JarFile getAgentJarFile() {
		return agentJarFile;
	}



	public JarFile getAttachToolsJarFile() {
		return this.attachToolsJarFile;
	}



	public JarFile getAttachProviderJarFile() {
		return this.attachProviderJarFile;
	}



	public String getAgentJarFilePath() {
		return this.agentJarFilePath;
	}

}
