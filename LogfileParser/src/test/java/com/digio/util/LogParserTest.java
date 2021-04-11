package com.digio.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class LogParserTest {
	final static Logger logger = Logger.getLogger(LogParserTest.class.getName());

	final static String LOG_FILE_NAME = "programming-task-example-data.log";
	final static String LOG_INVALID_FILE_NAME = "programming-task-example-data1.log";

	
	File configFile=null;
	LogParser parser = new LogParser();
	
	@BeforeAll
	public  void setup() throws FileNotFoundException {
		try {
			ClassLoader classLoader = this.getClass().getClassLoader();
			configFile = new File(classLoader.getResource(LOG_FILE_NAME).getFile());
			System.out.println(configFile.getAbsolutePath());
		}
		finally {
			logger.info("setup done");
		}
	}

	@Test
	void testUniqueIPCount(){
		assertEquals(11, parser.getUniqueIPCount(configFile));
	}
	
	@Test
	void testTop3IPs() {
		assertEquals(3, parser.getTopActiveIPs(configFile).size());
		assertEquals("168.41.191.40", parser.getTopActiveIPs(configFile).get(0));
	}
	
	@Test
	void testTop3URLs()  {
		assertEquals(3, parser.getTopURLs(configFile).size());
		assertEquals("/docs/manage-websites/", parser.getTopURLs(configFile).get(0));
	}
	

}
