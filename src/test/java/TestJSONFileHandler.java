package test.java;

import java.io.FileNotFoundException;

import org.junit.Test;

import main.java.model.JSONFileHandler;

public class TestJSONFileHandler {

	
	
	
	@Test(expected = FileNotFoundException.class)
	public void testFileNotFoundExceptionWhenFiledoesntExist() {
		
	}
	
	
}
