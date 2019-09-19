package com.mazaiting.struct;

import com.mazaiting.Util;

/**
 * DEX 头部信息类型
 * 主要分为两部分：
 * 	1). 魔数 + 签名 + 文件大小等信息
 * 	2). 后面的各个数据结构的大小和偏移值，成对出现
 * 
 * struct DexHeader {
	    u1  magic[8];           // includes version number
	    u4  checksum;           // adler32 checksum 
	    u1  signature[kSHA1DigestLen]; // SHA-1 hash
	    u4  fileSize;           // length of entire file
	    u4  headerSize;         // offset to start of next section
	    u4  endianTag;
	    u4  linkSize;
	    u4  linkOff;
	    u4  mapOff;
	    u4  stringIdsSize;
	    u4  stringIdsOff;
	    u4  typeIdsSize;
	    u4  typeIdsOff;
	    u4  protoIdsSize;
	    u4  protoIdsOff;
	    u4  fieldIdsSize;
	    u4  fieldIdsOff;
	    u4  methodIdsSize;
	    u4  methodIdsOff;
	    u4  classDefsSize;
	    u4  classDefsOff;
	    u4  dataSize;
	    u4  dataOff;
	};
 * 
 * @author mazaiting
 */
public class HeaderType {
	/**8个字节，一般是常量，为使.dex文件能够被识别出来，必须出现在.dex文件的最开头位置
	 *	数组的值一般可以转换为一个字符串： { 0x64 0x65 0x78 0x0a 0x30 0x33 0x35 0x00 } = "dex\n035\0"
	 *	中间是一个 ‘\n' 符号 ，后面 035 是 Dex 文件格式的版本 。
	 */
	public byte[] magic = new byte[8];
	/**文件校验码，使用alder32算法校验文件出去magic，checknum外余下所有文件区域用于检查文件错误*/
	public int checkSum;
	/**采用SHA-1算法hash出去magic,checknum和signature外余下所有的文件区域，用于唯一识别本文件*/
	public byte[] signAture = new byte[20];
	/**DEX文件的大小*/
	public int fileSize;
	/**header区域的大小，单位Byte，一般固定为0x70常量*/
	public int headerSize;
	/**大小端标签，标准.dex文件为小端，此项一般固定为0x12345678常量*/
	public int endianTag;
	/**链接数据的大小*/
	public int linkSize;
	/**链接数据的偏移值*/
	public int linkOff;
	/**map item的偏移地址，该item属于data区里的内容，值要大于等于dataOff的大小*/
	public int mapOff;
	/**DEX中用到的所有字符串内容的大小*/
	public int stringIdsSize;
	/**DEX中用到的所有字符串内容的偏移量*/
	public int stringIdsOff;
	/**DEX中类型数据结构的大小*/
	public int typeIdsSize;
	/**DEX中类型数据结构的偏移值*/
	public int typeIdsOff;
	/**DEX中的元数据信息数据结构的大小*/
	public int protoIdsSize;
	/**DEX中的元数据信息数据结构的偏移值*/
	public int protoIdsOff;
	/**DEX中字段信息数据结构的大小*/
	public int fieldIdsSize;
	/**DEX中字段信息数据结构的偏移值*/
	public int fieldIdsOff;
	/**DEX中方法信息数据结构的大小*/
	public int methodIdsSize;
	/**DEX中方法信息数据结构的偏移值*/
	public int methodIdsOff;
	/**DEX中的类信息数据结构的大小*/
	public int classDefsSize;
	/**DEX中的类信息数据结构的偏移值*/
	public int classDefsOff;
	/**DEX中数据区域的结构信息的大小*/
	public int dataSize;
	/**DEX中数据区域的结构信息的偏移值*/
	public int dataOff;
	
	@Override
	public String toString(){
		return "magic:"+Util.bytesToHexString(magic)+"\n"
				+ "checksum:"+checkSum + "\n"
				+ "siganature:"+Util.bytesToHexString(signAture) + "\n"
				+ "file_size:"+fileSize + "\n"
				+ "header_size:"+headerSize + "\n"
				+ "endian_tag:"+endianTag + "\n"
				+ "link_size:"+linkSize + "\n"
				+ "link_off:"+Util.bytesToHexString(Util.int2Byte(linkOff)) + "\n"
				+ "map_off:"+Util.bytesToHexString(Util.int2Byte(mapOff)) + "\n"
				+ "string_ids_size:"+stringIdsSize + "\n"
				+ "string_ids_off:"+Util.bytesToHexString(Util.int2Byte(stringIdsOff)) + "\n"
				+ "type_ids_size:"+typeIdsSize + "\n"
				+ "type_ids_off:"+Util.bytesToHexString(Util.int2Byte(typeIdsOff)) + "\n"
				+ "proto_ids_size:"+protoIdsSize + "\n"
				+ "proto_ids_off:"+Util.bytesToHexString(Util.int2Byte(protoIdsOff)) + "\n"
				+ "field_ids_size:"+fieldIdsSize + "\n"
				+ "field_ids_off:"+Util.bytesToHexString(Util.int2Byte(fieldIdsOff)) + "\n"
				+ "method_ids_size:"+methodIdsSize + "\n"
				+ "method_ids_off:"+Util.bytesToHexString(Util.int2Byte(methodIdsOff)) + "\n"
				+ "class_defs_size:"+classDefsSize + "\n"
				+ "class_defs_off:"+Util.bytesToHexString(Util.int2Byte(classDefsOff)) + "\n"
				+ "data_size:"+dataSize + "\n"
				+ "data_off:"+Util.bytesToHexString(Util.int2Byte(dataOff));
	}
}
