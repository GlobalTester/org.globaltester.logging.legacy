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
import java.util.Calendar;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.HTMLLayout;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;
import org.eclipse.core.runtime.Platform;
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

public class GTLogger {
	// single existing instance
	private static GTLogger instance;

	//where to log
	private String logDir;
	private String logFileName;
	private String htmlFileName;

	// Logger
	private Logger logger;

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

		//get the preference store
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		//configure filenames according to preferences
		setFileNames();

		// settings for logfiles		
		Layout fileLayout;
		if (store.getBoolean(PreferenceConstants.P_GT_USEISO8601LOGGING)) {
			fileLayout = new PatternLayout("%d %-5p - %m%n");
		} else {
			fileLayout = new PatternLayout("%m%n");
		}

		// settings for 'plain' logging
		if (store.getBoolean(PreferenceConstants.P_GT_PLAINLOGGING)) {
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

		// settings for 'console' logging
		if (store.getBoolean(PreferenceConstants.P_GT_CONSOLELOGGING)) {
			ConsoleAppender consoleAppender = new ConsoleAppender(fileLayout);
			logger.addAppender(consoleAppender);

		}

		//settings for html file
		if (store.getBoolean(PreferenceConstants.P_GT_HTMLLOGGING)) {
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
				.getBoolean(PreferenceConstants.P_MANUALDIRSETTINGS);
		if (manualDirSettings) {
			logDir = store.getString(PreferenceConstants.P_GT_LOGGINGDIR);
		} else {
			//TODO AM check whether this works
			logDir = Platform.getLocation().append(".metadata").toString();
		}

		//build the filenames
		htmlFileName = logDir + "/gt_log.html";
		logFileName = logDir + "/globaltester.log";
		System.out.println(htmlFileName);
		System.out.println(logFileName);
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
}
