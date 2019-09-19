package com.mazaiting.struct;

/**
 * class_defs区域里存放着class definitions，有些数据直接指向了data区
 * 
 * struct DexClassDef {
	    u4  classIdx;           /* index into typeIds for this class
	    u4  accessFlags;
	    u4  superclassIdx;      /* index into typeIds for superclass
	    u4  interfacesOff;      /* file offset to DexTypeList
	    u4  sourceFileIdx;      /* index into stringIds for source file name
	    u4  annotationsOff;     /* file offset to annotations_directory_item
	    u4  classDataOff;       /* file offset to class_data_item
	    u4  staticValuesOff;    /* file offset to DexEncodedArray
	};
 * 
 * @author mazaiting
 */
public class ClassDefItem {
	/**
	 * 描述具体的class类型，值是type_ids的一个index，值必须是一个class类型，不能是数组雷兴国或者基本类型
	 */
	public int classIdx;
	/**
	 * 描述class的访问类型，如public,final,static等
	 */
	public int accessFlags;
	/**
	 * 描述父类的类型，值必须是一个class类型，不能是数组雷兴国或者基本类型
	 */
	public int superClassIdx;
	/**
	 * 值为偏移地址，被指向的数据结构为type_list，class若没有interfaces，值为0
	 */
	public int interfacesOff;
	/**
	 * 表示源代码文件的信息，值为string_ids的一个index。若此项信息丢失，此项赋值为NO_INDEX=0xFFFFFFFF
	 */
	public int sourceFileIdx;
	/**
	 * 值为偏移地址，指向的内容是该class的注解，位置在data区，格式为annotations_directory_item，若没有此项，值为0
	 */
	public int annotationsOff;
	/**
	 * 值为偏移地址，指向的内容是该class的使用到的数据，位置在data区，格式为class_data_item。无偶没有此项，则值为0
	 */
	public int classDataOff;
	/**
	 * 值为偏移地址，指向data区里的一个列表，格式为encoded_array_item。若没有此项，值为0.
	 */
	public int staticValueOff;
	
	/**
	 * enum {
		    ACC_PUBLIC       = 0x00000001,       // class, field, method, ic
		    ACC_PRIVATE      = 0x00000002,       // field, method, ic
		    ACC_PROTECTED    = 0x00000004,       // field, method, ic
		    ACC_STATIC       = 0x00000008,       // field, method, ic
		    ACC_FINAL        = 0x00000010,       // class, field, method, ic
		    ACC_SYNCHRONIZED = 0x00000020,       // method (only allowed on natives)
		    ACC_SUPER        = 0x00000020,       // class (not used in Dalvik)
		    ACC_VOLATILE     = 0x00000040,       // field
		    ACC_BRIDGE       = 0x00000040,       // method (1.5)
		    ACC_TRANSIENT    = 0x00000080,       // field
		    ACC_VARARGS      = 0x00000080,       // method (1.5)
		    ACC_NATIVE       = 0x00000100,       // method
		    ACC_INTERFACE    = 0x00000200,       // class, ic
		    ACC_ABSTRACT     = 0x00000400,       // class, method, ic
		    ACC_STRICT       = 0x00000800,       // method
		    ACC_SYNTHETIC    = 0x00001000,       // field, method, ic
		    ACC_ANNOTATION   = 0x00002000,       // class, ic (1.5)
		    ACC_ENUM         = 0x00004000,       // class, field, ic (1.5)
		    ACC_CONSTRUCTOR  = 0x00010000,       // method (Dalvik only)
		    ACC_DECLARED_SYNCHRONIZED =
		                       0x00020000,       // method (Dalvik only)
		    ACC_CLASS_MASK =
		        (ACC_PUBLIC | ACC_FINAL | ACC_INTERFACE | ACC_ABSTRACT
		                | ACC_SYNTHETIC | ACC_ANNOTATION | ACC_ENUM),
		    ACC_INNER_CLASS_MASK =
		        (ACC_CLASS_MASK | ACC_PRIVATE | ACC_PROTECTED | ACC_STATIC),
		    ACC_FIELD_MASK =
		        (ACC_PUBLIC | ACC_PRIVATE | ACC_PROTECTED | ACC_STATIC | ACC_FINAL
		                | ACC_VOLATILE | ACC_TRANSIENT | ACC_SYNTHETIC | ACC_ENUM),
		    ACC_METHOD_MASK =
		        (ACC_PUBLIC | ACC_PRIVATE | ACC_PROTECTED | ACC_STATIC | ACC_FINAL
		                | ACC_SYNCHRONIZED | ACC_BRIDGE | ACC_VARARGS | ACC_NATIVE
		                | ACC_ABSTRACT | ACC_STRICT | ACC_SYNTHETIC | ACC_CONSTRUCTOR
		                | ACC_DECLARED_SYNCHRONIZED),
		};

	 */
	
