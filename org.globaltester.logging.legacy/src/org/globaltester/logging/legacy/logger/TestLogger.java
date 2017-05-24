package org.globaltester.logging.legacy.logger;

import java.util.HashMap;
import java.util.Map;

import org.globaltester.logging.BasicLogger;
import org.globaltester.logging.tags.LogLevel;

/**
 * This class implements methods for logging of messages regarding
 * TestExecution. This class heavily depends on {@link ThreadGroup}s beeing used
 * as a container for test execution code. It uses {@link ThreadGroup} objects
 * and names for distinguishing and filtering of log messages.
 * 
 * @author amay
 * 
 */

public class TestLogger {

	private static Map<ThreadGroup, TestLoggerContext> loggers = new HashMap<>();
		
	public static boolean isInitialized() {
		if (loggers.containsKey(Thread.currentThread().getThreadGroup())){
			return loggers.get(Thread.currentThread().getThreadGroup()).isInitialized();
		}
		return false;
	}

	public static boolean isTestCaseInitialized() {
		if (loggers.containsKey(Thread.currentThread().getThreadGroup())){
			return loggers.get(Thread.currentThread().getThreadGroup()).isTestCaseInitialized();
		}
		return false;
	}

	/**
	 * Log printable object with level 'debug'
	 * 
	 * @param obj
	 */
	public static void debug(Object obj) {
		loggers.get(Thread.currentThread().getThreadGroup()).debug(obj.toString());
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

	/**
	 * Initialize a the TestLogger for a new Test
	 */
	public static void init() {
		if (!loggers.containsKey(Thread.currentThread().getThreadGroup())){
			loggers.put(Thread.currentThread().getThreadGroup(), new TestLoggerContext());
		}
		loggers.get(Thread.currentThread().getThreadGroup()).init();		
	}

	/**
	 * Initialize the TestLogger for a new Test session
	 * 
	 * @param defaultLoggingDir
	 *            will be used as logging directory if user has not selected
	 *            manual directory
	 */
	public static void init(String defaultLoggingDir) {
		if (!loggers.containsKey(Thread.currentThread().getThreadGroup())){
			loggers.put(Thread.currentThread().getThreadGroup(), new TestLoggerContext());
		}
		loggers.get(Thread.currentThread().getThreadGroup()).init(defaultLoggingDir);
	}

	/**
	 * Initialize the TestLogger for a new TestCase
	 * 
	 * @param executableId
	 *            will be used as part of the logfile name of the current
	 *            testcase
	 */
	public static void initTestExecutable(String executableId) {
		loggers.get(Thread.currentThread().getThreadGroup()).initTestExecutable(executableId);
	}

	/**
	 * Initialize the TestLogger for a new TestCase
	 * 
	 * @param testCaseId
	 *            will be used as part of the logfile name of the current
	 *            testcase
	 */
	public static void initTestCase(String testCaseId) {
		loggers.get(Thread.currentThread().getThreadGroup()).initTestCase(testCaseId);
	}

	/**
	 * Dispose the TestLogger, TestLogger is unable to log anything until next
	 * call to init()
	 */
	public static void shutdown() {
		loggers.get(Thread.currentThread().getThreadGroup()).shutdown();
	}

	/**
	 * Dispose the TestCaseLogger, following log messages will go only to the
	 * session log until the next call to initTestCase()
	 */
	public static void shutdownTestExecutableLogger() {
		loggers.get(Thread.currentThread().getThreadGroup()).shutdownTestExecutableLogger();
	}

	/**
	 * Dispose the TestCaseLogger, following log messages will go only to the
	 * session log until the next call to initTestCase()
	 */
	public static void shutdownTestCaseLogger() {
		loggers.get(Thread.currentThread().getThreadGroup()).shutdownTestCaseLogger();
	}

	public static String getLogFileName() {
		return loggers.get(Thread.currentThread().getThreadGroup()).getLogFileName();
	}

	/**
	 * This method returns the name of the current log file that is used for the current TestCase.<br>
	 * It is either the name of the log file for the TestCase if single  log files for TestCases is requested 
	 * (e.g. triggered if "Single Log File for Each TestCase" is set in the Preferences),<br>
	 * or the name of the log file for the TestCampaign.
	 * @return	 name of the current log file
	 */
	public static String getTestCaseLogFileName() {
		return loggers.get(Thread.currentThread().getThreadGroup()).getTestCaseLogFileName();
	}

	/**
	 * construct and return logFileName for a single test case
	 * 
	 * @param testCaseId
	 * @return
	 */
	public static String getTestCaseLogFileName(String testCaseId) {
		return loggers.get(Thread.currentThread().getThreadGroup()).getTestCaseLogFileName(testCaseId);
	}
	
	public static int getLogFileLine(){
		return loggers.get(Thread.currentThread().getThreadGroup()).getLogFileLine();
	}
	
	public static String getLogDir() {
		return loggers.get(Thread.currentThread().getThreadGroup()).getLogDir();
	}

}
