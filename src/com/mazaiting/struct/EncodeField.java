package com.mazaiting.struct;

import com.mazaiting.Util;

/**
 * class_data_item 中引用，编码字段
 * struct encoded_field{
		uleb128 filed_idx_diff; // index into filed_ids for ID of this filed
		uleb128 access_flags; // access flags like public, static etc.
	};
 * 
 * @author mazaiting
 *
 */
public class EncodeField {
	/**
	 * 前缀field_idx表示它的值是field_ids的一个index，后缀_diff表示它是另外一个
	 * field_idx的一个差值。就是相对于encoded_field数组里上一个元素的差值。
	 */
	public byte[] fieldIdxDiff;
	/**
	 * 访问权限，如public,private,final等
	 */
	public byte[] accessFlags;
	
	@Override
	public String toString(){
		return "fieldIdxDiff: " + Util.bytesToHexString(fieldIdxDiff) 
		+ ",accessFlags:" + Util.bytesToHexString(accessFlags);
	}

}
