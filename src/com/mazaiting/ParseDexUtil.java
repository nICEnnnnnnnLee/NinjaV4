package com.mazaiting;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mazaiting.struct.ClassDataItem;
import com.mazaiting.struct.ClassDefItem;
import com.mazaiting.struct.CodeItem;
import com.mazaiting.struct.EncodeField;
import com.mazaiting.struct.EncodeMethod;
import com.mazaiting.struct.FieldIdsItem;
import com.mazaiting.struct.HeaderType;
import com.mazaiting.struct.MapItem;
import com.mazaiting.struct.MapList;
import com.mazaiting.struct.MethodIdsItem;
import com.mazaiting.struct.ProtoIdsItem;
import com.mazaiting.struct.StringIdsItem;
import com.mazaiting.struct.TypeIdsItem;

public class ParseDexUtil {
	/**头部数据*/
	private static HeaderType headerType = new HeaderType();
	/**字符串Id条目列表*/
	public static List<StringIdsItem> stringidsList = new ArrayList<>();
	/**字符串数据列表*/
	public static List<String> stringList = new ArrayList<>();
	/**类型Id条目列表*/
	public static List<TypeIdsItem> typeIdsList = new ArrayList<>();
	/**proto条目列表*/
	private static List<ProtoIdsItem> protoIdsList = new ArrayList<>();
	/**field条目列表*/
	private static List<FieldIdsItem> fieldIdsList = new ArrayList<>();
	/**method条目列表*/
	private static List<MethodIdsItem> methodIdsList = new ArrayList<>();
	/**class条目列表*/
	private static List<ClassDefItem> classIdsList = new ArrayList<>();
	/**class Data列表*/
	private static List<ClassDataItem> dataItemList = new ArrayList<>();
	/**Direct Method列表*/
	private static List<CodeItem> directMethodCodeItemList = new ArrayList<CodeItem>();
	/**虚拟 Method列表*/
	private static List<CodeItem> virtualMethodCodeItemList = new ArrayList<CodeItem>();
	
	/**这里的map用来存储code数据，因为一个ClassCode都是以class_idx为单位的，所以这里的key就是classname来存储*/
	private static Map<String, ClassDefItem> classDataMap = new HashMap<String, ClassDefItem>();
	
