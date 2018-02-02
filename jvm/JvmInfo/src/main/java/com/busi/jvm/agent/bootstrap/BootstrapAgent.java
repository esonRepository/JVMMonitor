package com.busi.jvm.agent.bootstrap;

import java.lang.instrument.Instrumentation;

import com.busi.jvm.agent.main.JvmAgent;

/**
 * Created by eson on 2017/11/17.
 */
public class BootstrapAgent {

	public static void premain(String agentArgs, Instrumentation inst) {
		init(agentArgs, inst);
	}



	public static void main(String[] args) {

	}



	private static void init(String agentArgs, Instrumentation inst) {

		try {
			AgentLocation agentLocation = new AgentLocation();
			if (agentLocation.getAgentJarFile() != null) {
				inst.appendToBootstrapClassLoaderSearch(agentLocation.getAgentJarFile());
				inst.appendToBootstrapClassLoaderSearch(agentLocation.getAttachToolsJarFile());
				inst.appendToBootstrapClassLoaderSearch(agentLocation.getAttachProviderJarFile());

				JvmAgent.ini(agentArgs);
			} else {
				System.err.println("jar file not found in " + agentLocation.getAgentJarFile());
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
