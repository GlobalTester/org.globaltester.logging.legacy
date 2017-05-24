package org.globaltester.logging.legacy.logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.FileAppender;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.jface.preference.IPreferenceStore;
import org.globaltester.logging.BasicLogger;
import org.globaltester.logging.LogListenerConfig;
import org.globaltester.logging.Message;
import org.globaltester.logging.MessageCoderJson;
import org.globaltester.logging.filelogger.FileLogger;
import org.globaltester.logging.filelogger.OsgiLogger;
import org.globaltester.logging.filter.AndFilter;
import org.globaltester.logging.filter.LogFilter;
import org.globaltester.logging.filter.TagFilter;
import org.globaltester.logging.filter.TagFilter.Mode;
import org.globaltester.logging.format.LogFormatService;
import org.globaltester.logging.legacy.Activator;
import org.globaltester.logging.legacy.preferences.PreferenceConstants;
import org.globaltester.logging.tags.LogLevel;
import org.osgi.service.log.LogEntry;

/**
 * This class implements methods for logging of messages regarding
 * TestExecution.
 * 
 * @author mboonk
 * 
 */

public class TestLoggerContext {

	private static final DateFormat DATE_FORMAT_GT = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss,SSS");
	private static final DateFormat DATE_FORMAT_ISO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	// Appender for single log files per test case
	private FileAppender testCaseAppender = null;

	// where to log
	private String logDir;
	private String logDate;
	private String logFileName;
	private String testCaseLogFileName;
	private String currentTestExecutable;
	
	//line number of log file
	private Integer logFileLine;
	private LineNumberReader lnr;

	private boolean initialized;
	private boolean testcaseInitialized;
	
	private LogLevel level;
	private OsgiLogger osgiLogger;
	private OsgiLogger osgiLoggerTestCase;
	
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss,SSS");
	
	
	public boolean isInitialized() {
		return initialized;
	}
	

	public boolean isTestCaseInitialized() {
		return testcaseInitialized;
	}

	/**
	 * Log printable object with level 'debug'
	 * 
	 * @param obj
	 */
	public void debug(Object obj) {
		debug(obj.toString());
	}

	/**
	 * Log string with level 'debug'
	 * 
	 * @param logString
	 */
	public void debug(String logString) {
		GTLogger.getInstance().debug(logString);
		if (isInitialized()) {
			BasicLogger.log(logString, LogLevel.DEBUG);
		}
	}

	/**
	 * Log printable object with level 'error'
	 * 
	 * @param obj
	 */
	public void error(Object obj) {
		error(obj.toString());
	}