	/**
	 * 解析DEX头部
	 * 	1. 解析magic魔数
	 * 	2. 解析checksum
	 * 	3. 解析signature
	 * 	4. 解析file_size
	 * 	5. 解析header_size
	 * 	6. 解析endian_tag
	 * 	7. 解析link_size
	 * 	8. 解析link_off
	 * 	9. 解析map_off
	 * 	10. 解析string_ids_size
	 * 	11. 解析string_ids_off
	 * 	12. 解析type_ids_size
	 * 	13. 解析type_ids_off
	 * 	14. 解析proto_ids_size
	 * 	15. 解析proto_ids_off
	 * 	16. 解析field_ids_size
	 * 	17. 解析field_ids_off
	 * 	18. 解析method_ids_size
	 * 	19. 解析method_ids_off
	 * 	20. 解析class_defs_size
	 * 	21. 解析class_defs_off
	 * 	22. 解析data_size
	 * 	23. 解析data_off
	 * @param dexBytes 二进制数据
	 */
	public static void parseDexHeader(byte[] dexBytes) {
		// 1. 解析magic魔数--8个字节
		byte[] magicByte = Util.copyByte(dexBytes, 0, 8);
		headerType.magic = magicByte;
		
		// 2. 解析checksum
		byte[] checkSumByte = Util.copyByte(dexBytes, 8, 4);
		headerType.checkSum = Util.byte2int(checkSumByte);
		
		// 3. 解析signature
		byte[] signAtureByte = Util.copyByte(dexBytes, 12, 20);
		headerType.signAture = signAtureByte;
		
		// 4. 解析file_size
		byte[] fileSizeByte = Util.copyByte(dexBytes, 32, 4);
		headerType.fileSize = Util.byte2int(fileSizeByte);
		
		// 5. 解析header_size
		byte[] headerSizeByte = Util.copyByte(dexBytes, 36, 4);
		headerType.headerSize = Util.byte2int(headerSizeByte);
		
		// 6. 解析endian_tag
		byte[] endianTagByte = Util.copyByte(dexBytes, 40, 4);
		headerType.endianTag = Util.byte2int(endianTagByte);
		
		// 7. 解析link_size
		byte[] linkSizeByte = Util.copyByte(dexBytes, 44, 4);
		headerType.linkSize = Util.byte2int(linkSizeByte);
		
		// 8. 解析link_off
		byte[] linkOffByte = Util.copyByte(dexBytes, 48, 4);
		headerType.linkOff = Util.byte2int(linkOffByte);
		
		// 9. 解析map_off
		byte[] mapOffByte = Util.copyByte(dexBytes, 52, 4);
		headerType.mapOff = Util.byte2int(mapOffByte);
		
		// 10. 解析string_ids_size
		byte[] stringIdsSizeByte = Util.copyByte(dexBytes, 56, 4);
		headerType.stringIdsSize = Util.byte2int(stringIdsSizeByte);
		
		// 11. 解析string_ids_off
		byte[] stringIdsOffByte = Util.copyByte(dexBytes, 60, 4);
		headerType.stringIdsOff = Util.byte2int(stringIdsOffByte);
		
		// 12. 解析type_ids_size
		byte[] typeIdsSizeByte = Util.copyByte(dexBytes, 64, 4);
		headerType.typeIdsSize = Util.byte2int(typeIdsSizeByte);
		
		// 13. 解析type_ids_off
		byte[] typeIdsOffByte = Util.copyByte(dexBytes, 68, 4);
		headerType.typeIdsOff = Util.byte2int(typeIdsOffByte); 
		
		// 14. 解析proto_ids_size
		byte[] protoIdsSizeByte = Util.copyByte(dexBytes, 72, 4);
		headerType.protoIdsSize = Util.byte2int(protoIdsSizeByte); 
		
		
		// 15. 解析proto_ids_off
		byte[] protoIdsOffByte = Util.copyByte(dexBytes, 76, 4);
		headerType.protoIdsOff = Util.byte2int(protoIdsOffByte);
		
		// 16. 解析field_ids_size
		byte[] fieldIdsSizeByte = Util.copyByte(dexBytes, 80, 4);
		headerType.fieldIdsSize = Util.byte2int(fieldIdsSizeByte);
		
		// 17. 解析field_ids_off
		byte[] fieldIdsOffByte = Util.copyByte(dexBytes, 84, 4);
		headerType.fieldIdsOff = Util.byte2int(fieldIdsOffByte);
		
		// 18. 解析method_ids_size
		byte[] methodIdsSizeByte = Util.copyByte(dexBytes, 88, 4);
		headerType.methodIdsSize = Util.byte2int(methodIdsSizeByte);
		
		// 19. 解析method_ids_off
		byte[] methodIdsOffByte = Util.copyByte(dexBytes, 92, 4);
		headerType.methodIdsOff = Util.byte2int(methodIdsOffByte);
		
		// 20. 解析class_defs_size
		byte[] classDefsSizeByte = Util.copyByte(dexBytes, 96, 4);
		headerType.classDefsSize = Util.byte2int(classDefsSizeByte);
		
		// 21. 解析class_defs_off
		byte[] classDefsOffByte = Util.copyByte(dexBytes, 100, 4);
		headerType.classDefsOff = Util.byte2int(classDefsOffByte);
		
		// 22. 解析data_size
		byte[] dataSizeByte = Util.copyByte(dexBytes, 104, 4);
		headerType.dataSize = Util.byte2int(dataSizeByte);
		
		// 23. 解析data_off
		byte[] dataOffByte = Util.copyByte(dexBytes, 108, 4);
		headerType.dataOff = Util.byte2int(dataOffByte);
		
		//System.out.println(headerType.toString());		
	}

	/**
	 * 解析字符串区域
	 * @param dexBytes 二进制数据
	 */
	public static void parseStringIds(byte[] dexBytes) {
		// 获取每条数据所占字节大小
		int idSize = StringIdsItem.getSize();
		// 获取偏移量
		int offset = headerType.stringIdsOff;
		// 获取字符串Id大小
		int countIds = headerType.stringIdsSize;
		for (int i = 0; i < countIds; i++) {
			//System.out.println(parseStringIdsItem(Util.copyByte(dexBytes, offset + i * idSize, idSize)));
			// 解析数据并添加到字符串列表
			stringidsList.add(parseStringIdsItem(Util.copyByte(dexBytes, offset + i * idSize, idSize)));
		}
		// 打印大小
//		System.out.println("string size: " + stringidsList.size());
	}

