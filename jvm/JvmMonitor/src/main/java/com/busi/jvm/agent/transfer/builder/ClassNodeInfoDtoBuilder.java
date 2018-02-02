package com.busi.jvm.agent.transfer.builder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import com.busi.jvm.agent.transfer.dto.ClassNodeInfoDto;

public class ClassNodeInfoDtoBuilder {

	public static ClassNodeInfoDto buildClassNodeInfoDto(ClassNode classNode, boolean isJdbcPackage,
			String isConcernStack) {

		if (Opcodes.ACC_PRIVATE == classNode.access || Opcodes.ACC_ENUM == classNode.access
				|| Opcodes.ACC_INTERFACE == classNode.access || Opcodes.ACC_NATIVE == classNode.access
				|| Opcodes.F_FULL == classNode.access || Opcodes.ACC_ANNOTATION == classNode.access) {
			return null;
		}

		ClassNodeInfoDto result = new ClassNodeInfoDto();
		result.setConcernStackFlag(isConcernStack);
		result.setJdbcPackage(isJdbcPackage);
		result.setJdbc(buildJdbcInfo(classNode));
		result.setClassName(classNode.name.replaceAll("/", "."));

		Map<String, String> notNeedTransferMethodMap = buildNotNeedTransferMethodMap(classNode);
		result.setNotNeedTransferMethodMap(notNeedTransferMethodMap);

		List<AnnotationNode> visibleAnnotations = classNode.visibleAnnotations;
		result.setRequestMappingMap(buildRequestMappingMap(classNode, notNeedTransferMethodMap, visibleAnnotations));
		return result;
	}



	private static boolean buildJdbcInfo(ClassNode classNode) {

		List<String> interfaces = classNode.interfaces;
		if (interfaces == null || interfaces.isEmpty()) {
			return false;
		}
		List<FieldNode> fields = classNode.fields;
		if (fields == null || fields.isEmpty()) {
			return false;
		}

		boolean hasJbdcInterface = false;
		for (String interfaceChars : interfaces) {
			if ("java/sql/PreparedStatement".equals(String.valueOf(interfaceChars))) {
				hasJbdcInterface = true;
			}
		}
		boolean hasSql = false;
		for (FieldNode field : fields) {
			String fieldName = field.name;
			if ("sql".equals(fieldName)) {
				hasSql = true;
			}
		}

		return hasJbdcInterface && hasSql;

	}



	private static Map<String, String> buildRequestMappingMap(ClassNode classNode,
			Map<String, String> notNeedTransferMethodMap, List<AnnotationNode> visibleAnnotations) {

		Map<String, String> requestMappingMap = new HashMap<>();

		boolean isController = false;
		if (visibleAnnotations != null) {
			for (AnnotationNode annotationNode : visibleAnnotations) {
				if (annotationNode.desc.equals("Lorg/springframework/stereotype/Controller;")
						|| annotationNode.desc.equals("Lorg/springframework/web/bind/annotation/RestController;")) {
					isController = true;
					break;
				}
			}
		}

		if (isController) {

			String classLevelPath = "";
			for (AnnotationNode annotationNode : visibleAnnotations) {
				if (annotationNode.desc.equals("Lorg/springframework/web/bind/annotation/RequestMapping;")
						|| annotationNode.desc.equals("Lorg/springframework/web/bind/annotation/PostMapping;")) {
					classLevelPath = ((List<String>) (annotationNode.values.get(1))).get(0);
					break;
				}
			}

			StringBuilder pathBuilder = new StringBuilder(40);
			List<MethodNode> methods = classNode.methods;
			for (MethodNode method : methods) {
				if (notNeedTransferMethodMap.get(method.name) == null) {
					List<AnnotationNode> methodAnnotations = method.visibleAnnotations;
					if (methodAnnotations != null && !methodAnnotations.isEmpty()) {
						for (AnnotationNode methodAnnotation : methodAnnotations) {
							if (methodAnnotation.desc
									.equals("Lorg/springframework/web/bind/annotation/RequestMapping;")
									|| methodAnnotation.desc
											.equals("Lorg/springframework/web/bind/annotation/PostMapping;")) {
								pathBuilder.delete(0, pathBuilder.length());
								pathBuilder.append(classLevelPath).append(
										((List<String>) (methodAnnotation.values.get(1))).get(0));
								requestMappingMap.put(method.name, pathBuilder.toString());
								break;
							}
						}
					}
				}
			}
		}
		return requestMappingMap;
	}



	private static Map<String, String> buildNotNeedTransferMethodMap(ClassNode classNode) {
		Map<String, String> notNeedTransferMethodMap = new HashMap<>();

		List<FieldNode> fields = classNode.fields;

		Map<String, String> fieldNameMap = new HashMap<>();
		if (fields != null && !fields.isEmpty()) {
			for (FieldNode field : fields) {
				String fieldName = field.name;
				fieldNameMap.put(fieldName, fieldName);
			}
		}

		List<MethodNode> methods = classNode.methods;
		if (methods != null && !methods.isEmpty()) {
			for (MethodNode method : methods) {
				if (method.name.startsWith("set") || method.name.startsWith("get")) {
					if (method.name.length() >= 4) {
						String otherStr = method.name.substring(3);
						String firstLetter = otherStr.substring(0, 1);
						String otherLetters = otherStr.substring(1);
						String nameInMethod = firstLetter.toLowerCase() + otherLetters;
						if (fieldNameMap.get(nameInMethod) != null) {
							notNeedTransferMethodMap.put(method.name, method.name);
						}
					}
				}
			}
		}

		notNeedTransferMethodMap.put("toString", "toString");
		notNeedTransferMethodMap.put("equals", "equals");
		notNeedTransferMethodMap.put("hashCode", "hashCode");
		notNeedTransferMethodMap.put("<init>", "<init>");
		notNeedTransferMethodMap.put("<clinit>", "<clinit>");
		notNeedTransferMethodMap.put("init", "init");

		return notNeedTransferMethodMap;
	}
}