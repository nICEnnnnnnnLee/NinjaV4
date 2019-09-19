package com.mazaiting.struct;

import com.mazaiting.Util;

/**
 * class_data_item中引用，编码方法
 * struct encoded_method{
		uleb128 method_idx_diff;
		uleb128 access_flags;
		uleb128 code_off;
	};
 * 
 * @author mazaiting
 */
public class EncodeMethod {
	/**
	 * 前缀method_idx表示它的值是method_ids的一个index，后缀_diff表示它是另外一个
	 * method_idx的一个差值。就是相对于encoded_method数组里上一个元素的差值。
	 */
	public byte[] methodIdxDiff;
	/**
	 * 访问权限，如public,private,final等
	 */
	public byte[] accessFlags;
	/**
	 * 一个指向data区的偏移地址，目标是本method的代码实现，被指向的结构code_item
	 */
	public byte[] codeOff;
	
	@Override
	public String toString(){
		return "methodIdxDiff: " + Util.bytesToHexString(methodIdxDiff) + "," + 
				Util.bytesToHexString(Util.int2Byte(Util.decodeUleb128(methodIdxDiff)))
				+ ",accessFlags: " + Util.bytesToHexString(accessFlags) + "," 
				+ Util.bytesToHexString(Util.int2Byte(Util.decodeUleb128(accessFlags)))
				+ ",codeOff: "+ Util.bytesToHexString(codeOff) + "," + 
				Util.bytesToHexString(Util.int2Byte(Util.decodeUleb128(codeOff)));
	}

}
