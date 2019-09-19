package com.mazaiting.struct;

/**
 * field_ids区里存放的是dex文件引用的所有的field，本区元素格式是field_id_item
 * 
 * struct DexFieldId {
	    u2  classIdx;           // index into typeIds list for defining class
	    u2  typeIdx;            // index into typeIds for field type
	    u4  nameIdx;            // index into stringIds for field name
	};
 * 
 * @author mazaiting
 */
public class FieldIdsItem {
	/**
	 * field所属的class类型，class_idx的值时type_ids的一个index，指向所属的类
	 */
	public short classIdx;
	/**
	 * field的类型，值是type_ids的一个index
	 */
	public short typeIdx;
	/**
	 * field的名称，它的值是string_ids的一个index
	 */
	public int nameIdx;
	
	/**
	 * 当前区域所占字节大小
	 * @return
	 */
	public static int getSize() {
		return 2 + 2 + 4;
	}
	
	@Override
	public String toString() {
		return "classIdx: " + classIdx + ",typeIdx: " + typeIdx + ",nameIdx: " + nameIdx;
	}
}
