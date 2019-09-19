package com.mazaiting.struct;

/**
 * method_ids是索引区的最后一个条目，它索引了dex文件里的所有method.
 * method_ids的元素格式是method_id_item
 * 
 * struct DexMethodId {
	    u2  classIdx;           // index into typeIds list for defining class
	    u2  protoIdx;           // index into protoIds for method prototype
	    u4  nameIdx;            // index into stringIds for method name
	};
 * 
 * @author mazaiting
 */
public class MethodIdsItem {
	/**
	 * method所属的class类型，class_idx的值是type_ids的一个index，必须指向一个class类型
	 */
	public short classIdx;
	/**
	 * method的原型，指向proto_ids的一个index
	 */
	public short protoIdx;
	/**
	 * method的名称，值为string_ids的一个index
	 */
	public int nameIdx;
	
	public static int getSize() {
		return 2 + 2 + 4;
	}

	@Override
	public String toString(){
		return "classIdx: " + classIdx + ",protoIdx: " + protoIdx + ",nameIdx: " + nameIdx;
	}

}
