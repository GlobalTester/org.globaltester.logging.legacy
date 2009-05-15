package org.globaltester.logger;

/*
 * Project GlobalTester File GTLogger.java
 * 
 * Developed by HJP Consulting GmbH Hauptstr. 35 33178 Borchen Germany
 * 
 * 
 * This software is the confidential and proprietary information of HJP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * Non-Disclosure Agreement you entered into with HJP.
 */

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.FileAppender;
import org.apache.log4j.HTMLLayout;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;
import org.eclipse.jface.preference.IPreferenceStore;
import org.globaltester.logging.Activator;
import org.globaltester.logging.preferences.PreferenceConstants;

/**
 * This class implements methods for logging of GT components.
 * 
 * Logging regarding TestExecution should be done wit TestLogger
 * 
 * @version Release 2.2.0
 * @author Alexander May
 * 
 */

public class TestLogger {

	// Logger
	private static Logger logger = null;

	//where to log
	private static String logDir;
	private static String htmlFileName;
	private static String logFileName;

	private static boolean isInitialized() {
		if (logger == null) {
			GTLogger.getInstance().warn(
					"The TestLogger is not properly initialized:");
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Log printable object with level 'debug'
	 * 
	 * @param obj
	 */
	public static void debug(Object obj) {
		TestLogger.debug(obj.toString());
	}

	/**
	 * Log string with level 'debug'
	 * 
	 * @param logString
	 */
	public static void debug(String logString) {
		GTLogger.getInstance().debug(logString);
		if (isInitialized()) {
			logger.debug(logString);
		}
	}

	/**
	 * Dispose the TestLogger, TestLogger is unable to log anything until next
	 * call to init()
	 */
	public static void shutdown() {
		logger = null;
	}

	/**
	 * Log printable object with level 'error'
	 * 
	 * @param obj
	 */
	public static void error(Object obj) {
		TestLogger.error(obj.toString());
	}

	/**
	 * Log string with level 'warn'
	 * 
	 * @param logString
	 */
	public static void error(String logString) {
		GTLogger.getInstance().error(logString);
		if (isInitialized()) {
			logger.error(logString);
		}
	}

	/**
	 * Log printable object with level 'fatal'
	 * 
	 * @param obj
	 */
	public static void fatal(Object obj) {
		TestLogger.fatal(obj.toString());
	}

	/**
	 * Log string with level 'fatal'
	 * 
	 * @param logString
	 */
	public static void fatal(String logString) {
		GTLogger.getInstance().fatal(logString);
		if (isInitialized()) {
			logger.fatal(logString);
		}
	}

	/**
	 * Getter for logger level
	 * 
	 * @return level of logging
	 */
	public static String getLevel() {
		if (logger.getLevel() == Level.TRACE) {
			return "TRACE";
		} else if (logger.getLevel() == Level.DEBUG) {
			return "DEBUG";
		} else if (logger.getLevel() == Level.INFO) {
			return "INFO";
		} else if (logger.getLevel() == Level.WARN) {
			return "WARN";
		} else if (logger.getLevel() == Level.ERROR) {
			return "ERROR";
		} else if (logger.getLevel() == Level.FATAL) {
			return "FATAL";
		}
		return null;
	}

	/**
	 * Log printable object with level 'info'
	 * 
	 * @param obj
	 */
	public static void info(Object obj) {
		TestLogger.info(obj.toString());
	}

	/**
	 * Log string with level 'info'
	 * 
	 * @param logString
	 */
	public static void info(String logString) {
		GTLogger.getInstance().info(logString);
		if (isInitialized()) {
			logger.info(logString);
		}
	}

	/**
	 * Initialize a the TestLogger for a new Test
	 */
	public static void init() {
		if (logger != null) {
			GTLogger.getInstance().error(
					"Only one TestLogger is allowed to be active at a time!");
			throw new RuntimeException(
					"Only one TestLogger is allowed to be active at a time");
		}

		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		//get the Logger from log4j
		BasicConfigurator.configure();
		logger = Logger.getLogger("TestLogger");

		//clean the logger (just to be sure)
		logger.removeAllAppenders();
		Logger.getRootLogger().removeAllAppenders();

		//configure filenames according to preferences
		setFileNames();

		// settings for logfiles		
		Layout fileLayout;
		if (store.getBoolean(PreferenceConstants.P_TEST_USEISO8601LOGGING)) {
			fileLayout = new PatternLayout("%d %-5p - %m%n");
		} else {
			fileLayout = new PatternLayout("%m%n");
		}

		// settings for 'plain' logging
		if (store.getBoolean(PreferenceConstants.P_TEST_PLAINLOGGING)) {
			try {
				FileAppender fileAppender = new FileAppender(fileLayout,
						logFileName);
				logger.addAppender(fileAppender);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			logFileName = "";
		}

		//settings for html file
		if (store.getBoolean(PreferenceConstants.P_TEST_HTMLLOGGING)) {
			HTMLLayout htmlLayout = new HTMLLayout();
			htmlLayout.setTitle(htmlFileName);
			WriterAppender writerAppender = null;
			try {
				FileOutputStream output = new FileOutputStream(htmlFileName);
				writerAppender = new WriterAppender(htmlLayout, output);
			} catch (Exception e) {
				e.printStackTrace();
			}
			writerAppender.activateOptions();
			logger.addAppender(writerAppender);
		} else {
			htmlFileName = "";
		}

	}

	/**
	 * Setter for Level of log4J
	 * 
	 * @param level
	 *            of logging
	 */
	public static void setLevel(String level) {

		if (level.equals("TRACE")) {
			logger.setLevel(Level.TRACE);
		} else if (level.equals("DEBUG")) {
			logger.setLevel(Level.DEBUG);
		} else if (level.equals("INFO")) {
			logger.setLevel(Level.INFO);
		} else if (level.equals("WARN")) {
			logger.setLevel(Level.WARN);
		} else if (level.equals("ERROR")) {
			logger.setLevel(Level.ERROR);
		} else if (level.equals("FATAL")) {
			logger.setLevel(Level.FATAL);
		}
	}

	/**
	 * Log printable object with level 'trace'
	 * 
	 * @param obj
	 */
	public static void trace(Object obj) {
		TestLogger.trace(obj.toString());
	}

	/**
	 * Log string with level 'trace'
	 * 
	 * @param logString
	 */
	public static void trace(String logString) {
		GTLogger.getInstance().trace(logString);
		if (isInitialized()) {
			logger.trace(logString);
		}
	}

	/**
	 * Log printable object with level 'warn'
	 * 
	 * @param obj
	 */
	public static void warn(Object obj) {
		TestLogger.warn(obj.toString());
	}

	/**
	 * Log string with level 'warn'
	 * 
	 * @param logString
	 */
	public static void warn(String logString) {
		GTLogger.getInstance().warn(logString);
		if (isInitialized()) {
			logger.warn(logString);
		}
	}

	/**
	 * private default Constructor makes sure this class is not instantiated
	 */
	private TestLogger() {
	}

	public static String getLogFileName() {
		return logFileName;
	}

	/**
	 * Initialize a the TestLogger for a new Test
	 * 
	 * @param defaultLoggingDir
	 *            will be used as logging directory if user has not selected
	 *            manual directory
	 */
	public static void init(String defaultLoggingDir) {
		logDir = defaultLoggingDir;
		init();
	}

	/**
	 * Create file names for log files. Add iso formated date to file name.
	 */
	private static void setFileNames() {
		//overwrite the logDir with value from PreferenceStore if manual settings are selected
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		boolean manualDirSettings = store
				.getBoolean(PreferenceConstants.P_MANUALDIRSETTINGS);
		if (manualDirSettings) {
			logDir = store.getString(PreferenceConstants.P_TEST_LOGGINGDIR);
		}

		//build the filenames
		htmlFileName = logDir + "/gt_" + GTLogger.getIsoDate("yyyyMMddHHmmss")
				+ ".html";
		logFileName = logDir + "/gt_" + GTLogger.getIsoDate("yyyyMMddHHmmss")
				+ ".log";
	}

}