	/**
	 * 解析字符串列表
	 * @param dexBytes 二进制数据
	 */
	public static void parseStringList(byte[] dexBytes) {
		// 第一个字节是字符串长度
		for (StringIdsItem item : stringidsList) {
			// 获取字符串内容
			String text = getString(dexBytes, item.stringDataOff);
			//System.out.println(text);
			// 添加到字符串列表
			stringList.add(text);
		}
	}

	/**
	 * 解析type_ids索引区
	 * @param dexBytes 二进制数据
	 */
	public static void parseTypeIds(byte[] dexBytes) {
		// 获取每条数据所占字节大小
		int idSize = TypeIdsItem.getSize();
		// 获取偏移量
		int offset = headerType.typeIdsOff;
		// 获取字符串Id大小
		int countIds = headerType.typeIdsSize;
		// 遍历
		for (int i = 0; i < countIds; i++) {
			typeIdsList.add(parseTypeIdsItem(Util.copyByte(dexBytes, offset + i * idSize, idSize)));
		}
		
		// 这里的descriptor_idx就是解析之后的字符串中的索引值
//		for (TypeIdsItem item : typeIdsList) {
//			//System.out.println("typeStr: " + stringList.get(item.descriptorIdx));
//			if(stringList.get(item.descriptorIdx).contains("nicelee")) {
//				System.out.println(stringList.get(item.descriptorIdx));
//			}
//		}
		
	}

	/**
	 * 解析proto_ids索引区
	 * @param dexBytes
	 */
	public static void parseProtoIds(byte[] dexBytes) {
		// 获取所占字节数
		int idSize = ProtoIdsItem.getSize();
		// 获取偏移量
		int offset = headerType.protoIdsOff;
		// 获取字符串Id大小
		int countIds = headerType.protoIdsSize;
		for (int i = 0; i < countIds; i++) {
			protoIdsList.add(parseProtoIdsItem(Util.copyByte(dexBytes, offset + i * idSize, idSize)));
		}
		
		for (ProtoIdsItem item : protoIdsList) {
			//System.out.println("proto: " + stringList.get(item.shortyIdx) + ", " + stringList.get(item.returnTypeIdx));
			// 有的方法没有参数，这个值为0
			if (0 != item.parametersOff) {
				item = parseParameterTypeList(dexBytes, item.parametersOff, item);
			}
		}
		
	}

	/**
	 * 解析字段索引区
	 * @param dexBytes
	 */
	public static void parseFieldIds(byte[] dexBytes) {
		// 获取所占字节数
		int idSize = FieldIdsItem.getSize();
		// 获取偏移量
		int offset = headerType.fieldIdsOff;
		// 获取字符串Id大小
		int countIds = headerType.fieldIdsSize;
		// 遍历
		for (int i = 0; i < countIds; i++) {
			fieldIdsList.add(parseFieldIdsItem(Util.copyByte(dexBytes, offset + i * idSize, idSize)));
		}
		
		for (FieldIdsItem item : fieldIdsList) {
			int classIndex = typeIdsList.get(item.classIdx).descriptorIdx;
			int typeIndex = typeIdsList.get(item.typeIdx).descriptorIdx;
			//System.out.println("class: " + stringList.get(classIndex) + ",name: " + stringList.get(item.nameIdx) + ", type: " + stringList.get(typeIndex));
		}
		
	}

	/**
	 * 解析方法索引区
	 * @param dexBytes 二进制数据
	 */
	public static void parseMethodIds(byte[] dexBytes) {
		// 获取所占字节数
		int idSize = MethodIdsItem.getSize();
		// 获取偏移量
		int offset = headerType.methodIdsOff;
		// 获取字符串Id大小
		int countIds = headerType.methodIdsSize;
		// 遍历
		for (int i = 0; i < countIds; i++) {
			methodIdsList.add(parseMethodIdsItem(Util.copyByte(dexBytes, offset + i * idSize, idSize)));
		}
		
		for (MethodIdsItem item : methodIdsList) {
			int classIndex = typeIdsList.get(item.classIdx).descriptorIdx;
			int returnIndex = protoIdsList.get(item.protoIdx).returnTypeIdx;
			String returnTypeStr = stringList.get(typeIdsList.get(returnIndex).descriptorIdx);
			int shortIndex = protoIdsList.get(item.protoIdx).shortyIdx;
			String shortStr = stringList.get(shortIndex);
			List<String> paramList = protoIdsList.get(item.protoIdx).paramtersList;
			StringBuilder parameters = new StringBuilder();
			parameters.append(returnTypeStr + "(");
			for (String str : paramList) {
				parameters.append(str + ",");
			}
			parameters.append(")" + shortStr);
			//System.out.println("class: " + stringList.get(classIndex) + ",name: " + stringList.get(item.nameIdx) + ",proto: " + parameters);
		}
		
	}

