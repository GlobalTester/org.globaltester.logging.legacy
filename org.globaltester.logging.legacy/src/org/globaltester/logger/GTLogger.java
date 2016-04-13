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

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.HTMLLayout;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.globaltester.logging.legacy.Activator;
import org.globaltester.logging.legacy.preferences.PreferenceConstants;

/**
 * This class implements methods for logging of GT components.
 * 
 * Logging regarding TestExecution should be done wit TestLogger
 * 
 * @version Release 2.2.0
 * @author Alexander May
 * 
 */

public class GTLogger {

	private static final String APPENDER_PLAIN = "GTLogger_Plain_Appender";
	private static final String APPENDER_HTML = "GTLogger_HTML_Appender";
	private static final String APPENDER_CONSOLE = "GTLogger_Console_Appender";

	// single existing instance
	private static GTLogger instance;

	//where to log
	private String logDir;
	private String logFileName;
	private String htmlFileName;

	// Logger
	private Logger logger;

	private FileAppender fileAppenderPlain;
	private FileAppender fileAppenderHtml;
	private ConsoleAppender consoleAppender;

	/**
	 * Constructor
	 */
	private GTLogger() {
		//get the Logger from log4j
		BasicConfigurator.configure();
		logger = Logger.getLogger("GTLogger");

		//clean the logger (just to be sure)
		logger.removeAllAppenders();
		Logger.getRootLogger().removeAllAppenders();

		//initialize all appenders with the options from preferences
		if (Platform.isRunning()){
			checkOptions();	
		} else {
			logger.addAppender(new ConsoleAppender(new PatternLayout("%m%n")));
		}

	}

	/**
	 * @return the instance
	 */
	public static GTLogger getInstance() {
		if (instance == null) {
			instance = new GTLogger();
		}
		return instance;
	}

	/**
	 * Log string with level 'debug'
	 * 
	 * @param logString
	 */
	public void debug(String logString) {
		logger.debug(logString);
	}

	/**
	 * Log printable object with level 'debug'
	 * 
	 * @param obj
	 */
	public void debug(Object obj) {
		logger.debug(obj.toString());
	}

	/**
	 * Log string with level 'trace'
	 * 
	 * @param logString
	 */
	public void trace(String logString) {
		logger.trace(logString);
	}

	/**
	 * Log printable object with level 'trace'
	 * 
	 * @param obj
	 */
	public void trace(Object obj) {
		logger.trace(obj.toString());
	}

	/**
	 * Log string with level 'info'
	 * 
	 * @param logString
	 */
	public void info(String logString) {
		logger.info(logString);
	}

	/**
	 * Log printable object with level 'info'
	 * 
	 * @param obj
	 */
	public void info(Object obj) {
		logger.info(obj.toString());
	}

	/**
	 * Log string with level 'warn'
	 * 
	 * @param logString
	 */
	public void warn(String logString) {
		logger.warn(logString);
	}

	/**
	 * Log printable object with level 'warn'
	 * 
	 * @param obj
	 */
	public void warn(Object obj) {
		logger.warn(obj.toString());
	}

	/**
	 * Log string with level 'warn'
	 * 
	 * @param logString
	 */
	public void error(String logString) {
		logger.error(logString);

	}

	/**
	 * Log printable object with level 'error'
	 * 
	 * @param obj
	 */
	public void error(Object obj) {
		logger.error(obj.toString());
	}

	/**
	 * Log string with level 'fatal'
	 * 
	 * @param logString
	 */
	public void fatal(String logString) {
		logger.fatal(logString);
	}

	/**
	 * Log printable object with level 'fatal'
	 * 
	 * @param obj
	 */
	public void fatal(Object obj) {
		logger.fatal(obj.toString());
	}

	/**
	 * Sets the logging level according to preferences
	 */
	public void setLevel() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String level = PreferenceConstants.LOGLEVELS[store
				.getInt(PreferenceConstants.P_GT_LOGLEVEL)];
		logger.setLevel(Level.toLevel(level));
	}

	/**
	 * Setter for Level of log4J
	 * 
	 * @param level
	 *            of logging
	 */
	public void setLevel(String level) {

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
	 * Getter for logger level
	 * 
	 * @return level of logging
	 */
	public String getLevel() {
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
	 * Create file names for log files. Add iso formated date to file name.
	 */
	private void setFileNames() {
		//get the logDir from PreferenceStore if manual settings are selected
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		boolean manualDirSettings = store
				.getBoolean(PreferenceConstants.P_MANUALFRAMEWORKDIRSETTINGS);
		if (manualDirSettings) {
			logDir = store.getString(PreferenceConstants.P_GT_LOGGINGDIR);
		} else {
			logDir = Platform.getLocation().append(".metadata").toString();
		}

		//build the filenames
		htmlFileName = logDir + File.separator + "gt_log.html";
		logFileName = logDir + File.separator + "globaltester.gtlog";

		//remove existing files
		File htmlLogFile = new File(htmlFileName);
		if (htmlLogFile.exists()) {
			htmlLogFile.delete();
		}
		File logFile = new File(logFileName);
		if (logFile.exists()) {
			logFile.delete();
		}
	}

	/**
	 * Returns the current date inclusive time in ISO format
	 */
	public static String getIsoDate(String DATE_FORMAT) {
		//String DATE_FORMAT = "yyyyMMddHHmmss";
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				DATE_FORMAT);
		// get current date:
		Calendar cal = Calendar.getInstance();
		return sdf.format(cal.getTime());
	}

	public void checkOptions() {
		//get the preference store
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		//set the loglevel
		setLevel();

		//configure filenames according to preferences
		setFileNames();

		// setting logfile layout		
		Layout fileLayout;
		if (store.getBoolean(PreferenceConstants.P_GT_USEISO8601LOGGING)) {
			fileLayout = new PatternLayout("%d %-5p - %m%n");
		} else {
			fileLayout = new PatternLayout("%m%n");
		}

		// settings for 'plain' logging
		if (store.getBoolean(PreferenceConstants.P_GT_PLAINLOGGING)) {
			if (fileAppenderPlain == null) {
				try {
					fileAppenderPlain = new FileAppender(fileLayout,
							logFileName);
					fileAppenderPlain.setName(APPENDER_PLAIN);
					logger.addAppender(fileAppenderPlain);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				fileAppenderPlain.setLayout(fileLayout);
				fileAppenderPlain.setFile(logFileName);
			}
		} else {
			logFileName = "";
			logger.removeAppender(APPENDER_PLAIN);
		}

		// settings for 'console' logging
		if (store.getBoolean(PreferenceConstants.P_GT_CONSOLELOGGING)) {
			if (consoleAppender == null) {
				consoleAppender = new ConsoleAppender(fileLayout);
				consoleAppender.setName(APPENDER_CONSOLE);
				logger.addAppender(consoleAppender);
			}
			consoleAppender.setLayout(fileLayout);
		} else {
			logger.removeAppender(APPENDER_CONSOLE);
		}

		//settings for html file
		if (store.getBoolean(PreferenceConstants.P_GT_HTMLLOGGING)) {
			if (fileAppenderHtml == null) {
				HTMLLayout htmlLayout = new HTMLLayout();
				htmlLayout.setTitle(htmlFileName);
				try {
					fileAppenderHtml = new FileAppender(htmlLayout,
							htmlFileName);
					fileAppenderHtml.setName(APPENDER_HTML);
					logger.addAppender(fileAppenderHtml);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			fileAppenderHtml.setFile(htmlFileName);
		} else {
			htmlFileName = "";
			logger.removeAppender(APPENDER_HTML);
		}

	}

}
