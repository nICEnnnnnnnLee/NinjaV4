package com.mazaiting.struct;

import java.util.ArrayList;
import java.util.List;

/**
 * proto 的意思是 method prototype 代表 java 语言里的一个 method 的原型 。proto_ids 里的元素为 proto_id_item ,
 * 
 * struct DexProtoId {
	    u4  shortyIdx;          // index into stringIds for shorty descriptor
	    u4  returnTypeIdx;      // index into typeIds list for return type 
	    u4  parametersOff;      // file offset to type_list for parameter types
	};
 *
 * @author mazaiting
 */
public class ProtoIdsItem {
	/**值为一个string_ids的index号，用来说明该method原型*/
	public int shortyIdx;
	/**值为一个type_ids的index，表示该method原型的返回值类型*/
	public int returnTypeIdx;
	/**指定method原型的参数列表type_list，若method没有参数，则值为0. 参数的格式是type_list*/
	public int parametersOff;
	
	// 这个不是公共字段，而是为了存储方法原型中的参数类型名和参数个数
	public List<String> paramtersList = new ArrayList<>();
	public int paramterCount;
	
	/**
	 * 获取当前类字节数
	 * @return
	 */
	public static int getSize() {
		return 4 + 4 + 4;
	}
	
	@Override
	public String toString() {
		return "shortyIdx:"+shortyIdx+",returnTypeIdx:"+returnTypeIdx+",parametersOff:"+parametersOff;
	}
}
