package com.mazaiting.struct;

/**
 * Map条目
 * struct DexMapItem {
    u2 type;              // type code (see kDexType* above)
    u2 unused;
    u4 size;              // count of items of the indicated type
    u4 offset;            // file offset to the start of data
	};
 * @author mazaiting
 */
public class MapItem {
	/**类型*/
	public short type;
	/**对齐字节，无实际用处*/
	public short unuse;
	/**个数*/
	public int size;
	/**第一个元素针对文件初始位置的偏移量*/
	public int offset;
	
	/**
	 * 获取当前类属性所占字节数
	 * @return
	 */
	public static int getSize() {
		return 2 + 2 + 4 + 4;
	}

	@Override
	public String toString(){
		return "type:" + type + ",unuse:" + unuse + ",size:" + size + ",offset:" + offset;
	}
}
