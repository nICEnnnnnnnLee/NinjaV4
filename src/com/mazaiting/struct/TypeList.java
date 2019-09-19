package com.mazaiting.struct;

import java.util.ArrayList;
import java.util.List;

/**
 * proto_ids的参数
 * 
 * struct DexTypeList {
	    u4  size;               // #of entries in list
	    DexTypeItem list[1];    // entries
	};
 * 
 * @author mazaiting
 */
public class TypeList {
	/**参数的个数*/
	public int size;
	/**参数的类型*/
	public List<Short> typeIdx = new ArrayList<>();
}
