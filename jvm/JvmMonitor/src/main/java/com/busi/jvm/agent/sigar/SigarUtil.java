package com.busi.jvm.agent.sigar;

import java.io.File;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarLoader;

/**
 * Created by eson on 2017/12/1.
 */
public class SigarUtil {

	private static long			processId	= 0l;
	private final static String	OS_NAME		= System.getProperty("os.name");
	private final static Sigar	sigar		= initSigar();



	public static Sigar getSigar() {
		return sigar;
	}



	public static long getProcessId() {
		return processId;
	}



	private static boolean isWindows() {
		return OS_NAME.toLowerCase().startsWith("win");
	}



	private static boolean isLinux() {
		return OS_NAME.toLowerCase().startsWith("linux");
	}



	/**
	 * 类加载时初始化sigar
	 *
	 * @return
	 */
	private static Sigar initSigar() {
		try {
			String sigarFilePath = System.getProperty("sigar.file.path");
			final SigarLoader sigarLoader = new SigarLoader(Sigar.class);

			String sigarPath = sigarFilePath + File.separator + sigarLoader.getLibraryName();
			/** agent sigar只能写死dll文件路径. */
			System.load(sigarPath);

			/** Tell sigar loader that the library is already loaded. */
			System.setProperty("org.hyperic.sigar.path", "-");
			sigarLoader.load();

			Sigar sigar = new Sigar();
			processId = sigar.getPid();

			return sigar;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
