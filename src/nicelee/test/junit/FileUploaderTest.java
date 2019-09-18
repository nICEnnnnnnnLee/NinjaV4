package nicelee.test.junit;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import nicelee.function.cloud.upload.FileUploader;
import nicelee.global.GlobalConfig;

public class FileUploaderTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		File file = new File(
				"D:\\Workspace\\GitWorkspace\\0_GitHub\\nICEnnnnnnnLee.github.io\\sources\\pics\\bg-catoon.jpg");
		boolean result = FileUploader.update(GlobalConfig.url_onlineDevices, file, GlobalConfig.token);
		System.out.println(result);
	}

}
