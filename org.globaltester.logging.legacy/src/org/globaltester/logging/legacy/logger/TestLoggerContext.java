package org.globaltester.logging.legacy.logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.globaltester.logging.BasicLogger;
import org.globaltester.logging.LogListenerConfig;
import org.globaltester.logging.filelogger.FileLogger;
import org.globaltester.logging.filter.AndFilter;
import org.globaltester.logging.filter.LogFilter;
import org.globaltester.logging.filter.TagFilter;
import org.globaltester.logging.format.GtFileLogFormatter;
import org.globaltester.logging.format.LogFormatService;
import org.globaltester.logging.legacy.Activator;
import org.globaltester.logging.legacy.preferences.PreferenceConstants;
import org.globaltester.logging.tags.LogLevel;

/**
 * This class manages all context needed for logging of a specific test run.
 * This includes configuration and formats of the logging to files.
 * 
 * @author mboonk
 * 
 */

public class TestLoggerContext {

	// where to log
	private String logDir;
	private String logDate;
	private String logFileName;
	private String testCaseLogFileName;
	
	//line number of log file
	private Integer logFileLine;
	private LineNumberReader lnr;
	
	private LogLevel level;
	private FileLogger fileLogger;
	private FileLogger fileLoggerTestCase;
	
	private DateFormat dateFormat;
	
	
	public boolean isInitialized() {
		return fileLogger != null;
	}
	

	public boolean isTestCaseInitialized() {
		return fileLoggerTestCase != null;
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
			dateFormat = new SimpleDateFormat(GtFileLogFormatter.DATE_FORMAT_GT_ISO_STRING);
		} else {
			dateFormat= new SimpleDateFormat(GtFileLogFormatter.DATE_FORMAT_GT_STRING);
		}
			
		try {
			fileLogger = new FileLogger(new File(getLogFileName())); //NOSONAR this is closed by the osgiLogger when that is stopped
			fileLogger.setConfig(getConfig(dateFormat, level));
			BasicLogger.addLogListener(fileLogger);
		} catch (IOException e1) {
			GtErrorLogger.log(Activator.PLUGIN_ID, e1);
		}
		
		try {
			if (lnr != null) {
				lnr.close();
			}
			lnr = new LineNumberReader(new FileReader(new File(getLogFileName()))); //NOSONAR
		} catch (IOException e) {
			GtErrorLogger.log(Activator.PLUGIN_ID, e);
		}
		
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
	 * @param testCaseId
	 *            will be used as part of the logfile name of the current
	 *            testcase
	 */
	public void initTestCase(String testCaseId) {
		if (!isInitialized()) {
			throw new RuntimeException(
					"TestLogger must be initialized before initializing for a testcase");
		}
	
		shutdownTestCase();
	
		// do not setup the logfile if single logging is disabled in the
		// preferences
		if (! Platform.getPreferencesService().getBoolean(Activator.PLUGIN_ID,
				PreferenceConstants.P_TEST_LOG_SINGLE_TESTCASES, true, null)) {
			testCaseLogFileName = "";
			logFileLine = 0;
			return;
		}
	
		testCaseLogFileName = getTestCaseLogFileName(testCaseId);
	
		try {
			FileLogger osgiLoggerTestCase = new FileLogger(new File(testCaseLogFileName));  //NOSONAR this is closed by the osgiLogger when that is stopped
			osgiLoggerTestCase.setConfig(getConfig(dateFormat, LogLevel.ERROR));
			BasicLogger.addLogListener(osgiLoggerTestCase);
		} catch (IOException e1) {
			GtErrorLogger.log(Activator.PLUGIN_ID, e1);
		}
		
		try {
			if (lnr != null) {
				lnr.close();
			}
			lnr = new LineNumberReader(new FileReader(new File(getTestCaseLogFileName()))); //NOSONAR
		} catch (IOException e) {
			GtErrorLogger.log(Activator.PLUGIN_ID, e);
		}
		
	}

	/**
	 * Dispose the TestLogger, TestLogger is unable to log anything until next
	 * call to init()
	 */
	public void shutdown() {
		if (fileLogger != null){
			BasicLogger.removeLogListener(fileLogger);
			fileLogger = null;	
		}
		
		if (isTestCaseInitialized()){
			shutdownTestCase();
		}
		
		if (lnr != null) {
			try {
				lnr.close();
			} catch (IOException e) {
				GtErrorLogger.log(Activator.PLUGIN_ID, e);
			}
			lnr = null;
		}
	}

	/**
	 * Dispose the TestCaseLogger, following log messages will go only to the
	 * session log until the next call to initTestCase()
	 */
	public void shutdownTestCase() {		
		if (fileLoggerTestCase != null){
			BasicLogger.removeLogListener(fileLoggerTestCase);
			fileLoggerTestCase = null;
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
		LogFilter filter = new AndFilter(
				new TagFilter(BasicLogger.LOG_LEVEL_TAG_ID, LogLevel.getUpToAsNames(level)),
				new TagFilter(BasicLogger.ORIGIN_THREAD_GROUP_TAG_ID, Thread.currentThread().getThreadGroup().getName()));
		LogFormatService formatter = new GtFileLogFormatter(dateFormat);
		
		return new LogListenerConfig() {
			
			@Override
			public LogFormatService getFormat() {
				return formatter;
			}
			
			@Override
			public LogFilter getFilter() {
				return filter;
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
