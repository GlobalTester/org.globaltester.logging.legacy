/*
 * Project GlobalTester-Plugin
 * 
 * Developed by HJP Consulting GmbH Hauptstr. 35 33106 Paderborn Germany
 * 
 * 
 * This software is the confidential and proprietary information of HJP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * Non-Disclosure Agreement you entered into with HJP.
 */

package org.globaltester.logging.preferences;

/**
 * This class stores the properties of the plugin Constant definitions for
 * plug-in preferences
 * 
 * @version Release 2.2.0
 * @author Alexander May
 * 
 */

public class PreferenceConstants {

	// directory name of logging files
	public static final String P_GT_LOGGINGDIR = "GT Framework - Logging directory";
	public static final String P_TEST_LOGGINGDIR = "GT Test - Logging directory";

	// switch to set manual directory settings
	public static final String P_MANUALDIRSETTINGS = "manualDirSettings";

	// whether TestLogger uses same options as framework logger
	public static final String P_TEST_SAME_OPTIONS = "GT Test - Use same options";

	// log level of log file
	public static final String P_GT_LOGLEVEL = "GT Framework - Logging level";
	public static final String P_TEST_LOGLEVEL = "GT Test - Logging level";

	// switch to use html log file
	public static final String P_GT_HTMLLOGGING = "GT Framework - Use HTML Logging";
	public static final String P_TEST_HTMLLOGGING = "GT Test - Use HTML Logging";

	// switch to use plain log file
	public static final String P_GT_PLAINLOGGING = "GT Framework - Use Plain Logging";
	public static final String P_TEST_PLAINLOGGING = "GT Test - Use Plain Logging";

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

	// tranlate loglevels to according Strings
	public static final String[] LOGLEVELS = new String[] { "TRACE", "DEBUG",
			"INFO", "WARN", "ERROR", "FATAL" };
	

}
