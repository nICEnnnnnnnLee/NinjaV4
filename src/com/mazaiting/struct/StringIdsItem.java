package com.mazaiting.struct;

import com.mazaiting.Util;

/**
 * 索引.dex文件所有的字符串
 * 
 * struct DexStringId {
    	u4 stringDataOff;      // file offset to string_data_item
	};
 * @author mazaiting
 */
public class StringIdsItem {
	/**字符串数据的偏移地址*/
	public int stringDataOff;
	
	/**
	 * 获取当前类属性所占字节大小
	 * @return
	 */
	public static int getSize() {
		return 4;
	}
	
	@Override
	public String toString() {
		return Util.bytesToHexString(Util.int2Byte(stringDataOff));
	}
}
