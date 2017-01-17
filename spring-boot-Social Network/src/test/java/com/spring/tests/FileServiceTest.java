package com.spring.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.lang.reflect.Method;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.whatever.App;
import com.whatever.service.FileService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(App.class)
@WebAppConfiguration
@Transactional
public class FileServiceTest {
	
	
	@Autowired
	FileService fileService;
	
	@Value("${photo.upload.directory}")
	private String photoUploadDirectory;
	
	
	@Test
	public void testGetExtension() throws Exception{
		
		//Folosim Reflection pentru a putea apela o metoda privata
		Method method = FileService.class.getDeclaredMethod("getFileExtensions", String.class);//numele metodei pe care o "copiem" este "getFileExtension
		method.setAccessible(true);
		
		assertEquals("should be png", "png", (String)method.invoke(fileService, "test.png"));
		assertEquals("should be jpg", "jpg", (String)method.invoke(fileService, "w.jpg"));
		assertEquals("should be jpeg", "jpeg", (String)method.invoke(fileService, "test.jpeg"));
		assertEquals("should be png", null, (String)method.invoke(fileService, "test"));
		
	}
	
	@Test
	public void testImageExtension() throws Exception{
		
		Method method = FileService.class.getDeclaredMethod("isImageExtension", String.class);
		method.setAccessible(true);
		
		assertTrue("png should be valid", (Boolean)method.invoke(fileService, "png"));
		assertTrue("PNG should be valid", (Boolean)method.invoke(fileService, "PNG"));
		assertTrue("gif should be valid", (Boolean)method.invoke(fileService, "gif"));
		assertTrue("bmp should be valid", (Boolean)method.invoke(fileService, "bmp"));
		assertTrue("jpg should be valid", (Boolean)method.invoke(fileService, "jpg"));
		assertTrue("JpG should be valid", (Boolean)method.invoke(fileService, "JpG"));
		assertFalse("bm should be invalid", (Boolean)method.invoke(fileService, "bm"));
		assertFalse("jp3 should be invalid", (Boolean)method.invoke(fileService, "jp3"));
		
	}
	
	@Test
	public void testMakeSubdirectory() throws Exception{
		Method method = FileService.class.getDeclaredMethod("makeSubdirectory", String.class, String.class);
		method.setAccessible(true);
		
		for (int i=0; i<10000; i++){
		
			File created = (File)method.invoke(fileService, photoUploadDirectory, "Photos");
			
			assertTrue("Directory should exist: " + created.getAbsolutePath(), created.exists());
		}
	}

}
