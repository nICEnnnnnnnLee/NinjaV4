package com.mazaiting.struct;

/**
 * Dex 文件结构
 *  struct DexFile {
	    const DexOptHeader* pOptHeader;
	    const DexHeader*    pHeader;
	    const DexStringId*  pStringIds;
	    const DexTypeId*    pTypeIds;
	    const DexFieldId*   pFieldIds;
	    const DexMethodId*  pMethodIds;
	    const DexProtoId*   pProtoIds;
	    const DexClassDef*  pClassDefs;
	    const DexLink*      pLinkData;
	    const DexClassLookup* pClassLookup;
	    const void*         pRegisterMapPool;
	    const u1*           baseAddr;
	    int                 overhead;
	};
 * @author mazaiting
 *
 */
public class DexFile {

}
