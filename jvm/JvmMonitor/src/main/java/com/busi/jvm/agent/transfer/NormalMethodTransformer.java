package com.busi.jvm.agent.transfer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import com.busi.jvm.agent.asm.MethodChangeVisitor;
import com.busi.jvm.agent.filter.FilterConfig;
import com.busi.jvm.agent.transfer.builder.ClassNodeInfoDtoBuilder;
import com.busi.jvm.agent.transfer.dto.ClassNodeInfoDto;

/**
 * Created by eson on 2017/11/15.
 */
public class NormalMethodTransformer implements ClassFileTransformer {

	private static String	LINE_SPRATOR	= System.getProperty("line.separator");



	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

		if (StringUtils.isBlank(className)) {
			return null;
		}
		// 内部类不管
		if (className.contains("$")) {
			return null;
		}
		// javassist的包名是用点分割的，需要转换下
		if (className.indexOf("/") != -1) {
			className = className.replaceAll("/", ".");
		} else {
			return null;
		}

		boolean isJdbcPackage = false;
		boolean isFiltered = false;
		String concernStackFlag = "0";

		List<String> jdbcPackages = FilterConfig.getJdbcPackages();
		for (String jdbcPackage : jdbcPackages) {
			if (className.contains(jdbcPackage)) {
				isJdbcPackage = true;
				break;
			}
		}

		List<String> needTransferOptions = FilterConfig.getNeedTransferOptions();
		for (String needTransferOption : needTransferOptions) {
			if (className.contains(needTransferOption)) {
				concernStackFlag = "1";
				break;
			}
		}

		List<String> filterOptions = FilterConfig.getFilterOptions();

		for (String filterOption : filterOptions) {
			if (className.contains(filterOption)) {
				isFiltered = true;
				break;
			}
		}

		if (!isJdbcPackage) {
			if (concernStackFlag.equals("0")) {
				return null;
			}

			if (isFiltered) {
				return null;
			}
		}

		try {
			ClassReader classReader = new ClassReader(classfileBuffer);

			ClassNode classNode = new ClassNode();
			classReader.accept(classNode, 0);
			ClassNodeInfoDto classNodeInfoDto = ClassNodeInfoDtoBuilder.buildClassNodeInfoDto(classNode, isJdbcPackage,
					concernStackFlag);
			if (classNodeInfoDto == null) {
				return null;
			}

			ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
			MethodChangeVisitor methodChangeVisitor = new MethodChangeVisitor(Opcodes.ASM5, classWriter,
					classNodeInfoDto);
			classReader.accept(methodChangeVisitor, ClassReader.EXPAND_FRAMES);
			return classWriter.toByteArray();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}