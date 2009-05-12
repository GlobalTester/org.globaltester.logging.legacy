package org.globaltester.logger;

/* 
 * Project 	GlobalTester 
 * File		TestLogger.java
 *
 * Date    	14.10.2005
 * 
 * 
 * Developed by HJP Consulting GmbH
 * Lanfert 24
 * 33106 Paderborn
 * Germany
 * 
 * 
 * This software is the confidential and proprietary information
 * of HJP ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance 
 * with the terms of the Non-Disclosure Agreement you entered 
 * into with HJP.
 * 
 */

import org.apache.log4j.Level;
import org.apache.log4j.Logger;


/**
 * This class implements the methods for logging.
 * 
 * @version		Release 1.5.0
 * @author 		Holger Funke
 *
 */


public class TestLoggerFromCVS {
	
	// Logger
	private static Logger logger = null;
	
	// LoggerFactory to create new logger
	private static LoggerFactory newLogger = null;
	
	/**
	 * Constructor
	 * @param initClass	name of calling class
	 */
	public TestLoggerFromCVS(Class initClass) {
		//super(initClass.toString());
		if (newLogger == null) {
			newLogger = new LoggerFactory(initClass);			
		}
		if (logger == null) {
			logger = newLogger.getLogger();	
		}
	}
	
	
	
	
	public void shutdown() {
		logger.getLoggerRepository().shutdown();
	}
	
	
	/**
	 * Start new and empty log file for new test session
	 */
	public void startNewLogFile(String loggingDir){
		newLogger.setLoggingDirectory(loggingDir);
		newLogger.startNewLogFile();
		
		logger = newLogger.getLogger();
		
	}
	
	
	/**
	 * Log string with level 'debug'
	 * @param logString
	 */
	public void debug (String logString) {
		logger.debug(logString);
	}

	/**
	 * Log printable object with level 'debug'
	 * @param obj
	 */
	public void debug (Object obj) {
		logger.debug(obj.toString());
	}

	
	/**
	 * Log string with level 'trace'
	 * @param logString
	 */
	public void trace (String logString) {
		logger.trace(logString);
	}

	/**
	 * Log printable object with level 'trace'
	 * @param obj
	 */
	public void trace (Object obj) {
		logger.trace(obj.toString());
		//console.println(obj.toString());
	}
	
	/**
	 * Log string with level 'info'
	 * @param logString
	 */
	public void info (String logString) {
		logger.info(logString);
		//console.println(logString);
	}
	
	/**
	 * Log printable object with level 'info'
	 * @param obj
	 */
	public void info (Object obj) {
		logger.info(obj.toString());
		//console.println(obj.toString());
	}


	/**
	 * Log string with level 'warn'
	 * @param logString
	 */
	public void warn (String logString) {
		logger.warn(logString);
	}

	/**
	 * Log printable object with level 'warn'
	 * @param obj
	 */
	public void warn (Object obj) {
		logger.warn(obj.toString());
	}

	
	/**
	 * Log string with level 'warn'
	 * @param logString
	 */
	public void error (String logString) {
		logger.error(logString);
		
	}

	
	/**
	 * Log printable object with level 'error'
	 * @param obj
	 */
	public void error (Object obj) {
		logger.error(obj.toString());
	}
	
	/**
	 * Log string with level 'fatal'
	 * @param logString
	 */
	public void fatal (String logString) {
		logger.fatal(logString);
	}

	/**
	 * Log printable object with level 'fatal'
	 * @param obj
	 */
	public void fatal (Object obj) {
		logger.fatal(obj.toString());
	}
		
	/**
	 * Getter for logger name (html)
	 * @return file name of html logger
	 */
	public String getHtmlFileName() {
		return newLogger.getHtmlFileName();
	}

	/**
	 * Getter for logger name (plain)
	 * @return file name of plain logger
	 */
	public String getLogFileName() {
		return newLogger.getLogFileName();
	}

	/**
	 * Setter for Level of log4J
	 * @param level of logging
	 */
	public void setLevel(String level) {
		
		if (level.equals("TRACE")) {	
			logger.setLevel(Level.TRACE);
		}
		else if (level.equals("DEBUG")) {
			logger.setLevel(Level.DEBUG);
		}
		else if (level.equals("INFO")) {
			logger.setLevel(Level.INFO);
		}
		else if (level.equals("WARN")) {
			logger.setLevel(Level.WARN);
		}
		else if (level.equals("ERROR")) {
			logger.setLevel(Level.ERROR);
		}
		else if (level.equals("FATAL")) {
			logger.setLevel(Level.FATAL);
		}
	}

	/**
	 * Getter for logger level
	 * @return level of logging
	 */
	public String getLevel() {
		if (logger.getLevel() == Level.TRACE) {	
			return "TRACE";
		}
		else if (logger.getLevel() == Level.DEBUG) {
			return "DEBUG";
		}
		else if (logger.getLevel() == Level.INFO) {
			return "INFO";
		}
		else if (logger.getLevel() == Level.WARN) {
			return "WARN";
		}
		else if (logger.getLevel() == Level.ERROR) {
			return "ERROR";
		}
		else if (logger.getLevel() == Level.FATAL) {
			return "FATAL";
		}
		return null;
	}

	public void useHTMLLogging(boolean set){
		newLogger.useHTMLLogging(set);
	}

	public void usePlainLogging(boolean set){
		newLogger.usePlainLogging(set);
	}

	public void useISO8601Logging(boolean set){
		newLogger.useISO8601Logging(set);
	}



}
