package org.globaltester.logging.legacy.preferences;

/**
 * This class stores the properties of the plugin Constant definitions for
 * plug-in preferences
 * 
 * @version Release 2.2.0
 * @author Alexander May
 * 
 */

public final class PreferenceConstants {
	
	private PreferenceConstants(){
	}

	// directory name of logging files
	public static final String P_GT_LOGGINGDIR = "GT Framework - Logging directory";
	public static final String P_TEST_LOGGINGDIR = "GT Test - Logging directory";

	// switch to set manual directory settings
	public static final String P_MANUALFRAMEWORKDIRSETTINGS = "manualFrameworkDirSettings";
	public static final String P_MANUALDIRSETTINGS = "manualDirSettings";
	public static final String P_USE_SIM_LOG = "Use GT Simulator Logging";

	// log level of log file
	public static final String P_GT_LOGLEVEL = "GT Framework - Logging level";
	public static final String P_TEST_LOGLEVEL = "GT Test - Logging level";

	// switch to use plain log file
	public static final String P_GT_PLAINLOGGING = "GT Framework - Use Plain Logging";
	public static final String P_TEST_PLAINLOGGING = "GT Test - Use Plain Logging";
	public static final String P_TEST_LOG_SINGLE_TESTCASES = "GT Test - Add single logfiles for Testcases";

	// log in ISO8601 format
	public static final String P_GT_USEISO8601LOGGING = "GT Framework - Use ISO 8601 Logging";
	public static final String P_TEST_USEISO8601LOGGING = "GT Test - Use ISO 8601 Logging";
	
	//log framework log to console
	public static final String P_GT_CONSOLELOGGING = "GT Framework - Use Console Logging";

	// constants for the different  loglevels
	public static final int LOGLEVEL_TRACE = 0;
	public static final int LOGLEVEL_DEBUG = 1;
	public static final int LOGLEVEL_INFO = 2;
	public static final int LOGLEVEL_WARN = 3;
	public static final int LOGLEVEL_ERROR = 4;
	public static final int LOGLEVEL_FATAL = 5;

	// store all marker of log file persistent 
	public static final String P_TEST_PERSISTENTMARKER= "GT Test - Store all markers of log file persistently";
	
	
	// tranlate loglevels to according Strings
	public static final String[] LOGLEVELS = new String[] { "TRACE", "DEBUG", "INFO", "WARN", "ERROR", "FATAL" };
	public static final String P_DOUBLECLICKRESULTVIEW =  "Double click in result view ";
	public static final String TEST_CASE = "Test Case";	
	public static final String LOG_FILE = "Log File";
	

}
