package org.globaltester.logging.legacy.logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import org.apache.log4j.FileAppender;
import org.apache.log4j.PatternLayout;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.jface.preference.IPreferenceStore;
import org.globaltester.logging.BasicLogger;
import org.globaltester.logging.legacy.Activator;
import org.globaltester.logging.legacy.preferences.PreferenceConstants;
import org.globaltester.logging.tags.LogLevel;

/**
 * This class implements methods for logging of messages regarding TestExecution
 * 
 * @author amay
 * 
 */

public class TestLogger {

	private static final String APPENDER_TESTCASE = "TestLogger_TestCase_Appender";
	public static final String DEFAULTFORMAT = "%-66s -";


	// Appender for single log files per test case
	private static FileAppender testCaseAppender = null;

	// where to log
	private static String logDir;
	private static String logDate;
	private static String logFileName;
	private static String testCaseLogFileName;
	private static PatternLayout fileLayout;
	private static String currentTestExecutable;
	
	//line number of log file
	private static int logFileLine;
	private static LineNumberReader lnr;

	private static boolean initialized;
	private static LogLevel level;
	
	public static boolean isInitialized() {
		return initialized;
	}

	public static boolean isTestCaseInitialized() {
		return testCaseAppender != null;
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
			BasicLogger.log(logString, LogLevel.DEBUG);
		}
	}

	/**
	 * Dispose the TestLogger, TestLogger is unable to log anything until next
	 * call to init()
	 */
	public static void shutdown() {
		if (lnr != null) {
			try {
				lnr.close();
			} catch (IOException e) {
				GtErrorLogger.log(Activator.PLUGIN_ID, e);
			}
			lnr = null;
		}
		initialized = false;;
	}

	/**
	 * Dispose the TestCaseLogger, following log messages will go only to the
	 * session log until the next call to initTestCase()
	 */
	public static void shutdownTestExecutableLogger() {
		if (isTestCaseInitialized()) {
			
			String format = "End execution of %-49s -"; //format the output to be 100 chars wide (including log4j start of line)
			TestLogger.info(String.format(format, currentTestExecutable));
			TestLogger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< -");
			testCaseAppender.close();
		}
		testCaseAppender = null;
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
	 * Log string with level 'error'
	 * 
	 * @param logString
	 */
	public static void error(String logString) {
		GTLogger.getInstance().error(logString);
		if (isInitialized()) {
			BasicLogger.log(logString, LogLevel.ERROR);
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
			BasicLogger.log(logString, LogLevel.FATAL);
		}
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
			BasicLogger.log(logString, LogLevel.INFO);
		}
	}

	/**
	 * Initialize a the TestLogger for a new Test
	 */
	public static void init() {
		if (initialized) {
			GTLogger.getInstance().error(
					"Only one TestLogger is allowed to be active at a time!");
			throw new RuntimeException(
					"Only one TestLogger is allowed to be active at a time");
		}

		IPreferencesService prefService = Platform.getPreferencesService();
		
		// set the loglevel
		level = LogLevel.valueOf(PreferenceConstants.LOGLEVELS[prefService.getInt(
				Activator.PLUGIN_ID, PreferenceConstants.P_TEST_LOGLEVEL, 0,
				null)]);
		
		//FIXME configure the filelogger level 

		// configure filenames according to preferences
		setFileNames();

		
		//FIXME configure log format for file logger
		// settings for logfiles
		if (prefService.getBoolean(Activator.PLUGIN_ID,
				PreferenceConstants.P_TEST_USEISO8601LOGGING, true, null)) {
			fileLayout = new PatternLayout("%d %-5p - %m%n");
		} else {
			fileLayout = new PatternLayout("%m%n");
		}

		//FIXME configure file logger file name
		
		try {
			if (lnr != null) {
				lnr.close();
			}
			lnr = new LineNumberReader(new FileReader(new File(getLogFileName())));
		} catch (IOException e) {
			GtErrorLogger.log(Activator.PLUGIN_ID, e);
		}
		
		
	}

	/**
	 * Sets the logging level according to preferences
	 */
	public static void setLevel() {
		if (!isInitialized()) {
			return;
		}

		level = LogLevel.valueOf(PreferenceConstants.LOGLEVELS[Platform.getPreferencesService().getInt(Activator.PLUGIN_ID, PreferenceConstants.P_TEST_LOGLEVEL, 0,
				null)]);
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
			BasicLogger.log(logString, LogLevel.TRACE);
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
			BasicLogger.log(logString, LogLevel.WARN);
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
	 * This method returns the name of the current log file that is used for the current TestCase.<br>
	 * It is either the name of the log file for the TestCase if single  log files for TestCases is requested 
	 * (e.g. triggered if "Single Log File for Each TestCase" is set in the Preferences),<br>
	 * or the name of the log file for the TestCampaign.
	 * @return	 name of the current log file
	 */
	public static String getTestCaseLogFileName() {
		if(testCaseLogFileName != null && testCaseLogFileName.equals("")){
			return getLogFileName();
		}
		return testCaseLogFileName;
	}

	/**
	 * Initialize the TestLogger for a new Test session
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
	 * Initialize the TestLogger for a new TestCase
	 * 
	 * @param executableId
	 *            will be used as part of the logfile name of the current
	 *            testcase
	 */
	public static void initTestExecutable(String executableId) {
		if (!isInitialized()) {
			throw new RuntimeException(
					"TestLogger must be initialized before initializing for a testcase");
		}

		shutdownTestExecutableLogger();
		
		currentTestExecutable = executableId;

		// do not setup the logfile if single logging is disabled in the
		// preferences
		if (! Platform.getPreferencesService().getBoolean(Activator.PLUGIN_ID,
				PreferenceConstants.P_TEST_LOG_SINGLE_TESTCASES, true, null)) {
			testCaseLogFileName = "";
			logFileLine = 0;
			return;
		}

		testCaseLogFileName = getTestCaseLogFileName(executableId);

		// create new Appender for current TestCase and add it to logger
		try {
			testCaseAppender = new FileAppender(fileLayout, testCaseLogFileName);
			testCaseAppender
					.setName(APPENDER_TESTCASE + "(" + executableId + ")");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Put info of new TestExecutable into log file(s)
		TestLogger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> -");
		String format = "Starting new test executable %-37s -"; //format the output to be 100 chars wide (including log4j start of line)
		TestLogger.info(String.format(format, currentTestExecutable));
		
		try {
			if (lnr != null) {
				lnr.close();
			}
			lnr = new LineNumberReader(new FileReader(new File(getTestCaseLogFileName())));
		} catch (IOException e) {
			GtErrorLogger.log(Activator.PLUGIN_ID, e);
		}
	}

	/**
	 * Create file names for log files. Add iso formated date to file name.
	 */
	private static void setFileNames() {
		// overwrite the logDir with value from PreferenceStore if manual
		// settings are selected
		
		boolean manualDirSettings = Platform.getPreferencesService().getBoolean(Activator.PLUGIN_ID,
				PreferenceConstants.P_MANUALDIRSETTINGS, false, null); 
		if (manualDirSettings) {
			logDir = Platform.getPreferencesService().getString(Activator.PLUGIN_ID,
					PreferenceConstants.P_TEST_LOGGINGDIR, logDir, null);
		}

		// build the filenames
		logDate = GTLogger.getDefaultTimeString();
		logFileName = logDir + File.separator + "gt_" + logDate + ".gtlog";
	}

	/**
	 * construct and return logFileName for a single test case
	 * 
	 * @param testCaseId
	 * @return
	 */
	public static String getTestCaseLogFileName(String testCaseId) {
		return logDir + File.separator + "gt_" + logDate + "_" + testCaseId
				+ ".gtlog";
	}
	
	public static int getLogFileLine(){
		//count line numbers
		try {
			if(lnr != null){
				lnr.skip(Long.MAX_VALUE);
				//increase line number because counting of lnr starts with 0
				logFileLine = lnr.getLineNumber()+1;
			}
		} catch (IOException e) {
			GtErrorLogger.log(Activator.PLUGIN_ID, e);
		}
		
		return logFileLine;
	}
	
	public static String getLogDir() {
		return logDir;
	}
	
	/**
	 * Initialize the TestLogger for a new TestCase
	 * 
	 * @param testCaseId
	 *            will be used as part of the logfile name of the current
	 *            testcase
	 */
	public static void initTestCase(String testCaseId) {
		if (!isInitialized()) {
			throw new RuntimeException(
					"TestLogger must be initialized before initializing for a testcase");
		}
		
		shutdownTestCaseLogger();
		
		//do not setup the logfile if single logging is disabled in the preferences
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		if (!store.getBoolean(PreferenceConstants.P_TEST_LOG_SINGLE_TESTCASES)) {
			testCaseLogFileName = "";
			return;
		} 

		testCaseLogFileName = getTestCaseLogFileName(testCaseId);
		
		// create new Appender for current TestCase and add it to logger
		try {
			testCaseAppender = new FileAppender(fileLayout, testCaseLogFileName);
			testCaseAppender
					.setName(APPENDER_TESTCASE + "(" + testCaseId + ")");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Dispose the TestCaseLogger, following log messages will go only to the
	 * session log until the next call to initTestCase()
	 */
	public static void shutdownTestCaseLogger() {
		testCaseAppender = null;
	}

}
