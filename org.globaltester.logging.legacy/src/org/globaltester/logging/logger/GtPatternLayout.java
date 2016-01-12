package org.globaltester.logging.logger;

import org.apache.log4j.PatternLayout;

public class GtPatternLayout extends PatternLayout {
	public static String HEADER_LINE = "GlobalTester log file\n";
	
	public GtPatternLayout(String pattern) {
		super(pattern);
	}

	@Override
	public String getHeader() {
		return HEADER_LINE;
	}
}
