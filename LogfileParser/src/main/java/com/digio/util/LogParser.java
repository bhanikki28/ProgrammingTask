package com.digio.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogParser {

	final static Logger logger = Logger.getLogger(LogParser.class.getName());

	/* RegEx Pattern for HTTP Log file */

	final String regex = "^(\\S+) (\\S+) (\\S+) " + "\\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(\\S+)"
			+ " (\\S+)\\s*(\\S+)?\\s*\" (\\d{3}) (\\S+)";
	final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);

	public long getUniqueIPCount(File configFile){
		logger.info("Inside getUniqueIPCount");
		long uniqueIpAddressCount = 0;
		List<String> ipAddressList = new ArrayList<>();

		try (Stream<String> linesStream = Files.lines(Paths.get(configFile.getAbsolutePath()))) {
			linesStream.forEach(line -> {
				final Matcher matcher = pattern.matcher(line.toString());
				while (matcher.find()) {
					String ip_address = matcher.group(1);
					ipAddressList.add(ip_address);
				}
			});
		}

		catch (IOException e) {
			e.printStackTrace();
		}
		uniqueIpAddressCount = ipAddressList.stream().distinct().count();
		logger.info("Unique IP Address Count:" + uniqueIpAddressCount);

		return uniqueIpAddressCount;

	}

	public List<String> getTopActiveIPs(File configFile) {
		logger.info("Inside getTop3 Active");

		List<String> items = new ArrayList<>();

		try (Stream<String> linesStream = Files.lines(Paths.get(configFile.getAbsolutePath()))) {
			linesStream.forEach(line -> {
				final Matcher matcher = pattern.matcher(line);
				while (matcher.find()) {
					String ip_address = matcher.group(1);
					String status_code = matcher.group(8);
					int response = Integer.parseInt(status_code);
					if (response == 200) {
						items.add(ip_address);
					}
				}
			});

		}

		catch (IOException e) {
			e.printStackTrace();
		}
		Map<String, Long> result = items.stream()
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		List<String> topActiveIP = new ArrayList<>();

		// Sort a map and add to list
		result.entrySet().stream()
		                 .sorted(Map.Entry.<String, Long>comparingByValue()
		                 .reversed())
		                 .limit(3)
				         .forEachOrdered(e -> topActiveIP.add(e.getKey()));

		return topActiveIP;

	}
	
	public List<String> getTopURLs(File configFile) {
		logger.info("Inside getTop3URLs");

		List<String> urls = new ArrayList<>();

		try (Stream<String> linesStream = Files.lines(Paths.get(configFile.getAbsolutePath()))) {
			linesStream.forEach(line -> {
				final Matcher matcher = pattern.matcher(line);
				while (matcher.find()) {
					String url = matcher.group(6);
					urls.add(url);
					
				}
			});

		}

		catch (IOException e) {
			e.printStackTrace();
		}
		Map<String, Long> result = urls.stream()
				                       .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		List<String> top3Urls = new ArrayList<>();

		// Sort a map and add to list
		result.entrySet().stream()
		                 .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
		                 .limit(3)
				         .forEachOrdered(e -> top3Urls.add(e.getKey()));

		return top3Urls;

	}
}
