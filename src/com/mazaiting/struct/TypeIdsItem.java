package com.mazaiting.struct;

/**
 * DEX所有类型，基本类型等信息
 * type_ids 区索引了 dex 文件里的所有数据类型 ，包括 class 类型 ，数组类型（array types）和基本类型(primitive types)
 * 
 * struct DexTypeId {
    u4  descriptorIdx;      // index into stringIds list for type descriptor 
	};
 * @author mazaiting
 */
public class TypeIdsItem {
	/**
	 * ID索引
	 */
	public int descriptorIdx;
	
	/**
	 * 获取所属
	 * @return
	 */
	public static int getSize() {
		return 4;
	}
}