	/**
	 * 解析class_def区域
	 * @param dexBytes 二进制数据
	 */
	public static void parseClassIds(byte[] dexBytes) {
		// 获取所占字节数
		int idSize = ClassDefItem.getSize();
		// 获取偏移量
		int offset = headerType.classDefsOff;
		// 获取字符串Id大小
		int countIds = headerType.classDefsSize;
		// 遍历
		for (int i = 0; i < countIds; i++) {
			classIdsList.add(parseClassDefItem(Util.copyByte(dexBytes, offset + i * idSize, idSize)));
		}
		// 遍历
		for (ClassDefItem item : classIdsList) {
			//System.out.println("item" + item.toString());
			int classIdx= item.classIdx;
			TypeIdsItem typeItem = typeIdsList.get(classIdx);
			int superClassIdx = item.superClassIdx;
			TypeIdsItem superTypeItem = typeIdsList.get(superClassIdx);
			int sourceIdx = item.sourceFileIdx;
			String sourceFile = stringList.get(sourceIdx);
			//System.out.println("classIdx: " + stringList.get(typeItem.descriptorIdx) + "superItem: " + stringList.get(superTypeItem.descriptorIdx) + "sourceFile: " + sourceFile);
			
			classDataMap.put(sourceFile, item);
		}
	}

	/**
	 * 解析map_off数据
	 * @param dexBytes
	 */
	public static void parseMapItemList(byte[] dexBytes) {
		MapList mapList = new MapList();
		// 获取所占字节数
		int idSize = MapItem.getSize();
		// 获取偏移量
		int offset = headerType.mapOff;
		// 获取map_off字节数组
		byte[] sizeByte = Util.copyByte(dexBytes, offset, 4);		
		int size = Util.byte2int(sizeByte);
		for (int i = 0; i < size; i++) {
			mapList.mapItems.add(parseMapItem(Util.copyByte(dexBytes, offset + 4 + i * idSize, idSize)));
		}
		
		for (MapItem item : mapList.mapItems) {
			//System.out.println(item.toString());
		}
	}

	/**
	 * 解析ClassData
	 * @param dexBytes 二进制数据
	 */
	public static void parseClassData(byte[] dexBytes) {
		for (String key : classDataMap.keySet()) {
			int dataOffset = classDataMap.get(key).classDataOff;
			//System.out.println("data offset: " + Util.bytesToHexString(Util.int2Byte(dataOffset)));;
			ClassDataItem item = parseClassDataItem(dexBytes, dataOffset);
			dataItemList.add(item);
			//System.out.println("class item: " + item);
		}
	}

	/**
	 * 解析代码内容
	 * @param dexBytes 二进制数据
	 */
	public static void parseCode(byte[] dexBytes) {
		for (ClassDataItem dataItem : dataItemList) {
			for (EncodeMethod method : dataItem.directMethods) {
				int offset = Util.decodeUleb128(method.codeOff);
				CodeItem item = parseCodeItem(dexBytes, offset);
				directMethodCodeItemList.add(item);
				System.out.println("direct method item: " + item);
			}
			for (EncodeMethod method : dataItem.virtualMethods) {
				int offset = Util.decodeUleb128(method.codeOff);
				CodeItem item = parseCodeItem(dexBytes, offset);
				virtualMethodCodeItemList.add(item);
				System.out.println("virtual method item: " + item);
			}
		}
	}


	/**
	 * 解析字符串数据
	 * @param srcByte 二进制数据
	 * @return
	 */
	private static StringIdsItem parseStringIdsItem(byte[] srcByte) {
		StringIdsItem item = new StringIdsItem();
		byte[] idsByte = Util.copyByte(srcByte, 0, 4);
		item.stringDataOff = Util.byte2int(idsByte);
		return item;
	}