	/**
	 * Log string with level 'error'
	 * 
	 * @param logString
	 */
	public void error(String logString) {
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
	public void fatal(Object obj) {
		fatal(obj.toString());
	}

	/**
	 * Log string with level 'fatal'
	 * 
	 * @param logString
	 */
	public void fatal(String logString) {
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
	public void info(Object obj) {
		info(obj.toString());
	}

	/**
	 * Log string with level 'info'
	 * 
	 * @param logString
	 */
	public void info(String logString) {
		GTLogger.getInstance().info(logString);
		if (isInitialized()) {
			BasicLogger.log(logString, LogLevel.INFO);
		}
	}

	/**
	 * Sets the logging level according to preferences
	 */
	public void setLevel() {
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
	public void trace(Object obj) {
		trace(obj.toString());
	}

	/**
	 * Log string with level 'trace'
	 * 
	 * @param logString
	 */
	public void trace(String logString) {
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
	public void warn(Object obj) {
		warn(obj.toString());
	}

	/**
	 * Log string with level 'warn'
	 * 
	 * @param logString
	 */
	public void warn(String logString) {
		GTLogger.getInstance().warn(logString);
		if (isInitialized()) {
			BasicLogger.log(logString, LogLevel.WARN);
		}
	}
	
	/**
	 * Initialize a the TestLogger for a new Test
	 */
	public void init() {
		if (isInitialized()) {
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
	
		// configure filenames according to preferences
		setFileNames();
		
		// settings for logfiles
		if (prefService.getBoolean(Activator.PLUGIN_ID,
				PreferenceConstants.P_TEST_USEISO8601LOGGING, true, null)) {
			dateFormat = DATE_FORMAT_ISO;
		} else {
			dateFormat= DATE_FORMAT_GT;
		}
			
		FileLogger logger;
		try {
			logger = new FileLogger(new File(getLogFileName()));
			logger.setConfig(getConfig(dateFormat, level));
			osgiLogger = new OsgiLogger(Activator.getDefault().getBundle().getBundleContext(), logger);
			osgiLogger.start();
		} catch (IOException e1) {
			GtErrorLogger.log(Activator.PLUGIN_ID, e1);
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
		initialized = true;
		
	}

	/**
	 * Initialize the TestLogger for a new Test session
	 * 
	 * @param defaultLoggingDir
	 *            will be used as logging directory if user has not selected
	 *            manual directory
	 */
	public void init(String defaultLoggingDir) {
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
	public void initTestExecutable(String executableId) {
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
	
		FileLogger logger;
		try {
			logger = new FileLogger(new File(testCaseLogFileName));
			logger.setConfig(getConfig(dateFormat, LogLevel.ERROR));
			osgiLoggerTestCase = new OsgiLogger(Activator.getDefault().getBundle().getBundleContext(), logger);
			osgiLoggerTestCase.start();
		} catch (IOException e1) {
			GtErrorLogger.log(Activator.PLUGIN_ID, e1);
		}
		
		// Put info of new TestExecutable into log file(s)
		info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> -");
		String format = "Starting new test executable %-37s -"; //format the output to be 100 chars wide (including log4j start of line)
		info(String.format(format, currentTestExecutable));
		
		try {
			if (lnr != null) {
				lnr.close();
			}
			lnr = new LineNumberReader(new FileReader(new File(getTestCaseLogFileName())));
		} catch (IOException e) {
			GtErrorLogger.log(Activator.PLUGIN_ID, e);
		}
		
		testcaseInitialized = true;
	}

	/**
	 * Initialize the TestLogger for a new TestCase
	 * 
	 * @param testCaseId
	 *            will be used as part of the logfile name of the current
	 *            testcase
	 */
	public void initTestCase(String testCaseId) {
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

		
		FileLogger logger;
		try {
			logger = new FileLogger(new File(testCaseLogFileName));
			logger.setConfig(getConfig(dateFormat, LogLevel.ERROR));
			osgiLoggerTestCase = new OsgiLogger(Activator.getDefault().getBundle().getBundleContext(), logger);
			osgiLoggerTestCase.start();
		} catch (IOException e) {
			GtErrorLogger.log(Activator.PLUGIN_ID, e);
		}
		
		testcaseInitialized = true;
	}

	/**
	 * Dispose the TestLogger, TestLogger is unable to log anything until next
	 * call to init()
	 */
	public void shutdown() {
		osgiLogger.stop();
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
	public void shutdownTestExecutableLogger() {
		if (isTestCaseInitialized()) {
			
			String format = "End execution of %-49s -"; //format the output to be 100 chars wide (including log4j start of line)
			info(String.format(format, currentTestExecutable));
			info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< -");
			testCaseAppender.close();
		}
		testcaseInitialized = false;
		if (osgiLoggerTestCase != null){
			osgiLoggerTestCase.stop();	
		}
	}

	/**
	 * Dispose the TestCaseLogger, following log messages will go only to the
	 * session log until the next call to initTestCase()
	 */
	public void shutdownTestCaseLogger() {
		testcaseInitialized = false;
		if (osgiLoggerTestCase != null){
			osgiLoggerTestCase.stop();	
		}
	}

	public String getLogFileName() {
		return logFileName;
	}

	/**
	 * This method returns the name of the current log file that is used for the current TestCase.<br>
	 * It is either the name of the log file for the TestCase if single  log files for TestCases is requested 
	 * (e.g. triggered if "Single Log File for Each TestCase" is set in the Preferences),<br>
	 * or the name of the log file for the TestCampaign.
	 * @return	 name of the current log file
	 */
	public String getTestCaseLogFileName() {
		if(testCaseLogFileName != null && testCaseLogFileName.equals("")){
			return getLogFileName();
		}
		return testCaseLogFileName;
	}

	private LogListenerConfig getConfig(DateFormat dateFormat, LogLevel maxLevel){
		return new LogListenerConfig() {
			
			@Override
			public LogFormatService getFormat() {
				return new LogFormatService() {
					
					@Override
					public String format(LogEntry entry) {
						Message message = MessageCoderJson.decode(entry.getMessage());
						return dateFormat.format(new Date(entry.getTime())) + " "
								+ message.getLogTags().stream()
										.filter(p -> p.getId().equals(BasicLogger.LOG_LEVEL_TAG_ID)).findFirst().get()
										.getAdditionalData()[0]
								+ " - " + message.getMessageContent();
					}
				};
			}
			
			@Override
			public LogFilter getFilter() {
				return new AndFilter(
						new TagFilter(BasicLogger.LOG_LEVEL_TAG_ID, Mode.AT_LEAST_ONE, LogLevel.getUpToAsNames(level)),
						new TagFilter(BasicLogger.ORIGIN_THREAD_GROUP_TAG_ID, Thread.currentThread().getThreadGroup().getName()));
			}
		};
	}
	
	/**
	 * Create file names for log files. Add iso formated date to file name.
	 */
	private void setFileNames() {
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
	public String getTestCaseLogFileName(String testCaseId) {
		return logDir + File.separator + "gt_" + logDate + "_" + testCaseId
				+ ".gtlog";
	}
	
	public int getLogFileLine(){
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
	
	public String getLogDir() {
		return logDir;
	}

}