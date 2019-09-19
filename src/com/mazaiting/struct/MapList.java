package com.mazaiting.struct;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * struct DexMapList {
	    u4  size;               // of entries in list
	    DexMapItem list[1];     // entries
	};
 * 定义位置：data区
 * 引用位置：header区	
 * @author mazaiting
 */
public class MapList {
	/**大小*/
	public int size;
	/**描述有size个MapItem*/
	public List<MapItem> mapItems = new ArrayList<>();
}
