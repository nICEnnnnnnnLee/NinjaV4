package com.mazaiting.struct;

/**
 * 注解
 * struct DexAnnotationsDirectoryItem {
	    u4  classAnnotationsOff;  /* offset to DexAnnotationSetItem
	    u4  fieldsSize;           /* count of DexFieldAnnotationsItem
	    u4  methodsSize;          /* count of DexMethodAnnotationsItem
	    u4  parametersSize;       /* count of DexParameterAnnotationsItem
	};
 * @author mazaiting
 *
 */
public class AnnotationsDirectoryItem {
	/**
	 * 类注解偏移量
	 */
	public int classAnnotationsOff;
	/**
	 * 字段数量
	 */
	public int fieldsSize;
	/**
	 * 方法数量
	 */
	public int methodsSize;
	/**
	 * 参数数量
	 */
	public int parametersSize;
}
