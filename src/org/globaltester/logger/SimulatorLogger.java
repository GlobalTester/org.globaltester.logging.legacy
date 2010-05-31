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

import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.FileAppender;
import org.apache.log4j.HTMLLayout;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.eclipse.jface.preference.IPreferenceStore;
import org.globaltester.logging.Activator;
import org.globaltester.logging.preferences.PreferenceConstants;

/**
 * This class implements methods for logging of messages regarding GT Simulator
 * 
 * @version Release 2.2.0
 * @author Alexander May
 * 
 */

public class SimulatorLogger {

	private static final String APPENDER_PLAIN = "GTSimulatorLogger_Plain_Appender";
	private static final String APPENDER_HTML = "GTSimulatorLogger_HTML_Appender";

	// Logger
	private static Logger logger = null;

	//where to log
	private static String logDir;
	private static String htmlFileName;
	private static String logFileName;

	public static boolean isInitialized() {
		if (logger == null) {
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
		SimulatorLogger.debug(obj.toString());
	}

	/**
	 * Log string with level 'debug'
	 * 
	 * @param logString
	 */
	public static void debug(String logString) {
		TestLogger.debug(logString);
		if (isInitialized()) {
			logger.debug(logString);
		}
	}

	/**
	 * Dispose the SimulatorLogger, SimulatorLogger is unable to log anything until next
	 * call to init()
	 */
	public static void shutdown() {
		if (logger != null) {
			logger.removeAllAppenders();
		}
		logger = null;
	}

	/**
	 * Log printable object with level 'error'
	 * 
	 * @param obj
	 */
	public static void error(Object obj) {
		SimulatorLogger.error(obj.toString());
	}

	/**
	 * Log string with level 'warn'
	 * 
	 * @param logString
	 */
	public static void error(String logString) {
		TestLogger.error(logString);
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
		SimulatorLogger.fatal(obj.toString());
	}

	/**
	 * Log string with level 'fatal'
	 * 
	 * @param logString
	 */
	public static void fatal(String logString) {
		TestLogger.fatal(logString);
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
		SimulatorLogger.info(obj.toString());
	}

	/**
	 * Log string with level 'info'
	 * 
	 * @param logString
	 */
	public static void info(String logString) {
		TestLogger.info(logString);
		if (isInitialized()) {
			logger.info(logString);
		}
	}

	/**
	 * Initialize a the TestLogger for a new Test
	 */
	public static void init() {
		if (logger != null) {
			TestLogger.error(
					"Only one SimulatorLogger is allowed to be active at a time!");
			throw new RuntimeException(
					"Only one SimulatorLogger is allowed to be active at a time");
		}

		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		//get the Logger from log4j
		BasicConfigurator.configure();
		logger = Logger.getLogger("GTSimulatorLogger");

		//clean the logger (just to be sure)
		logger.removeAllAppenders();
		Logger.getRootLogger().removeAllAppenders();

		//set the loglevel
		String level = PreferenceConstants.LOGLEVELS[store
				.getInt(PreferenceConstants.P_GT_SIM_LOGLEVEL)];
		logger.setLevel(Level.toLevel(level));

		//configure filenames according to preferences
		setFileNames();

		// settings for logfiles		
		Layout fileLayout;
		if (store.getBoolean(PreferenceConstants.P_GT_SIM_USEISO8601LOGGING)) {
			fileLayout = new PatternLayout("%d %-5p - %m%n");
		} else {
			fileLayout = new PatternLayout("%m%n");
		}

		// settings for 'plain' logging
		if (store.getBoolean(PreferenceConstants.P_GT_SIM_PLAINLOGGING)) {
			try {
				FileAppender fileAppenderPlain = new FileAppender(fileLayout,
						logFileName);
				fileAppenderPlain.setName(APPENDER_PLAIN);
				fileAppenderPlain.setImmediateFlush(true);
				logger.addAppender(fileAppenderPlain);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			logFileName = "";
		}

		//settings for html file
		if (store.getBoolean(PreferenceConstants.P_GT_SIM_HTMLLOGGING)) {
			HTMLLayout htmlLayout = new HTMLLayout();
			htmlLayout.setTitle(htmlFileName);
			try {
				FileAppender fileAppenderHtml = new FileAppender(htmlLayout,
						htmlFileName);
				fileAppenderHtml.setName(APPENDER_HTML);
				logger.addAppender(fileAppenderHtml);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			htmlFileName = "";
		}

	}

	/**
	 * Sets the logging level according to preferences
	 */
	public static void setLevel() {
		if (logger == null) {
			return;
		}

		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String level = PreferenceConstants.LOGLEVELS[store
				.getInt(PreferenceConstants.P_GT_SIM_LOGLEVEL)];
		logger.setLevel(Level.toLevel(level));
	}

	/**
	 * Setter for Level of log4J
	 * 
	 * @param level
	 *            of logging
	 */
	public static void setLevel(String level) {

		if (logger == null) {
			return;
		}

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
		SimulatorLogger.trace(obj.toString());
	}

	/**
	 * Log string with level 'trace'
	 * 
	 * @param logString
	 */
	public static void trace(String logString) {
		TestLogger.trace(logString);
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
		SimulatorLogger.warn(obj.toString());
	}

	/**
	 * Log string with level 'warn'
	 * 
	 * @param logString
	 */
	public static void warn(String logString) {
		TestLogger.warn(logString);
		if (isInitialized()) {
			logger.warn(logString);
		}
	}

	/**
	 * private default Constructor makes sure this class is not instantiated
	 */
	private SimulatorLogger() {

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
			logDir = store.getString(PreferenceConstants.P_GT_SIM_LOGGINGDIR);
		}

		//build the filenames
		htmlFileName = logDir + "/gt_sim_" + GTLogger.getIsoDate("yyyyMMddHHmmss")
				+ ".html";
		logFileName = logDir + "/gt_sim_" + GTLogger.getIsoDate("yyyyMMddHHmmss")
				+ ".log";
	}

}
