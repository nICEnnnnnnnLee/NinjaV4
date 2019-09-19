package com.mazaiting;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ParseDexMain {
	/**指定要解析的文件*/
	private static final String DEFAULT_DEX = "classes.dex";
	
	public static void main(String[] args) {
		
		String apkPath = "D:\\Workspace\\com.termux.api_34.apk";
		// 从apk读取
		byte[] dexBytes = getDexFromApk(apkPath);
		// 直接读取dex
		byte[] dexBytes2 = getDexFromDisk("D:\\Workspace\\javaweb-springboot\\ASimpleWork\\release\\NinjaV4-1.0.0.dex");
		
		System.out.println("Parse Header: ===============================");
		ParseDexUtil.parseDexHeader(dexBytes);
		System.out.println();
		
		System.out.println("Parse StringIds: ===============================");
		ParseDexUtil.parseStringIds(dexBytes);
		System.out.println();
		
		System.out.println("Parse StringList: ===============================");
		ParseDexUtil.parseStringList(dexBytes);
		System.out.println();
		
		System.out.println("Parse TypeIds: ===============================");
		ParseDexUtil.parseTypeIds(dexBytes);
		System.out.println();
		
//		System.out.println("Parse ProtoIds: ===============================");
//		ParseDexUtil.parseProtoIds(dexBytes);
//		System.out.println();
//		
//		System.out.println("Parse FieldIds: ===============================");
//		ParseDexUtil.parseFieldIds(dexBytes);
//		System.out.println();
//		
//		System.out.println("Parse MethodIds: ===============================");
//		ParseDexUtil.parseMethodIds(dexBytes);
//		System.out.println();
//		
//		System.out.println("Parse ClassIds: ===============================");
//		ParseDexUtil.parseClassIds(dexBytes);
//		System.out.println();
//		
//		System.out.println("Parse MapList: ===============================");
//		ParseDexUtil.parseMapItemList(dexBytes);
//		System.out.println();
//		
//		System.out.println("Parse ClassData: ===============================");
//		ParseDexUtil.parseClassData(dexBytes);
//		System.out.println();
//		
//		System.out.println("Parse CodeContent: ===============================");
//		ParseDexUtil.parseCode(dexBytes);
//		System.out.println();
	}

	/**
	 * 从磁盘读取DEX
	 * @param path
	 * @return
	 */
	public static byte[] getDexFromDisk(String path) {
		byte[] srcByte = null;
		InputStream fis = null;
		ByteArrayOutputStream bos = null;
		try{
			fis = new FileInputStream(path);
			bos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while((len=fis.read(buffer)) != -1){
				bos.write(buffer, 0, len);
			}
			srcByte = bos.toByteArray();
		}catch(Exception e){
			System.out.println("read res file error:"+e.toString());
		}finally{
			try{
				fis.close();
				bos.close();
			}catch(Exception e){
				System.out.println("close file error:"+e.toString());
			}
		}
		return srcByte;
	}

	/**
	 * 从apk中获取classes.dex文件
	 * @param apkPath apk路径
	 * @return 
	 */
	private static byte[] getDexFromApk(String apkPath) {
		ZipFile zipFile = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		try {
			File apkFile = new File(apkPath);
			zipFile = new ZipFile(apkFile, ZipFile.OPEN_READ);
			ZipEntry entry = zipFile.getEntry(DEFAULT_DEX);
			is = zipFile.getInputStream(entry);
			baos = new ByteArrayOutputStream();
			int len = 0;
			byte[] bytes = new byte[1024];
			while ((len = is.read(bytes)) != -1) {
				baos.write(bytes, 0, len);
			}
			zipFile.close();
			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				baos.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return null;
	}
}
