package org.globaltester.logging.legacy.logger;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;
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
	private static final String APPENDER_CONSOLE = "GTLogger_Console_Appender";
	public static final String DEFAULT_DATE_FORMAT = "yyyyMMdd_HHmmss";

	// single existing instance
	private static GTLogger instance;

	//where to log
	private String logDir;
	private String logFileName;

	// Logger
	private Logger logger;

	private FileAppender fileAppenderPlain;
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
		checkOptions();

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
	 * Log string with level 'error'
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
		String level;
		IPreferencesService prefService = Platform.getPreferencesService();
		if(prefService != null) {
			level = PreferenceConstants.LOGLEVELS[prefService.getInt(
					Activator.PLUGIN_ID, PreferenceConstants.P_GT_LOGLEVEL, 0,
					null)];
		} else {
			level = PreferenceConstants.LOGLEVELS[PreferenceConstants.LOGLEVEL_TRACE];
		}
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
		//set default logdir in workspace metadata
		
		logDir = Platform.getLocation().append(".metadata").toString();
		
		String oldLogFileName = logFileName;
		
		//get the logDir from preference service if manual settings are selected
		IPreferencesService prefService = Platform.getPreferencesService();
		boolean manualDirSettings = prefService.getBoolean(Activator.PLUGIN_ID,
				PreferenceConstants.P_MANUALDIRSETTINGS, false, null);
		if (manualDirSettings) {
			logDir = Platform.getPreferencesService().getString(Activator.PLUGIN_ID,
					PreferenceConstants.P_GT_LOGGINGDIR, logDir, null);
		}

		logFileName = logDir + File.separator + "globaltester.gtlog";

		//remove existing files
		File logFile = new File(logFileName);
		if (!logFileName.equals(oldLogFileName) && logFile.exists()) {
			if (!logFile.delete()){
				debug("Logfile "+logFileName+" could not be deleted, logging will try to append to it");
			}
		}
	}
	
	/**
	 * Returns the current date inclusive time in ISO format
	 */
	public static String getDefaultTimeString() {
		return getTimeString(DEFAULT_DATE_FORMAT);
	}

	/**
	 * Returns the current tame formatted according to dateFormat
	 */
	private static String getTimeString(String dateFormat) {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				dateFormat);
		// get current date:
		Calendar cal = Calendar.getInstance();
		return sdf.format(cal.getTime());
	}

	public void checkOptions() {
		//get the preference service
		IPreferencesService prefService = Platform.getPreferencesService();

		//set the loglevel
		setLevel();

		//configure filenames according to preferences
		if(prefService != null) {
			setFileNames();
		}

		// setting logfile layout		
		Layout fileLayout;
		if (getBooleanPreference(PreferenceConstants.P_GT_USEISO8601LOGGING, true)) {
			fileLayout = new PatternLayout("%d{yyyy-MM-ss'T'HH:mm:ss} - %-5p - %m%n");
		} else {
			fileLayout = new PatternLayout("%d{yyyy-MM-ss HH:mm:ss,SSS} - %-5p -%m%n");
		}

		// settings for 'plain' logging
		if(prefService != null) {
			if (getBooleanPreference(PreferenceConstants.P_GT_PLAINLOGGING, true)) {
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
		}

		// settings for 'console' logging
		if (getBooleanPreference(PreferenceConstants.P_GT_CONSOLELOGGING, true)) {
			if (consoleAppender == null) {
				consoleAppender = new ConsoleAppender(fileLayout);
				consoleAppender.setName(APPENDER_CONSOLE);
				logger.addAppender(consoleAppender);
			}
			consoleAppender.setLayout(fileLayout);
		} else {
			logger.removeAppender(APPENDER_CONSOLE);
		}


	}
	
	/**
	 * Get value of a boolean preference and return default if pref service does not exist
	 * @param preferenceKey the name of the preference
	 * @param defaultValue the value to use if the preference is not defined or the pref service does not exist
	 * @return
	 */
	private boolean getBooleanPreference(String preferenceKey, boolean defaultValue) {
		IPreferencesService prefService = Platform.getPreferencesService();
		if(prefService != null) {
			return prefService.getBoolean(Activator.PLUGIN_ID,
					preferenceKey, defaultValue, null);
		} else {
			return defaultValue;
		}
	}
	

	public String getLogDir() {
		return logDir;
	}

}
