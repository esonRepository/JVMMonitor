package com.busi.jvm.agent.bootstrap;

import java.io.File;
import java.net.URL;

/**
 * Created by eson on 2017/11/17.
 */
public class BootstrapAgentEnvironment {

	private static File			baseDir;

	private static final String	OS_NAME	= System.getProperty("os.name");

	static {
		initLoadingInformation();
	}



	private static void initLoadingInformation() {
		String className = BootstrapAgentEnvironment.class.getName().replace('.', '/') + ".class";
		String simpleName = className.substring(className.lastIndexOf('/') + 1);
		URL classUrl = getResourceUrl(simpleName);
		if (classUrl == null) {
			return;
		}
		String classLocation = classUrl.toString();

		String baseName = classLocation.substring(0, classLocation.lastIndexOf(className) - 1);
		baseName = getWorkaroundPath(baseName);

		if (baseName.startsWith("jar:file:")) {

			int lastIndex = baseName.lastIndexOf('/');
			if (lastIndex == -1) {
				lastIndex = baseName.lastIndexOf('\\');
			}
			if (lastIndex == -1) {
				return;
			}
			baseName = baseName.substring("jar:file:".length(), lastIndex);
		} else {
			if (baseName.startsWith("file:")) {
				baseName = baseName.substring("file:".length());
			} else {
				return;
			}
		}

		baseDir = new File(baseName);

	}



	private static URL getResourceUrl(String simpleName) {
		try {
			return BootstrapAgentEnvironment.class.getResource(simpleName);
		} catch (Exception e) {
		}
		return null;
	}



	private static String getWorkaroundPath(String path) {
		StringBuilder builder = new StringBuilder();
		char c;
		for (int i = 0; i < path.length(); builder.append(c)) {
			c = path.charAt(i);
			if (c != '%') {
				i++;
			} else {
				try {
					c = unescape(path, i);
					i += 3;
					if ((c & 0x80) != 0) {
						switch (c >> '\004') {
						case 12:
						case 13:
							char c1 = unescape(path, i);
							i += 3;
							c = (char) ((c & 0x1F) << '\006' | c1 & 0x3F);
							break;
						case 14:
							char c2 = unescape(path, i);
							i += 3;
							char c3 = unescape(path, i);
							i += 3;
							c = (char) ((c & 0xF) << '\f' | (c2 & 0x3F) << '\006' | c3 & 0x3F);
							break;
						default:
							throw new IllegalArgumentException();
						}
					}
				} catch (NumberFormatException ex) {
					throw new IllegalArgumentException();
				}
			}
		}
		return builder.toString();
	}



	private static char unescape(String s, int i) {
		return (char) Integer.parseInt(s.substring(i + 1, i + 3), 16);
	}



	public static File getAgentBaseDir() {
		return baseDir;
	}



	public static boolean isLinux() {
		return OS_NAME.toLowerCase().startsWith("linux");
	}



	public static boolean isWindows() {
		return OS_NAME.toLowerCase().startsWith("win");
	}

}