	/**
	 * 访问修饰符
	 */
	public static final int 
		ACC_PUBLIC       = 0x00000001,       // class, field, method, ic
	    ACC_PRIVATE      = 0x00000002,       // field, method, ic
	    ACC_PROTECTED    = 0x00000004,       // field, method, ic
	    ACC_STATIC       = 0x00000008,       // field, method, ic
	    ACC_FINAL        = 0x00000010,       // class, field, method, ic
	    ACC_SYNCHRONIZED = 0x00000020,       // method (only allowed on natives)
	    ACC_SUPER        = 0x00000020,       // class (not used in Dalvik)
	    ACC_VOLATILE     = 0x00000040,       // field
	    ACC_BRIDGE       = 0x00000040,       // method (1.5)
	    ACC_TRANSIENT    = 0x00000080,       // field
	    ACC_VARARGS      = 0x00000080,       // method (1.5)
	    ACC_NATIVE       = 0x00000100,       // method
	    ACC_INTERFACE    = 0x00000200,       // class, ic
	    ACC_ABSTRACT     = 0x00000400,       // class, method, ic
	    ACC_STRICT       = 0x00000800,       // method
	    ACC_SYNTHETIC    = 0x00001000,       // field, method, ic
	    ACC_ANNOTATION   = 0x00002000,       // class, ic (1.5)
	    ACC_ENUM         = 0x00004000,       // class, field, ic (1.5)
	    ACC_CONSTRUCTOR  = 0x00010000,       // method (Dalvik only)
	    ACC_DECLARED_SYNCHRONIZED =
	                       0x00020000,       // method (Dalvik only)
	    ACC_CLASS_MASK =
	        (ACC_PUBLIC | ACC_FINAL | ACC_INTERFACE | ACC_ABSTRACT
	                | ACC_SYNTHETIC | ACC_ANNOTATION | ACC_ENUM),
	    ACC_INNER_CLASS_MASK =
	        (ACC_CLASS_MASK | ACC_PRIVATE | ACC_PROTECTED | ACC_STATIC),
	    ACC_FIELD_MASK =
	        (ACC_PUBLIC | ACC_PRIVATE | ACC_PROTECTED | ACC_STATIC | ACC_FINAL
	                | ACC_VOLATILE | ACC_TRANSIENT | ACC_SYNTHETIC | ACC_ENUM),
	    ACC_METHOD_MASK =
	        (ACC_PUBLIC | ACC_PRIVATE | ACC_PROTECTED | ACC_STATIC | ACC_FINAL
	                | ACC_SYNCHRONIZED | ACC_BRIDGE | ACC_VARARGS | ACC_NATIVE
	                | ACC_ABSTRACT | ACC_STRICT | ACC_SYNTHETIC | ACC_CONSTRUCTOR
	                | ACC_DECLARED_SYNCHRONIZED);

	/**
	 * 获取当前区域所占字节数
	 * @return
	 */
	public static int getSize() {
		return 4 * 8;
	}
	
	@Override
	public String toString(){
		return "classIdx: " + classIdx + ",accessFlags: " + accessFlags + ",superClassIdx: " + superClassIdx + 
				",iterfacesOff: " + interfacesOff + ",sourceFileIdx: " + sourceFileIdx + ",annotationsOff: " + 
				annotationsOff + ",classDataOff: " + classDataOff + ",staticValueOff: " + staticValueOff;
	}

}
