package com.mazaiting.struct;

import com.mazaiting.Util;

/**
 * 代码条目
 * 
 * 	(1) 一个 .dex 文件被分成了 9 个区 ，其中有一个索引区叫做class_defs ， 索引了 .dex 里面用到的 class ，以及对这个 class 的描述 。
 *	(2) class_defs 区 ， 里面其实是class_def_item 结构 。这个结构里描述了 LHello; 的各种信息 ，诸如名称 ，superclass , access flag，
 *		 interface 等 。class_def_item 里有一个元素 class_data_off , 指向data 区里的一个 class_data_item 结构 ，
 *		 用来描述 class 使用到的各种数据 。自此以后的结构都归于 data区了 。
 *	(3) class_data_item 结构 ，里描述值着 class 里使用到的 static field , instance field , direct_method ，和 virtual_method 
 *		的数目和描述 。例子 Hello.dex 里 ，只有 2 个 direct_method , 其余的 field 和method 的数目都为 0 。描述 direct_method 的结构叫
 *		做 encoded_method ，是用来详细描述某个 method的 。
 *	(4) encoded_method 结构 ，描述某个 method 的 method 类型 ， access flags 和一个指向 code_item的偏移地址 ，里面存放的是该 method 
 *		的具体实现 。
 *	(5) code_item ，结构里描述着某个 method 的具体实现 。
 * 
 * struct DexCode {
	    u2  registersSize;
	    u2  insSize;
	    u2  outsSize;
	    u2  triesSize;
	    u4  debugInfoOff;       /* file offset to debug info stream
	    u4  insnsSize;          /* size of the insns array, in u2 units
	    u2  insns[1];
	    /* followed by optional u2 padding
	    /* followed by try_item[triesSize]
	    /* followed by uleb128 handlersSize
	    /* followed by catch_handler_item[handlersSize]
	};
 * 
 * @author mazaiting
 *
 */
public class CodeItem {
	/**
	 * 本段代码使用到的寄存器数目
	 */
	public short registersSize;
	/**
	 * method传入参数的数目
	 */
	public short insSize;
	/**
	 * 本段代码调用其他方法时需要的参数个数
	 */
	public short outsSize;
	/**
	 * try_item结构的个数
	 */
	public short triesSize;
	/**
	 * 偏移地址，指向本段代码的debug信息存放位置，是一个debug_info_item结构
	 */
	public int debugInfoOff;
	/**
	 * 指令列表的大小，以16-bit为单位。insns是instructions的缩写
	 */
	public int insnsSize;
	/**
	 * insns数组
	 */
	public short[] insns;
	
	/**tries和handlers用于处理java中的exception*/
	
	@Override
	public String toString(){
		return "registersSize: " + registersSize + ",insSize: " + insSize + ",outsSize: " + outsSize + 
				",triesSize: " + triesSize + ",debugInfoOff: " + debugInfoOff + ",insnsSize: " + insnsSize 
				+ "\ninsns: " + getInsnsStr();
	}
	
	private String getInsnsStr(){
		StringBuilder sb = new StringBuilder();
		if (insns != null && insns.length > 0) {
			for(int i=0;i<insns.length;i++){
				sb.append(Util.bytesToHexString(Util.short2Byte(insns[i]))+",");
			}	
		}
		
		return sb.toString();
	}
	

}
