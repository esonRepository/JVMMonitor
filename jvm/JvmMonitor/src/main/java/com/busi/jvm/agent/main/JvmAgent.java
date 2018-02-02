package com.busi.jvm.agent.main;

import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.busi.jvm.agent.util.AgentConstants;
import com.busi.monitor.TraceContext;
import org.apache.commons.lang.StringUtils;

import sun.management.VMManagement;
import sun.tools.attach.LinuxAttachProvider;
import sun.tools.attach.LinuxVirtualMachine;
import sun.tools.attach.WindowsAttachProvider;
import sun.tools.attach.WindowsVirtualMachine;

import com.busi.jvm.agent.container.CallTreeContainer;
import com.busi.jvm.agent.scheduler.StatisticsScheduler;
import com.busi.jvm.agent.transfer.NormalMethodTransformer;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

/**
 * Created by eson on 2017/11/16.
 */
public class JvmAgent {

	private final static String	OS_NAME	= System.getProperty("os.name");



	public static void agentmain(String agentArgs, Instrumentation inst) {
		NormalMethodTransformer transformer = new NormalMethodTransformer();
		inst.addTransformer(transformer);

		StatisticsScheduler.startStatistics();
	}



	public static void ini(String agentArgs) {
		VirtualMachine vm = null;
		try {

			parseAgentParams(agentArgs);

			RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
			Field jvm = runtime.getClass().getDeclaredField("jvm");
			jvm.setAccessible(true);
			VMManagement vmMgmt = (VMManagement) jvm.get(runtime);
			Method pIdMethod = vmMgmt.getClass().getDeclaredMethod("getProcessId", new Class[0]);
			pIdMethod.setAccessible(true);

			int processId = ((Integer) pIdMethod.invoke(vmMgmt, new Object[0])).intValue();

			if (OS_NAME.toLowerCase().startsWith("win")) {
				vm = WindowsVirtualMachine.attach(new VirtualMachineDescriptor(new WindowsAttachProvider(), String
						.valueOf(processId), "monitor" + processId));
			} else {
				vm = LinuxVirtualMachine.attach(new VirtualMachineDescriptor(new LinuxAttachProvider(), String
						.valueOf(processId), "monitor" + processId));
			}

			String agentJarFilePath = System.getProperty("agent.jar.file.path");
			vm.loadAgent(agentJarFilePath);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}



	private static void parseAgentParams(String agentArgs) {
		System.out.println("参数：" + agentArgs);
		if (StringUtils.isNotBlank(agentArgs)) {
			String[] paramPairs = agentArgs.split(",");
			for (String paramPair : paramPairs) {
				if (StringUtils.isNotBlank(paramPair)) {
					String[] param = paramPair.split("=");
					if (param != null && param.length == 2) {
						if (param[0].equals("pool")) {
							List<String> poolList = new ArrayList<>();
							String[] pool = param[1].split("/");

							for (int i = 0; i < pool.length; i++) {
								if (StringUtils.isNotBlank(pool[i])) {
									poolList.add(pool[i]);
								}
							}

							List<String> poolParms = new ArrayList<>();
							for (int i = 0; i < poolList.size(); i++) {
								StringBuilder strBuilder = new StringBuilder(100);
								for (int j = 0; j <= i; j++) {
									if (j > 0) {
										strBuilder.append("/");
									}
									strBuilder.append(poolList.get(j).trim());
								}
								poolParms.add(strBuilder.toString());
							}

							CallTreeContainer.setPool(param[1]);
							CallTreeContainer.setPoolList(poolParms);
						} else if (param[0].equals("msgProducerIpPort")) {
							CallTreeContainer.setKafkaMsgProducerIpPort(param[1]);
						} else if (param[0].equals("webApplicationName")) {
							TraceContext.setApplicationName(param[1]);
							CallTreeContainer.setWebFlag(AgentConstants.webApplicationFlag);
						}
					}
				}
			}
		}
	}

}