	/**
	 * 解析一个字符串
	 *  两种方式：
	 *  	1. 第一个字节是字符串的长度
	 *  	2. 每个字符串的结束符是00
	 * @param srcBytes 二进制数据
	 * @param startOff 开始位置
	 * @return
	 */
	private static String getString(byte[] srcBytes, int startOff) {
		// 第一种方式
		byte size = srcBytes[startOff];
		byte[] byteAry = Util.copyByte(srcBytes, startOff + 1, size);
		
		// 第二种方式
//		List<Byte> byteList = new ArrayList<>();
//		// 第一个字节是长度，所有过滤
//		byte b = srcBytes[startOff + 1];
//		int index = 1;
//		while (b != 0) {
//			byteList.add(b);
//			index++;
//			b = srcBytes[startOff + index];		
//		}
//		byte[] byteAry = new byte[byteList.size() + 1];
//		for (int i = 0; i < byteList.size(); i++) {
//			byteAry[i] = byteList.get(i);
//		}
//		byteAry[byteList.size()] = 0;
//		System.out.println("------------" + Util.bytesToHexString(byteAry));
		
		String result = "";
		try {
			if (null != byteAry) {
				result = new String(byteAry, "UTF-8");				
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 解析TypeIdsItem 
	 * @param srcByte 二进制数据
	 * @return
	 */
	private static TypeIdsItem parseTypeIdsItem(byte[] srcByte) {
		TypeIdsItem item = new TypeIdsItem();
		byte[] descriptorIdxByte = Util.copyByte(srcByte, 0, 4);
		item.descriptorIdx = Util.byte2int(descriptorIdxByte);
		return item;
	}

	/**
	 * 解析ProtoIdsItem
	 * @param dexBytes 二进制数据
	 * @return
	 */
	private static ProtoIdsItem parseProtoIdsItem(byte[] dexBytes) {
		ProtoIdsItem item = new ProtoIdsItem();
		byte[] shortyIdxByte = Util.copyByte(dexBytes, 0, 4);
		item.shortyIdx = Util.byte2int(shortyIdxByte);
		byte[] returnTypeIdxByte = Util.copyByte(dexBytes, 4, 4);
		item.returnTypeIdx = Util.byte2int(returnTypeIdxByte);
		byte[] parametersOffByte = Util.copyByte(dexBytes, 8, 4);
		item.parametersOff = Util.byte2int(parametersOffByte);
		return item;
	}

	/**
	 * 解析方法的所有参数类型
	 * @param dexBytes 二进制数据
	 * @param startOff 参数偏移量
	 * @param item 条目
	 * @return
	 */
	private static ProtoIdsItem parseParameterTypeList(byte[] dexBytes, int startOff, ProtoIdsItem item) {
		// 解析size和size大小的List中的内容
		byte[] sizeByte = Util.copyByte(dexBytes, startOff, 4);
		// 获取size
		int size = Util.byte2int(sizeByte);
		List<String> parametersList = new ArrayList<>();
		List<Short> typeList = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			byte[] typeByte = Util.copyByte(dexBytes, startOff + 4 + 2 * i, 2);
			typeList.add(Util.byte2Short(typeByte));
		}
		//System.out.println("param count: " + size);
		
		for (int i = 0; i < typeList.size(); i++) {
			//System.out.println("type: " + stringList.get(typeList.get(i)));
			int index = typeIdsList.get(typeList.get(i)).descriptorIdx;
			parametersList.add(stringList.get(index));
		}
		item.paramterCount = size;
		item.paramtersList = parametersList;
		return item;
	}

	/**
	 * 解析FieldIdsItem
	 * @param srcByte 二进制数据字节
	 * @return
	 */
	private static FieldIdsItem parseFieldIdsItem(byte[] srcByte) {
		FieldIdsItem item = new FieldIdsItem();
		byte[] classIdxByte = Util.copyByte(srcByte, 0, 2);
		item.classIdx = Util.byte2Short(classIdxByte);
		byte[] typeIdxByte = Util.copyByte(srcByte, 2, 2);
		item.typeIdx = Util.byte2Short(typeIdxByte);
		byte[] nameIdxByte = Util.copyByte(srcByte, 4, 4);
		item.nameIdx = Util.byte2int(nameIdxByte);
		return item;
	}
	
	/**
	 * 解析MethodIdsItem
	 * @param srcByte 二进制数据
	 * @return
	 */
	private static MethodIdsItem parseMethodIdsItem(byte[] srcByte) {
		MethodIdsItem item = new MethodIdsItem();
		byte[] classIdxByte = Util.copyByte(srcByte, 0, 2);
		item.classIdx = Util.byte2Short(classIdxByte);
		byte[] protoIdxByte = Util.copyByte(srcByte, 2, 2);
		item.protoIdx = Util.byte2Short(protoIdxByte);
		byte[] nameIdxByte = Util.copyByte(srcByte, 4, 4);
		item.nameIdx = Util.byte2int(nameIdxByte);
		return item;
	}

	/**
	 * 解析ClassDefIem
	 * @param srcByte 二进制数据
	 * @return
	 */
	private static ClassDefItem parseClassDefItem(byte[] srcByte) {
		ClassDefItem item = new ClassDefItem();
		byte[] classIdxByte = Util.copyByte(srcByte, 0, 4);
		item.classIdx = Util.byte2int(classIdxByte);
		byte[] accessFlagsByte = Util.copyByte(srcByte, 4, 4);
		item.accessFlags = Util.byte2int(accessFlagsByte);
		byte[] superClassIdxByte = Util.copyByte(srcByte, 8, 4);
		item.superClassIdx = Util.byte2int(superClassIdxByte);
		// 这里如果class没有interfaces的话，这里为0
		byte[] interfacesOffByte = Util.copyByte(srcByte, 12, 4);
		item.interfacesOff = Util.byte2int(interfacesOffByte);
		// 如果此项信息缺失，值为0xFFFFFFFF
		byte[] sourceFileIdxByte = Util.copyByte(srcByte, 16, 4);
		item.sourceFileIdx = Util.byte2int(sourceFileIdxByte);
		byte[] annotationsOffByte = Util.copyByte(srcByte, 20, 4);
		item.annotationsOff = Util.byte2int(annotationsOffByte);
		byte[] classDataOffByte = Util.copyByte(srcByte, 24, 4);
		item.classDataOff = Util.byte2int(classDataOffByte);
		byte[] staticValueOffByte = Util.copyByte(srcByte, 28, 4);
		item.staticValueOff = Util.byte2int(staticValueOffByte);
		return item;
	}

	/**
	 * 解析MapItem
	 * @param srcByte 二进制数据
	 * @return
	 */
	private static MapItem parseMapItem(byte[] srcByte) {
		MapItem item = new MapItem();
		byte[] typeByte = Util.copyByte(srcByte, 0, 2);
		item.type = Util.byte2Short(typeByte);
		byte[] unUseByte = Util.copyByte(srcByte, 2, 2);
		item.unuse = Util.byte2Short(unUseByte);
		byte[] sizeByte = Util.copyByte(srcByte, 4, 4);
		item.size = Util.byte2int(sizeByte);
		byte[] offsetByte = Util.copyByte(srcByte, 8, 4);
		item.offset = Util.byte2int(offsetByte);
		return item;
	}

	/**
	 * 解析ClassDataItem
	 * @param srcBytes 二进制数据
	 * @param offset 数据偏移
	 * @return
	 */
	private static ClassDataItem parseClassDataItem(byte[] srcBytes, int offset) {
		ClassDataItem item = new ClassDataItem();
		for (int i = 0; i < 4; i++) {
			byte[] byteAry = Util.readUnsignedLeb128(srcBytes, offset);
			offset += byteAry.length;
			int size = 0;
			if (byteAry.length == 1) {
				size = byteAry[0];
			} else if (byteAry.length == 2) {
				size = Util.byte2Short(byteAry);
			} else if (byteAry.length == 4) {
				size = Util.byte2int(byteAry);
			}
			
			switch (i) {
			case 0:
				item.staticFieldsSize = size;
				break;
			case 1:
				item.instanceFieldsSize = size;
				break;
			case 2:
				item.directMethodsSize = size;
				break;
			case 3:
				item.virtualMethodsSize = size;
				break;
			default:
				break;
			}
		}
		
		// 解析static_fields数组
		EncodeField[] staticFieldAry = new EncodeField[item.staticFieldsSize];
		for (int i = 0; i < item.staticFieldsSize; i++) {
			EncodeField staticField = new EncodeField();
			staticField.fieldIdxDiff = Util.readUnsignedLeb128(srcBytes, offset);
			offset += staticField.fieldIdxDiff.length;
			staticField.accessFlags = Util.readUnsignedLeb128(srcBytes, offset);
			offset += staticField.accessFlags.length;
			staticFieldAry[i] = staticField;
		}
		
		// 解析instance_fields数组
		EncodeField[] instanceFieldAry = new EncodeField[item.instanceFieldsSize];
		for (int i = 0; i < item.instanceFieldsSize; i++) {
			EncodeField instanceField = new EncodeField();
			instanceField.fieldIdxDiff = Util.readUnsignedLeb128(srcBytes, offset);
			offset += instanceField.fieldIdxDiff.length;
			instanceField.accessFlags = Util.readUnsignedLeb128(srcBytes, offset);
			offset += instanceField.accessFlags.length;
			instanceFieldAry[i] = instanceField;
		}
		
		// 解析static_methods数组
		EncodeMethod[] staticMethodsAry = new EncodeMethod[item.directMethodsSize];
		for (int i = 0; i < item.directMethodsSize; i++) {
			EncodeMethod staticMethod = new EncodeMethod();
			staticMethod.methodIdxDiff = Util.readUnsignedLeb128(srcBytes, offset);
			offset += staticMethod.methodIdxDiff.length;
			staticMethod.accessFlags = Util.readUnsignedLeb128(srcBytes, offset);
			offset += staticMethod.accessFlags.length;
			staticMethod.codeOff = Util.readUnsignedLeb128(srcBytes, offset);
			offset += staticMethod.codeOff.length;
			staticMethodsAry[i] = staticMethod;			
		}
		
		// 解析Virtual_methods数组
		EncodeMethod[] instanceMethodsAry = new EncodeMethod[item.virtualMethodsSize];
		for (int i = 0; i < item.virtualMethodsSize; i++) {
			EncodeMethod instanceMethod = new EncodeMethod();
			instanceMethod.methodIdxDiff = Util.readUnsignedLeb128(srcBytes, offset);
			offset += instanceMethod.methodIdxDiff.length;
			instanceMethod.accessFlags = Util.readUnsignedLeb128(srcBytes, offset);
			offset += instanceMethod.accessFlags.length;
			instanceMethod.codeOff = Util.readUnsignedLeb128(srcBytes, offset);
			offset += instanceMethod.codeOff.length;
			instanceMethodsAry[i] = instanceMethod;
		}
		
		item.staticFields = staticFieldAry;
		item.instanceFields = instanceFieldAry;
		item.directMethods = staticMethodsAry;
		item.virtualMethods = instanceMethodsAry;
		
		return item;
	}
	
	/**
	 * 解析CodeItem
	 * @param srcBytes 二进制数据
	 * @param offset 偏移量
	 * @return
	 */
	private static CodeItem parseCodeItem(byte[] srcBytes, int offset) {
		CodeItem item = new CodeItem();
		
		byte[] regSizeByte = Util.copyByte(srcBytes, offset, 2);
		item.registersSize = Util.byte2Short(regSizeByte);
		
		byte[] insSizeByte = Util.copyByte(srcBytes, offset + 2, 2);
		item.insSize = Util.byte2Short(insSizeByte);
		
		byte[] outsSizeByte = Util.copyByte(srcBytes, offset + 4, 2);
		item.outsSize = Util.byte2Short(outsSizeByte);
		
		byte[] triesSizeByte = Util.copyByte(srcBytes, offset + 6, 2);
		item.triesSize = Util.byte2Short(triesSizeByte);
		
		byte[] debugInfoByte = Util.copyByte(srcBytes, offset + 8, 4);
		item.debugInfoOff = Util.byte2int(debugInfoByte);
		
		byte[] insnsSizeByte = Util.copyByte(srcBytes, offset + 12, 4);
		item.insnsSize = Util.byte2int(insnsSizeByte);
		
//		short[] insnsAry = new short[item.insnsSize];
//		int aryOffset = offset + 16;
//		for(int i=0;i<item.insnsSize;i++){
//			byte[] insnsByte = Util.copyByte(srcBytes, aryOffset+i*2, 2);
//			if (null != insnsByte) {
//				insnsAry[i] = Util.byte2Short(insnsByte);	
//			}
//		}
//		item.insns = insnsAry;
		
		return item;
	}
	
}















;

