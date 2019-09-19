package com.mazaiting.struct;

/**
 * class_data_off指向data区里的class_data_item结构，class_data_item里存放着本class使用到的各种数据
 * 
 * 	struct class_data_item	{
		uleb128 static_fields_size;
		uleb128 instance_fields_size;
		uleb128 direct_methods_size;
		uleb128 virtual_methods_size;
		encoded_field static_fields [ static_fields_size ];
		encoded_field instance_fields [ instance_fields_size ];
		encoded_method direct_methods [ direct_method_size ];
		encoded_method virtual_methods [ virtual_methods_size ];
	};

 * 
 * @author mazaiting
 */
public class ClassDataItem {
	/**
	 * 静态字段的大小
	 */
	public int staticFieldsSize;
	/**
	 * 实例化字段的大小
	 */
	public int instanceFieldsSize;
	/**
	 * 实现方法的大小
	 */
	public int directMethodsSize;
	/**
	 * 虚拟方法的大小
	 */
	public int virtualMethodsSize;
	/**
	 * 静态字段数组
	 */
	public EncodeField[] staticFields;
	/**
	 * 实例化字段数组
	 */
	public EncodeField[] instanceFields;
	/**
	 * 实现方法数组
	 */
	public EncodeMethod[] directMethods;
	/**
	 * 虚拟方法的数组
	 */
	public EncodeMethod[] virtualMethods;
	
	@Override
	public String toString(){
		return "staticFieldsSize: " + staticFieldsSize + ",instanceFieldsSize: " + instanceFieldsSize
				+ ",directMethodsSize:" + directMethodsSize + ",virtualMethodsSize: " + virtualMethodsSize
				+ "\n" + getFieldsAndMethods();
	}



	private String getFieldsAndMethods() {
		StringBuilder sb = new StringBuilder();
		sb.append("staticFields:\n");
		for(int i=0;i<staticFields.length;i++){
			sb.append(staticFields[i]+"\n");
		}
		sb.append("instanceFields:\n");
		for(int i=0;i<instanceFields.length;i++){
			sb.append(instanceFields[i]+"\n");
		}
		sb.append("directMethods:\n");
		for(int i=0;i<directMethods.length;i++){
			sb.append(directMethods[i]+"\n");
		}
		sb.append("virtualMethods:\n");
		for(int i=0;i<virtualMethods.length;i++){
			sb.append(virtualMethods[i]+"\n");
		}
		return sb.toString();
	}

}

