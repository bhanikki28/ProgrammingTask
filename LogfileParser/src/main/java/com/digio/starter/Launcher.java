package com.digio.starter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.logging.Logger;

import com.digio.util.LogParser;

public class Launcher {
	final static Logger logger = Logger.getLogger(Launcher.class.getName());
	final static String LOG_FILE_NAME = "programming-task-example-data.log";

	public File readFile(String fileName) throws NullPointerException {
		ClassLoader classLoader = this.getClass().getClassLoader();
		File configFile = new File(classLoader.getResource(fileName).getFile());
		return configFile;
	}

	public static void main(String as[]) throws FileNotFoundException {
		final Logger logger = Logger.getLogger(Launcher.class.getName());

		Launcher objLauncher = new Launcher();
		LogParser objParser = new LogParser();
		logger.info("App Launcher Initialized");
		File configFile = null;
		try {
			configFile = objLauncher.readFile(LOG_FILE_NAME);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new FileNotFoundException("File name not valid");
		}

		objParser.getUniqueIPCount(configFile);
		List<String> activeIPs = objParser.getTopActiveIPs(configFile);
		logger.info("Top 3 Active IPs(Status Code:200)" + activeIPs);

		List<String> topURLs = objParser.getTopURLs(configFile);
		logger.info("Top 3 Visited URLs:" + topURLs);


	}
}
